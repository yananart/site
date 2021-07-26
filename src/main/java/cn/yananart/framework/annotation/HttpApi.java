package cn.yananart.framework.annotation;

import java.lang.annotation.*;

/**
 * 接口类声明
 *
 * @author yananart
 * @date 2021/7/20
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpApi {

    /**
     * path
     *
     * @return path
     */
    String path() default "/";

    /**
     * 接口类简介
     *
     * @return description
     */
    String description() default "";
}
