package com.epicsell.profiles;

import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
public class FirefoxSimpleProfile {
    private final FirefoxProfile profile;

    FirefoxSimpleProfile() {
        this.profile = new FirefoxProfile();
    }

    public FirefoxProfile getProfile() {
        return this.profile;
    }
}
