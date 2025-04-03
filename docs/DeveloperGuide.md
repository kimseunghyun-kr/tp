---
layout: page
title: H'Reers Developer Guide
---

## *Mock UI*

<img src="./images/MockUI.png" alt="UI">

## *Table of Contents*
1. [Mock UI](#mock-ui)
2. [Setting up, Getting started](#setting-up-getting-started)
2. [Architecture](#architecture)
    1. [UI Component](#ui-component)
    2. [Logic Component](#logic-component)
    3. [Model Component](#model-component)
    4. [Storage Component](#storage-component)
    5. [Common Classes](#common-classes)
3. [Implementation](#implementation)
   1. [Save Employee Records](#save-employee-records)
   2. smth
4. [Documentation, Logging, Testing, Configuration, Dev-Ops](#documentation-logging-testing-configuration-dev-ops)
5. [Appendix: Requirements](#appendix-requirements)
   1. [Product Scope](#product-scope)
   2. [User Stories](#user-stories)
   3. [Use Cases](#use-cases)
   4. [Non-Functional Requirements](#non-functional-requirements)
   5. [Glossary](#glossary)
6. [Appendix: Instructions for Manual Testing](#appendix-instructions-for-manual-testing)
   1. [Core Features]()
       1. [Add Employee Records](#add-employee-records)
       2. [Edit Employee Records](#edit-employee-records)
       3. [Delete Employee Records](#delete-employee-records)
       4. [Undo Changes](#undo-changes)
   2. [Anniversary Commands](#anniversary-commands)
      1. [AddAnniversaryCommand](#addanniversarycommand)
      2. [DeleteAnniversaryCommand](#deleteanniversarycommand)
      3. [ShowAnniversaryCommand](#showanniversarycommand)
   3. [Reminder for Events](#reminder-for-events)

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the [AddressBook-Level3 project](https://se-education.org/guides/conventions/java/intermediate.html) created by the [SE-EDU initiative](https://se-education.org). ([UG](https://se-education.org/addressbook-level3/UserGuide.html), [DG](https://se-education.org/addressbook-level3/DeveloperGuide.html),[github](https://github.com/se-edu/addressbook-level3))

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete an employee).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />

The `Model` component:

* Stores the address book data i.e. all `Person` objects (which are contained in a `UniquePersonList` object).
* Stores the currently 'selected' `Person` objects (e.g. results of a search query) as a separate `filtered` list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed'.  
  For example, the UI can be bound to this list so that the UI automatically updates when the data in the list changes.
* Stores a `UserPrefs` object that represents the user's preferences. This is exposed to the outside as a `ReadOnlyUserPrefs` object.
* Does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should be able to exist on their own without depending on other components).
* Additionally, the model includes data structures for reminders, such as `Reminder`, `Anniversary`, and `AnniversaryType`. These are used to support the reminder functionality described later in the [Implementation](#implementation) section.

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------
## Implementation
This section describes some noteworthy details on how certain features are implemented.

### Employee Identification

Our employeeId utilize a UUID based prefix matching system.
The employeeId is generated using the `UUID` class in Java, which creates a universally unique identifier. This identifier is then used as a prefix for each employee's record, allowing for easy searching and retrieval of information.
The prefix matching logic is primarily managed within the EmployeeId class.
The prefix matching logic is mostly used by the Model and AddressBook class, which serves as the facade
that maintains the internal UniquePersonsList.
This feature is also present in the LogicManager class, where before every command is launched, it triggers a scan throughout the database to check if there are any entries
that violate the prefix matching rule, following the lazy validation principle.

---
### Reminder Feature

The `reminder` feature displays a list of upcoming employee anniversaries (birthdays, work anniversaries, and custom anniversaries) occurring within the next 3 days. This section details the implementation of this feature.

#### Design Overview

The `reminder` command is implemented using the `ReminderCommand` class. It interacts with the `Model` to compute a list of upcoming reminders. These reminders are displayed in the UI using a custom `ReminderListPanel`.

The model maintains an internal `ObservableList<Reminder>` that is updated when the command is executed. The `Reminder` class encapsulates:
- A reference to the `Person` whose anniversary is being shown
- The `AnniversaryType` (e.g., birthday, wedding)
- The date of the anniversary
- An optional description

#### Execution Flow

The execution of the `reminder` command proceeds as follows:

1. `LogicManager` receives the command string `"reminder"` and passes it to the `ReminderCommandParser`.
2. `ReminderCommandParser` creates a new `ReminderCommand` object.
3. Upon execution, `ReminderCommand` calls `model.updateReminderList()`, which filters all `Person` objects to find anniversaries within the next 3 days.
4. The `ModelManager` updates its internal observable reminder list.
5. `ReminderCommand` then calls `model.getReminderList()` to retrieve this list.
6. The UI listens to this observable list and renders a `ReminderCard` for each upcoming reminder in a `ReminderListPanel`.

#### Sequence Diagram

The following sequence diagram illustrates the steps described above:

![Reminder Sequence Diagram](images/ReminderSequence.png)

Note: The filtering logic (`within 3 days`) is abstracted into the model for separation of concerns.

#### Activity Diagram

The diagram below illustrates the internal logic of how the model filters the reminder list:

![Reminder Activity Diagram](images/ReminderActivityDiagram.png)

The filtering is based on whether an anniversary falls within the next 3 days. In code, this value is stored as a constant:

```java
public static final int REMINDED_DATE_RANGE = 3;
```

### Find Employees Features
The Find feature allows users to filter and view employees in the address book based on search criteria such as name and job position. This section explains how the feature is implemented and how it behaves under different inputs.

The Find feature is primarily driven by:

* FindCommand: Executes the filtered search based on a predicate

* FindCommandParser: Parses user input and builds the corresponding predicate

* PersonSearchPredicateBuilder: Constructs combined Predicate<Employee> from argument values

* Model: Provides access to the filtered employee list and the update mechanism

Given below is an explanation of how the Find feature works:
When a user enters a command such as:
`find n/Alice jp/engineer` the following steps occur:

Step 1. FindCommandParser uses ArgumentTokenizer to extract the values for prefixes (`n/`, `jp/`).

Step 2. It checks for errors such as:
- No valid prefixes
- Empty input values for all fields
- Invalid preamble

If the tokens are valid, it delegates predicate construction to PersonSearchPredicateBuilder.

Step 3. The builder constructs Predicate<Employee> objects for each field:
- NameContainsKeywordsPredicate
- JobPositionContainsKeywordsPredicate

The two predicates behave slightly differently to suit their field contexts:
- NameContainsKeywordsPredicate uses partial matching.
  This allows users to match names using any substring of a word.
    - `n/Ali` matches with "Alice Tan", "Khalid Ali"
- JobPositionContainsKeywordsPredicate uses full-word matching.
  A keyword must match a whole word in the job position exactly (case-insensitive).
    - `jp/engineer` matches "Software Engineer", "Senior Engineer".
    - `jp/eng` does not match "Software Engineer", "Senior Engineer".

This design was decided because:
- Partial matching in names is user-friendly — users often search by fragments of names.
- Full word matching in job titles avoids false positives and returns more accurate results in professional roles.

It combines both search predicates with logical `AND` so all conditions must be satisfied for an employee to be included in the search results. For example:
`find n/Alice jp/engineer`matches employees with "Alice" in their name AND "engineer" in their job position.

Meanwhile, each predicate performs keyword-based partial matching (OR logic within the field). For example:
`find n/Alice Bob`matches anyone with "Alice" OR "Bob" in their name.

This design was chosen to support both broad and targeted search strategies:
- A user looking for a specific employee is likely to include more fields (e.g., both name and job position).
- A user performing a general search may only filter by a single field, like a partial name.

This flexible approach aims to enable the command to be both intuitive and powerful, depending on the user’s intent.

Step 4: FindCommand executes by calling:
```
model.updateFilteredEmployeeList(predicate);
```
The `FilteredList<Employee>` inside the model is updated, triggering the GUI to reflect the new list.

The diagram below illustrates the sequence of interactions when a user issues the command `find n/Alice jp/engineer`:
<img src="images/FindSequenceDiagram.png" width="700" />

---
--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
HR workers in small companies who are responsible for managing employee engagement and morale. These users often have multiple administrative tasks and struggle to keep up with birthdays, anniversaries, and festive greetings. They can type quickly and prefer CLI over GUI.

**Value proposition**:
We can now have assurance that we aren’t missing any customary birthday/festive remarks. ‘H’Reers automates the process of sending custom birthday and anniversary messages for small company HR workers. Optimized for CLI users, it streamlines contact management and ensures timely, personalized greetings, enhancing employee engagement with minimal effort.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                     | I want to …​                       | So that I can…​                                                |
| -------- |---------------------------------------------|------------------------------------|----------------------------------------------------------------|
| `* * *`  | new HR worker using this for the first time | add employees' contact details     | easily manage my company's records                             |
| `* * *`  | HR worker                                   | delete an old employee's records   | remove outdated or incorrect information from the system       |
| `* * *`  | HR worker                                   | view reminders for upcoming events | be prepared and not miss any anniversary events.               |
| `* * *`  | HR worker                                   | view employee's details            | know if a certain employee has any anniversarys coming up soon |
| `* *`    | HR worker                                   | go back to the previous page       |                                                                |
| `*`      | HR worker                                   | have buttons                       | rest my fingers from typing                                    |

*{More to be added}*

### **Use cases**

#### Use case 1: Adding an Employee

**System**: H'Reers
**Use case**: UC01 - Add New Employee
**Actor:** HR Worker


**Preconditions:**
- The system is running.
- The HR worker has valid employee data to input.

**Guarantees:**
- The employee record is stored successfully in the system.
- If an error occurred, the system remains unchanged.

**Main Success Scenario (MSS)**:
1. HR worker chooses to add new employee.
2. HR worker enters required details
3. If valid, the system adds the employee record to the database.
4. The system displays confirmation: `Employee John Doe added successfully.`

**Alternative Flows:**
    - If the format is incorrect, an error message is displayed (e.g., `Error: Invalid date format`).
    - If the email already exists, the system rejects the entry: `Error: Employee already exists.`

---

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

#### Use case 2: Showing Anniversaries

**System**: H'Reers
**Use case**: UC02 - Add New Employee
**Actor:** HR Worker

**Preconditions**:
- The employee exists in the system, identified by their Employee ID.

**Guarantees**:
- The anniversaries associated with the specified employee are displayed.

**Main Success Scenario (MSS)**:
1. HR Worker enters the `showAnni` command with the specified employee’s ID.
2. H'Reers retrieves the list of anniversaries associated with the employee. 
3. H'Reers opens a new window or panel displaying:
   - Each anniversary’s name, date, and description (if any).
4. A confirmation message is displayed, indicating successful retrieval. 
5. Use case ends.

**Extensions**:
- 1a. Employee Not Found:
  - H'Reers displays an error message indicating that no employee matches the specified ID. 
  - Use case ends.

- 1b. Preamble Found:
    - H'Reers displays an error message indicating that the correct usage of the command.
    - Use case ends.

- 3a. No anniversaries found:
    - H'Reers displays a new windows with no anniversaries found.
    - Use case resume at step 5.

#### Use case 3: Find Employees

**System**: H'Reers
**Use case**: UC03 – Find Employees
**Actor:** HR Worker

**Preconditions**:
- The system has at least one employee record stored.

**Guarantees**:
- Employees matching the specified search criteria are displayed.
- The filtered list replaces the currently displayed list.
- If no employees match the criteria, an empty list is shown.
- The system state remains unchanged.

**Main Success Scenario (MSS)**:
1. HR Worker enters the `find` command with one or more search criteria using supported prefixes (`n/`, `jp/`).
2. H'Reers filters the employee list using a combined predicate and displays the filtered list.
3. A success message is shown indicating how many results were found.
4. Use case ends.

**Extensions**:
- 1a. No Prefix Provided:
  - H'Reers displays an error message indicating to add at least one prefix.
  - Use case ends.

- 1b. Preamble Found:
  - H'Reers displays an error message indicating the command format is invalid.
  - Use case ends.

- 1c. All Fields Empty:
  - H'Reers displays an error message indicating to add at least one prefix.
  - Use case ends.

- 2a. No Matching Employees Found:
  - H'Reers displays an empty list.
  - Use case resume at step 3.

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 employees without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The product should be for a single user.
5.  No usage of a shared file storage mechanism.

*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------
## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

- Initial launch

    1. Download the jar file and copy into an empty folder

    2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

- Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

---
### **Add Employee Records**

#### Purpose:
Enables HR workers to store employee information, including name, position, birthday, and work anniversary.

#### Command Format:
```
add n/NAME p/POSITION b/BIRTHDAY wa/WORK_ANNIVERSARY e/EMAIL t/TAGS
```

#### Example Commands:
```
add n/John Doe p/Software Engineer b/1990-05-10 wa/2015-07-20 e/johndoe@abc.com
```

#### Parameter Rules:
- **Name**: Alphabets and spaces only, case-insensitive.
- **Position**: Must match predefined job titles.
- **Birthday & Work Anniversary**: Format - YYYY-MM-DD.
- **Email**: Must contain '@domainname.com'.

#### Outputs:
- **Success**: `Employee John Doe added successfully.`
- **Failure**: `Error: Invalid date format.`

#### Duplicate Handling:
- If an employee with the same email exists: `Error: Employee already exists.`

#### Additional Targets:
- Partial addition of data.
- Import employee records from CSV.
---

### **Delete Employee Records**
#### Purpose:
Allows HR workers to remove outdated or incorrect employee records.

#### Command Format:
```
delete n/NAME p/POSITION b/BIRTHDAY wa/WORK_ANNIVERSARY
```

#### Example Commands:
```
delete n/John Doe p/Software Engineer b/1990-05-10 wa/2015-07-20
```

#### Outputs:
- **Success:** `Employee John Doe deleted successfully.`
- **Failure:** `Error: Employee not found.`

#### Duplicate Handling:
If multiple employees match, prompt for additional details to ensure correctness.

---

### **Edit Employee Records**

#### Purpose:
Allows HR workers to modify existing employee information, such as name, phone number, email, job position, or tags.

#### Command Format:

```
edit EMPLOYEE_ID_PREFIX [n/NAME] [p/PHONE] [e/EMAIL] [j/JOBPOSITION] [t/TAG]... [eid/EMPLOYEE_ID]
```

#### Example Commands:

```
edit abcd12 p/91234567 e/johndoe@example.com eid/efgh3123
```
```
edit 5678ef n/Jane Smith j/Senior Manager t/management
```

#### Parameter Rules:
- **EMPLOYEE_ID_PREFIX**: Must match exactly one employee in the system
- **NAME**: Alphabets and spaces only, case-insensitive
- **PHONE**: Numbers only
- **EMAIL**: Must contain '@' and valid domain
- **JOBPOSITION**: Must be a valid job position
- **TAG**: Alphanumeric words
- **EMPLOYEE_ID**: Valid UUID format

#### Outputs:
- **Success**: `Edited Employee: [name] Phone: [phone] Email: [email] Job Position: [jobPosition] Tags: [tags]`
- **Failure**: Various error messages depending on the issue:
    - `At least one field to edit must be provided.`
    - `Multiple employees found with prefix XYZ`
    - `No employee found with prefix XYZ`
    - `The new employee ID conflicts with existing employee IDs`

#### Implementation:

The edit command is implemented by the `EditCommand` class, which extends the abstract `Command` class. It works through the following process:

1. The `EditCommandParser` parses the command input to create an `EditEmployeeDescriptor` containing the fields to be updated.
2. The command identifies the target employee using the employee ID prefix.
3. The system verifies that exactly one employee matches the provided prefix.
4. A new employee object is created with updated fields from the descriptor, preserving unchanged fields from the original employee.
5. The system validates that the new employee ID (if changed) doesn't conflict with existing IDs.
6. The original employee record is replaced with the edited version in the model.

The edit command also supports the undo/redo feature by preserving the previous state via the model's commit function.

![EditCommandDiagram](images/EditSequenceDiagram.png)

### **Undo Changes**
#### Purpose:
Allows HR workers to revert the most recent change made to the employee records, such as undoing an added or deleted employee.

#### Command Format:
```
undo
```

#### Functionality:
* **Undo Last Action**: Reverts the most recent change made to the employee data. If the last operation was adding a new employee, the employee will be removed. If the last operation was deleting an employee, the employee will be restored.
##### Outputs:
* **Success**: Undo successful. Last action reverted.
* **Failure**: Error: No changes to undo. (This will occur if there are no actions to undo or the history stack is empty.)

---
### **Anniversary commands**
#### Purpose:
Allows HR workers to manage employee anniversaries.

---
### **AddAnniversaryCommand**
#### Purpose
Creates a new anniversary entry for an existing employee. This command can create custom Anniversaries that were otherwise not supported within the AddPerson Command.

#### Command Format
``` plaintext
addAnni eid/EMPLOYEE_ID_PREFIX d/DATE an/ANNIVERSARY_NAME at/ANNIVERSARY_TYPE [ad/DESCRIPTION] [atdesc/TYPE_DESCRIPTION]
```
short form support for Birthdays
``` plaintext
addAnni eid/EMPLOYEE_ID_PREFIX n/name bd/DATE
```
short form support for Work Anniversaries
``` plaintext
addAnni eid/EMPLOYEE_ID_PREFIX n/name wa/DATE
```

| **Prefix** | **Meaning**                                               | **Required?**                     | **Example Value**      |
|------------|-----------------------------------------------------------|-----------------------------------|------------------------|
| `eid/`      | A partial prefix of the Employee ID                       | Required                          | `0c2414da`             |
| `d/`       | The date of the anniversary                               | Required                          | `2025-03-13`           |
| `an/`      | A short name for the anniversary                          | Required                          | `Silver Wedding`       |
| `at/`      | The main category/type of the event                       | Required                          | `Wedding`              |
| `desc/`    | A text description of the anniversary                     | Optional                          | `Celebrating 25 years` |
| `atdesc/`  | A description of the type                                 | Optional                          | `Personal`, `Work`     |
| `bd/`      | A short name for the birthday                             | Optional                          | `Birthday`             |
| `wa/`      | A short name for the work anniversary                     | Optional                          | `Work Anniversary`     |
| `n/`       | Name of the person required for birthday/work anniversary | Optional(required for bd/wa only) | `Alex shenanigans`     |

> **Note**: Brackets `[ ]` indicate an optional field. The prefix `td/` can appear multiple times to supply multiple type descriptors.

#### Example Command
```plaintext
addAnni eid/0c2414da d/2025-03-13 an/Silver Wedding at/Wedding ad/Celebrating 25 years atdesc/Personal
```
- `addAnni` - the addAnniversary command you are running
- `eid/0c2414da`: the Employee Id prefix you are attaching the anniversary to
- `d/2025-03-13`: the date of the anniversary on `2025-03-13`
- `an/Silver Wedding`: the name of the anniversary `Silver Wedding`
- `at/Wedding`: The name of the anniversary type - `Wedding`
- `ad/Celebrating 25 years` :  The description of the anniversary - `Celebrating 25 years` (optional)
- `atdesc/Personal`: The description of the anniversary type -`Personal` (optional)

If exactly one employee’s ID starts with `0c2414da`, this will create a `Silver Wedding` anniversary of the type `Wedding` for that employee, with an optional description and additional type descriptors.

```plaintext
addAnni eid/0c2414da n/Alex shenanigans bd/2025-03-13
```
- `addAnni` - the addAnniversary command you are running
- `eid/0c2414da`: the Employee Id prefix you are attaching the anniversary to
- `n/Alex shenanigans`: the name of the person you are attaching the birthday to (note that it is **strongly** recommended to use the name of the person the employee id belongs, unless otherwise needed)
- `bd/2025-03-13`: the date of the anniversary on `2025-03-13`
  If exactly one employee’s ID starts with `0c2414da`, this will create a `birthday` (anniversary) with the Persons' `name` specified in the command.

```plaintext
addAnni eid/0c2414da n/Alex shenanigans wa/2025-03-13
```
- `addAnni` - the addAnniversary command you are running
- `eid/0c2414da`: the Employee Id prefix you are attaching the anniversary to
- `n/Alex shenanigans`: the name of the person you are attaching the birthday to (note that it is **strongly** recommended to use the name of the person the employee id belongs, unless otherwise needed)
- `wa/2025-03-13`: the date of the anniversary on `2025-03-13`
  If exactly one employee’s ID starts with `0c2414da`, this will create a `work anniversary` with the Persons' `name` specified in the command.

#### Parameter Rules:
- The eid/ prefix must contain a valid, non-empty partial Employee ID. It should not have spaces and must conform to the format expected by EmployeeId.isValidEmployeeId().
- **For a Standard Anniversary:**
  - The d/ (date) must follow the ISO format (YYYY-MM-DD). 
  - Both an/ (anniversary name) and at/ (anniversary type) are required. 
  - Optional fields like desc/ (description) and atdesc/ (type description) can be provided, but if provided, they should be in a valid text format. 
- **For Birthdays (short form):**
  - The bd/ prefix should contain a valid date in the ISO format (YYYY-MM-DD). 
  - The n/ prefix must be provided to denote the name for the birthday entry.
- **For Work Anniversaries (short form):**
  - The wa/ prefix should contain a valid date in the ISO format (YYYY-MM-DD). 
  - The n/ prefix must be provided to denote the name for the work anniversary entry. 
- Do not mix fields from different anniversary types. For example, providing both an/ with bd/ or wa/ in the same command will result in a conflict.
- Last of same field win:
  - the last occurence of a field will win. this means that for duplicated fields, the last occuring field will be used

#### Outputs:
- **success:** `New anniversary added: <anniversary_details>`
- **Failure Cases**:
    - Missing Required Fields: `Invalid command format! <AddAnniversaryCommand MESSAGE_USAGE>`
    - Invalid employeeId format : `"Employee ID prefix must be 1-36 characters long, containing only letters, digits, and '-'.";`
    - Invalid mix of fields : `Invalid command format! Cannot mix standard anniversary fields with birthday or work anniversary fields.`
    - Invalid Date: `Invalid command format! Invalid date format! Please use the format YYYY-MM-DD.`
    - Employee Resolution Issue: `Found multiple employees with employeeId starting with <employeeId_prefix>`
    - No Matching Employee: `No employee found with employeeId starting with <employeeId_prefix>`
    - Duplicate Anniversary: `This exact anniversary (date + name + type + description) already exists for that employee.`

#### Implementation:
The add anniversary command is implemented by the AddAnniversaryCommand class, which extends the abstract Command class. It works through the following process:

1. The AddAnniversaryCommandParser parses the command input to extract the employee ID prefix and the anniversary details.
2. The command identifies the target employee using the employee ID prefix.
3. The system verifies that exactly one employee matches the provided prefix.
4. The command checks for duplicate anniversary entries in the target employee's record.
5. If no duplicate exists, a new employee object is created with an updated anniversary list, while preserving unchanged fields from the original employee.
6. The original employee record is replaced with the updated version in the model.
7. A success message is returned confirming the addition of the anniversary.

![AddAnniversaryCommandDiagram](images/AddAnniversaryCommandSequenceDiagram.png)

---
### **DeleteAnniversaryCommand**
#### Purpose
removes a specific anniversary from an existing employee’s record, based on the anniversary's
order within the Employee's list of anniversaries.

#### **Command Format**
```plaintext 
deleteAnniversary eid/EMPLOYEE_ID ai/INDEX
```
#### **Parameters**

| **Prefix** | **Meaning**                                                   | **Required?** | **Example**  |
|------------|---------------------------------------------------------------|---------------|-------------|
| `eid/`     | A partial (or full) prefix of the Employee ID                | Required      | `0c2414da`  |
| `ai/`      | The 1-based index of the anniversary you wish to remove      | Required      | `1`         |

#### **Example Command**
```plaintext
deleteAnniversary eid/0c2414da ai/1
```
- `deleteAnniversary` - the command you are running
- `eid/0c2414da`: the Employee Id prefix you are attaching the anniversary to
- `ai/1`: the index of the anniversary you want to delete
  this will delete the anniversary at index 1 of the employee with the Employee ID prefix `0c2414da`.

#### Parameter Rules:
- Employee ID Prefix (eid/):
  - Must contain a valid, non-empty Employee ID prefix. 
  - Must not include spaces. 
  - Must conform to the format expected by EmployeeId.isValidEmployeeId().
- Anniversary Index (ai/):
  - Must be provided as a 1-based index (via the ai/ prefix). 
  - The index must represent a valid position within the target employee’s anniversary list. 
  - A negative or out-of-bounds index will trigger an error.
- Field Exclusivity:
  - Only the eid/ and ai/ prefixes are expected. If any unexpected fields are present, the command should be considered invalid.
- Last of same field win:
  - the last occurence of a field will win. this means that for duplicated fields, the last occuring field will be used

#### Outputs:
**Success:**
- Returns a success message: `anniversary deleted: <anniversary_details>`
** Failure Cases: **
- Missing Required Fields: `Invalid command format! <DeleteAnniversaryCommand MESSAGE_USAGE>`
- Invalid Employee ID Format: `Employee ID prefix must be 1-36 characters long, containing only letters, digits, and '-'.`
- Employee Resolution Issues:`Found multiple employees with employeeId starting with <employeeId_prefix>`
- Employee Not Found: `No employee found with employeeId starting with <employeeId_prefix>`
- Anniversary Index Out of Bounds:`The index you are searching for is out of bounds for the anniversary.`

#### Implementation:
1. Parsing the Input:
- The DeleteAnniversaryCommandParser tokenizes the input using the PREFIX_EMPLOYEEID (eid/) and PREFIX_ANNIVERSARY_INDEX (ai/). It validates that both required prefixes are present. Missing prefixes trigger a ParseException with the usage message.

2. Validating and Converting Input:
- The parser uses ParserUtil.parseEmployeeIdPrefix() to validate and convert the employee ID prefix. It uses ParserUtil.parseIndex() to convert the anniversary index string into an Index object. An additional check confirms that the index is within acceptable bounds (non-negative).

3. Identifying the Target Employee:
- In the execute method of DeleteAnniversaryCommand, the system retrieves all employees whose IDs start with the given prefix.

4. It verifies that exactly one employee matches:
- If multiple employees are found, it throws a CommandException with a corresponding error message. If no employee is found, it also throws a CommandException.

5. Deleting the Anniversary:
- The command retrieves the target employee’s anniversary list. It checks whether the provided index is within the bounds of this list. If valid, the anniversary at the specified index is removed.

6. Updating the Employee Record:
- A new employee object is created using the builder pattern with the updated anniversary list while preserving unchanged fields (e.g., employee ID, name, job position, email, phone, tags). The model is updated by replacing the old employee record with the new one.

7. Returning the Outcome:
- On successful deletion, the command returns a success message that includes the details of the deleted anniversary.

![DeleteAnniversaryCommand](images/DeleteAnniversaryCommandSequenceDiagram.png)
---
### **exportCommand**
#### Purpose
You can use `export` to save the currently visible list of people in the Hreers application to a file (JSON or CSV).
If you provide a specific directory path (`fp/`), the system will export the file there.
If you also include a file name (`fn/`), any missing extension is automatically appended based on the file type (`ft/`) chosen
This means that you do **not** need to include the extension behind the file name.
For CSV based inputs, an employee entry with multiple Anniversaries will be duplicated to multiple rows
with same employeeId and same details(name, job position, phone number, email), but each row having different anniversaries

#### **Command Format**
```plaintext
export ft/FILE_TYPE [fp/FILE_PATH] [fn/FILE_NAME]
```

#### **Parameters**

| **Prefix** | **Meaning**                                     | **Required?**              | **Example Value**     |
|------------|-------------------------------------------------|----------------------------|------------------------|
| `ft/`      | The file type to export (`json` or `csv`)       | **Required**               | `json` or `csv`       |
| `fp/`      | The optional file path (directory or full path) | Optional if `fn/` is used | `./output/`           |
| `fn/`      | The optional filename (extension auto-added)    | Optional if `fp/` is used | `contacts`, `data.csv`|

#### **Example Usage**
```plaintext
export ft/json fp/data/ fn/contacts
```
Explanation:
`export` — the command you're running
`ft/json` — file type is JSON
`fp/data/` — file path is the data/ directory
`fn/contacts` — file name is contacts (without extension)

This will save your current contact list as a file named contacts.json in the data/ folder.

```plaintext
export ft/csv fp/data/contacts.csv
```
Explanation:
`export` — the command you're running
`ft/csv` — file type is CSV
`fp/data/contacts.csv` — file path is the data/ directory and the file name is contacts.csv - note that if you want to define the file within the file path, you have to ensure that the file type matches the extension of your file. so `contaacts.json` when set to csv will give you an error

This will save your current contact list as a file named contacts.csv in the data/ folder.

```plaintext
export ft/json fp/data/ fn/contacts
```
Explanation:
`export` — the command you're running
`ft/json` — file type is JSON
`fp/data/` — file path is the data/ directory
`fn/contacts` — file name is contacts (without extension)

This will save your current contact list as a file named contacts.json in the data/ folder.

```plaintext
export ft/json
```
Explanation:
`export` — the command you're running
`ft/json` — file type is JSON

This will save your current contact list as a file named `output.json` in the folder where the jar is stored.
#### Parameter Rules:
- File Type (ft/ via PREFIX_FILETYPE):
  - Required. Must be provided as either "json" or "csv". If missing or invalid, an error is thrown indicating an invalid file type using the command usage message.

- File Path (fp/ via PREFIX_FILEPATH):
  - Optional. Represents either a full file path (including the filename) or a directory path. If provided as a full file path that includes a filename, no separate filename should be provided.

- Filename (fn/ via PREFIX_FILENAME):
  - Optional. Must be provided if the file path only specifies a directory. If provided without an extension, the required extension (matching the file type) is automatically appended.

- Field Exclusivity & Consistency:
  - If both a file path (with a filename) and a separate filename are provided, the parser throws an error to avoid ambiguity. 
  - The final resolved file path must have an extension that exactly matches the provided file type (.json for json and .csv for csv).
#### Outputs:
Success:
- On successful export, the command returns a message formatted as: `Exported <displayed_employees> employees in <filetype> format to <resolved_path>`
- `<displayed_employees>` lists the employees that were visible at the time of export.
- `<resolved_path>` is the final file path where data was exported.

Failure Cases:

- No Data to Export:
  - If the filtered employee list is empty, a CommandException is thrown with the message:No people to export.

- Invalid File Type:
  - If the file type provided is not "json" or "csv", a CommandException is thrown using the export command’s usage message.

- File Path Resolution Errors:
  - If both a full file path (with filename) and a separate filename are provided, an error is thrown with the message:
  Provide either a full file path or a filename, not both.

- If a file path is provided as a directory and no filename is given, an error is thrown stating that the filename must be provided.

- If the resolved file’s extension does not match the provided file type, an error is thrown indicating the mismatch.

#### Implementation:
1. Parsing the Input:
- The ExportCommandParser tokenizes the user input using the prefixes for file type (ft/), file path (fp/), and filename (fn/).
- It calls verifyFileTypePresentAndValid from the FilePathResolverUtils to ensure the file type is provided and valid. 
- The parser retrieves the optional file path and filename values. 
- The final file path is determined by calling FilePathResolverUtils.resolveFilePath(filePath, filename, fileType).

2. Command Construction:
- After resolving the file type and file path, a new ExportCommand instance is created with these parameters. 
- The command object stores the file type as a string and the file path as a Path object.

3. Executing the Export:
- In the execute method of ExportCommand, the command:
- Retrieves the current list of filtered employees from the model. 
- Checks whether there are any employees to export; if none, it throws a CommandException. 
- Depending on the file type:
- If "json", it calls AddressBookFormatConverter.exportToJson(displayedEmployees, path). 
- If "csv", it calls AddressBookFormatConverter.exportToCsv(displayedEmployees, path). 
- The export process is logged using the application's logger for tracking purposes. 
- Any errors during file writing or conversion result in a caught exception and an appropriate error message.

4. Returning the Outcome:
- Upon successful export, the command returns a CommandResult containing a success message with details of the export (number of employees, file type, and resolved file path).

![exportCommand](images/ExportSequenceDiagram.png)

#### Implementation (FileDataPathUtils)
1. Determining the Final File Path:
- The utility method resolveFilePath takes in three parameters: an optional file path, an optional filename, and the file type. 
- It first calculates the expected file extension based on the file type (e.g., .json or .csv).

2. Handling Various Input Combinations:
    2.1. Full File Path Provided:
   - If the provided file path appears to include a filename (determined by the presence of a dot in the filename), the method validates the extension. 
   - If a separate filename is also provided in this case, a ParseException is thrown to avoid ambiguity.

    2.2. Directory Path Provided:
   - If the file path represents a directory (i.e., it does not contain a filename), a filename must be provided. 
   - The provided filename is then checked and automatically appended with the expected extension if it is missing.

    2.3. Only Filename Provided:
   - If no file path is given but a filename is provided, the filename is used (with the appropriate extension ensured).

3. Validation of File Extension:
- The method validateFileExtension checks that the actual file extension of the resolved file matches the expected extension. 
- If there is a mismatch, a ParseException is thrown with a message indicating the file extension conflict.

4. Error Handling:
- If neither a file path nor a filename is provided, an IllegalArgumentException is thrown to indicate that at least one must be provided.

---
### Reminder for Events
#### Purpose:
Notifies HR about upcoming employee birthdays and work anniversaries.

#### Command Format:
No commands needed

#### Outputs:
- **GUI Output:**
```
Jane Doe's birthday is today! (May 9, 1990).
John Doe's birthday is tomorrow (May 10, 1990). 
Jane Smith’s work anniversary is in 2 days! (November 1, 2010).
```
---
### **Save Employee Records**
#### Purpose:
Ensures employee records persist across sessions.

#### Command Format:
- **Automatically saves every 30 seconds.**

#### Outputs:
- **Success:** `Save occurred successfully.`
- **Failure:** `Save failed -> reverting to backup file.`

#### Additional Targets:
- Full flush backup (complete overwrite).
- Intermediate .tmp file for autosave.

---

## **Appendix: Planned Enhancements**

Team Size: 5 

In future versions of H'Reers, the following enhancements are planned to improve functionality, user experience, and data consistency:

1. 
