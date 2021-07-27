package cn.yananart.site.service;

import cn.yananart.framework.annotation.Bean;
import io.vertx.ext.web.RoutingContext;

/**
 * 图片服务
 *
 * @author yananart
 * @date 2021/7/27
 */
@Bean
public class PhotoService {

    public void queryPhotos(RoutingContext context) {
        context.next();
    }

}
