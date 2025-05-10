package com.xiaofutest.unit;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = PropertyUtils.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("找不到配置文件");
            }
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("加载配置失败", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    // 示例用法
    public static void main(String[] args) {
        String dbUrl = getProperty("app.database.url");
        System.out.println("数据库URL: " + dbUrl);
    }
}