package org.nsu.fit.tests.ui;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.services.rest.data.ContactPojo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.swing.*;
import java.util.List;
import java.util.Locale;

public class DeleteTest {

    private Browser browser = null;

    @BeforeClass
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
        browser.waitForElement(By.id("email"));

        browser.getElement(By.id("email")).sendKeys("admin");
        browser.getElement(By.id("password")).sendKeys("setup");

        browser.getElement(By.xpath("//button[@type = 'submit']")).click();

    }

    private ContactPojo createTestCredentials() {
        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        ContactPojo contactPojo = new ContactPojo();
        contactPojo.firstName = fakeValuesService.letterify("?????");
        contactPojo.lastName = fakeValuesService.letterify("?????????");
        contactPojo.login = fakeValuesService.bothify("?????_##@??????.com");
        contactPojo.pass = fakeValuesService.bothify("??##??#??#");
        return contactPojo;
    }

    @Test(description = "Delete customer via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create delete feature")
    public void deleteCustomer() {
        browser.waitForElement(By.tagName("h2"));
        Assert.assertEquals(browser.getElement(By.tagName("h2")).getText(), "Hi admin!");

        browser.getElements(By.className("MuiButtonBase-root")).get(1).click();
        ContactPojo cp = createTestCredentials();

        browser.waitForElement(By.name("firstName"));
        browser.getElement(By.name("firstName")).sendKeys(cp.firstName);
        browser.getElement(By.name("lastName")).sendKeys(cp.lastName);
        browser.getElement(By.name("login")).sendKeys(cp.login);
        browser.getElement(By.name("password")).sendKeys(cp.pass);
        browser.getElement(By.xpath("//button[@type = 'submit']")).click();

        List<WebElement> trs = browser.waitForElement(By.className("MuiTableBody-root"))
                .getElements(By.className("MuiTableBody-root"))
                .get(0)
                .findElements(By.tagName("tr"));
        trs.get(trs.size() - 5).findElement(By.xpath("//button[@title = 'Delete']")).click();
        browser.getElements(By.tagName("tbody")).get(0).findElements(By.tagName("td")).get(0)
                .findElements(By.tagName("button")).get(0).click();
        //document.getElementsByTagName("tbody")[0].getElementsByTagName("td")[0].getElementsByTagName("button")[0].click()

        boolean exists = browser.waitForElement(By.className("MuiTableBody-root"))
                .getElements(By.className("MuiTableBody-root"))
                .get(0)
                .findElements(By.tagName("tr"))
                .stream()
                .anyMatch(row -> row.findElements(By.tagName("td"))
                        .get(1)
                        .getText().equals(cp.login));
        Assert.assertFalse(exists);
    }

    @Test(description = "Create plan via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Create plan feature")
    public void createPlan() {
        browser.waitForElement(By.tagName("h2"));
        Assert.assertEquals(browser.getElement(By.tagName("h2")).getText(), "Hi admin!");

        browser.getElements(By.className("MuiButtonBase-root")).get(12).click();

        FakeValuesService fakeValuesService = new FakeValuesService(
                new Locale("en-GB"), new RandomService());
        browser.waitForElement(By.name("firstName"));
        String name = fakeValuesService.letterify("??????");
        browser.getElement(By.name("name")).sendKeys(name);
        browser.getElement(By.name("details")).sendKeys(fakeValuesService.letterify("?????????????????"));
        browser.getElement(By.name("fee")).sendKeys(fakeValuesService.numerify("#####"));
        browser.getElement(By.xpath("//button[@type = 'submit']")).click();

        boolean exists = browser.waitForElement(By.className("MuiTableBody-root"))
                .getElements(By.className("MuiTableBody-root"))
                .get(1)
                .findElements(By.tagName("tr"))
                .stream()
                .anyMatch(row -> row.findElements(By.tagName("td"))
                        .get(1)
                        .getText().equals(name));
        Assert.assertTrue(exists);
    }

    @AfterClass
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
