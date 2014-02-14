package com.epicsell.tests.admin.automation.export;

import com.epicsell.TestCore;
import com.epicsell.beans.ProductUploadingFileHeaders;
import com.epicsell.beans.TestCategory;
import com.epicsell.drivers.FirefoxTestDriver;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Settings;
import com.epicsell.pages.wizard.SiteLogin;
import com.epicsell.profiles.FirefoxProfileForFileDownloading;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 18.05.13
 */
@Category(TestCategory.Ignored.class)
public class ProductsExport extends TestCore {
    File csv;

    @Ignore("Не скачивается файл с продуктами")
    @FirefoxTestDriver(profile = FirefoxProfileForFileDownloading.class)
    @Test
    public void fileUploading() throws Exception {
        String extension = "csv";
        String directoryPath = new File("").getAbsolutePath() + File.separator + "files";
        Settings settings = ((Settings) new SiteLogin(getWebDriver())
                .login(test_shop)
                .openMenuItem(AdminBasePage.MenuItem.SETTINGS));
        List<String> files = Arrays.asList(new File(directoryPath).list(settings.getFilenameFilter(extension)));
        settings.openSettingsItem(Settings.SettingsItem.EXPORT);
        String filePath = settings.exportProducts(files, directoryPath, extension);
        csv = new File(filePath);
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

    @FirefoxTestDriver(profile = FirefoxProfileForFileDownloading.class)
    @Test
    public void closeUploadingWithoutSaving() throws Exception {
        Settings settings = ((Settings) new SiteLogin(getWebDriver())
                .login(test_shop)
                .openMenuItem(AdminBasePage.MenuItem.SETTINGS));
        settings.openSettingsItem(Settings.SettingsItem.EXPORT);
        WebElement button = settings.waiter().until(ExpectedConditions.visibilityOf(
                settings.waiter().until(ExpectedConditions.presenceOfElementLocated(Settings.stop_download_button))));
        TimeUnit.SECONDS.sleep(1);
        button.click();
        settings.waiter().until(ExpectedConditions.stalenessOf(button));
        Assert.assertTrue(settings.isElementPresent(Settings.catalog_menu_item));
        logger.info("Тест закрытия формы экспорта файла товаров пройден успешно");
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
