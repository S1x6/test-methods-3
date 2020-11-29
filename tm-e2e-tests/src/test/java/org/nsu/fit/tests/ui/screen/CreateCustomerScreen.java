package org.nsu.fit.tests.ui.screen;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.nsu.fit.services.rest.data.CustomerPojo;
import org.openqa.selenium.By;

import java.util.Locale;

public class CreateCustomerScreen implements Screen {
    private Browser browser;

    public CreateCustomerScreen(Browser browser) {
        this.browser = browser;
    }

    public static ContactPojo createTestCredentials() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        ContactPojo contactPojo = new ContactPojo();
        contactPojo.firstName = fakeValuesService.letterify("?????");
        contactPojo.lastName = fakeValuesService.letterify("?????????");
        contactPojo.login = fakeValuesService.bothify("?????_##@??????.com");
        contactPojo.pass = fakeValuesService.bothify("??##??#??#");
        return contactPojo;
    }

    public CreateCustomerScreen fillCustomer(ContactPojo cp) {
        browser.waitForElement(By.name("firstName"));
        browser.getElement(By.name("firstName")).sendKeys(cp.firstName);
        browser.getElement(By.name("lastName")).sendKeys(cp.lastName);
        browser.getElement(By.name("login")).sendKeys(cp.login);
        browser.getElement(By.name("password")).sendKeys(cp.pass);
        return this;
    }

    public String getErrorText() {
        return browser.getElements(By.className("container")).get(0)
                .findElements(By.tagName("div")).get(0).getText();
    }

    public Screen clickCreate() {
        browser.getElement(By.xpath("//button[@type = 'submit']")).click();
        browser.waitForElement(By.className("container"));

        try {
            browser.getElement(By.name("firstName"));
        } catch (Exception ignore) {
            return new CustomersScreen(browser);
        }
        return this;
    }

    @Override
    public String getName() {
        return "createCustomerScreen";
    }
}
