package cn.yananart.framework.provider;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * Vertx提供
 *
 * @author yananart
 * @date 2021/7/27
 */
public class VertxProvider extends AbstractModule {

    @Provides
    public Vertx vertx() {
        return Vertx.vertx();
    }


    @Inject
    @Provides
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }
}
