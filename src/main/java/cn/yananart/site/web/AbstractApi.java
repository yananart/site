package cn.yananart.site.web;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

/**
 * @author zhouye25337
 * @date 2021/7/21
 */
public abstract class AbstractApi {

    /**
     * 处理
     *
     * @param request  请求
     * @param response 响应
     */
    public abstract void handler(HttpServerRequest request, HttpServerResponse response);

}
