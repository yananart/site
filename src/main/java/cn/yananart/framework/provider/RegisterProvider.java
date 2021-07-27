package cn.yananart.framework.provider;

import cn.yananart.framework.YananartApplication;
import cn.yananart.framework.annotation.Provides;
import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import cn.yananart.framework.worker.Scanner;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

/**
 * bean创建提供
 *
 * @author yananart
 * @date 2021/7/27
 */
public class RegisterProvider implements Module {

    private static final Logger log = LoggerFactory.getLogger(RegisterProvider.class);

    @Override
    public void configure(Binder binder) {
        try {
            register(binder, "cn.yananart.framework");
            register(binder, YananartApplication.getStartClass().getPackageName());
        } catch (Exception e) {
            log.error("注册至IOC框架(Guice)失败", e);
        }
    }


    /**
     * 将有Bean注解的类注册到Guice上
     *
     * @param binder  绑定器
     * @param pkgName 扫描的包名
     * @throws Exception 注册的异常，主要发生在扫描包内类时
     */
    private void register(Binder binder, String pkgName) throws Exception {
        var classes = Scanner.getAnnotationClasses(pkgName, Provides.class);
        for (var clazz : classes) {
            if (clazz.isAssignableFrom(Object.class)) {
                binder.bind(clazz).in(Scopes.SINGLETON);
            } else if (clazz.isInterface()) {
                // todo register if interface have class
            }
        }
    }
}
