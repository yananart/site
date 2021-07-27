package cn.yananart.site.service;

import cn.yananart.framework.annotation.Provides;
import cn.yananart.site.dao.PhotoDao;
import com.google.inject.Inject;
import io.netty.util.internal.StringUtil;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;

/**
 * 图片服务
 *
 * @author yananart
 * @date 2021/7/27
 */
@Provides
public class PhotoService {

    @Inject
    private PhotoDao photoDao;

    public void queryPhotos(RoutingContext context) {
        photoDao.queryPhotos(result -> {
            var photoList = new ArrayList<String>();
            if (result.succeeded()) {
                result.result().forEach(r -> {
                    var url = r.get(String.class, "p_src");
                    if (!StringUtil.isNullOrEmpty(url)) {
                        photoList.add(url);
                    }
                });
            }
            context.put("photoList", photoList);
            context.next();
        });
    }

}
