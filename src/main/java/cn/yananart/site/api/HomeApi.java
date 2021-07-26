package cn.yananart.site.api;

import cn.yananart.framework.annotation.ApiMapping;
import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.commons.ResponseType;
import io.vertx.ext.web.RoutingContext;

/**
 * 主页
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
@HttpApi
public class HomeApi {

    @ApiMapping(type = ResponseType.TEMPLATE)
    public void index(RoutingContext context) {
        context.next();
    }

}
