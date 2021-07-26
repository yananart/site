package cn.yananart.site.api;

import cn.yananart.framework.annotation.ApiMapping;
import cn.yananart.framework.annotation.HttpApi;
import cn.yananart.framework.commons.ResponseType;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDate;

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
        var localDate = LocalDate.now();
        var year = localDate.getYear();
        context.put("year", year);
        context.next();
    }


    @ApiMapping(path = "/photo", type = ResponseType.TEMPLATE)
    public void photos(RoutingContext context) {
        var localDate = LocalDate.now();
        var year = localDate.getYear();
        context.put("year", year);
        context.next();
    }


    @ApiMapping(path = "/admin", type = ResponseType.TEMPLATE)
    public void admin(RoutingContext context) {
        // todo 授权
        context.next();
    }

}
