package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginScreen implements Screen {
    private final Browser browser;
    public LoginScreen(Browser browser) {
        this.browser = browser;
    }

    public CustomersScreen loginAsAdmin() {
        browser.waitForElement(By.id("email"));

        browser.getElement(By.id("email")).sendKeys("admin");
        browser.getElement(By.id("password")).sendKeys("setup");

        browser.getElement(By.xpath("//button[@type = 'submit']")).click();
        browser.waitForElement(By.tagName("h2"));
        Assert.assertEquals(browser.getElement(By.tagName("h2")).getText(), "Hi admin!");
        return new CustomersScreen(browser);
    }

    @Override
    public String getName() {
        return "login";
    }
}
