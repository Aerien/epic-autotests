package com.epicsell.profiles;

import com.epicsell.TestCore;
import com.epicsell.utils.TestUtils;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
public class ChromeProfileForFileDownloading extends ChromeSimpleProfile {
    private final DesiredCapabilities capabilities;

    public ChromeProfileForFileDownloading() {
        DesiredCapabilities chromeCapabilities = (new ChromeSimpleProfile()).getCapabilities();
        chromeCapabilities.setCapability("download.prompt_for_download", "false");
        chromeCapabilities.setCapability("download.default_directory", TestUtils.getProjectLocation() + File.separator + "files" + File.separator);
        chromeCapabilities.setCapability("savefile.default_directory", TestUtils.getProjectLocation() + File.separator + "files" + File.separator);
        chromeCapabilities.setCapability("homepage_is_newtabpage", false);
        chromeCapabilities.setCapability("homepage_changed", TestCore.testProperties.getProperty("url"));
        this.capabilities = chromeCapabilities;
    }

    public DesiredCapabilities getCapabilities() {
        return this.capabilities;
    }
}
