"""
Tests for EPMCDMETST-60027: Inline validation and accessible error messaging
for the Add Todo form.

Testing design follows LLD s4 (Component/Unit tests).
These tests cover:
  - Server-side validate_todo_form() function
  - Flask /  route POST endpoint behaviour (server-side fallback)
"""

import sys
import os
import pytest

# Make project root importable so we can import main.py
sys.path.insert(0, os.path.join(os.path.dirname(__file__), ".."))

import main as app_module
from main import app, validate_todo_form


# ── validate_todo_form unit tests ──────────────────────────────────────────
# LLD s4 Component/Unit tests table


class TestValidateTodoForm:
    """Unit tests for the validate_todo_form() helper (main.py).
    
    LLD s1 Validation rules table:
      taskName  required, trim().length > 0  -> Error: Task name is required.
      dueDate   required, parseable YYYY-MM-DD -> Error: Due date is required.
    """

    def test_empty_task_name_returns_error(self):
        """LLD s1 - taskName required rule."""
        errors = validate_todo_form("", "2026-12-31")
        assert "taskName" in errors
        assert errors["taskName"] == "Task name is required."

    def test_whitespace_only_task_name_returns_error(self):
        """LLD s1 - taskName trim() check."""
        errors = validate_todo_form("   ", "2026-12-31")
        assert "taskName" in errors

    def test_none_task_name_returns_error(self):
        """LLD s1 - taskName None guard."""
        errors = validate_todo_form(None, "2026-12-31")
        assert "taskName" in errors

    def test_empty_due_date_returns_error(self):
        """LLD s1 - dueDate required rule."""
        errors = validate_todo_form("Buy milk", "")
        assert "dueDate" in errors
        assert errors["dueDate"] == "Due date is required."

    def test_none_due_date_returns_error(self):
        """LLD s1 - dueDate None guard."""
        errors = validate_todo_form("Buy milk", None)
        assert "dueDate" in errors

    def test_invalid_date_format_returns_error(self):
        """LLD s1 - dueDate parseable date check."""
        errors = validate_todo_form("Buy milk", "not-a-date")
        assert "dueDate" in errors

    def test_both_empty_returns_both_errors(self):
        """LLD s4 - Empty submit shows errors for both fields."""
        errors = validate_todo_form("", "")
        assert "taskName" in errors
        assert "dueDate" in errors

    def test_valid_input_returns_no_errors(self):
        """LLD s4 - Valid submit calls handler (no errors)."""
        errors = validate_todo_form("Buy milk", "2026-12-31")
        assert errors == {}

    def test_valid_input_with_leading_trailing_spaces_in_task_is_valid(self):
        """Task name with surrounding spaces but non-empty content is valid."""
        errors = validate_todo_form(" Buy milk ", "2026-12-31")
        # validate_todo_form only checks trim().length > 0; it does not strip for storage
        assert "taskName" not in errors


# ── Flask integration tests ────────────────────────────────────────────────


@pytest.fixture
def client():
    """Flask test client with a logged-in session."""
    app.config["TESTING"] = True
    app.config["SECRET_KEY"] = "testkey"
    # Reset global state between tests
    app_module.items.clear()
    app_module.users.clear()

    with app.test_client() as client:
        with client.session_transaction() as sess:
            sess["user"] = "testuser"
        yield client


class TestAddTodoRouteValidation:
    """Integration tests for POST / (home route) validation fallback.
    
    LLD s2 - API/backend interaction and server-side error handling.
    """

    def test_empty_post_returns_200_with_task_error(self, client):
        """Server returns form with taskName error when task is empty."""
        resp = client.post("/", data={"newItem": "", "duedate": "2026-12-31"})
        assert resp.status_code == 200
        assert b"Task name is required." in resp.data

    def test_empty_date_returns_200_with_date_error(self, client):
        """Server returns form with dueDate error when date is empty."""
        resp = client.post("/", data={"newItem": "Buy milk", "duedate": ""})
        assert resp.status_code == 200
        assert b"Due date is required." in resp.data

    def test_both_empty_returns_both_errors(self, client):
        """LLD s4 - Server fallback for empty submit shows both errors."""
        resp = client.post("/", data={"newItem": "", "duedate": ""})
        assert resp.status_code == 200
        assert b"Task name is required." in resp.data
        assert b"Due date is required." in resp.data

    def test_valid_post_redirects(self, client):
        """LLD s4 - Valid submit redirects (item saved)."""
        resp = client.post("/", data={"newItem": "Buy milk", "duedate": "2026-12-31"})
        assert resp.status_code == 302

    def test_aria_invalid_true_in_error_response(self, client):
        """LLD s1 ARIA wiring - aria-invalid=true present for invalid field."""
        resp = client.post("/", data={"newItem": "", "duedate": "2026-12-31"})
        assert b'aria-invalid="true"' in resp.data

    def test_aria_describedby_in_error_response(self, client):
        """LLD s1 ARIA wiring - aria-describedby present linking input to error."""
        resp = client.post("/", data={"newItem": "", "duedate": "2026-12-31"})
        assert b"aria-describedby" in resp.data
        assert b"taskName-error" in resp.data

    def test_error_summary_visible_in_error_response(self, client):
        """LLD s1 - Error summary role=alert present when hasErrors."""
        resp = client.post("/", data={"newItem": "", "duedate": ""})
        # error-summary--hidden class should NOT be present in the summary div
        html = resp.data.decode()
        assert "error-summary--hidden" not in html.split("form-error-summary")[1].split("><")[0]

    def test_preserved_form_values_in_error_response(self, client):
        """LLD s1 State model - form_values preserved after error re-render."""
        resp = client.post("/", data={"newItem": "Partial task", "duedate": ""})
        assert b"Partial task" in resp.data
