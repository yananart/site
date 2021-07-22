package cn.yananart.framework.worker;

import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.annotation.Mapping;
import cn.yananart.framework.config.VertxConfig;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Objects;

/**
 * web应用启动工作者
 *
 * @author yananart
 * @date 2021/7/21
 */
@Slf4j
public class WebWorker {

    /**
     * vertx单例
     */
    private final Vertx vertx;

    /**
     * router
     */
    private final Router router;

    /**
     * vertx配置
     */
    private final VertxConfig vertxConfig;

    /**
     * spring context
     */
    private final ApplicationContext applicationContext;


    public WebWorker(Vertx vertx,
                     Router router,
                     VertxConfig vertxConfig,
                     ApplicationContext applicationContext) {
        this.vertx = vertx;
        this.router = router;
        this.vertxConfig = vertxConfig;
        this.applicationContext = applicationContext;
    }


    /**
     * 初始化web服务器
     */
    public void startWebServer() {
        // creat a server
        var httpServer = vertx.createHttpServer();

        // 扫描所有注解声明的类
        var beanWithHttp = applicationContext.getBeansWithAnnotation(HttpApi.class);


        for (Object httpController : beanWithHttp.values()) {
            Class<?> clazz = httpController.getClass();
            HttpApi httpApi = clazz.getAnnotation(HttpApi.class);
            var methods = clazz.getMethods();
            for (final var method : methods) {
                Mapping mapping = method.getAnnotation(Mapping.class);
                if (Objects.nonNull(mapping)) {
                    var path = httpApi.path() + mapping.path();
                    path = path.replaceAll("//+", "/");
                    var route = router.route(path);
                    route.handler(context -> {
                        var request = context.request();
                        var response = context.response();
                        var paramClassList = method.getParameterTypes();
                        var paramList = new ArrayList<>(paramClassList.length);
                        for (var paramClass : paramClassList) {
                            if (RoutingContext.class.equals(paramClass)) {
                                paramList.add(context);
                            } else if (HttpServerRequest.class.equals(paramClass)) {
                                paramList.add(request);
                            } else if (HttpServerResponse.class.equals(paramClass)) {
                                paramList.add(response);
                            } else {
                                // todo format obj
                                paramList.add(null);
                            }
                        }
                        try {
                            method.invoke(httpController, paramList.toArray());
                        } catch (Exception e) {
                            log.error("http error", e);
                        }
                    });
                }
            }
        }

        httpServer.requestHandler(router)
                .listen(vertxConfig.getPort())
                .onFailure(error -> log.error("start web server fail", error))
                .onSuccess(server -> log.info("start web server in {} with vertx", server.actualPort()));
    }
}
