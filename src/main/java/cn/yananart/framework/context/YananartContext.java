package cn.yananart.framework.context;

import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.Getter;

/**
 * yananart上下文
 *
 * @author yananart
 * @date 2021/7/20
 */
public class YananartContext {

    private static final Logger log = LoggerFactory.getLogger(YananartContext.class);

    private static final String CONFIG_FILE_NAME = "config.json";

    /**
     * 配置文件配置
     */
    @Getter
    private JsonObject config;

    /**
     * 端口号
     */
    @Getter
    private Integer port = 8080;

    /**
     * 网站图标
     */
    @Getter
    private String favicon = "favicon.ico";

    /**
     * vertx
     */
    @Getter
    private final Vertx vertx;

    /**
     * router
     */
    @Getter
    private final Router router;


    public YananartContext() {
        this.vertx = Vertx.vertx();
        this.router = Router.router(vertx);
        initConfig();
    }


    /**
     * 初始化配置文件
     */
    public void initConfig() {
        FileSystem fs = vertx.fileSystem();
        if (fs.existsBlocking(CONFIG_FILE_NAME)) {
            try {
                Buffer buffer = fs.readFileBlocking("config.json");
                JsonObject config = new JsonObject(buffer);
                // load config
                this.config = config;
                var serverConfig = config.getJsonObject("server", new JsonObject());
                this.port = serverConfig.getInteger("port", 8080);
                this.favicon = serverConfig.getString("favicon", "favicon.ico");
            } catch (NullPointerException e) {
                log.warn("load config file failed.", e);
            }
        } else {
            config = new JsonObject();
        }
    }
}
