package com.epicsell.drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
@TestDriver(driver = FirefoxDriver.class)
@Retention(value = RetentionPolicy.RUNTIME)       //
@Target(value = ElementType.METHOD)
public @interface UseDriver {
    Class<? extends WebDriver> driver() default FirefoxDriver.class;
}