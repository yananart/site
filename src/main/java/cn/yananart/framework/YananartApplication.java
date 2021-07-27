package cn.yananart.framework;

import cn.yananart.framework.context.YananartContext;
import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import cn.yananart.framework.provider.RegisterProvider;
import cn.yananart.framework.provider.VertxProvider;
import cn.yananart.framework.worker.WebWorker;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
        // 初始化上下文
        YananartApplication.context = initContext();
        // 加载IOC容器
        injector = Guice.createInjector(new VertxProvider(), new RegisterProvider());
        // 启动web服务
        initWebServer();
        // log
        log.info("Yananart application start success!");
        return YananartApplication.context;
    }


    /**
     * 初始化 主要包含vertx的实例化与配置文件的读取
     */
    private static YananartContext initContext() {
        YananartContext context = new YananartContext();
        var vertx = context.getVertx();
        Objects.requireNonNull(vertx, "Load vertx instance failed");
        return context;
    }


    /**
     * 初始化web服务
     */
    private static void initWebServer() {
        injector.getInstance(WebWorker.class).startWebServer();
    }
}
