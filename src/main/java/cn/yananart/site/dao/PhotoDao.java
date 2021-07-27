package cn.yananart.site.dao;

import cn.yananart.framework.annotation.Provides;
import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

/**
 * 图片信息的dao查询
 *
 * @author yananart
 * @date 2021/7/27
 */
@Provides
public class PhotoDao {

    @Inject
    private Pool pool;

    public void queryPhotos(Handler<AsyncResult<RowSet<Row>>> handler) {
        pool.query("select * from yananart_photo.tb_photo")
                .execute(handler);
    }

}
