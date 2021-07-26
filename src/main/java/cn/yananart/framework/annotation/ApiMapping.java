package cn.yananart.framework.annotation;

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
     * 响应类型
     *
     * @return 类型
     */
    String type() default "";
}
