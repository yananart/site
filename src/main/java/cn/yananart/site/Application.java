package cn.yananart.site;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 *
 * @author yananart
 * @date 2021/7/20
 */
@Slf4j
@SpringBootApplication
public class Application {

    /**
     * main
     *
     * @param args args
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
