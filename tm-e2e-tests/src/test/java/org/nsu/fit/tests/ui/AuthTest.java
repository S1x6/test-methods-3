package org.nsu.fit.tests.ui;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.nsu.fit.services.browser.Browser;
import org.nsu.fit.services.browser.BrowserService;
import org.nsu.fit.tests.ui.screen.LoginScreen;
import org.testng.annotations.*;

public class AuthTest {

    private Browser browser = null;

    @BeforeMethod
    public void beforeClass() {
        browser = BrowserService.openNewBrowser();
    }

    @Test(description = "Login via UI.")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Login feature")
    public void login() {
        new LoginScreen(browser)
                .loginAsAdmin();
    }

    @Test(description = "Logout via UI.", dependsOnMethods = "login")
    @Severity(SeverityLevel.BLOCKER)
    @Feature("Logout feature")
    public void logout() {
        new LoginScreen(browser)
                .loginAsAdmin()
                .logout();
    }

    @AfterMethod
    public void afterClass() {
        if (browser != null) {
            browser.close();
        }
    }
}
