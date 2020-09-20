package com.github.bdqfork.core.extension;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI扩展
 *
 * @author bdq
 * @since 2019-08-20
 */
public class ExtensionLoader<T> {
    /**
     * 扫描路径
     */
    private static final String PREFIX = "META-INF/extensions/";
    /**
     * 缓存
     */
    private static final Map<String, ExtensionLoader<?>> CACHES = new ConcurrentHashMap<>();
    /**
     * 扩展Class名称缓存
     */
    private final Map<Class<T>, String> classNames = new ConcurrentHashMap<>();
    /**
     * 扩展Class缓存
     */
    private final Map<String, Class<T>> extensionClasses = new ConcurrentHashMap<>();
    /**
     * 扩展实例缓存
     */
    private volatile Map<String, T> cacheExtensions;
    /**
     * 默认扩展名
     */
    private String defaultName;
    /**
     * 扩展服务类型
     */
    private Class<T> type;

    private ExtensionLoader(Class<T> type) {
        this.type = type;
    }

    /**
     * 获取扩展接口对应的ExtensionLoader
     *
     * @param clazz 扩展接口
     * @param <T>   Class类型
     * @return ExtensionLoader<T>
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        String className = clazz.getName();

        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Fail to create ExtensionLoader for class " + className
                    + ", class is not Interface !");
        }

        SPI spi = clazz.getAnnotation(SPI.class);

        if (spi == null) {
            throw new IllegalArgumentException("Fail to create ExtensionLoader for class " + className
                    + ", class is not annotated by @SPI !");
        }

        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) CACHES.get(className);

        if (extensionLoader == null) {
            CACHES.putIfAbsent(className, new ExtensionLoader<>(clazz));
            extensionLoader = (ExtensionLoader<T>) CACHES.get(className);
            extensionLoader.defaultName = spi.value();
        }

        return extensionLoader;
    }

    /**
     * 根据extensionName获取扩展实例
     *
     * @param extensionName 扩展名称
     * @return T
     */
    public T getExtension(String extensionName) {
        T extension = getExtensions().get(extensionName);
        if (extension != null) {
            return extension;
        }
        throw new IllegalStateException("No extension named " + extensionName + " for class " + type.getName() + "!");
    }

    /**
     * 根据extensionName获取扩展实例
     *
     * @return T
     */
    public T getDefaultExtension() {
        T extension = getExtensions().get(defaultName);
        if (extension != null) {
            return extension;
        }
        throw new IllegalStateException("No default extension named " + defaultName + " for class " + type.getName() + "!");
    }

    /**
     * 获取所有扩展
     *
     * @return Map<String, T>
     */
    public Map<String, T> getExtensions() {
        if (cacheExtensions == null) {
            cacheExtensions = new ConcurrentHashMap<>();
            loadExtensionClasses();

            for (Map.Entry<String, Class<T>> entry : extensionClasses.entrySet()) {
                Class<T> clazz = entry.getValue();
                T instance;
                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
                cacheExtensions.putIfAbsent(entry.getKey(), instance);
            }

        }
        return Collections.unmodifiableMap(cacheExtensions);
    }

    private void loadExtensionClasses() {
        if (classNames.size() > 0) {
            return;
        }
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            // 加载扩展文件
            Enumeration<URL> urlEnumeration = classLoader.getResources(PREFIX + type.getName());

            while (urlEnumeration.hasMoreElements()) {

                URL url = urlEnumeration.nextElement();

                if (url.getPath().isEmpty()) {
                    throw new IllegalArgumentException("Extension path " + PREFIX + type.getName() + " don't exsist !");
                }

                // 读取文件内容
                if (url.getProtocol().equals("file") || url.getProtocol().equals("jar")) {

                    URLConnection urlConnection = url.openConnection();
                    Reader reader = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    // 逐行读取
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {

                        if (line.equals("")) {
                            continue;
                        }

                        // 过滤注释
                        if (line.contains("#")) {
                            line = line.substring(0, line.indexOf("#"));
                        }

                        // 解析key=value
                        String[] values = line.split("=");
                        String name = values[0].trim();
                        String impl = values[1].trim();

                        if (extensionClasses.containsKey(name)) {
                            throw new IllegalStateException("Duplicate extension named " + name);
                        }

                        // 加载Class
                        @SuppressWarnings("unchecked")
                        Class<T> clazz = (Class<T>) classLoader.loadClass(impl);

                        // 缓存Class
                        classNames.putIfAbsent(clazz, name);
                        extensionClasses.putIfAbsent(name, clazz);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Fail to get extension class from " + PREFIX + type.getName() + "!", e);
        }
    }

}
