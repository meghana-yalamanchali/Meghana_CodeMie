package com.epam.todo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class TodoDashboardPage extends BasePage {
    private static final By TODO_FORM            = By.id("todo-form");
    private static final By TASK_NAME_INPUT      = By.id("newItem");
    private static final By DUE_DATE_INPUT       = By.id("duedate");
    private static final By SUBMIT_BUTTON        = By.cssSelector(".add-button");
    private static final By TASK_NAME_ERROR      = By.id("taskName-error");
    private static final By TASK_NAME_ERROR_TEXT = By.cssSelector("#taskName-error .error-text");
    private static final By DUE_DATE_ERROR       = By.id("duedate-error");
    private static final By DUE_DATE_ERROR_TEXT  = By.cssSelector("#duedate-error .error-text");
    private static final By JS_ERROR_SUMMARY     = By.id("form-errors");
    private static final By TASK_TITLES          = By.cssSelector(".task-title");

    public TodoDashboardPage(WebDriver driver) { super(driver); }

    public void navigate(String baseUrl) { driver.get(baseUrl + "/"); }

    public boolean isOnDashboard() {
        try { return driver.findElement(TODO_FORM).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public void clearAndEnterTaskName(String taskName) {
        WebElement input = driver.findElement(TASK_NAME_INPUT);
        input.clear();
        if (taskName != null && !taskName.isEmpty()) { input.sendKeys(taskName); }
    }
    public void clearAndEnterDueDate(String dueDate) {
        WebElement input = driver.findElement(DUE_DATE_INPUT);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
            "arguments[0].value = arguments[1];"
            + "arguments[0].dispatchEvent(new Event('input',  {bubbles: true}));"
            + "arguments[0].dispatchEvent(new Event('change', {bubbles: true}));",
            input, dueDate);
    }

    public void clickSubmitButton() { waitForClickable(SUBMIT_BUTTON).click(); }

    public void submitEmptyForm() {
        WebElement taskInput = driver.findElement(TASK_NAME_INPUT);
        taskInput.clear();
        WebElement dateInput = driver.findElement(DUE_DATE_INPUT);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", dateInput, "");
        waitForClickable(SUBMIT_BUTTON).click();
    }

    public void triggerValidationErrors() { submitEmptyForm(); }

    public boolean isTaskNameErrorDisplayed() {
        try { return driver.findElement(TASK_NAME_ERROR).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean isDueDateErrorDisplayed() {
        try { return driver.findElement(DUE_DATE_ERROR).isDisplayed(); }
        catch (Exception e) { return false; }
    }

    public boolean waitForTaskNameErrorVisible() {
        try { wait.until(ExpectedConditions.visibilityOfElementLocated(TASK_NAME_ERROR)); return true; }
        catch (Exception e) { return false; }
    }

    public boolean waitForTaskNameErrorHidden() {
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(TASK_NAME_ERROR)); return true; }
        catch (Exception e) { return false; }
    }

    public boolean waitForDueDateErrorHidden() {
        try { wait.until(ExpectedConditions.invisibilityOfElementLocated(DUE_DATE_ERROR)); return true; }
        catch (Exception e) { return false; }
    }

    public String getTaskNameErrorText() {
        try { return driver.findElement(TASK_NAME_ERROR_TEXT).getText().trim(); }
        catch (Exception e) { return ""; }
    }

    public String getDueDateErrorText() {
        try { return driver.findElement(DUE_DATE_ERROR_TEXT).getText().trim(); }
        catch (Exception e) { return ""; }
    }

    public String getTaskNameAriaInvalid() {
        return driver.findElement(TASK_NAME_INPUT).getAttribute("aria-invalid");
    }

    public String getDueDateAriaInvalid() {
        return driver.findElement(DUE_DATE_INPUT).getAttribute("aria-invalid");
    }

    public String getTaskNameAriaDescribedBy() {
        return driver.findElement(TASK_NAME_INPUT).getAttribute("aria-describedby");
    }

    public String getDueDateAriaDescribedBy() {
        return driver.findElement(DUE_DATE_INPUT).getAttribute("aria-describedby");
    }

    public String getJsErrorSummaryAriaLive() {
        return driver.findElement(JS_ERROR_SUMMARY).getAttribute("aria-live");
    }

    public String getJsErrorSummaryRole() {
        return driver.findElement(JS_ERROR_SUMMARY).getAttribute("role");
    }

    public String getJsErrorSummaryText() {
        return driver.findElement(JS_ERROR_SUMMARY).getText().trim();
    }

    public boolean isJsErrorSummaryAnnouncingErrors() { return !getJsErrorSummaryText().isEmpty(); }

    public String getFocusedElementId() {
        Object result = ((JavascriptExecutor) driver)
            .executeScript("var el = document.activeElement; return el ? el.id : null;");
        return result != null ? result.toString() : "";
    }

    public boolean isTaskInList(String taskName) {
        try {
            List<WebElement> titles = driver.findElements(TASK_TITLES);
            return titles.stream().anyMatch(el -> el.getText().contains(taskName));
        } catch (Exception e) { return false; }
    }
}
