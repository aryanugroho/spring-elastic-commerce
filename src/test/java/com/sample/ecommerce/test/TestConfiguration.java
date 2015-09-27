package com.sample.ecommerce.test;

import com.sample.ecommerce.BatchConfiguration;
import java.io.InputStream;

public class TestConfiguration {


    public static String getContentFromClasspath(String resourcePath) {
        InputStream inputStream = BatchConfiguration.class.getResourceAsStream(resourcePath);
        java.util.Scanner scanner = new java.util.Scanner(inputStream, "UTF-8").useDelimiter("\\A");
        String theString = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return theString;
    }

}
