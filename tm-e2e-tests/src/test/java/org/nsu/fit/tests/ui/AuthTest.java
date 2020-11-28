package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

public class AuthTest {
    private Browser browser;

    @Test(description = "Login via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login feature")
    public void loginTest() {
        browser = BrowserService.openNewBrowser();
        browser.waitForElement(By.id("email"));

        browser.getElement(By.id("email")).sendKeys("admin");
        browser.getElement(By.id("password")).sendKeys("setup");

        browser.getElement(By.xpath("//button[@type = 'submit']")).click();
        browser.waitForElement(By.tagName("h2"));
        Assert.assertEquals(browser.getElement(By.tagName("h2")).getText(), "Hi admin!");
    }

    @Test(description = "Logout via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Logout feature")
    public void logoutTest() {
        loginTest();
        browser.getElements(By.tagName("a")).get(0).click();
        browser.waitForElement(By.id("email"));
        boolean exists = false;
        try {
            browser.getElement(By.id("email"));
            browser.getElement(By.id("password"));
            exists = true;
        } catch (NoSuchElementException ignore) {

        }
    Assert.assertTrue(exists);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
