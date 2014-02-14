package com.epicsell.runners;

import com.epicsell.TestCore;
import com.epicsell.drivers.*;
import com.epicsell.profiles.FirefoxProfileForFileDownloading;
import org.apache.log4j.Logger;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
@SuppressWarnings("unchecked")
public class TestRunner extends BlockJUnit4ClassRunner {

    private static final Logger logger = Logger.getLogger(TestRunner.class);

    public TestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Class driverClass = null;
        DesiredCapabilities capabilities = null;
        FirefoxProfile profile = null;
        Annotation annotation = null;
        for (Annotation annotation1 : method.getAnnotations()) {
            try {
                if (Class.forName(annotation1.annotationType().getName()).getAnnotation(TestDriver.class) != null) {
                    annotation = annotation1;
                    break;
                }
            } catch (ClassNotFoundException e) {
                logger.error(e);
            }
        }
        if (annotation != null) {
            Class annotationClass = annotation.annotationType();
            if (annotationClass.isAssignableFrom(FirefoxTestDriver.class)) {
                FirefoxTestDriver driver = (FirefoxTestDriver) annotation;
                try {
                    profile = (driver.profile().getConstructor().newInstance()).getProfile();
                } catch (Exception e) {
                    logger.error(e);
                }
                driverClass = driver.driver();
            } else if (annotationClass.isAssignableFrom(ChromeTestDriver.class)) {
                ChromeTestDriver driver = (ChromeTestDriver) annotation;
                try {
                    capabilities = (driver.capabilities().newInstance()).getCapabilities();
                } catch (Exception e) {
                    logger.error(e);
                }
                driverClass = driver.driver();
            } else if (annotationClass.isAssignableFrom(UseDriver.class)) {
                UseDriver testDriver = (UseDriver) annotation;
                driverClass = testDriver.driver();
            } else if (annotationClass.isAssignableFrom(TestDriver.class)) {
                TestDriver testDriver = (TestDriver) annotation;
                driverClass = testDriver.driver();
            }
        } else {
            driverClass = FirefoxDriver.class;
        }
        WebDriver webDriver = null;
        try {
            if (capabilities != null) {
                webDriver = (WebDriver) driverClass.getConstructor(Capabilities.class).newInstance(capabilities);
            } else if (profile != null) {
                webDriver = (WebDriver) driverClass.getConstructor(FirefoxProfile.class).newInstance(profile);
            } else {
                assert driverClass != null;
                webDriver = (WebDriver) driverClass.newInstance();
            }
        } catch (Exception e) {
//            logger.fatal(e);
        }
        if (webDriver == null) {
            logger.error("WebDriver id null");
            webDriver = new FirefoxDriver((new FirefoxProfileForFileDownloading()).getProfile());
        }
        webDriver.manage().timeouts().implicitlyWait(1L, TimeUnit.SECONDS);
        try {
            webDriver.manage().window().maximize();
        } catch (Exception we) {
            //
        }
        webDriver.get(TestCore.testProperties.getProperty("url"));
//        ____________________________________________
        TestCore.setWebDriver(new ExtendedWebDriver(webDriver));
        return super.methodBlock(method);
    }


}