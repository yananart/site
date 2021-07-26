package cn.yananart.framework;

import cn.yananart.framework.config.YananartContext;
import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import cn.yananart.framework.worker.WebWorker;
import io.vertx.core.Vertx;
import lombok.Getter;

import java.util.Objects;

/**
 * 启动类
 *
 * @author yananart
 * @date 2021/7/22
 */
public class YananartApplication {

    private static final Logger log = LoggerFactory.getLogger(YananartApplication.class);


    /**
     * 启动类
     */
    @Getter
    private static Class<?> startClass;


    /**
     * 应用上下文
     */
    @Getter
    private static YananartContext context;


    /**
     * 启动方法
     *
     * @param clazz 启动类
     * @param args  启动参数
     */
    public static YananartContext run(Class<?> clazz, String... args) {
        Objects.requireNonNull(clazz, "Start class can not be null");
        startClass = clazz;
        YananartApplication.context = init();
        log.info("Yananart application start success!");
        return YananartApplication.context;
    }


    /**
     * 初始化
     */
    private static YananartContext init() {
        YananartContext context = new YananartContext();
        Vertx vertx = context.getVertx();
        Objects.requireNonNull(vertx, "Load vertx instance failed");
        new WebWorker(context).startWebServer();
        return context;
    }
}
