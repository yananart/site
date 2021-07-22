package cn.yananart.framework;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 应用启动注解
 *
 * @author yananart
 * @date 2021/7/22
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackages = "cn.yananart.framework")
public @interface YananartApplication {

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "exclude"
    )
    Class<?>[] exclude() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "excludeName"
    )
    String[] excludeName() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "scanBasePackages"
    )
    String[] scanBasePackages() default {};

    @AliasFor(
            annotation = SpringBootApplication.class,
            attribute = "scanBasePackageClasses"
    )
    Class<?>[] scanBasePackageClasses() default {};

}
