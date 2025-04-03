---
layout: page
title: Developer Guide
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

### **AddAnniversaryCommand**
- **Description**: Creates a new anniversary entry for an existing employee.
- **Usage**: `anniversary eid/EMPLOYEE_ID_PREFIX d/DATE n/ANNIVERSARY_NAME [ad/DESCRIPTION] [at/TYPE]...`
- **Constraints**:
    - Valid date format: YYYY-MM-DD
    - Must specify at least one `at/` prefix
- **Required**:
  - eid/ to match an existing employee ID prefix
  - d/ must be a valid date in `YYYY-MM-DD`
  - n/ is the anniversary name
  - at/ is at least one type (e.g., personal, family)
- **Optional**:
  - ad/ is an extra description (e.g., birthday celebration, gift ideas)
- **Example**:
  - `anniversary eid/abc d/2025-01-01 n/Birthday at/Personal`
  - `anniversary eid/1234 d/2023-12-25 n/ChristmasParty ad/GiftExchange at/Cultural`
- **Success**:
```
  Anniversary: <Details> added successfully.  
```
- **Failure Cases**:
  - Multiple Matches: `Multiple employees found with prefix XYZ`
  - No Matches: `No employee found with prefix XYZ`
  - Invalid Date: `Invalid date format: <date>`</date>
  - Missing Required Prefix: `eid/`, `d/`, `n/`, or `at/` not provided
  - Duplicate Anniversary: `This anniversary already exists.`
  - Too Many/Too Few 'at/' Prefixes: Causes parse errors, prompting recheck of syntax.

```
Anniversaries
```
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

### Architecture

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


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

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

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

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

Step 1. FindCommandParser uses ArgumentTokenizer to extract the values for prefixes (n/, jp/, etc.).

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
  - `n/engineer` matches "Software Engineer", "Senior Engineer".
  - `n/eng` does not match "Software Engineer", "Senior Engineer".

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

### Use cases

**Use case: Adding an Employee**

**Actor:** HR Worker

**Preconditions:**
- The system is running.
- The HR worker has valid employee data to input.

**MSS**
1. HR worker chooses to add new employee.
2. HR worker enters required details
3. If valid, the system adds the employee record to the database.
4. The system displays confirmation: `Employee John Doe added successfully.`

**Alternative Flows:**
    - If the format is incorrect, an error message is displayed (e.g., `Error: Invalid date format`).
    - If the email already exists, the system rejects the entry: `Error: Employee already exists.`

**Postconditions:**
    - The employee record is stored successfully in the system.
    - If an error occurred, the system remains unchanged.

---

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

*{More to be added}*

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

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting an employee

1. Deleting an employee while all employees are being shown

   1. Prerequisites: List all employees using the `list` command. Multiple employees in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No employee is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
