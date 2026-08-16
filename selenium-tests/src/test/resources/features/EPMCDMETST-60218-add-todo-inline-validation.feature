@EPMCDMETST-60218
Feature: EPMCDMETST-60218 Registered User: Inline form validation and accessible error messaging for Add Todo

  As a registered user,
  I want the "Add Todo" form to validate required fields in the browser and show clear, accessible error messages,
  So that I can immediately understand what needs fixing and avoid failed submissions.

  Background:
    Given I am logged in as a registered user and viewing the todo dashboard

  # ============================================================
  # HLD Design Goals: Required-field validation prevents submission
  # LLD Validation Trigger Matrix: Submit click/Enter
  # ============================================================

  @smoke @validation
  Scenario: SC01 - Submitting with both fields empty shows inline error messages and focuses first field
    When I submit the Add Todo form with both fields empty
    Then the form is not submitted
    And an inline error message "Task name is required." is shown for the task name field
    And an inline error message "Due date is required." is shown for the due date field
    And keyboard focus is moved to the first invalid field

  @validation
  Scenario: SC02 - Submitting with only task name empty shows task name error only
    When I enter only a due date "2026-12-31" and submit the form
    Then the form is not submitted
    And an inline error message "Task name is required." is shown for the task name field
    And no error message is shown for the due date field

  @validation
  Scenario: SC03 - Submitting with only due date empty shows due date error only
    When I enter only a task name "Write unit tests" and submit the form
    Then the form is not submitted
    And an inline error message "Due date is required." is shown for the due date field
    And no error message is shown for the task name field

  # ============================================================
  # HLD Design Goals: Clear field error as soon as field becomes valid
  # LLD Validation Trigger Matrix: Change taskName / Change dueDate
  # LLD Sequence Diagram 2: Fixing a field clears the error immediately
  # ============================================================

  @validation @clearing
  Scenario: SC04 - Task name error disappears automatically when valid text is typed
    Given inline validation errors are visible on the Add Todo form
    When I type "Buy groceries" into the task name field
    Then the task name error message disappears automatically

  @validation @clearing
  Scenario: SC05 - Due date error disappears automatically when a date is selected
    Given inline validation errors are visible on the Add Todo form
    When I select "2026-12-31" as the due date
    Then the due date error message disappears automatically

  # ============================================================
  # LLD Sequence Diagram 3: Successful submit
  # ============================================================

  @validation @happy-path
  Scenario: SC06 - Form submits successfully when both required fields are provided
    When I enter task name "Complete test report" and due date "2026-12-31"
    And I click the Add Todo submit button
    Then the form is submitted successfully

  # ============================================================
  # HLD / LLD A11y Implementation Details:
  #   - aria-invalid="true" on invalid inputs
  #   - stable error IDs: taskName-error, dueDate-error (camelCase per design spec)
  #   - aria-describedby references error element IDs
  # ============================================================

  @accessibility
  Scenario: SC07 - Invalid inputs are marked with aria-invalid="true" on failed submit
    When I submit the Add Todo form with both fields empty
    Then the task name input has aria-invalid attribute set to "true"
    And the due date input has aria-invalid attribute set to "true"

  @accessibility
  Scenario: SC08 - Each input is programmatically associated with its error via aria-describedby
    Then the task name input has aria-describedby referencing id "taskName-error"
    And the due date input has aria-describedby referencing id "dueDate-error"

  @accessibility
  Scenario: SC09 - A JS live region announces form errors to assistive technology
    When I submit the Add Todo form with both fields empty
    Then the error summary element has an aria-live attribute
    And the error summary element announces the validation errors
    And the error summary element has role "status"

  # ============================================================
  # HLD A11y: Error summary fallback uses role=alert / aria-live=assertive
  # LLD Wireframe: Failed Submit State
  # ============================================================

  @accessibility
  Scenario: SC10 - Server-side error summary has role alert and aria-live assertive
    Then the server-side error summary has role "alert"
    And the server-side error summary has aria-live "assertive"
