package aqashop.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class DataHelper {
    public enum PaymentResult {
        APPROVED, DECLINED
    }

    public static String getProperty(String fileName, String propertyValue) {
        Properties property = new Properties();
        String propertyFromFile = null;
        try (FileInputStream file = new FileInputStream(fileName)) {
            property.load(new InputStreamReader(file, Charset.forName("UTF-8")));
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
