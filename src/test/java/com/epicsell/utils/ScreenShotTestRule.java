package com.epicsell.utils;

import com.epicsell.TestCore;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.04.13
 */
public class ScreenShotTestRule {
    public void captureScreenshot(String fileName) {
        try {
            fileName = fileName.replace(".", "_");
            File screenShot = TestCore.getWebDriver().getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenShot, new File((new File("").getAbsolutePath()) + File.separator
                    + "files" + File.separator + "artifacts" + File.separator + fileName + System.currentTimeMillis() + ".png"));
        } catch (Exception e) {
            //
        }
    }
}
