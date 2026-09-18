# student-task-manager
Task manager in Java language designed specifically for students to organize their projects, exams, deadlines, and other college-related activities.
# Student Task Manager

Java console application aimed at helping the student manage his or her assignments, exams and other types of tasks.

This small project was created with the purpose of gaining **practical experience in Java** basics: OOP, `ArrayList`, files manipulation, exception handling and `HashMap`.

## Functionality

* Add tasks
* Display tasks
* Mark tasks as done
* Remove tasks
* Categorize tasks
* Provide some productivity metrics
* Store tasks to allow access to them even when the application is closed
* Handle invalid task IDs and errors in user input
* Log some operations

## Used technologies

* Java
* Java Collections (`ArrayList`, `HashMap`)
* Files manipulation
* Exception handling
* `LocalDate`
* Java Logging

## Project structure

This project uses multiple classes instead of putting everything in `main()` method.

### `Task`

Represents an individual task.

Task consists of such attributes as:

* Title
* Deadline
* Category
* Status
* Date of creation

### `TaskManager`

All main operations related to tasks are handled by this class.

Task manager handles:

* Adding tasks
* Displaying tasks
* Marking tasks as done
* Removing tasks
* Saving tasks to a file
* Loading saved tasks from a file

### `AnalyticsEngine`

Provides some
