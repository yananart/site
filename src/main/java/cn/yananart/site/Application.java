package cn.yananart.site;

import cn.yananart.site.config.VertxConfig;
import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author yananart
 */
@Slf4j
@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    private Vertx vertx;

    @Autowired
    private VertxConfig vertxConfig;

    /**
     * main
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var httpServer = vertx.createHttpServer();
        httpServer.requestHandler(request -> {
            // 所有的请求都会调用这个处理器处理
            var response = request.response();
            response.putHeader("content-type", "text/plain");
            // 写入响应并结束处理
            response.end("Hello World!");
        });
        httpServer.listen(vertxConfig.getPort())
                .onFailure(event -> log.error("start web server fail", event))
                .onSuccess(server -> log.info("start web server in {} with vertx", server.actualPort()));
    }
}
