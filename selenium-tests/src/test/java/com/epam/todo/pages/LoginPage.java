package com.epam.todo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the /login page (login.html).
 *
 * Locators based on:
 *   <input type="text"     name="username">
 *   <input type="password" name="password">
 *   <button type="submit">Login</button>
 */
public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.cssSelector("input[name='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[name='password']");
    private static final By SUBMIT_BUTTON  = By.cssSelector("button[type='submit']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigate(String baseUrl) {
        driver.get(baseUrl + "/login");
    }

    /**
     * Fills in the login form and submits it.
     * After a successful login the app redirects to /.
     */
    public void login(String username, String password) {
        waitForVisible(USERNAME_INPUT).sendKeys(username);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        driver.findElement(SUBMIT_BUTTON).click();
    }
}
