# Project Requirements: Flask Todo List Web Application

## 1. Project Purpose
The application is a simple web-based todo list for tracking tasks and their due dates. It allows users to sign up, log in, add tasks, view task status, and delete completed tasks.

## 2. Users and Roles
### 2.1 Guest User
- Can view the login page.
- Can access the sign-up page.
- Cannot access the todo dashboard without logging in.

### 2.2 Registered User
- Can log in with a username and password.
- Can create new todo items.
- Can view all saved todo items.
- Can delete todo items.
- Can log out.

## 3. Functional Requirements

### 3.1 User Registration
- The system must provide a sign-up page.
- The user must be able to create an account using:
  - username
  - password
- The password must be stored as a hashed value, not in plain text.
- After successful registration, the system must redirect the user to the login page.

### 3.2 User Authentication
- The system must provide a login page.
- The user must be able to log in using a username and password.
- If the credentials match a stored user, the system must create a session for that user.
- If login fails, the system must return a failure response.
- The todo dashboard must only be accessible when the user is logged in.

### 3.3 Session Management
- The system must track the logged-in user using session storage.
- The system must clear the session when the user logs out.
- Unauthorized users must be redirected to the login page.

### 3.4 Add Todo Item
- The authenticated user must be able to add a new task from the dashboard.
- Each task must include:
  - task name/content
  - due date
- The task name is required.
- The due date is required.
- When a task is added, it must be saved to the in-memory task list.
- Each task must be assigned a unique numeric ID.

### 3.5 Display Todo Items
- The dashboard must show all existing tasks.
- If no tasks exist, the system must display an empty-state message such as "No Task Added Yet".
- For each task, the system must show:
  - task name
  - due date
  - status label

### 3.6 Overdue Status Handling
- The system must compare task due dates against the current date.
- If a task due date is earlier than the current day in the same month and year, it must be marked as overdue.
- Overdue tasks must be visually distinguished from non-overdue tasks.
- The dashboard must display the status text as either:
  - Overdue
  - Due

### 3.7 Delete Todo Item
- The authenticated user must be able to delete a task.
- Deletion must happen when the checkbox for a task is checked and the form is submitted.
- The system must remove the matching task from the task list.

### 3.8 Logout
- The system must provide a logout action.
- Logging out must clear the session and redirect the user to the login page.

## 4. User Interface Requirements
- The application must have separate pages for:
  - login
  - sign-up
  - todo dashboard
- The dashboard must show the current day in the header.
- Tasks must be displayed in a card/list style layout.
- Overdue tasks must be shown in red.
- Non-overdue tasks must be shown in green.
- The UI should be usable on typical desktop and mobile screen sizes.

## 5. Data Requirements

### 5.1 User Data
Each user record must contain:
- username
- hashed password

### 5.2 Task Data
Each task record must contain:
- id
- content
- due_date
  - year
  - month
  - day
- overdue flag

## 6. Application Routes
The project currently implements the following routes:
- `GET /signup` and `POST /signup` for registration
- `GET /login` and `POST /login` for authentication
- `GET /logout` for ending the session
- `GET /` and `POST /` for displaying and adding tasks
- `POST /delete-item` for deleting a task

## 7. Non-Functional Requirements
- The backend must be implemented with Python and Flask.
- Passwords must be hashed using Werkzeug security utilities.
- The application should start in debug mode during development.
- The app should have a simple and lightweight design.
- The code should be easy to understand and suitable for beginner-level developers.

## 8. Constraints and Limitations of the Current Implementation
- Data is stored only in memory.
- User accounts are not persisted to a database.
- Todo items are not persisted to a database.
- All data is lost when the application restarts.
- Overdue status is calculated using the app's current date values at runtime.
- The current overdue logic only compares year, month, and day values and does not continuously recalculate on every page load.
- There is no password recovery feature.
- There is no account editing feature.
- There is no task editing feature.
- There is no search, filtering, or sorting feature.

## 9. Notes
- The runnable Flask entry point in this workspace is `main.py`.
- The existing README mentions `app.py`, but that file is not present in the current project structure.
- The current implementation is a small demo-style application, not a production-ready system.

## 10. Summary of Core Functionality
In short, the application supports:
1. User sign-up
2. User login/logout
3. Session-protected todo dashboard
4. Adding tasks with due dates
5. Viewing overdue and non-overdue tasks
6. Deleting tasks

