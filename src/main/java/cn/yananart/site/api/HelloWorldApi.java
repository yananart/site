package cn.yananart.site.api;

import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.annotation.Mapping;
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
        // 写入响应并结束处理
        response.end("Hello World!");
    }

    @Mapping(path = "/1")
    public String helloWorld1() {
        // 写入响应并结束处理
        return "Hello World!";
    }

    @Mapping(path = "/2")
    public int helloWorld2() {
        // 写入响应并结束处理
        return 123;
    }

    @Mapping(path = "/e")
    public void exception() {
        // 写入响应并结束处理
        throw new RuntimeException("这是异常信息");
    }
}
