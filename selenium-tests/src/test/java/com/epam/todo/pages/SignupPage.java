package com.epam.todo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page object for the /signup page (signup.html).
 *
 * Locators based on:
 *   <input type="text"     name="username">
 *   <input type="password" name="password">
 *   <button type="submit">Create Account</button>
 */
public class SignupPage extends BasePage {

    private static final By USERNAME_INPUT = By.cssSelector("input[name='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[name='password']");
    private static final By SUBMIT_BUTTON  = By.cssSelector("button[type='submit']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public void navigate(String baseUrl) {
        driver.get(baseUrl + "/signup");
    }

    /**
     * Fills in the signup form and submits it.
     * After a successful signup the app redirects to /login.
     */
    public void signUp(String username, String password) {
        waitForVisible(USERNAME_INPUT).sendKeys(username);
        driver.findElement(PASSWORD_INPUT).sendKeys(password);
        driver.findElement(SUBMIT_BUTTON).click();
    }
}
