package cn.yananart.site;

import cn.yananart.framework.YananartApplication;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动类
 *
 * @author yananart
 * @date 2021/7/20
 */
@Slf4j
public class Application {

    /**
     * main
     *
     * @param args args
     */
    public static void main(String[] args) {
        YananartApplication.run(Application.class, args);
    }
}
