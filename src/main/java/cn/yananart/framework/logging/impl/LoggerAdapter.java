package cn.yananart.framework.logging.impl;

import cn.yananart.framework.logging.Logger;

import java.text.MessageFormat;

/**
 * 日志适配器
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
public final class LoggerAdapter implements Logger {

    private static final String PLACE_TAG = "\\{}";

    private final io.vertx.core.impl.logging.Logger vertxLogger;

    public LoggerAdapter(io.vertx.core.impl.logging.Logger logger) {
        this.vertxLogger = logger;
    }

    @Override
    public void debug(String message) {
        vertxLogger.debug(message);
    }

    @Override
    public void debug(String message, Object... args) {
        vertxLogger.debug(formatMessage(message, args));
    }

    @Override
    public void debug(String message, Throwable t, Object... args) {
        vertxLogger.debug(formatMessage(message, args), t);
    }

    @Override
    public void info(String message) {
        vertxLogger.info(message);
    }

    @Override
    public void info(String message, Object... args) {
        vertxLogger.info(formatMessage(message, args));
    }

    @Override
    public void info(String message, Throwable t, Object... args) {
        vertxLogger.info(formatMessage(message, args), t);
    }

    @Override
    public void warn(String message) {
        vertxLogger.warn(message);
    }

    @Override
    public void warn(String message, Object... args) {
        vertxLogger.warn(formatMessage(message, args));
    }

    @Override
    public void warn(String message, Throwable t, Object... args) {
        vertxLogger.warn(formatMessage(message, args), t);
    }

    @Override
    public void error(String message) {
        vertxLogger.error(message);
    }

    @Override
    public void error(String message, Object... args) {
        vertxLogger.error(formatMessage(message, args));
    }

    @Override
    public void error(String message, Throwable t, Object... args) {
        vertxLogger.error(formatMessage(message, args), t);
    }

    private String formatMessage(String message, Object[] args) {
        int num = args.length;
        var params = new String[num];
        for (int index = 0; index < num; index++) {
            message = message.replaceFirst(PLACE_TAG, "{" + index + "}");
            params[index] = String.valueOf(args[index]);
        }
        return MessageFormat.format(message, params);
    }
}
