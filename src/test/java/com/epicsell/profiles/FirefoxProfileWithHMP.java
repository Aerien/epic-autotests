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
public class FirefoxProfileWithHMP {
    private FirefoxProfile profile;


    public FirefoxProfileWithHMP() {
        new FirefoxProfileWithHMP("10.1.1.20");
    }

    public FirefoxProfileWithHMP(String ip) {
        FirefoxProfile ffProfile = (new FirefoxProfileForFileDownloading()).getProfile();
        try {
            ffProfile.addExtension(new File(TestUtils.getProjectLocation() + "/extensions/modify_headers.xpi"));
            ffProfile.setPreference("modifyheaders.config.active", true);
            ffProfile.setPreference("modifyheaders.config.alwaysOn", true);
            ffProfile.setPreference("modifyheaders.headers.count", 1);
            ffProfile.setPreference("modifyheaders.headers.action0", "Add");
            ffProfile.setPreference("modifyheaders.headers.name0", "X-IP");
            ffProfile.setPreference("modifyheaders.headers.value0", ip);
            ffProfile.setPreference("modifyheaders.headers.enabled0", true);
        } catch (IOException e) {
            Logger logger = Logger.getLogger(FirefoxProfileForFileDownloading.class);
            logger.error(e);
        }
        this.profile = ffProfile;
    }

    public FirefoxProfile getProfile() {
        return this.profile;
    }
}