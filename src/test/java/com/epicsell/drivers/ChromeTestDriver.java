package com.epicsell.drivers;

import com.epicsell.profiles.ChromeSimpleProfile;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
@TestDriver(driver = ChromeDriver.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface ChromeTestDriver {
    Class<? extends WebDriver> driver() default ChromeDriver.class; //

    Class<? extends ChromeSimpleProfile> capabilities() default ChromeSimpleProfile.class;
}
