package org.nsu.fit.tests.ui;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
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
import java.util.Locale;

public class DeleteTest {

    private Browser browser = null;

    @BeforeMethod
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Delete customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer delete feature")
    public void deleteCustomer() {
        ContactPojo cp = CreateCustomerScreen.createTestCredentials();
        CustomersScreen screen = (CustomersScreen) new LoginScreen(browser)
                .loginAsAdmin()
                .createCustomerClick()
                .fillCustomer(cp)
                .clickCreate();

        screen.deleteCustomerWithLogin(cp.login);

        List<WebElement> trs = screen.getCustomerElements();

        boolean exists = trs.stream().anyMatch(row -> row.findElements(By.tagName("td"))
                .get(1)
                .getText().equals(cp.login));
        Assert.assertFalse(exists);
    }

    @Test(description = "Delete plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Customer plan feature")
    public void deletePlan() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        String name = fakeValuesService.bothify("????????");
        Screen screen = new LoginScreen(browser)
                .loginAsAdmin()
                .createPlanClick()
                .fillPlan(
                        name,
                        fakeValuesService.bothify("????????"),
                        Integer.parseInt(fakeValuesService.numerify("###"))
                )
                .clickCreate();

        ((CustomersScreen)screen).deletePlanWithLogin(name);

        List<WebElement> trs = ((CustomersScreen) screen).getPlanElements();

        boolean exists = trs.stream().anyMatch(row -> row.findElements(By.tagName("td"))
                .get(1)
                .getText().equals(name));
        Assert.assertFalse(exists);
    }

    @AfterMethod
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
