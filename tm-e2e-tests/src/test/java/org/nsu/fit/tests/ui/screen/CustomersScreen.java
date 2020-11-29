package org.nsu.fit.tests.ui.screen;

import org.nsu.fit.services.browser.Browser;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

public class CustomersScreen implements Screen {
    private Browser browser;

    public CustomersScreen(Browser browser) {
        this.browser = browser;
    }

    public CreateCustomerScreen createCustomerClick() {
        browser.waitForElement(By.tagName("button"));
        browser.getElements(By.tagName("button")).get(1).click();
        browser.waitForElement(By.tagName("h3"));
        Assert.assertEquals(browser.getElement(By.tagName("h3")).getText(), "Crete new customer");
        return new CreateCustomerScreen(browser);
    }

    public LoginScreen logout() {
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
        return new LoginScreen(browser);
    }

    public List<WebElement> getCustomerElements() {

        WebElement tbody = browser.getElements(By.tagName("tbody")).get(0);
        int amount = tbody.findElements(By.tagName("button")).size();

        return tbody.findElements(By.tagName("tr")).stream().limit(amount).collect(Collectors.toList());
    }

    @Override
    public String getName() {
        return "customerScreen";
    }

    public CustomersScreen deleteCustomer() {
        List<WebElement> list = getCustomerElements();
        list.get(0).findElements(By.tagName("td")).get(0).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> list1 = getCustomerElements();
        list1.get(0).findElements(By.tagName("button")).get(0).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomersScreen deleteCustomerWithLogin(String login) {
        List<WebElement> list = getCustomerElements()
                .stream()
                .filter(row -> row.findElements(By.tagName("td")).get(1).getText().equals(login))
                .collect(Collectors.toList());
        Assert.assertEquals(list.size(), 1);
        list.get(0).findElements(By.tagName("td")).get(0).click();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> list1 = getCustomerElements()
                .stream()
                .filter(row -> row.findElements(By.tagName("td")).get(1).getText().equals("Are you sure you want to delete this row?"))
                .collect(Collectors.toList());
        Assert.assertEquals(list1.size(), 1);
        list1.get(0).findElements(By.tagName("td")).get(0).findElements(By.tagName("button")).get(0).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }


    public List<WebElement> getPlanElements() {

        WebElement tbody = browser.getElements(By.tagName("tbody")).get(1);
        int amount = tbody.findElements(By.tagName("button")).size();

        return tbody.findElements(By.tagName("tr")).stream().limit(amount).collect(Collectors.toList());
    }


    public CreatePlanScreen createPlanClick() {
        browser.waitForElement(By.tagName("button"));
        browser.getElement(By.xpath("//button[@title = 'Add plan']")).click();
        browser.waitForElement(By.tagName("h3"));
        Assert.assertEquals(browser.getElement(By.tagName("h3")).getText(), "Create new plan");
        return new CreatePlanScreen(browser);
    }

    public CustomersScreen deletePlanWithLogin(String name) {
        List<WebElement> list = getPlanElements()
                .stream()
                .filter(row -> row.findElements(By.tagName("td")).get(1).getText().equals(name))
                .collect(Collectors.toList());
        Assert.assertEquals(list.size(), 1);
        list.get(0).findElements(By.tagName("td")).get(0).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<WebElement> list1 = getPlanElements()
                .stream()
                .filter(row -> row.findElements(By.tagName("td")).get(1).getText().equals("Are you sure you want to delete this row?"))
                .collect(Collectors.toList());
        Assert.assertEquals(list1.size(), 1);
        list1.get(0).findElements(By.tagName("td")).get(0).findElements(By.tagName("button")).get(0).click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public CustomersScreen deletePlan() {
        List<WebElement> list = getPlanElements();
        list.get(0).findElements(By.tagName("td")).get(0).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<WebElement> list1 = getPlanElements();
        list1.get(0).findElements(By.tagName("button")).get(0).click();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }
}
