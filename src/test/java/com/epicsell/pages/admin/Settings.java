package com.epicsell.pages.admin;

import com.epicsell.beans.Shop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Jane T.
 * Date: 07.05.13
 */
@SuppressWarnings("unused")
public class Settings extends AdminBasePage {
    public static final By settings = By.xpath("//a[@class='settings']");
    public static final By delivery = By.xpath("//a[@class='delivery']");
    public static final By payment = By.xpath("//a[@class='payment']");
    public static final By product_export = By.xpath("//a[@class='export']");
    public static final By product_import = By.xpath("//a[@class='import']");
    public static final By domains = By.xpath("//a[@class='domains']");

    // automation menu tab
    public static final By automation_menu_link = By.xpath("//ul[@id][contains(@class,'toolbox__nav')]/li[contains(@class,'csv')]/a");
    public static final By import_menu_link = By.xpath("//a[@class='import']");
    public static final By file_upload_form = By.xpath("//form[@id='fileUploadForm']");
    public static final By begin_upload_button = By.xpath("//form[@id='fileUploadForm']//div[@class='modal-footer']/button[@type='submit']");
    public static final By begin_download_button = By.xpath("//div[contains(@class,'modal')]//a");
    public static final By stop_download_button = By.xpath("//div[contains(@class,'modal')]//button");
    public static final By products_upload_input = By.xpath("//form[@id='fileUploadForm']//input[@type='file']");
    public static final By uploading_error_message = By.xpath("//div[@id='yw0']");
    public static final By footer_uploading_form = By.xpath("//div[contains(@class,'modal-footer')]");
    public static final By loader = By.xpath("//div[@id='loading']");
    public static final By upload_file_success_message = By.xpath("//form[@id='fileUploadForm']/div[contains(@class,'modal-body')]//div[@class='success']");
    public static final By upload_file_error_message = By.xpath("//div[contains(@class,'alert-error')]");
    public static final By download_sample_button = By.xpath("//a[@class='btn pull-left']");


    public Settings(WebDriver webDriver) {
        super(webDriver);
    }

    public Settings uploadProductsFile(Shop shop, String filePath) {
        WebDriverWait wait = waiter();
        findElement(Catalog.settings_menu_item).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(import_menu_link)).click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(file_upload_form));
        wait.until(ExpectedConditions.visibilityOfElementLocated(file_upload_form));
        wait.until(ExpectedConditions.elementToBeClickable(begin_upload_button));
        if (filePath != null) {
            WebElement input = findElement(products_upload_input);
            executeScript("arguments[0].style.visibility = 'visible'; " +
                    "arguments[0].style.height = '1px'; " +
                    "arguments[0].style.width = '1px'; " +
                    "arguments[0].style.opacity = 1", input);
            input.sendKeys(filePath);
        }
        findElement(begin_upload_button).click();
        loader();
        return this;
    }

    public enum SettingsItem {
        DELIVERY_TYPES, PAYMENTS_TYPES, EXPORT, IMPORT, DOMAINS
    }

    public AdminBasePage openSettingsItem(SettingsItem item) {
        switch (item) {
            case DELIVERY_TYPES:
                waiter().until(ExpectedConditions.presenceOfElementLocated(delivery)).click();
                return new DeliveryTypes(this);
            case PAYMENTS_TYPES:
                waiter().until(ExpectedConditions.presenceOfElementLocated(payment)).click();
                return new PaymentTypes(this);
            case EXPORT:
                waiter().until(ExpectedConditions.presenceOfElementLocated(product_export)).click();
                return this;
            case IMPORT:
                waiter().until(ExpectedConditions.presenceOfElementLocated(product_import)).click();
                return this;
            case DOMAINS:
                waiter().until(ExpectedConditions.presenceOfElementLocated(domains)).click();
                return new DomainManager(this);
            default:
                return null;
        }
    }

    public String waitForFileToBeDownloaded(final List<String> files, final String directoryPath, final String extention) {
        final FilenameFilter filter = getFilenameFilter(extention);
        String filePath = new WebDriverWait(this, 60).until(new ExpectedCondition<String>() {
            @Override
            public String apply(org.openqa.selenium.WebDriver driver) {
                List<String> filesAfterDownloading = new ArrayList<String>();
                filesAfterDownloading.addAll(Arrays.asList(new File(directoryPath).list(filter)));
                filesAfterDownloading.removeAll(files);
                if (filesAfterDownloading.size() > 0) {
                    return filesAfterDownloading.get(0);
                } else {
                    return null;
                }
            }
        });
        try {
            File testFile = new File(filePath);
            File testFileTemp = new File(filePath + ".part");
            long size = 0;
            long newSize = testFile.length();
            long identicalStepsCounter = 0;
            while (identicalStepsCounter < 3 || testFileTemp.exists()) {
                if (!(size == newSize) || testFileTemp.exists()) {
                    size = newSize;
                    TimeUnit.MILLISECONDS.sleep(200);
                    newSize = testFile.length();
                } else {
                    identicalStepsCounter++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public FilenameFilter getFilenameFilter(final String extention) {
        return new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("." + extention);
            }
        };
    }

    public String exportProducts(final List<String> files, final String directoryPath, final String extention) {
        waiter().until(ExpectedConditions.visibilityOf(
                waiter().until(ExpectedConditions.presenceOfElementLocated(begin_download_button)))).click();
        return waitForFileToBeDownloaded(files, directoryPath, extention);
    }
}
