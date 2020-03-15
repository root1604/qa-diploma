package aqashop.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class DataHelper {
    public enum PaymentResult {
        APPROVED, DECLINED
    }

    public static String getEnvironmentProperty(String propertyName){
        return getProperty("environment.properties", propertyName);
    }

    public static String getDataProperty(String propertyName){
        return getProperty("data.properties", propertyName);
    }

    private static String getProperty(String fileName, String propertyValue) {
        Properties property = new Properties();
        String propertyFromFile = null;
        try (FileInputStream file = new FileInputStream(fileName)) {
            property.load(new InputStreamReader(file, StandardCharsets.UTF_8));
            propertyFromFile = property.getProperty(propertyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyFromFile;
    }

    @Data
    @RequiredArgsConstructor
    public static class ResponseApi {
        private String status;
    }
}
