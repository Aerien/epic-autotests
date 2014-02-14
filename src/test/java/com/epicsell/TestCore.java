package com.epicsell;

import com.epicsell.beans.Shop;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.profiles.FirefoxProfileForFileDownloading;
import com.epicsell.runners.TestRunner;
import com.epicsell.utils.ScreenShotTestRule;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
@RunWith(TestRunner.class)
public class TestCore extends Assert {
    public static Shop test_shop;
    public static Shop test_shop2;
    public static Shop test_shop3;
    public static final Logger logger = Logger.getLogger(TestCore.class);
    public static final Properties testProperties = TestingPropertiesLoader.getTestingProperties();

    private static final ThreadLocal<ExtendedWebDriver> webDriver = new InheritableThreadLocal<ExtendedWebDriver>();

    public static ExtendedWebDriver getWebDriver() {
        return webDriver.get();
    }

    public static void setWebDriver(ExtendedWebDriver driver) {
        Thread.currentThread();
        TestCore.webDriver.set(driver);
    }

    //__________________________________________________________________________________________________________________
    @BeforeClass
    public static void setUp() {
        Shop shop = new Shop();
        shop.setEmail(TestCore.testProperties.getProperty("email"));
        shop.setPassword(TestCore.testProperties.getProperty("password"));
        shop.setShopName(TestCore.testProperties.getProperty("shop_name"));
        shop.setCity("Kharkiv");
        shop.setCountry("Украина");
        shop.setClientName("team city");
        shop.setActivityType("Электроника");
        test_shop = shop;
        Shop shop2 = new Shop();
        shop2.setEmail(TestCore.testProperties.getProperty("email"));
        shop2.setPassword(TestCore.testProperties.getProperty("password"));
        shop2.setShopName(TestCore.testProperties.getProperty("shop_name2"));
        shop2.setCity("Kharkiv");
        shop2.setCountry("Украина");
        shop2.setClientName("team city");
        shop2.setActivityType("Электроника");
        test_shop2 = shop2;
        Shop shop3 = new Shop();
        shop3.setEmail(TestCore.testProperties.getProperty("email"));
        shop3.setPassword(TestCore.testProperties.getProperty("password"));
        shop3.setShopName(TestCore.testProperties.getProperty("shop_name3"));
        shop3.setCity("Kharkiv");
        shop3.setCountry("Украина");
        shop3.setClientName("team city");
        shop3.setActivityType("Электроника");
        test_shop3 = shop3;
    }

    @Before
    public void setUrl() {
    }

    @AfterClass
    public static void tearDown() {
        ExtendedWebDriver webDriver = getWebDriver();
        try {
            if (webDriver != null) {
                webDriver.quit();
                TestCore.webDriver.remove();
            }
        } catch (UnreachableBrowserException ex) {
        }
    }

    @After
    public void browserShutDown() {
        getWebDriver().close();
    }

    @Rule
    public TestRule watchman = new TestWatcher() {
        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }

        @Override
        protected void succeeded(Description description) {
            //
        }

        @Override
        protected void failed(Throwable e, Description description) {
            logger.error(TestCore.getWebDriver().getCurrentUrl());
            new ScreenShotTestRule().captureScreenshot(description.getClassName() + "_" + description.getMethodName());
        }

        @Override
        protected void starting(Description description) {
            super.starting(description);
        }

        @Override
        protected void finished(Description description) {
            super.finished(description);
        }
    };

    public static void setDriver() {
        WebDriver driver = new FirefoxDriver((new FirefoxProfileForFileDownloading()).getProfile());
        driver.manage().timeouts().implicitlyWait(1L, TimeUnit.SECONDS);
        try {
            driver.manage().window().maximize();
        } catch (Exception we) {
            //
        }
        driver.get(testProperties.getProperty("url"));
        TestCore.setWebDriver(new ExtendedWebDriver(driver));
    }

    public void testPassed() {
    }

    public void testFailed() {
        getWebDriver().takeScreenShoot();
    }
}
