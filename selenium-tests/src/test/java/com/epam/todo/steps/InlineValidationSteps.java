package com.epam.todo.steps;

import com.epam.todo.config.DriverManager;
import com.epam.todo.pages.LoginPage;
import com.epam.todo.pages.SignupPage;
import com.epam.todo.pages.TodoDashboardPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for EPMCDMETST-60027: Inline Form Validation and Accessible Error Messaging.
 *
 * Covers three acceptance criteria:
 *   AC-1: Required field validation prevents submission
 *   AC-2: Validation errors clear when user fixes input
 *   AC-3: Accessible aria-invalid, aria-describedby, and live region announcements
 */
public class InlineValidationSteps {

    private static final String BASE_URL =
        System.getProperty("app.base.url", "http://localhost:5000");

    private final WebDriver        driver;
    private final TodoDashboardPage dashboardPage;

    public InlineValidationSteps() {
        this.driver        = DriverManager.getDriver();
        this.dashboardPage = new TodoDashboardPage(driver);
    }

    // =========================================================================
    // Background
    // =========================================================================

    @Given("I am logged in as a registered user and viewing the todo dashboard")
    public void iAmLoggedInAsRegisteredUserAndViewingTheTodoDashboard() {
        SignupPage signupPage = new SignupPage(driver);
        signupPage.navigate(BASE_URL);
        signupPage.signUp("seleniumtestuser", "testPass123");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigate(BASE_URL);
        loginPage.login("seleniumtestuser", "testPass123");

        assertThat(dashboardPage.isOnDashboard())
            .as("Should be on the Todo Dashboard after login")
            .isTrue();
    }

    // =========================================================================
    // AC-1: Validation prevents submission
    // =========================================================================

    @When("I submit the Add Todo form with both fields empty")
    public void iSubmitFormWithBothFieldsEmpty() {
        dashboardPage.submitEmptyForm();
    }

    @Then("the form is not submitted")
    public void theFormIsNotSubmitted() {
        assertThat(driver.getCurrentUrl())
            .as("URL should remain at dashboard root after blocked submission")
            .endsWith("/");
        assertThat(dashboardPage.isOnDashboard())
            .as("The Add Todo form should still be visible")
            .isTrue();
    }

    @Then("an inline error message {string} is shown for the task name field")
    public void inlineErrorMessageIsShownForTaskNameField(String expectedMessage) {
        assertThat(dashboardPage.waitForTaskNameErrorVisible())
            .as("Task name error message should become visible")
            .isTrue();
        assertThat(dashboardPage.getTaskNameErrorText())
            .as("Task name inline error text")
            .contains(expectedMessage);
    }

    @Then("an inline error message {string} is shown for the due date field")
    public void inlineErrorMessageIsShownForDueDateField(String expectedMessage) {
        assertThat(dashboardPage.isDueDateErrorDisplayed())
            .as("Due date error message should be visible")
            .isTrue();
        assertThat(dashboardPage.getDueDateErrorText())
            .as("Due date inline error text")
            .contains(expectedMessage);
    }

    @Then("keyboard focus is moved to the first invalid field")
    public void keyboardFocusIsMovedToFirstInvalidField() {
        assertThat(dashboardPage.getFocusedElementId())
            .as("Focus should move to the task name input (first invalid field)")
            .isEqualTo("newItem");
    }

    @When("I enter only a due date {string} and submit the form")
    public void iEnterOnlyDueDateAndSubmitForm(String dueDate) {
        dashboardPage.clearAndEnterDueDate(dueDate);
        dashboardPage.clickSubmitButton();
    }

    @Then("no error message is shown for the due date field")
    public void noErrorMessageIsShownForDueDateField() {
        assertThat(dashboardPage.isDueDateErrorDisplayed())
            .as("Due date error should NOT be visible when date is provided")
            .isFalse();
    }

    @Then("no error message is shown for the task name field")
    public void noErrorMessageIsShownForTaskNameField() {
        assertThat(dashboardPage.isTaskNameErrorDisplayed())
            .as("Task name error should NOT be visible when task name is provided")
            .isFalse();
    }

    @When("I enter only a task name {string} and submit the form")
    public void iEnterOnlyTaskNameAndSubmitForm(String taskName) {
        dashboardPage.clearAndEnterTaskName(taskName);
        dashboardPage.clickSubmitButton();
    }

    // =========================================================================
    // AC-2: Validation clears when user fixes input
    // =========================================================================

    @Given("inline validation errors are visible on the Add Todo form")
    public void inlineValidationErrorsAreVisibleOnTheAddTodoForm() {
        dashboardPage.triggerValidationErrors();
        assertThat(dashboardPage.waitForTaskNameErrorVisible())
            .as("Task name validation error should be triggered and visible")
            .isTrue();
        assertThat(dashboardPage.isDueDateErrorDisplayed())
            .as("Due date validation error should be triggered and visible")
            .isTrue();
    }

    @When("I type {string} into the task name field")
    public void iTypeIntoTaskNameField(String taskName) {
        dashboardPage.clearAndEnterTaskName(taskName);
    }

    @Then("the task name error message disappears automatically")
    public void theTaskNameErrorMessageDisappearsAutomatically() {
        assertThat(dashboardPage.waitForTaskNameErrorHidden())
            .as("Task name error should disappear after entering a valid value")
            .isTrue();
    }

    @When("I select {string} as the due date")
    public void iSelectAsTheDueDate(String dueDate) {
        dashboardPage.clearAndEnterDueDate(dueDate);
    }

    @Then("the due date error message disappears automatically")
    public void theDueDateErrorMessageDisappearsAutomatically() {
        assertThat(dashboardPage.waitForDueDateErrorHidden())
            .as("Due date error should disappear after selecting a valid date")
            .isTrue();
    }

    @When("I enter task name {string} and due date {string}")
    public void iEnterTaskNameAndDueDate(String taskName, String dueDate) {
        dashboardPage.clearAndEnterTaskName(taskName);
        dashboardPage.clearAndEnterDueDate(dueDate);
    }

    @And("I click the Add Todo submit button")
    public void iClickTheAddTodoSubmitButton() {
        dashboardPage.clickSubmitButton();
    }

    @Then("the form is submitted successfully")
    public void theFormIsSubmittedSuccessfully() {
        assertThat(driver.getCurrentUrl())
            .as("URL should redirect back to dashboard root after successful submission")
            .endsWith("/");
        assertThat(dashboardPage.isOnDashboard())
            .as("Dashboard should still be visible after successful submission")
            .isTrue();
        assertThat(dashboardPage.isTaskNameErrorDisplayed())
            .as("No task name error should be visible after successful submission")
            .isFalse();
        assertThat(dashboardPage.isDueDateErrorDisplayed())
            .as("No due date error should be visible after successful submission")
            .isFalse();
    }

    // =========================================================================
    // AC-3: Accessible errors for screen readers
    // =========================================================================

    @Then("the task name input has aria-invalid attribute set to {string}")
    public void taskNameInputHasAriaInvalidAttributeSetTo(String expectedValue) {
        assertThat(dashboardPage.getTaskNameAriaInvalid())
            .as("Task name input should have aria-invalid=" + expectedValue)
            .isEqualTo(expectedValue);
    }

    @Then("the due date input has aria-invalid attribute set to {string}")
    public void dueDateInputHasAriaInvalidAttributeSetTo(String expectedValue) {
        assertThat(dashboardPage.getDueDateAriaInvalid())
            .as("Due date input should have aria-invalid=" + expectedValue)
            .isEqualTo(expectedValue);
    }

    @Then("the task name input has aria-describedby referencing id {string}")
    public void taskNameInputHasAriaDescribedByReferencingId(String expectedId) {
        assertThat(dashboardPage.getTaskNameAriaDescribedBy())
            .as("Task name input aria-describedby should reference id: " + expectedId)
            .contains(expectedId);
    }

    @Then("the due date input has aria-describedby referencing id {string}")
    public void dueDateInputHasAriaDescribedByReferencingId(String expectedId) {
        assertThat(dashboardPage.getDueDateAriaDescribedBy())
            .as("Due date input aria-describedby should reference id: " + expectedId)
            .contains(expectedId);
    }

    @Then("the error summary element has an aria-live attribute")
    public void theErrorSummaryElementHasAnAriaLiveAttribute() {
        assertThat(dashboardPage.getJsErrorSummaryAriaLive())
            .as("Error summary (id=form-errors) should have aria-live attribute set")
            .isNotNull()
            .isNotEmpty();
    }

    @Then("the error summary element announces the validation errors")
    public void theErrorSummaryElementAnnouncesTheValidationErrors() {
        assertThat(dashboardPage.isJsErrorSummaryAnnouncingErrors())
            .as("Error summary should contain non-empty announcement text after failed submit")
            .isTrue();
    }

    @Then("the error summary element has role {string}")
    public void theErrorSummaryElementHasRole(String expectedRole) {
        assertThat(dashboardPage.getJsErrorSummaryRole())
            .as("Error summary element should have role=" + expectedRole)
            .isEqualTo(expectedRole);
    }
}
