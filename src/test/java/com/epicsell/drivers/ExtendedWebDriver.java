package com.epicsell.drivers;

import com.epicsell.TestCore;
import com.epicsell.beans.Link;
import com.epicsell.pages.admin.AdminBasePage;
import com.epicsell.pages.admin.Journal;
import com.epicsell.pages.admin.Settings;
import com.epicsell.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;

/**
 * User: Jane T.
 * Date: 09.02.13
 */
@SuppressWarnings("unused")
public class ExtendedWebDriver implements WebDriver, TakesScreenshot, JavascriptExecutor, HasInputDevices {
    public Logger logger = Logger.getLogger(ExtendedWebDriver.class);
    public final WebDriver webDriver;
    private WebDriverWait wait;

    public static final By orders_menu_item = By.xpath("//ul[contains(@class,'toolbox')]/li[contains(@class,'orders')]//a");
    public static final By catalog_menu_item = By.xpath("//ul[contains(@class,'toolbox')]/li[contains(@class,'catalog')]//a");
    public static final By categories_menu_item = By.xpath("//ul[contains(@class,'toolbox')]/li[contains(@class,'category')]//a");
    public static final By pages_menu_item = By.xpath("//ul[contains(@class,'toolbox')]/li[contains(@class,'pages')]//a");
    public static final By settings_menu_item = By.xpath("//ul[contains(@class,'toolbox')]/li[contains(@class,'system')]//a");
    public static final By logout_menu_link = By.xpath("//ul[contains(@class,'toolbox')]/li[contains(@class,'logout')]/a");

    public static final By tab_caption = By.xpath("//div[@class='es-head row']/h1");
    public static final By create_bean_button = By.xpath("//div[@class='es-head row']/a");
    public static final By search_button = By.xpath("//div[@class='es-head row']/div//i");
    public static final By search_input = By.id("prependedInput");
    public static final By popup = By.xpath("//div[contains(@class,'humane-jackedup')]");

    public static final By content_text_area = By.xpath("//div[contains(@class,'redactor_editor')]");
    public static final By video_code_text_area = By.xpath("//textarea[@id='redactor_insert_video_area']");
    public static final By image_url_input = By.xpath("//input[@id='redactor_file_link']");
    public static final By insert_image_btn = By.xpath("//input[@id='redactor_upload_btn']");
    public static final By insert_video_button = By.xpath("//input[@id='redactor_insert_video_btn']");

    public static final By drop_down_menu = By.xpath("//div[@class='redactor_dropdown'][not(contains(@style,'display: none;'))]");
    //-------------------LINK FORM----------------------------------------------------------------
    public static final By link_insertion_form = By.xpath("//form[@id='redactorInsertLinkForm']");
    public static final By link_url_input = By.xpath("//input[@id='redactor_link_url']");
    public static final By link_text_input = By.xpath("//input[@id='redactor_link_url_text']");
    public static final By new_tab_checkbox = By.xpath("//input[@id='redactor_link_blank']");
    public static final By link_insert_btn = By.xpath("//input[@id='redactor_insert_link_btn']");
    public static final By mailto_input = By.xpath("//input[@id='redactor_link_mailto']");
    public static final By mailto_text_input = By.xpath("//input[@id='redactor_link_mailto_text']");
    public static final By anchor_input = By.xpath("//input[@id='redactor_link_anchor']");
    public static final By anchor_text_input = By.xpath("//input[@id='redactor_link_anchor_text']");
    //--------------------------------------------------------------------------------------------
    public static final By html_mode_link = By.xpath("//a[contains(@class,'redactor_btn_html')]");
    public static final By bold_mode_link = By.xpath("//a[contains(@class,'redactor_btn_bold')]");
    public static final By italic_mode_link = By.xpath("//a[contains(@class,'redactor_btn_italic')]");
    public static final By video_mode_link = By.xpath("//a[contains(@class,'redactor_btn_video')]");
    public static final By image_mode_link = By.xpath("//a[contains(@class,'redactor_btn_image')]");
    public static final By link_mode_link = By.xpath("//a[contains(@class,'redactor_btn_link')]");
    public static final By horizontal_rule_mode_link = By.xpath("//a[contains(@class,'redactor_btn_horizontalrule')]");
    public static final By font_color_mode_link = By.xpath("//a[contains(@class,'redactor_btn_fontcolor')]");
    public static final By bg_color_mode_link = By.xpath("//a[contains(@class,'redactor_btn_backcolor')]");
    public static final By formatting_mode_link = By.xpath("//a[contains(@class,'redactor_btn_formatting')]");
    public static final By alignment_mode_link = By.xpath("//a[contains(@class,'redactor_btn_alignment')]");
    public static final By full_screen_mode_link = By.xpath("//a[contains(@class,'redactor_btn_fullscreen')]");
    //---------------------------------------------------------------------------------------------
    public static final By full_screen_mode_block = By.xpath("//div[@class='redactor_box redactor_box_fullscreen']");

    public ExtendedWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Long.parseLong(TestCore.testProperties.getProperty("timeout")));
    }

    public WebDriver getWebDriver() {
        return this.webDriver;
    }

    //--------------------------------------------------------------------------

    @Override
    public void get(String url) {
        webDriver.get(url);
    }

    public ExtendedWebDriver open(String url) {
        webDriver.get(url);
        return this;
    }
    //--------------------------------------------------------------------------

    @Override
    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return webDriver.getTitle();
    }

    //--------------------------------------------------------------------------
    @Override
    public WebElement findElement(By by) {
        return webDriver.findElement(by);
    }

    public WebElement findElement(String by) {
        return webDriver.findElement(By.xpath(by));
    }
    //--------------------------------------------------------------------------

    @Override
    public List<WebElement> findElements(By by) {
        return webDriver.findElements(by);
    }

    public List<WebElement> findElements(String by) {
        return webDriver.findElements(By.xpath(by));
    }
    //--------------------------------------------------------------------------

    @Override
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    @Override
    public void close() {
        webDriver.close();
    }

    @Override
    public void quit() {
        webDriver.quit();
    }

    @Override
    public Set<String> getWindowHandles() {
        return webDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return webDriver.getWindowHandle();
    }

    @Override
    public TargetLocator switchTo() {
        return webDriver.switchTo();
    }

    @Override
    public Navigation navigate() {
        return webDriver.navigate();
    }

    @Override
    public Options manage() {
        return webDriver.manage();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) this.getWebDriver()).getScreenshotAs(target);
    }

    //--------------------------------------------------------------------------
    public String getLocation() {
        return getCurrentUrl();
    }


    public Integer getResponseCode(String link) throws Exception {
        URL url = new URL(link);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.connect();
        return connection.getResponseCode();
    }

    public void takeScreenShoot() {
        try {
            int traceLevel = 1;
            String className = "";
            while (className.isEmpty() ||
                    className.equals("SiteScenarios") ||
                    className.equals("AdminScenarios") ||
                    className.equals("ERPScenarios") ||
                    className.equals("TestCore")) {
                className = (new Throwable()).getStackTrace()[traceLevel].getClassName();
                className = className.substring(className.lastIndexOf(".") + 1, className.length());
                traceLevel++;
            }
            String path = TestUtils.getProjectLocation() + File.separator + "files" + File.separator + className;
            path += ".png";
            logger.info("(" + className + ") " + webDriver.getCurrentUrl());
            takeScreenShoot(path);
        } catch (WebDriverException wde) {
            logger.error("Could not take screenshot of current page " + webDriver.getCurrentUrl());
        }
    }

    public void takeScreenShoot(String filePath) {
        try {
            File scrShot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
            File fileForScreenShot = new File(filePath);
            FileUtils.copyFile(scrShot, fileForScreenShot);
        } catch (IOException ioe) {
            logger.error("Ошибка сохранения файла! \n", ioe);
        } catch (WebDriverException wde) {
            logger.error("Could not take screenshot of current page " + webDriver.getCurrentUrl());
        }
    }

    public void refresh() {
        this.navigate().refresh();
        try {
            this.switchTo().alert().dismiss();
        } catch (NoAlertPresentException nape) {
            //
        }
    }

    public ExtendedWebDriver loader() {
        try {
            new WebDriverWait(webDriver, 2).until(ExpectedConditions.visibilityOfElementLocated(Settings.loader));
        } catch (TimeoutException te) {
            //
        }
        try {
            new WebDriverWait(webDriver, Long.parseLong(TestCore.testProperties.getProperty("timeout")))
                    .until(ExpectedConditions.invisibilityOfElementLocated(Settings.loader));
        } catch (TimeoutException te) {
            //
        }
        return this;
    }

    public void setAttributeToWebElement(WebElement webElement, String attribute,
                                         String attributeValue) {
        executeScript("arguments[0].setAttribute('" + attribute + "', arguments[1]);",
                webElement, attributeValue);
    }


    public String getInnerHtml(WebElement element) {
        return (String) ((JavascriptExecutor) this.webDriver).executeScript("return arguments[0].innerHTML;", element);
    }

    public void blurField(WebElement webElement) {
//        webElement.sendKeys(Keys.TAB);
        String script = "arguments[0].blur();";
        ((JavascriptExecutor) webDriver).executeScript(script, webElement);
    }

    public void type(WebElement element, String text) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].textContent = \"" + text + "\";", element);
    }
    //--------------------------------------------------------------------------

    public void click(String locator, String elementName) {
        try {
            findElement(locator).click();
        } catch (ElementNotVisibleException enve) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not visible on page " + getLocation());
            throw enve;
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    public void click(WebElement source) {
        String mouseDownMove = "var mouseMove = document.createEvent(\"MouseEvents\");" +
                "mouseMove.initMouseEvent(\"click\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);" +
                "arguments[0].dispatchEvent(mouseMove);";
        executeScript(mouseDownMove, source);
    }

    public void click(By locator) {
        click(findElement(locator));
    }

    /**
     * <meta charset="UTF-8">
     * <p>Клик на Element, который получен по его xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     */
    public void click(String locator) {
        click(locator, locator);
    }

    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Выбор Elementа, который получен по его xpath или id</p>
     * <p>Отностится к Elementам типа checkbox </p>
     *
     * @param locator xpath или id веб-Elementа
     */
    public void check(String locator) {
        WebElement element = findElement(By.xpath(locator));
        if (!element.isSelected()) {
            element.click();
        }
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Печать текста в Input, который получен по его xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     * @param message текст, который будет напечатан в инпуте
     */
    public void typeHidden(String locator, String message) {
        ((JavascriptExecutor) getWebDriver()).executeScript("function a(){var m_obj = document.evaluate(\"." + locator + "\", " +
                "document.body, null, XPathResult.ANY_TYPE, null).iterateNext();\n" +
                "m_obj.value =\"" + message + "\";}");
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Печать текста в Input, который получен по его xpath или id</p>
     *
     * @param locator     xpath или id веб-Elementа
     * @param elementName назначение эелемента
     * @param text        текст, который будет напечатан в инпуте
     */
    public void type(String locator, String elementName, String text) {
        try {
            WebElement element = findElement(locator);
            element.sendKeys(Keys.CONTROL + "a");
            element.sendKeys(text);
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Печать текста в Input, который получен по его xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     * @param text    текст, который будет напечатан в инпуте
     */
    public void type(String locator, String text) {
        type(locator, locator, text);
    }
    //--------------------------------------------------------------------------

    public void clear(String locator, String elementName) {
        try {
            findElement(locator).clear();
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Удаление текста из Input, который получен по его xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     */
    public void clear(String locator) {
        clear(locator, locator);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Выбор Elementа из выпадающего списка, который получен по его xpath или id</p>
     *
     * @param locator     xpath или id выпадающего списка
     * @param elementName назначение Elementа
     * @param option      текст или index выбираемого Elementа списка
     */
    public void select(String locator, String elementName, String option) {
        try {
//            assertTrue("Element " + locator + " не существует на странице " + getLocation(), isElementPresent(locator, driver));
            Select select = new Select(findElement(locator));
            if (option.contains("value=")) {
                select.selectByValue(option.replace("value=", ""));
            } else if (option.contains("index=")) {
                select.selectByIndex(Integer.parseInt(option.replace("index=", "")));
            } else {
                select.selectByVisibleText(option);
            }
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Выбор Elementа из выпадающего списка, который получен по его xpath или id</p>
     *
     * @param locator xpath или id выпадающего списка
     * @param option  текст или index выбираемого Elementа списка
     */
    public void select(String locator, String option) {
        select(locator, locator, option);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Вернуться на предыдущую открытую страницу</p>
     */
    public void goBack() {
        WebDriver driver = getWebDriver();
        driver.navigate().back();
        driver.navigate().refresh();
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Навести курсор мыши на Element, полученный по xpath или id</p>
     *
     * @param locator     xpath или id веб-Elementа
     * @param elementName назначение Elementа
     */
    public void mouseOver(String locator, String elementName) {
        try {
            Actions action = new Actions(this);
            WebElement element = findElement(locator);
            action.moveToElement(element);
            action.perform();
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    public ExtendedWebDriver mouseMove(WebElement source) {
        StringBuilder sb = new StringBuilder();
        sb.append("var mde = document.createEvent(\"MouseEvents\");");
        sb.append("mde.initMouseEvent(\"mousemove\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);");
        sb.append("arguments[0].dispatchEvent(mde);");
        executeScript(sb.toString(), source);
        return this;
    }

    public ExtendedWebDriver mouseOver(WebElement source) {
        StringBuilder sb = new StringBuilder();
        sb.append("var mde = document.createEvent(\"MouseEvents\");");
        sb.append("mde.initMouseEvent(\"mouseover\", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);");
        sb.append("arguments[0].dispatchEvent(mde);");
        executeScript(sb.toString(), source);
        return this;
    }

    /**
     * <meta charset="UTF-8">
     * <p>Навести курсор мыши на Element, полученный по xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     */
    public void mouseOver(String locator) {
        mouseOver(locator, locator);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Получение тайтла текущей страницы</p>
     *
     * @param driver webdriver
     * @return String page title
     */
    public String getTitle(WebDriver driver) {
        String title = driver.getTitle();
        logger.debug("на странице " + getLocation() + " был получен title = " + title);
        return title;
    }

    //--------------------------------------------------------------------------

//    public Double priceToDouble(String price) throws NumberFormatException {
//        return Double.parseDouble(price.replace(" ", "").replace(StringConvertions.PRICE_SEPARATOR.getString(), ".0").replace("руб.", ".0").replace(",", "."));
//    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Получение текста, который содержится в веб-Elementе</p>
     *
     * @param locator     xpath или id выпадающего списка
     * @param elementName назначение Elementа
     * @return String текст, который содержится в веб-Elementе
     */
    public String getText(String locator, String elementName) {
        try {
            WebElement element = findElement(locator);
//            if (element.isDisplayed()) {
            return element.getText();
//            } else {
//                String script = "return arguments[0].innerText";
//                return (String) ((JavascriptExecutor) driver).executeScript(script, element);
//            }
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Получение текста, который содержится в веб-Elementе</p>
     *
     * @param locator xpath или id выпадающего списка
     * @return String текст, который содержится в веб-Elementе
     */
    public String getText(String locator) {
        return getText(locator, locator);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Получение значения атрибута веб-Elementа</p>
     *
     * @param xpath         xpath веб-Elementа
     * @param elementName   назначение эелемнта
     * @param attributeName имя атрибута
     * @return String значение выбранного атрибута
     */
    public String getAttribute(String xpath, String elementName, String attributeName) {
        try {
            return findElement(xpath).getAttribute(attributeName);
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Element " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }


    /**
     * <meta charset="UTF-8">
     * <p>Получение значения атрибута веб-Elementа</p>
     *
     * @param xpath xpath веб-Elementа+"@"+<имя атрибута>, например "//a/@href"
     * @return String значение выбранного атрибута
     */
    public String getAttribute(String xpath) {
        String attribute = xpath.substring(xpath.lastIndexOf("@") + 1, xpath.length());
        String locator = xpath.substring(0, xpath.lastIndexOf("@") - 1);
        return getAttribute(locator, locator, attribute);
    }

    /**
     * <meta charset="UTF-8">
     * <p>Получение значения атрибута веб-Elementа</p>
     *
     * @param xpath         xpath веб-Elementа
     * @param attributeName имя атрибута
     * @return String значение выбранного атрибута
     */
    public String getAttribute(String xpath, String attributeName) {
        return getAttribute(xpath, xpath, attributeName);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Получить значение атрибута value веб-Elementа Input</p>
     *
     * @param locator xpath веб-Elementа Input
     * @return String значение value
     */
    public String getValue(String locator) {
        return getAttribute(locator, locator, "value");
    }

    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Получить количество веб-Elementов, которые можно получить по выбранному xpath</p>
     *
     * @param locator     xpath веб-Elementов
     * @param elementName назначение Elementа
     * @return Integer количество найденных Elementов
     */
    public Integer getElementsCount(String locator, String elementName) {
        try {
            return findElements(By.xpath(locator)).size();
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Elements " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Получить количество веб-Elementов, которые можно получить по выбранному xpath</p>
     *
     * @param locator xpath веб-Elementов
     * @return Integer количество найденных Elementов
     */
    public Integer getElementsCount(String locator) {
        return getElementsCount(locator, locator);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Выбран ли Element типа checkbox или radiobutton</p>
     *
     * @param locator     xpath или id веб-Elementа
     * @param elementName назначение Elementа
     * @return Boolean true = выбран, false = не выбран
     */
    public boolean isChecked(String locator, String elementName) {
        try {
            return findElement(locator).isSelected();
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Elements " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Выбран ли Element типа checkbox или radiobutton</p>
     *
     * @param locator xpath или id веб-Elementа
     * @return Boolean true = выбран, false = не выбран
     */
    public boolean isChecked(String locator) {
        return isChecked(locator, locator);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Видим ли Element, полученный по xpath или id</p>
     *
     * @param locator     xpath или id веб-Elementа
     * @param elementName назначение Elementа
     * @return Boolean true = видим, false = не видим
     */
    public boolean isVisible(String locator, String elementName) {
        try {
            return findElement(locator).isDisplayed();
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Elements " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    /**
     * <meta charset="UTF-8">
     * <p>Видим ли Element, полученный по xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     * @return Boolean true = видим, false = не видим
     */
    public boolean isVisible(String locator) {
        return isVisible(locator, locator);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Существует ли текст text на текущей странице</p>
     *
     * @param text текст, наличие которого проверяется
     * @return Boolean true = существует, false = не существует
     */
    public boolean isTextPresent(String text) {
        return getWebDriver().getPageSource().contains(text);
    }

    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Существует ли веб-Element, полученный по xpath или id</p>
     *
     * @param locator xpath или id веб-Elementа
     * @return Boolean true = существует, false = не существует
     */
    public boolean isElementPresent(By locator) {
        try {
            findElement(locator);
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public boolean isElementPresent(String locator) {
        return isElementPresent(By.xpath(locator));
    }

    //--------------------------------------------------------------------------
    public boolean isElementEnable(String locator, String elementName) {
        try {
            return findElement(locator).isEnabled();
        } catch (NoSuchElementException nsee) {
            String className = (new Throwable()).getStackTrace()[1].getClassName();
            className = className.substring(className.lastIndexOf(".") + 1, className.length());
            logger.warn("(" + className + ") Elements " + elementName + " is not present on page " + getLocation());
            throw nsee;
        }
    }

    public boolean isElementEnable(String locator) {
        return isElementEnable(locator, locator);
    }

    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     *
     * @return true = страница отдает 404, false = никаких ошибок на странице нет
     */
    public boolean is404() {
        return isTextPresent("Error 404");        //

    }

    public boolean is404(String link) {
        boolean result = open(link).is404();
        TestCore.assertFalse("Ошибка на странице " + link, result);
        goBack();
        return result;
    }

    /*---------------------------------Взаимодействие с диалоговым окном браузера-------------------------------------*/
    public Alert waitForAlert(int seconds) {
        Wait<WebDriver> wait = new WebDriverWait(this, seconds).ignoring(NullPointerException.class);
        return wait.until(new ExpectedCondition<Alert>() {
            @Override
            public Alert apply(WebDriver driver) {
                Alert alert = driver.switchTo().alert();
                alert.getText();
                return alert;
            }
        });
    }

    public Alert getAlert() {
        return waitForAlert(10);
    }

    public void clickOkOnAlert() {         // подтвердить диалоговое окно
        getAlert().accept();
    }

    public void clickCancelOnAlert() {     // отменить диалоговое окно
        getAlert().dismiss();
    }

    public String getAlertText() {         // получить текст диалогового окна
        return getAlert().getText();
    }
    //--------------------------------------------------------------------------

    public void resetPageLoadTimeoutToDefault() {
        getWebDriver().manage().timeouts().pageLoadTimeout(0L, TimeUnit.SECONDS);
    }
    //--------------------------------------------------------------------------

    /**
     * <meta charset="UTF-8">
     * <p>Получить значение атрибута href веб-Elementа 'a'</p>
     *
     * @param locator xpath веб-Elementа 'a'
     * @return String значение href
     */
    public String getUrl(String locator) {
        return getAttribute(locator, "href");
    }

    //--------------------------------------------------------------------------

    public void selectFromDiv(String rootLocator, String option) {
        click(rootLocator + "//div[contains(@class,'icon-arrow-down')]");
        new WebDriverWait(this, 30).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id,'listBox')][contains(@style,'display: block')]")));
        String locator = "//div[contains(@style,'block')]//div[contains(@id,'listitem')][span[text()='" + option + "']]";
        Actions builder = new Actions(webDriver);
        try {
            builder.moveToElement(findElement(By.xpath(locator)));
            builder.click(findElement(By.xpath(locator))).perform();
        } catch (ElementNotVisibleException enve) {
            //
        }
    }

    public WebDriverWait waiter() {
        return this.wait;
    }

    public void getScreenShot(String filePath) {
        File scrFile = ((TakesScreenshot) this.getWebDriver()).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(filePath));
        } catch (IOException e) {
            //
        }
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        return ((JavascriptExecutor) this.getWebDriver()).executeScript(s, objects);
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        return ((JavascriptExecutor) this.getWebDriver()).executeAsyncScript(s, objects);
    }

    @Override
    public Keyboard getKeyboard() {
        return ((HasInputDevices) webDriver).getKeyboard();
    }

    @Override
    public Mouse getMouse() {
        return ((HasInputDevices) webDriver).getMouse();
    }

    public ExtendedWebDriver editContent(WebElement editor_field,
                                         String content,
                                         AdminBasePage.Formatting formatting,
                                         AdminBasePage.Alignment alignment,
                                         AdminBasePage.Mode mode,
                                         Link link,
                                         String color) {
        WebElement contentInput = findElement(content_text_area);
        if (mode != null) {
            switch (mode) {
                case ALIGNMENT:
                    Assert.assertNotNull("Alignment type must not be equal null", alignment);
                    contentInput.sendKeys(Keys.CONTROL, "a");
                    contentInput.sendKeys(Keys.BACK_SPACE);
                    contentInput.sendKeys(content);
                    contentInput.sendKeys(Keys.CONTROL, "a");
                    findElement(alignment_mode_link).click();
                    waiter().until(ExpectedConditions.visibilityOfElementLocated(drop_down_menu))
                            .findElement(alignment.getLink()).click();
                    break;
                case FORMATTING:
                    Assert.assertNotNull("Formatting type must not be equal null", formatting);
                    contentInput.sendKeys(Keys.CONTROL, "a");
                    contentInput.sendKeys(Keys.BACK_SPACE);
                    contentInput.sendKeys(content);
                    contentInput.sendKeys(Keys.CONTROL, "a");
                    findElement(formatting_mode_link).click();
                    waiter().until(ExpectedConditions.visibilityOfElementLocated(drop_down_menu))
                            .findElement(formatting.getLink()).click();
                    break;
                case BG_COLOR:
                    findElement(full_screen_mode_link).click();
                    loader();
                    contentInput.sendKeys(content);
                    new AdminBasePage(webDriver).setTextContentStyle(mode, color);
                    findElement(full_screen_mode_link).click();
                    loader();
                    break;
                case FONT_COLOR:
                    findElement(full_screen_mode_link).click();
                    loader();
                    contentInput.sendKeys(content);
                    new AdminBasePage(webDriver).setTextContentStyle(mode, color);
                    findElement(full_screen_mode_link).click();
                    loader();
                    break;
                case HORIZONTAL_RULE:
                    findElement(full_screen_mode_link).click();
                    loader();
                    findElement(horizontal_rule_mode_link).click();
                    loader();
                    findElement(full_screen_mode_link).click();
                    loader();
                    break;
                case LINK:
                    Assert.assertNotNull("link must not be equal to null", link);
                    findElement(full_screen_mode_link).click();
                    findElement(link_mode_link).click();
                    WebDriverWait wait = waiter();
                    wait.until(ExpectedConditions.presenceOfElementLocated(drop_down_menu))
                            .findElement(By.xpath(".//a[text()='Insert link' or text()='Вставить ссылку ...']")).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(link_insertion_form));
                    if (link.getTarget().equals(Link.Target.URL)) {
                        wait.until(ExpectedConditions.visibilityOfElementLocated(link_url_input)).sendKeys(link.getUrl());
                        wait.until(ExpectedConditions.visibilityOfElementLocated(link_text_input)).sendKeys(link.getText());
                        WebElement newTab = wait.until(ExpectedConditions.visibilityOfElementLocated(new_tab_checkbox));
                        if (newTab.isSelected() != link.openInNewTab()) {
                            newTab.click();
                        }
                    } else {
                        By linkType = null;
                        if (link.getTarget().getTarget1() != null) {
                            linkType = By.xpath("//div[@id='redactor_tabs']//a[text()='" + link.getTarget().getTarget() + "' or text()='" + link.getTarget().getTarget1() + "']");
                        } else {
                            linkType = By.xpath("//div[@id='redactor_tabs']//a[text()='" + link.getTarget().getTarget() + "']");
                        }
                        findElement(linkType).click();
                        if (link.getTarget().equals(Link.Target.EMAIL)) {
                            wait.until(ExpectedConditions.visibilityOfElementLocated(mailto_input)).sendKeys(link.getUrl());
                            wait.until(ExpectedConditions.visibilityOfElementLocated(mailto_text_input)).sendKeys(link.getText());
                        } else {
                            wait.until(ExpectedConditions.visibilityOfElementLocated(anchor_input)).sendKeys(link.getUrl());
                            wait.until(ExpectedConditions.visibilityOfElementLocated(anchor_text_input)).sendKeys(link.getText());
                        }
                    }
                    findElement(link_insert_btn).click();
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(link_insertion_form));
                    findElement(full_screen_mode_link).click();
                    break;
                case IMAGE:
                    findElement(full_screen_mode_link).click();
                    findElement(image_mode_link).click();
                    contentInput = waiter().until(ExpectedConditions.visibilityOfElementLocated(image_url_input));
                    contentInput.sendKeys(content);
                    findElement(insert_image_btn).click();
                    findElement(full_screen_mode_link).click();
                    break;
                case VIDEO:
                    findElement(full_screen_mode_link).click();
                    findElement(video_mode_link).click();
                    contentInput = waiter().until(ExpectedConditions.visibilityOfElementLocated(video_code_text_area));
                    contentInput.sendKeys(content);
                    findElement(insert_video_button).click();
                    findElement(full_screen_mode_link).click();
                    break;
                case BOLD:
                    contentInput.sendKeys(content);
                    new AdminBasePage(webDriver).setTextContentStyle(mode);
                    break;
                case ITALIC:
                    contentInput.sendKeys(content);
                    new AdminBasePage(webDriver).setTextContentStyle(mode);
                    break;
                case HTML:
                    findElement(html_mode_link).click();
                    contentInput = findElement(Journal.content_text_area_html_mode);
                    contentInput.sendKeys(Keys.CONTROL, "a");
                    contentInput.sendKeys(Keys.BACK_SPACE);
                    contentInput.sendKeys(content);
                    break;
                default:
                    contentInput.sendKeys(content);
                    break;
            }
        } else {
            contentInput.sendKeys(content);
        }
        return this;
    }

    public ExtendedWebDriver editContent(WebElement editor_field, String content, AdminBasePage.Mode mode) {
        return editContent(editor_field, content, null, null, mode, null, null);
    }

    public ExtendedWebDriver editContent(WebElement editor_field, String content, AdminBasePage.Mode mode, Link link) {
        return editContent(editor_field, content, null, null, mode, link, null);
    }

    public ExtendedWebDriver editContent(WebElement editor_field, String content, AdminBasePage.Mode mode, AdminBasePage.Formatting formatting) {
        return editContent(editor_field, content, formatting, null, mode, null, null);
    }

    public ExtendedWebDriver editContent(WebElement editor_field, String content, AdminBasePage.Mode mode, AdminBasePage.Alignment alignment) {
        return editContent(editor_field, content, null, alignment, mode, null, null);
    }
}
