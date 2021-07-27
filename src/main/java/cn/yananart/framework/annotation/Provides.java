package cn.yananart.framework.annotation;

import java.lang.annotation.*;

/**
 * 服务提供者
 *
 * @author yananart
 * @date 2021/7/27
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Provides {
}
