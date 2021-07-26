package cn.yananart.framework.annotation;

import cn.yananart.framework.commons.ResponseType;

import java.lang.annotation.*;

/**
 * Api的路由映射
 *
 * @author yananart
 * @date 2021/7/22
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiMapping {

    /**
     * path
     *
     * @return path
     */
    String path() default "/";


    /**
     * 是否为异步
     *
     * @return 异步
     */
    boolean async() default true;

    /**
     * 响应类型
     *
     * @return 类型
     */
    ResponseType type() default ResponseType.JSON;
}
