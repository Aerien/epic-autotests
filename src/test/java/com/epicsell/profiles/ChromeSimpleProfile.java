package com.epicsell.profiles;

import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
public class ChromeSimpleProfile {
    private final DesiredCapabilities capabilities;

    ChromeSimpleProfile() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        this.capabilities = DesiredCapabilities.chrome();
    }

    public DesiredCapabilities getCapabilities() {
        return this.capabilities;
    }
}
