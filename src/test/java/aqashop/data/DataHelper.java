package aqashop.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DataHelper {
    public enum PaymentResult {
        APPROVED, DECLINED
    }

    public static String getProperty(String propertyValue) {
        Properties property = new Properties();
        String propertyFromFile = null;
        try (FileInputStream file = new FileInputStream("environment.properties")) {
            property.load(file);
            propertyFromFile = property.getProperty(propertyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertyFromFile;
    }
}
