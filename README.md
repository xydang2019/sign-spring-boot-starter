# sign-spring-boot-starter
# 默认签名规则：
1、设接口所有请求内容或请求处理结果（body）的数据为集合M，向集合M添加字段signTime，其值为请求时间（格式：yyyyMMddhhmm，精准到分）。
2、将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），按照key:value的格式生成键值对（即key1:value1:key2:value2:key3:value3…）拼接成字符串stringA。
3、在stringA最前与最后都拼接上秘钥signKey（分配得到）得到stringB字符串（signKey:key1:value1:key2:value2…key9:value9:signKey），并对stringB进行MD5运算，再将得到的字符串所有字符转换为大写，得到最终sign值。

# 特别注意以下重要规则：
◆ 参数名ASCII码从小到大排序（字典序）；
◆ 如果参数的值为空不参与签名；
◆ 参数名区分大小写；
◆ 传送的sign参数不参与签名。
