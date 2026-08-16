package com.epam.todo.steps;

import com.epam.todo.config.DriverManager;
import com.epam.todo.pages.TodoDashboardPage;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for EPMCDMETST-60218: Add Todo Inline Validation and Accessible Errors.
 *
 * This file provides step definitions specific to EPMCDMETST-60218 that extend
 * the baseline coverage in InlineValidationSteps (EPMCDMETST-60027).
 *
 * Design reference: Confluence page 2912166181
 *   HLD A11y: Error summary fallback - role=alert / aria-live=assertive
 *   LLD Wireframe: Failed Submit State
 */
public class InlineValidation60218Steps {

    private final WebDriver         driver;
    private final TodoDashboardPage dashboardPage;

    public InlineValidation60218Steps() {
        this.driver        = DriverManager.getDriver();
        this.dashboardPage = new TodoDashboardPage(driver);
    }

    // =========================================================================
    // SC10 - Server-side error summary ARIA attributes
    // Design Ref: HLD A11y / LLD Wireframe "Failed Submit State"
    // =========================================================================

    @Then("the server-side error summary has role {string}")
    public void serverSideErrorSummaryHasRole(String expectedRole) {
        assertThat(dashboardPage.getServerErrorSummaryRole())
            .as("Server-side error summary (.form-error-summary) should have role=%s", expectedRole)
            .isEqualTo(expectedRole);
    }

    @Then("the server-side error summary has aria-live {string}")
    public void serverSideErrorSummaryHasAriaLive(String expectedAriaLive) {
        assertThat(dashboardPage.getServerErrorSummaryAriaLive())
            .as("Server-side error summary (.form-error-summary) should have aria-live=%s", expectedAriaLive)
            .isEqualTo(expectedAriaLive);
    }
}
