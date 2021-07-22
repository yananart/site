package cn.yananart.framework.config;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * vertx配置项
 *
 * @author yananart
 * @date 2021/7/20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "vertx")
public class VertxConfig {

    /**
     * 端口号
     */
    private Integer port = 8080;

    @Bean
    protected Vertx createVertx() {
        return Vertx.vertx();
    }

    @Bean
    protected Router createRouter(Vertx vertx) {
        return Router.router(vertx);
    }
}
