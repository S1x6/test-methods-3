package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.tests.ui.screen.CreateCustomerScreen;
import org.nsu.fit.tests.ui.screen.CustomersScreen;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.nsu.fit.tests.ui.screen.Screen;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CreateCustomerTest {
    private Browser browser = null;

    @BeforeMethod
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create customer feature")
    public void createCustomer() {

        ContactPojo contactPojo = CreateCustomerScreen.createTestCredentials();

        Screen screen = new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomerClick()
                .fillCustomer(contactPojo)
                .clickCreate();
        Assert.assertTrue(screen instanceof CustomersScreen);
        List<WebElement> trs = ((CustomersScreen) screen).getCustomerElements();

        boolean exists = trs.stream().anyMatch(row -> row.findElements(By.tagName("td"))
                        .get(1)
                        .getText().equals(contactPojo.login));
        Assert.assertTrue(exists);
    }

    @Test(description = "Create customer with error via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Show create customer error feature")
    public void createCustomerWithError() {

        ContactPojo contactPojo = CreateCustomerScreen.createTestCredentials();
        contactPojo.pass = "123qwe";

        Screen screen = new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomerClick()
                .fillCustomer(contactPojo)
                .clickCreate();
        Assert.assertTrue(screen instanceof CreateCustomerScreen);
        Assert.assertTrue(((CreateCustomerScreen) screen).getErrorText().startsWith("Password is easy"));
    }

    @AfterMethod
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
