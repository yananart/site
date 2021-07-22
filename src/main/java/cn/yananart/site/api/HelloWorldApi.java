package cn.yananart.site.api;

import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.annotation.Mapping;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * hello world
 *
 * @author yananart
 * @date 2021/7/21
 */
@HttpApi
public class HelloWorldApi {

    @Mapping
    public void helloWorld(HttpServerResponse response) {
        // 类型
        response.putHeader("content-type", "text/plain");
        // 写入响应并结束处理
        response.end("Hello World!");
    }
}
