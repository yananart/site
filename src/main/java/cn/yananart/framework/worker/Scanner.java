package cn.yananart.framework.worker;

import cn.yananart.framework.logging.Logger;
import cn.yananart.framework.logging.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描包
 *
 * @author zhouye25337
 * @date 2021/7/26
 */
public class Scanner {

    private static final Logger log = LoggerFactory.getLogger(Scanner.class);

    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";

    /**
     * 从包package中获取所有的Class
     *
     * @param packageName 包名
     * @return 类
     */
    public static Set<Class<?>> getClasses(String packageName) throws Exception {
        // 第一个class类的集合
        var classes = new HashSet<Class<?>>();
        // 获取包的名字 并进行替换
        var packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                if (PROTOCOL_FILE.equals(protocol)) {
                    // 包在文件路径下，一般是开发过程中ide中启动
                    String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
                    // 以文件的方式扫描整个包下的文件,并添加到集合中
                    addClass(classes, filePath, packageName);
                } else if (PROTOCOL_JAR.equals(protocol)) {
                    // 如果是jar包文件，定义一个JarFile
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体，可以是目录和一些jar包里的其他文件，如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.startsWith("/")) {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                // 如果是一个.class文件 而且不是目录
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // 去掉后面的".class" 获取真正的类名
                                    String className = name.substring(packageName.length() + 1, name.length() - 6);
                                    try {
                                        // 添加到classes
                                        classes.add(Class.forName(packageName + '.' + className));
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        log.error("加载jar异常", e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("加载资源异常", e);
        }

        return classes;
    }

    public static void addClass(Set<Class<?>> classes, String filePath, String packageName) throws Exception {
        File[] files = new File(filePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String classsName = fileName.substring(0, fileName.lastIndexOf("."));
                if (!packageName.isEmpty()) {
                    classsName = packageName + "." + classsName;
                }
                doAddClass(classes, classsName);
            } else if (file.isDirectory()) {
                String subPkgName = packageName + "." + fileName;
                addClass(classes, file.getPath(), subPkgName);
            }
        }
    }

    public static void doAddClass(Set<Class<?>> classes, final String classsName) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };
        classes.add(classLoader.loadClass(classsName));
    }


    public static <A extends Annotation> Set<Class<?>> getAnnotationClasses(String packageName, Class<A> annotationClass) throws Exception {
        // 找用了annotationClass注解的类
        Set<Class<?>> controllers = new HashSet<>();
        Set<Class<?>> clsList = getClasses(packageName);
        if (!clsList.isEmpty()) {
            for (Class<?> cls : clsList) {
                if (cls.getAnnotation(annotationClass) != null) {
                    controllers.add(cls);
                }
            }
        }
        return controllers;
    }
}
