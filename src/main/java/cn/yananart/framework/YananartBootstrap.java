package cn.yananart.framework;

import cn.yananart.framework.config.VertxConfig;
import cn.yananart.framework.worker.WebWorker;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

/**
 * 启动类
 *
 * @author yananart
 * @date 2021/7/22
 */
@Slf4j
public class YananartBootstrap {

    public YananartBootstrap() {
    }

    /**
     * 启动方法
     *
     * @param clazz 启动类
     * @param args  启动参数
     * @return spring上下文
     */
    public static ConfigurableApplicationContext run(Class<?> clazz, String... args) {
        Objects.requireNonNull(clazz, "Start class can not be null");
        ConfigurableApplicationContext context = SpringApplication.run(clazz, args);
        init(context);
        log.info("Yananart application start success!");
        return context;
    }

    /**
     * 初始化
     *
     * @param context spring context
     */
    private static void init(ApplicationContext context) {
        var vertx = context.getBean(Vertx.class);
        Objects.requireNonNull(vertx, "Load vertx instance failed");
        var router = context.getBean(Router.class);
        var vertxConfig = context.getBean(VertxConfig.class);
        new WebWorker(vertx, router, vertxConfig, context).startWebServer();
    }
}
