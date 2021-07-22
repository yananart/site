package cn.yananart.site;

import cn.yananart.framework.YananartApplication;
import cn.yananart.framework.YananartBootstrap;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
 * @author yananart
 * @date 2021/7/20
 */
@Slf4j
@YananartApplication
public class Application {

    /**
     * main
     *
     * @param args args
     */
    public static void main(String[] args) {
        YananartBootstrap.run(Application.class, args);
    }
}
