package org.nsu.fit.services.browser;

import io.qameta.allure.Attachment;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Please read: https://github.com/SeleniumHQ/selenium/wiki/Grid2
 */
public class Browser implements Closeable {
    private WebDriver webDriver;

    public Browser() {
        // create web driver.
        try {
            ChromeOptions chromeOptions = new ChromeOptions();

            // for running in Docker container as 'root'.
            chromeOptions.addArguments("no-sandbox");
            chromeOptions.addArguments("disable-dev-shm-usage");
            chromeOptions.addArguments("disable-setuid-sandbox");
            chromeOptions.addArguments("disable-infobars");

            chromeOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            chromeOptions.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

            // we use Windows platform for development only and not for AT launch.
            // For launch AT regression, we use Linux platform.
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                // Done
                // Лабораторная 4: Указать путь до chromedriver на вашей системе.
                // Для того чтобы подобрать нужный chromedriver, необходимо посмотреть версию браузера Chrome
                // на системе, на которой будут запускаться тесты и скачать соотвествующий ей chromedriver с сайта:
                // https://chromedriver.chromium.org/downloads
                System.setProperty("webdriver.chrome.driver", "C:/Programming/NSU/Test/chromedriver.exe");
                chromeOptions.setHeadless(Boolean.parseBoolean(System.getProperty("headless")));
                webDriver = new ChromeDriver(chromeOptions);
            } else {
                File f = new File("/usr/bin/chromedriver");
                if (f.exists()) {
                    chromeOptions.addArguments("single-process");
                    chromeOptions.addArguments("headless");
                    System.setProperty("webdriver.chrome.driver", f.getPath());
                    webDriver = new ChromeDriver(chromeOptions);
                }
            }

            if (webDriver == null) {
                throw new RuntimeException();
            }

            webDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Browser openPage(String url) {
        webDriver.get(url);
        return this;
    }


    public Browser waitForElement(By element) {
        return waitForElement(element, 10);
    }

    public Browser waitForElement(final By element, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeoutSec);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        return this;
    }

    public Browser click(By element) {
        webDriver.findElement(element).click();
        return this;
    }

    public Browser typeText(By element, String text) {
        webDriver.findElement(element).sendKeys(text);
        return this;
    }

    public WebElement getElement(By element) {
        return webDriver.findElement(element);
    }

    public String getValue(By element) {
        return getElement(element).getAttribute("value");
    }

    public List<WebElement> getElements(By element) {
        return webDriver.findElements(element);
    }

    public boolean isElementPresent(By element) {
        return getElements(element).size() != 0;
    }

    @Attachment(value = "Page screenshot", type = "image/png")
    public byte[] makeScreenshot() {
        return ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public void close() {
        webDriver.close();
    }
}
