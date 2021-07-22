package cn.yananart.framework.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * ws
 *
 * @author yananart
 * @date 2021/7/22
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface WebSocketApi {

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
