package cn.yananart.framework.provider;

import cn.yananart.framework.YananartApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

/**
 * Vertx提供
 *
 * @author yananart
 * @date 2021/7/27
 */
public class VertxProvider extends AbstractModule {

    /**
     * vert单例
     *
     * @return vert单例
     */
    @Provides
    public Vertx vertx() {
        // 自应用上下文获取
        return YananartApplication.getContext().getVertx();
    }


    /**
     * Router单利
     *
     * @return Router单例
     */
    @Provides
    public Router router() {
        // 自应用上下文获取
        return YananartApplication.getContext().getRouter();
    }


    @Provides
    public Pool pool() {
        var mysqlConfig = YananartApplication.getContext().getConfig()
                .getJsonObject("datasource", new JsonObject())
                .getJsonObject("mysql", new JsonObject());
        var connectOptions = new MySQLConnectOptions()
                .setHost(mysqlConfig.getString("host"))
                .setPort(mysqlConfig.getInteger("port"))
                .setDatabase(mysqlConfig.getString("database"))
                .setUser(mysqlConfig.getString("username"))
                .setPassword(mysqlConfig.getString("password"));
        // 连接池选项
        var poolOptions = new PoolOptions()
                .setMaxSize(5);
        return MySQLPool.pool(connectOptions, poolOptions);
    }
}
