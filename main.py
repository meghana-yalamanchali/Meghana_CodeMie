# from flask import Flask, render_template, redirect, url_for, request
from flask import Flask, render_template, redirect, url_for, request, session
from datetime import datetime
from werkzeug.security import generate_password_hash, check_password_hash


app = Flask(__name__)
app.secret_key = "secretkey" 

users = []


items = []
week_days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
months = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August',
          'September', 'October', 'November', 'December']

year = int(datetime.now().year)
month = int(datetime.now().month)
day = int(datetime.now().day)
weekday = datetime.now().weekday()
week_day = week_days[weekday]
month_name = months[month-1]
curr_day = f'{day} {month_name} {year}, {week_day}'


def validate_todo_form(task_name, due_date):
    """
    Validate Add Todo form inputs.

    Returns a dict of field-level error messages (empty dict means valid).
    Implements validation rules from LLD section 1 (EPMCDMETST-60027):
      - taskName: required, non-empty after trim
      - dueDate:  required, must be a parseable YYYY-MM-DD date
    """
    errors = {}

    if not task_name or not task_name.strip():
        errors['taskName'] = 'Task name is required.'

    if not due_date or not due_date.strip():
        errors['dueDate'] = 'Due date is required.'
    else:
        try:
            datetime.strptime(due_date.strip(), '%Y-%m-%d')
        except ValueError:
            errors['dueDate'] = 'Please enter a valid date (YYYY-MM-DD).'

    return errors


@app.route("/signup", methods=["GET", "POST"])
def signup():
    if request.method == "POST":
        username = request.form["username"]
        password = request.form["password"]

        users.append({
            "username": username,
            "password": generate_password_hash(password)
        })

        return redirect("/login")

    return render_template("signup.html")

@app.route("/login", methods=["GET", "POST"])
def login():
    if request.method == "POST":
        username = request.form["username"]
        password = request.form["password"]

        for user in users:
            if user["username"] == username and check_password_hash(user["password"], password):
                session["user"] = username
                return redirect("/")

        return "Login failed"

    return render_template("login.html")

@app.route("/logout")
def logout():
    session.clear()
    return redirect("/login")


@app.route('/', methods=['GET', 'POST'])
def home():
    if "user" not in session:
        return redirect("/login")

    global year, month, day
    # Server-side validation errors to pass to the template (LLD §2 – backend interaction)
    form_errors = {}
    form_values = {'taskName': '', 'dueDate': ''}

    if request.method == 'POST':
        form_data = request.form
        new_item_content = form_data.get('newItem', '')
        new_item_duedate = form_data.get('duedate', '')

        # Server-side validation (LLD §1 – validation rules)
        form_errors = validate_todo_form(new_item_content, new_item_duedate)
        form_values = {'taskName': new_item_content, 'dueDate': new_item_duedate}

        if form_errors:
            # Re-render form with errors and preserved values
            return render_template(
                'index.html',
                list_items=items,
                today=curr_day,
                leng=len(items),
                form_errors=form_errors,
                form_values=form_values,
            )

        date_is = new_item_duedate.split("-")
        due_year = int(date_is[0])
        due_month = int(date_is[1])
        due_day = int(date_is[2])

        print(date_is)
        new_item_id = len(items) + 1
        new_item = {
            'id': int(new_item_id),
            'content': new_item_content,
            'due_date': {
                'year': due_year,
                'month': due_month,
                'day': due_day
            }
        }

        items.append(new_item)

        for item in items:
            date = item['due_date']
            if date['year'] == year:
                if date['month'] == month:
                    if date['day'] < day:
                        item['overdue'] = True
                    else:
                        item['overdue'] = False

        return redirect(url_for('home'))

    return render_template(
        'index.html',
        list_items=items,
        today=curr_day,
        leng=len(items),
        form_errors=form_errors,
        form_values=form_values,
    )


@app.route('/delete-item', methods=['POST'])
def delete_item():
    if request.method == 'POST':
        form = request.form
        id = int(form['checkbox'])
        for item in items:
            if item['id'] == id:
                del items[items.index(item)]
                break
        return redirect('/')


if __name__ == '__main__':
    app.run(debug=True)
