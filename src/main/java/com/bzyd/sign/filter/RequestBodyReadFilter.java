package com.bzyd.sign.filter;

import com.bzyd.sign.wrapper.RequestWrapper;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 请求体读取过滤器
 */
public class RequestBodyReadFilter implements Filter {

    /**
     * 过滤路径列表
     */
    private List<String> excludeList;

    /**
     * Ant匹配模式
     */
    private AntPathMatcher pathMatcher;

    public RequestBodyReadFilter(String[] excludePaths) {
        this.excludeList = Arrays.asList(excludePaths);
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 读取流后，将流继续写出去
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        if (!isExcludeUri(httpServletRequest)) {
            // 这里将原始request传入，读出流并存储
            ServletRequest requestWrapper = new RequestWrapper(httpServletRequest);
            // 这里将原始request替换为包装后的request，此后所有进入controller的request均为包装后的
            filterChain.doFilter(requestWrapper, servletResponse);//
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * 判断是否排除掉过滤
     *
     * @param httpServletRequest
     * @return
     */
    private boolean isExcludeUri(HttpServletRequest httpServletRequest) {
        String contextPath = httpServletRequest.getContextPath();
        String uri = httpServletRequest.getRequestURI();
        String urlStr = uri.replace(contextPath, "");

        for (String pattern : this.excludeList) {
            if (pathMatcher.match(pattern, urlStr)) {
                return true;
            }
        }

        return false;
    }
}
