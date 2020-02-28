package com.github.bdqfork.core.util;

import java.io.File;
import java.net.URL;

/**
 * FileUtil
 */
public class FileUtils {

    /**
     * 获取统一的路径，过滤windows上的路径分隔符
     *
     * @param file 文件
     * @return 文件路径
     */
    public static String getUniformAbsolutePath(File file) {
        return file.getAbsolutePath().replaceAll("\\\\", "\\/");
    }

    /**
     * 加载资源文件
     *
     * @param path 文件路径
     * @return 文件URL
     */
    public static URL loadResourceByPath(String path) {
        return FileUtils.class.getClassLoader().getResource(path);
    }

    /**
     * 检测资源是否存在
     *
     * @param path 文件路径
     * @return 资源是否存在
     */
    public static boolean isResourceExists(String path) {
        return FileUtils.class.getClassLoader().getResource(path) != null;
    }
}