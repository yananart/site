package cn.yananart.framework.worker;

import cn.yananart.framework.annotation.ApiMapping;
import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.config.YananartConfig;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.FaviconHandler;
import io.vertx.ext.web.handler.ResponseContentTypeHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

import static cn.yananart.framework.commons.Constants.URL_PATH_SPLIT;

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

    public WebWorker(Vertx vertx,
                     Router router) {
        this.vertx = vertx;
        this.router = router;
    }


    /**
     * 初始化web服务器
     */
    public void startWebServer() {
        // creat a server
        var httpServer = vertx.createHttpServer();
        // favicon
        setFavicon();
        // 注册http接口
        registerHttpApi();
        // 启动监听
        httpServer.requestHandler(router)
                .listen(vertxConfig.getPort())
                .onFailure(error -> log.error("Started web server fail", error))
                .onSuccess(server -> log.info("Started web server in {} with vertx", server.actualPort()));
    }


    /**
     * 转换path为正确的格式
     *
     * @param path url path
     * @return 正确的可识别的path
     */
    private String translatePath(@NonNull String path) {
        path = path.replaceAll("//+", URL_PATH_SPLIT);
        if (!path.startsWith(URL_PATH_SPLIT)) {
            path = URL_PATH_SPLIT + path;
        }
        if (!URL_PATH_SPLIT.equals(path) && path.endsWith(URL_PATH_SPLIT)) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }


    /**
     * 判断是否为可以直接返回的基础类型
     *
     * @param result 返回结果
     * @return true if is basic
     */
    private boolean isBasicReturnType(@NonNull Object result) {
        var clazz = result.getClass();
        return clazz.isPrimitive() ||
                String.class.equals(clazz) ||
                BigInteger.class.equals(clazz) ||
                BigDecimal.class.equals(clazz);
    }


    /**
     * 设置网站图标
     */
    private void setFavicon() {
        FaviconHandler faviconHandler = FaviconHandler.create(vertx, vertxConfig.getFavicon());
        router.route("/favicon.ico").handler(faviconHandler);
    }


    /**
     * 将异常转化为json输出
     *
     * @param e 异常
     * @return json with message and stackTrace
     */
    private JsonObject translateExceptionJson(@NonNull Throwable e) {
        var json = new JsonObject();
        json.put("message", e.getMessage());
        var stackTrace = new JsonArray();
        for (var item : e.getStackTrace()) {
            stackTrace.add(item.toString());
        }
        json.put("stackTrace", stackTrace);
        return json;
    }


    /**
     * 扫描spring容器，将所有包含注解的bean转换注册到vertx上
     */
    private void registerHttpApi() {
        // 扫描所有注解声明的类
        var beanWithHttp = applicationContext.getBeansWithAnnotation(HttpApi.class);
        // 遍历所有api
        for (final var httpBean : beanWithHttp.values()) {
            // 获取当前bean的类型，且获取其上的注解
            final var clazz = httpBean.getClass();
            final var apiAnnotation = clazz.getAnnotation(HttpApi.class);
            // 获取该类的所有方法，遍历查看是否包含注解
            final var methods = clazz.getMethods();
            for (final var method : methods) {
                final var mapAnnotation = method.getAnnotation(ApiMapping.class);
                // 如果包含接口声明注解，则注册
                if (Objects.nonNull(mapAnnotation)) {
                    // 路径拼接处理，注册
                    var path = apiAnnotation.path() + URL_PATH_SPLIT + mapAnnotation.path();
                    path = translatePath(path);
                    final var route = router.route(path);
                    // 请求体处理
                    route.handler(BodyHandler.create());
                    // 返回类型
                    route.handler(ResponseContentTypeHandler.create());
                    route.produces("application/json");
                    route.produces("text/plain");
                    // 获取当前方法的出入参类型，后面要按类型注入参数
                    final var paramClassList = method.getParameterTypes();
                    final var resultClass = method.getReturnType();
                    // 响应回调
                    route.handler(context -> {
                        var request = context.request();
                        var response = context.response();
                        // 创建请求的参数
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
                                // JsonObject jsonObject = context.getBodyAsJson();
                                paramList.add(null);
                            }
                        }
                        Object result;
                        try {
                            result = method.invoke(httpBean, paramList.toArray());
                        } catch (Exception e) {
                            var cause = e.getCause();
                            log.error("http handler error", cause);
                            response.setStatusCode(500);
                            response.end(translateExceptionJson(cause).toString());
                            return;
                        }
                        // 制定规则，如果返回结果不为Void，默认是同步方法，在这里判断并结束
                        if (!resultClass.equals(Void.class) && !resultClass.equals(void.class)) {
                            if (!response.ended()) {
                                if (Objects.nonNull(result)) {
                                    // todo 需要调整输出形式
                                    if (isBasicReturnType(result)) {
                                        response.end(String.valueOf(result));
                                    } else {
                                        response.end(Json.encode(result));
                                    }
                                } else {
                                    response.end();
                                }
                            }
                        }
                    });
                    // log info
                    log.info("Http api path [{}] register with {}.{}", path, clazz.getName(), method.getName());
                }
            }
        }
    }
}
