package cn.yananart.site.worker;

import cn.yananart.site.annotation.HttpApi;
import cn.yananart.site.config.VertxConfig;
import cn.yananart.site.web.AbstractApi;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * web应用启动工作者
 *
 * @author yananart
 * @date 2021/7/21
 */
@Slf4j
@Component
public class WebWorker implements ApplicationRunner {

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

    @Override
    public void run(ApplicationArguments args) {
        // creat a server
        var httpServer = vertx.createHttpServer();

        var apiMap = applicationContext.getBeansOfType(AbstractApi.class);

        // 路由控制
        for (Map.Entry<String, AbstractApi> item : apiMap.entrySet()) {
            AbstractApi api = item.getValue();
            HttpApi httpApi = api.getClass().getAnnotation(HttpApi.class);
            if (Objects.nonNull(httpApi)) {
                Route route;
                route = router.route(httpApi.path());
//                if (StringUtils.isEmpty(httpApi.path())) {
//                    route = router.route();
//                } else {
//
//                }
                route.handler(context -> {
                    var request = context.request();
                    var response = context.response();
                    api.handler(request, response);
                });
            }
        }
        httpServer.requestHandler(router)
                .listen(vertxConfig.getPort())
                .onFailure(error -> log.error("start web server fail", error))
                .onSuccess(server -> log.info("start web server in {} with vertx", server.actualPort()));
    }
}
