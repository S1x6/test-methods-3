package org.nsu.fit.tests.ui;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.tests.ui.screen.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;

public class CreatePlanTest {

    private Browser browser;

    @BeforeMethod
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Create plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan feature")
    public void createPlan() {
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
        Assert.assertTrue(screen instanceof CustomersScreen);
        List<WebElement> trs = ((CustomersScreen) screen).getPlanElements();

        boolean exists = trs.stream().anyMatch(row -> row.findElements(By.tagName("td"))
                .get(1)
                .getText().equals(name));
        Assert.assertTrue(exists);
    }

    @Test(description = "Create plan with error via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Show create plan error feature")
    public void createPlanWithError() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());

        Screen screen = new LoginScreen(browser)
                .loginAsAdmin()
                .createPlanClick()
                .fillPlan(
                        fakeValuesService.bothify("????????"),
                        fakeValuesService.bothify("????????"),
                        6000
                )
                .clickCreate();
        // todo HERE SHOULD BE ERROR
//        Assert.assertTrue(screen instanceof CreatePlanScreen);
//        Assert.assertTrue(((CreatePlanScreen) screen).getErrorText().startsWith("Password is easy"));
    }

    @AfterMethod
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }

}
