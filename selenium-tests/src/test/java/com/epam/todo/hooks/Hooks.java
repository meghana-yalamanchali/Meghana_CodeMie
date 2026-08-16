package com.epam.todo.hooks;

import com.epam.todo.config.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;

/**
 * Cucumber lifecycle hooks.
 * A new WebDriver is created per scenario (via @Before) and torn down after (via @After).
 */
public class Hooks {

    /**
     * Ensures a fresh driver is available for the scenario.
     * DriverManager.getDriver() creates it on first call per thread.
     */
    @Before
    public void setUp() {
        WebDriver driver = DriverManager.getDriver();
        driver.manage().window().maximize();
    }

    /** Quits the browser and removes the driver from the ThreadLocal after each scenario. */
    @After
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
