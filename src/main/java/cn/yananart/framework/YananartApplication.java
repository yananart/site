package cn.yananart.framework;

import cn.yananart.framework.context.YananartContext;
import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import cn.yananart.framework.provider.BeanProvider;
import cn.yananart.framework.provider.VertxProvider;
import cn.yananart.framework.worker.WebWorker;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
     * ioc
     */
    @Getter
    private static Injector injector;


    /**
     * 启动方法
     *
     * @param clazz 启动类
     * @param args  启动参数
     */
    public static YananartContext run(Class<?> clazz, String... args) {
        Objects.requireNonNull(clazz, "Start class can not be null");
        startClass = clazz;
        injector = Guice.createInjector(new VertxProvider(), new BeanProvider());
        YananartApplication.context = init();
        log.info("Yananart application start success!");
        return YananartApplication.context;
    }


    /**
     * 初始化
     */
    private static YananartContext init() {
        YananartContext context = injector.getInstance(YananartContext.class);
        Vertx vertx = context.getVertx();
        Objects.requireNonNull(vertx, "Load vertx instance failed");
        injector.getInstance(WebWorker.class).startWebServer();
        return context;
    }
}
