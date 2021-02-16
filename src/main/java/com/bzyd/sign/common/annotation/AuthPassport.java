package com.bzyd.sign.common.annotation;


import com.bzyd.sign.common.enums.SignEnum;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPassport {
    SignEnum type() default SignEnum.FORM;
}
