package com.epam.todo.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * Thread-safe WebDriver factory and lifecycle manager.
 * Browser choice and headless mode are controlled via system properties:
 *   -Dbrowser=chrome|firefox   (default: chrome)
 *   -Dheadless=true|false      (default: true)
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_HOLDER = new ThreadLocal<>();

    private DriverManager() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Returns the WebDriver for the current thread, creating one if it does not exist.
     */
    public static WebDriver getDriver() {
        if (DRIVER_HOLDER.get() == null) {
            DRIVER_HOLDER.set(createDriver());
        }
        return DRIVER_HOLDER.get();
    }

    /**
     * Quits the WebDriver for the current thread and removes it from the ThreadLocal.
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER_HOLDER.get();
        if (driver != null) {
            driver.quit();
            DRIVER_HOLDER.remove();
        }
    }

    private static WebDriver createDriver() {
        String browser  = System.getProperty("browser",  "chrome");
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));

        switch (browser.toLowerCase()) {
            case "firefox": {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("--headless");
                }
                return new FirefoxDriver(options);
            }
            case "chrome":
            default: {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--no-sandbox",
                            "--disable-dev-shm-usage", "--disable-gpu",
                            "--window-size=1920,1080");
                }
                return new ChromeDriver(options);
            }
        }
    }
}
