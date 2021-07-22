package cn.yananart.framework.annotation;

import java.lang.annotation.*;

/**
 * @author yananart
 * @date 2021/7/22
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapping {

    /**
     * path
     *
     * @return path
     */
    String path() default "/";
}
