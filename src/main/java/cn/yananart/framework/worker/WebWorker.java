package cn.yananart.framework.worker;

import cn.yananart.framework.YananartApplication;
import cn.yananart.framework.annotation.ApiMapping;
import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.annotation.paramter.Body;
import cn.yananart.framework.annotation.paramter.Param;
import cn.yananart.framework.commons.ResponseType;
import cn.yananart.framework.handler.TemplateHandler;
import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import com.google.inject.Inject;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
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
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import lombok.NonNull;

import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static cn.yananart.framework.commons.Constants.URL_PATH_SPLIT;

/**
 * web应用启动工作者
 *
 * @author yananart
 * @date 2021/7/21
 */
public class WebWorker {

    private static final Logger log = LoggerFactory.getLogger(WebWorker.class);

    /**
     * vertx单例
     */
    private final Vertx vertx;

    /**
     * router
     */
    private final Router router;


    @Inject
    public WebWorker(Vertx vertx, Router router) {
        this.vertx = vertx;
        this.router = router;
    }


    /**
     * 初始化web服务器
     */
    public void startWebServer() {
        log.info("begin start web server");
        // creat a server
        var httpServer = vertx.createHttpServer();
        // favicon
        setFavicon();
        // static
        setStaticHandler();
        // 注册http接口
        try {
            registerHttpApi();
        } catch (Exception e) {
            log.error("加载http接口异常", e);
        }
        // 启动监听
        httpServer.requestHandler(router)
                .listen(YananartApplication.getContext().getPort())
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
        FaviconHandler faviconHandler = FaviconHandler.create(vertx, YananartApplication.getContext().getFavicon());
        router.route("/favicon.ico").handler(faviconHandler);
    }


    /**
     * 设置静态资源处理器
     */
    private void setStaticHandler() {
        // static
        var sHandler = StaticHandler.create();
        sHandler.setWebRoot("static");
        router.route("/static/*").handler(sHandler);
    }


    /**
     * 从请求体中获取参数
     *
     * @param context   上下文
     * @param parameter 参数
     * @return 参数对象
     */
    private Object getParam(RoutingContext context,
                            Parameter parameter) {
        var request = context.request();

        Class<?> clazz = parameter.getType();

        if (Objects.isNull(parameter.getAnnotation(Body.class))) {
            if (RoutingContext.class.equals(clazz)) {
                return context;
            } else if (HttpServerRequest.class.equals(clazz)) {
                return context.request();
            } else if (HttpServerResponse.class.equals(clazz)) {
                return context.response();
            } else if (String.class.equals(clazz)) {
                Param param = parameter.getAnnotation(Param.class);
                return request.getParam(param.value());
            }
        } else {
            if (Buffer.class.equals(clazz)) {
                return context.getBody();
            } else if (JsonObject.class.equals(clazz)) {
                return context.getBodyAsJson();
            } else if (JsonArray.class.equals(clazz)) {
                return context.getBodyAsJsonArray();
            } else if (String.class.equals(clazz)) {
                return context.getBodyAsString();
            }
        }
        log.warn("can not load parameter [{}]", parameter.getName());
        return null;
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
    private void registerHttpApi() throws Exception {
        // TemplateHandler
        final TemplateHandler templateHandler = TemplateHandler.create(ThymeleafTemplateEngine.create(vertx));

        // start class
        var startClass = YananartApplication.getStartClass();
        var pkgName = startClass.getPackageName();

        Set<Class<?>> classes = Scanner.getAnnotationClasses(pkgName, HttpApi.class);

        // 遍历所有api
        for (final var clazz : classes) {
            // 实例 todo 考虑单例 IOC
            Object bean = YananartApplication.getInjector().getInstance(clazz);
            // 获取当前bean的类型，且获取其上的注解
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
                    switch (mapAnnotation.type()) {
                        case JSON:
                            route.handler(ResponseContentTypeHandler.create());
                            route.produces("application/json");
                            break;
                        case PLAIN:
                            route.handler(ResponseContentTypeHandler.create());
                            route.produces("application/json");
                            route.produces("text/plain");
                            break;
                        default:
                            // noting
                    }
                    // 获取当前方法的出入参，后面要按类型注入参数
                    final var parameterArray = method.getParameters();
                    // 响应回调
                    route.handler(context -> {
                        var request = context.request();
                        var response = context.response();
                        // 创建请求的参数
                        var paramList = new ArrayList<>(parameterArray.length);
                        for (Parameter parameter : parameterArray) {
                            paramList.add(getParam(context, parameter));
                        }
                        Object result;
                        try {
                            result = method.invoke(bean, paramList.toArray());
                        } catch (Exception e) {
                            var cause = e.getCause();
                            log.error("http handler error", cause);
                            response.setStatusCode(500);
                            response.end(translateExceptionJson(cause).toString());
                            return;
                        }
                        if (!mapAnnotation.async()) {
                            switch (mapAnnotation.type()) {
                                case JSON:
                                case PLAIN:
                                    if (!response.ended()) {
                                        if (Objects.nonNull(result)) {
                                            if (isBasicReturnType(result)) {
                                                response.end(String.valueOf(result));
                                            } else {
                                                response.end(Json.encode(result));
                                            }
                                        } else {
                                            response.end();
                                        }
                                    }
                                    break;
                                default:
                                    if (!response.ended()) {
                                        response.end();
                                    }
                            }
                        }
                    });
                    // 模版类型
                    if (ResponseType.TEMPLATE.equals(mapAnnotation.type())) {
                        route.handler(templateHandler);
                    }
                    // log info
                    log.info("Http api path [{}] register with {}.{}", path, clazz.getName(), method.getName());
                }
            }
        }
    }
}
