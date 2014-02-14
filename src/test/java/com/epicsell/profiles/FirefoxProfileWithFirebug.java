package com.epicsell.profiles;

import com.epicsell.utils.TestUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.io.IOException;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
public class FirefoxProfileWithFirebug extends FirefoxSimpleProfile {
    private final FirefoxProfile profile;


    public FirefoxProfileWithFirebug() {
        FirefoxProfile ffProfile = (new FirefoxProfileForFileDownloading()).getProfile();
        try {
            ffProfile.addExtension(new File(TestUtils.getProjectLocation() + "/extensions/firebug.xpi"));
            ffProfile.addExtension(new File(TestUtils.getProjectLocation() + "/extensions/xpath_checker.xpi"));
            ffProfile.setPreference("extensions.firebug.currentVersion", "1.9.2");
            ffProfile.setPreference("extensions.firebug.allPagesActivation", "off");
            ffProfile.setPreference("extensions.firebug.defaultPanelName", "net");
            ffProfile.setPreference("extensions.firebug.net.enableSites", true);
        } catch (IOException ioe) {
            Logger logger = Logger.getLogger(FirefoxProfileForFileDownloading.class);
            logger.error(ioe);
        }
        this.profile = ffProfile;
    }

    @Override
    public FirefoxProfile getProfile() {
        return (new FirefoxProfileWithFirebug()).profile;
    }
}

