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
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;

public class ShowManyTest {

    private Browser browser = null;

    @BeforeMethod
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Show 5 customers via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Show 5 customers feature")
    public void createManyCustomers() {
        CustomersScreen customersScreen = new LoginScreen(browser).loginAsAdmin();
        while (customersScreen.getCustomerElements().size() > 0) {
            customersScreen.deleteCustomer();
        }

        for (int i = 0; i < 6; i++) {
            ContactPojo contactPojo = CreateCustomerScreen.createTestCredentials();
            customersScreen = (CustomersScreen) customersScreen.createCustomerClick()
                    .fillCustomer(contactPojo)
                    .clickCreate();
        }
        Assert.assertEquals(customersScreen.getCustomerElements().size(), 5);
    }

    @Test(description = "Show 5 plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Show 5 plan feature")
    public void createManyPlans() {
        CustomersScreen customersScreen = new LoginScreen(browser).loginAsAdmin();
        while (customersScreen.getPlanElements().size() > 0) {
            customersScreen.deletePlan();
        }
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        for (int i = 0; i < 6; i++) {
            customersScreen = (CustomersScreen) customersScreen.createPlanClick()
                    .fillPlan(
                            fakeValuesService.bothify("????????"),
                            fakeValuesService.bothify("????????"),
                            Integer.parseInt(fakeValuesService.numerify("###"))
                    )
                    .clickCreate();
        }
        Assert.assertEquals(customersScreen.getPlanElements().size(), 5);
    }

    @AfterMethod
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }

}
