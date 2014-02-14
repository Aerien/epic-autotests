package com.epicsell.profiles;

import com.epicsell.TestCore;
import com.epicsell.utils.TestUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;
import java.io.IOException;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
public class FirefoxProfileForFileDownloading extends FirefoxSimpleProfile {
    private FirefoxProfile profile = null;


    public FirefoxProfileForFileDownloading() {
        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setPreference("browser.download.dir", TestUtils.getProjectLocation() + File.separator + "files" + File.separator);
        ffProfile.setPreference("browser.download.folderList", 2);
        ffProfile.setPreference("browser.startup.homepage", TestCore.testProperties.getProperty("url"));
        ffProfile.setPreference("browser.download.manager.closeWhenDone", true);
        ffProfile.setPreference("webdriver.enable.native.events", true);
        ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv");
        ffProfile.setEnableNativeEvents(true);
//        profile.setPreference("webdriver.load.strategy", "unstable");
        ffProfile.setPreference("webdriver.firefox.logfile", TestUtils.getProjectLocation() + File.separator +
                "files" + File.separator + "logs" + File.separator + "fflog" + File.separator + "fflog.log");
        try {
            ffProfile.addExtension(new File(TestUtils.getProjectLocation() + "/extensions/JSErrorCollector.xpi"));
        } catch (IOException ioe) {
            Logger logger = Logger.getLogger(FirefoxProfileForFileDownloading.class);
            logger.error(ioe);
        }
        this.profile = ffProfile;
    }

    @Override
    public FirefoxProfile getProfile() {
        return new FirefoxProfileForFileDownloading().profile;
    }
}
