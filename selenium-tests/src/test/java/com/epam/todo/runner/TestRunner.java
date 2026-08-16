package com.epam.todo.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * JUnit 4 Cucumber test runner for EPMCDMETST-60027.
 *
 * Run all scenarios:      mvn test
 * Run only smoke tests:   mvn test -Dcucumber.filter.tags=@smoke
 * Run specific browser:   mvn test -Dbrowser=firefox
 * Show browser window:    mvn test -Dheadless=false
 * Target custom URL:      mvn test -Dapp.base.url=http://localhost:5000
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue     = {"com.epam.todo.steps", "com.epam.todo.hooks"},
    plugin   = {
        "pretty",
        "html:target/cucumber-reports/cucumber-report.html",
        "json:target/cucumber-reports/cucumber-report.json"
    },
    tags      = "@EPMCDMETST-60027",
    monochrome = true
)
public class TestRunner {
}
