package cn.yananart.framework;

import cn.yananart.framework.config.YananartConfig;
import cn.yananart.framework.worker.WebWorker;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 启动类
 *
 * @author yananart
 * @date 2021/7/22
 */
@Slf4j
public class YananartApplication {

    public YananartApplication() {
    }

    /**
     * 启动方法
     *
     * @param clazz 启动类
     * @param args  启动参数
     */
    public static void run(Class<?> clazz, String... args) {
        Objects.requireNonNull(clazz, "Start class can not be null");
        init();
        log.info("Yananart application start success!");
    }

    /**
     * 初始化
     */
    private static void init() {
        Vertx vertx = YananartConfig.getVertx();
        Objects.requireNonNull(vertx, "Load vertx instance failed");
        var router = YananartConfig.getRouter();
        new WebWorker(vertx, router).startWebServer();
    }
}
