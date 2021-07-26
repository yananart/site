package cn.yananart.framework.logging;

import cn.yananart.framework.logging.impl.LoggerAdapter;

/**
 * 日志工厂
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
public class LoggerFactory {

    public static Logger getLogger(Class<?> clazz) {
        return new LoggerAdapter(io.vertx.core.impl.logging.LoggerFactory.getLogger(clazz));
    }

    public static Logger getLogger(String name) {
        return new LoggerAdapter(io.vertx.core.impl.logging.LoggerFactory.getLogger(name));
    }
}
