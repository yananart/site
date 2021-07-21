package cn.yananart.site.api;

import cn.yananart.site.annotation.HttpApi;
import cn.yananart.site.web.AbstractApi;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * hello world
 *
 * @author yananart
 * @date 2021/7/21
 */
@HttpApi
public class HelloWorldApi extends AbstractApi {

    @Override
    public void handler(HttpServerRequest request, HttpServerResponse response) {
        // 类型
        response.putHeader("content-type", "text/plain");
        // 写入响应并结束处理
        response.end("Hello World!");
    }
}
