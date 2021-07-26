package cn.yananart.framework.annotation.paramter;

import java.lang.annotation.*;

/**
 * @author zhouye25337
 * @date 2021/7/26
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String value();
}
