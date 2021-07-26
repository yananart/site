package cn.yananart.framework.annotation.paramter;

import java.lang.annotation.*;

/**
 * 请求体
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Body {
}
