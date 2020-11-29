package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.openqa.selenium.By;

public class CreatePlanScreen implements Screen {

    private final Browser browser;

    public CreatePlanScreen(Browser browser) {
        this.browser = browser;
    }

    public CreatePlanScreen fillPlan(String name, String details, Integer fee) {
        browser.getElement(By.name("name")).sendKeys(name);
        browser.getElement(By.name("details")).sendKeys(details);
        browser.getElement(By.name("fee")).sendKeys(fee.toString());
        return this;
    }

    public Screen clickCreate() {
        browser.getElement(By.xpath("//button[@type = 'submit']")).click();
        browser.waitForElement(By.className("container"));

        try {
            browser.getElement(By.name("name"));
        } catch (Exception ignore) {
            return new CustomersScreen(browser);
        }
        return this;
    }

    public String getErrorText() {
        return browser.getElements(By.className("container")).get(0)
                .findElements(By.tagName("div")).get(0).getText();
    }

    @Override
    public String getName() {
        return "createPlanScreen";
    }
}
