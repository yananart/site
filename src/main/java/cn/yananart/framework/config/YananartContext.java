package cn.yananart.framework.config;

import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.Data;

/**
 * yananart上下文
 *
 * @author yananart
 * @date 2021/7/20
 */
@Data
public class YananartContext {

    private static final Logger log = LoggerFactory.getLogger(YananartContext.class);

    private static final String CONFIG_FILE_NAME = "config.json";

    /**
     * 配置文件配置
     */
    private JsonObject config;

    /**
     * 端口号
     */
    private Integer port = 8080;

    /**
     * 网站图标
     */
    private String favicon = "favicon.ico";

    /**
     * vertx
     */
    private final Vertx vertx;

    /**
     * router
     */
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
                this.port = config.getInteger("server.port", 8080);
                this.favicon = config.getString("server.favicon", "favicon.ico");
            } catch (NullPointerException e) {
                log.warn("load config file failed.", e);
            }
        } else {
            config = new JsonObject();
        }
    }
}
