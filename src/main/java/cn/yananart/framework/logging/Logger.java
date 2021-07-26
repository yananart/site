package cn.yananart.framework.logging;

/**
 * 日志
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
public interface Logger {

    void debug(String message);

    void debug(String message, Object... args);

    void debug(String message, Throwable t, Object... args);

    void info(String message);

    void info(String message, Object... args);

    void info(String message, Throwable t, Object... args);

    void warn(String message);

    void warn(String message, Object... args);

    void warn(String message, Throwable t, Object... args);

    void error(String message);

    void error(String message, Object... args);

    void error(String message, Throwable t, Object... args);

}
