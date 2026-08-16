package com.epam.todo.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * JUnit 4 Cucumber test runner for EPMCDMETST-60218.
 *
 * Design reference: Confluence page 2912166181
 *   Ticket: EPMCDMETST-60218 - Add Todo Inline Validation and Accessible Errors
 *
 * Run all EPMCDMETST-60218 scenarios:  mvn test -Dcucumber.filter.tags=@EPMCDMETST-60218
 * Run only smoke tests:                mvn test -Dcucumber.filter.tags=@smoke
 * Run specific browser:                mvn test -Dbrowser=firefox
 * Show browser window:                 mvn test -Dheadless=false
 * Target custom URL:                   mvn test -Dapp.base.url=http://localhost:5000
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features  = "src/test/resources/features",
    glue      = {"com.epam.todo.steps", "com.epam.todo.hooks"},
    plugin    = {
        "pretty",
        "html:target/cucumber-reports/cucumber-report-60218.html",
        "json:target/cucumber-reports/cucumber-report-60218.json"
    },
    tags      = "@EPMCDMETST-60218",
    monochrome = true
)
public class TestRunner60218 {
}
