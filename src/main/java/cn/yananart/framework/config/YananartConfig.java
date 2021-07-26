package cn.yananart.framework.config;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.Data;

import java.util.Objects;

/**
 * vertx配置项
 *
 * @author yananart
 * @date 2021/7/20
 */
@Data
public class YananartConfig {

    /**
     * 端口号
     */
    private Integer port = 8080;

    /**
     * 网站图标
     */
    private String favicon = "favicon.ico";

    public YananartConfig() {
        init();
    }

    public void init() {

    }

    private static Vertx vertxInstance;

    public static Vertx getVertx() {
        if (Objects.isNull(vertxInstance)) {
            vertxInstance = Vertx.vertx();
        }
        return vertxInstance;
    }

    private static Router routerInstance;

    public static Router getRouter() {
        if (Objects.isNull(routerInstance)) {
            routerInstance = Router.router(getVertx());
        }
        return routerInstance;
    }
}
