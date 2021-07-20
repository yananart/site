package cn.yananart.site.config;

import io.vertx.core.Vertx;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
