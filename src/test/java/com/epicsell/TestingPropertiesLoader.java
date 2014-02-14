package com.epicsell;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
public class TestingPropertiesLoader {

    private static final Logger log = Logger.getLogger(TestingPropertiesLoader.class);

    private static final String TESTING_PATH = "Testing.properties";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle("Testing");

    public static String getProperty(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return null;
        }
    }

    public static Properties getTestingProperties() {
        return initProperties(TESTING_PATH);
    }

    private static Properties initProperties(String path) {
        InputStream stream;
        Properties properties = null;
        try {
            stream = TestingPropertiesLoader.class.getClassLoader().getResourceAsStream(path);
            properties = new Properties();
            properties.load(stream);
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException on method initProperties  \n File in path  = '" + path + "' are not found");
        } catch (IOException e) {
            log.error("IOException on method initProperties");
        }
        return properties;
    }

}
