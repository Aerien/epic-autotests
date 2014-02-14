package com.epicsell.tests.admin.automation.export;

import com.epicsell.TestCore;
import com.epicsell.beans.ProductUploadingFileHeaders;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.ExtendedWebDriver;
import com.epicsell.drivers.FirefoxTestDriver;
import com.epicsell.links.AdminLinks;
import com.epicsell.pages.admin.Catalog;
import com.epicsell.pages.admin.Settings;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.profiles.FirefoxProfileForFileDownloading;
import com.epicsell.utils.TestUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.04.13
 */
@Category(TestCategory.Ignored.class)
public class DownloadFileSample extends TestCore {
    File csv;

    @FirefoxTestDriver(profile = FirefoxProfileForFileDownloading.class)
    @Test
    public void fileSampleUploading() throws Exception {
        ExtendedWebDriver DRIVER = getWebDriver();
        new SiteLogin(DRIVER).login(test_shop);
        WebDriverWait wait = DRIVER.waiter();
        if (!DRIVER.getCurrentUrl().endsWith(AdminLinks.ADMIN_ORDERS.$())) {
            DRIVER.navigate().to(test_shop.getShopUrl() + AdminLinks.ADMIN_ORDERS.$());
        }
        DRIVER.findElement(Catalog.settings_menu_item).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(Settings.import_menu_link)).click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(Settings.file_upload_form));
        wait.until(ExpectedConditions.visibilityOfElementLocated(Settings.file_upload_form));
        csv = TestUtils.downloadFileFromUrl(DRIVER.findElement(Settings.download_sample_button).getAttribute("href"),
                new File("").getAbsolutePath() + File.separator + "files" + File.separator + "sample.csv");
        verifyCsvSample();
        logger.info("Тест загрузки образца файла товаров пройден успешно");
    }

    private void verifyCsvSample() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv), "cp1251"));
        assertEquals(ProductUploadingFileHeaders.fullValidHeasder, reader.readLine());
        String line;
        while ((line = reader.readLine()) != null) {
            assertNotEquals("", line);
        }
    }

    @After
    public void delete() {
        if (csv != null && csv.exists()) {
            if (csv.delete()) {
                logger.info("Can't delete file " + csv.getAbsolutePath());
            }
        }
    }
}
