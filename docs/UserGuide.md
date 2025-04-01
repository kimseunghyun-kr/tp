---
layout: page
title: H'Reers User Guide
---

Managing *employee details, birthdays, and work anniversaries* can be time-consuming and prone to error. 
For HR professionals, keeping track of all this information manually can become overwhelming, even in small to medium organizations. 
**H'Reers** is designed to solve these issues by offering an intuitive desktop application with both a [GUI](#glossary) and [CLI](#glossary) for seamless interaction.

With features like [adding](#adding-an-employee-add), [editing](#editing-an-employee-edit), and [deleting](#deleting-an-employee-delete) employee records and [adding birthdays and anniversaries](#anniversary-commands),
H'Reers helps you to stay organized, accurate, and on top of important milestones.
The [Reminder](#reminder-commands) system provides a focused view of your employees with these events occurring within the next 3 days.
When the [command](#viewing-upcoming-birthdays-reminder-bd) is used, new panels appear beside the main list, displaying the filtered employees.

By centralizing these tasks in one tool, H'Reers makes employee management more efficient and less error-prone, saving you time and improving overall workflow.

## Table of Contents

1. [Quick Start](#quick-start)
2. [Features](#features)
2. [Basic Commands](#basic-commands)
    - [Viewing help: `help`](#viewing-help-codehelpcode)
    - [Adding an employee: `add`](#adding-an-employee-codeaddcode)
    - [Listing all employees: `list`](#listing-all-employees-codelistcode)
    - [Editing an employee: `edit`](#editing-an-employee-codeeditcode)
    - [Undoing the last command: `undo`](#undoing-the-last-command-codeundocode)
    - [Locating employees: `find`](#locating-employees-codefindcode)
    - [Deleting an employee: `delete`](#deleting-an-employee-codedeletecode)
3. [Anniversary Commands](#anniversary-commands)
    - [Adding anniversaries: `addAnni`](#adding-anniversaries-codeaddannicode)
    - [Showing anniversaries: `showAnni`](#showing-anniversaries-codeshowannicode)
    - [Deleting anniversaries `deleteAnni`](#deleting-anniversaries-codedeleteannicode)
4. [Reminder Commands](#reminder-commands)
    - [Viewing upcoming birthdays: `reminder bd`](#viewing-upcoming-birthdays-codereminder-bdcode)
    - [Viewing upcoming work anniversaries: `reminder wa`](#viewing-upcoming-work-anniversaries-codereminder-wacode)
5. [Quality of Life Commands](#quality-of-life-features)
    - [Clearing all entries: `clear`](#clearing-all-entries-codeclearcode)
    - [Exiting the program: `exit`](#exiting-the-program-codeexitcode)
    - [Saving the data](#saving-the-data)
    - [Editing the data file](#editing-the-data-file)
6. [Import and Export](#import-and-export)
    - [Importing data: `import`](#importing-data-codeimportcode)
    - [Exporting data: `export`](#exporting-data-codeexportcode)
7. [FAQ](#faq)
8. [Known issues](#known-issues)
9. [Command summary](#command-summary)
10. [Glossary](#glossary)

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your H'Reers.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar H'Reers.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/MockUI.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

    * `list` : Lists all contacts.

    * `add n/John Doe p/98765432 e/johnd@example.com jb/Crypto Minor bd/2000-01-01 wa/2014-12-12` : Adds a contact named `John Doe` to H'Reers.

    * `delete Employee_ID_prefix` : Deletes the specified employee contact. _Note: Employee_ID_prefix has to pinpoint only one Employee for delete to work._

    * `clear` : Deletes all contacts.

    * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

<br>**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `add n/NAME`, `NAME` is a parameter which can be used as `add n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</div>

---
## Basic Commands

---
### Viewing help: `help`

Shows a message explaining how to access the help page.

![help message](images/HelpMessage.png)

Format: `help`

---
### Adding an employee: `add`

Adds an employee to H'Reers.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL jp/JOB [t/TAG]… [bd/DATE] [wa/DATE]​`

Date format: `YYYY-MM-DD`
<div markdown="span" class="alert alert-primary">:bulb: Tip:
An employee can have any number of tags (including 0)
</div>

<div markdown="span" class="alert alert-primary">:bulb: Tip:
bd/ stands for birthday, and wa/ stands for work anniversary — these are standard anniversaries the app automatically creates for you when you provide a date.

If you wish to track other types of anniversaries, you can add them using the [add anniversary command below](#add-anniversaries-addanni).
bd/ stands for birthday and wa/ stands for work anniversary.
</div>

Examples:

* `add n/John Doe p/98765432 e/johnd@example.com jp/President bd/2001-01-01 wa/2020-07-08` Adds `John Doe` into H'Reers
* `add n/Betsy Crowe t/Part Time Worker e/betsycrowe@example.com jp/Cleaner p/1234567 t/Personal Trainer bd/2005-12-01 wa/2025-05-21` Adds `Betsy Crowe` into H'Reers with a tag of Part Time Worker and Personal Trainer

---
### Listing all employees: `list`

Shows a list of all employees in H'Reers.

Format: `list`

---
### Editing an employee: `edit`

Edits an existing employee in H'Reers.

Format: `edit Employee_ID_prefix [n/NAME] [eid/EMPLOYEE_ID] [p/PHONE] [e/EMAIL] [jp/JOB] [t/TAG]…​`

* Edits the specified employee. The Employee ID can be shortened down and not necessarily needed to type in the full ID. The Employee ID prefix **must be Unique.**
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the employee will be removed i.e adding of tags is not cumulative.
* You can remove all the employee’s tags by typing `t/` without
  specifying any tags after it.
* You can change the employee id by typing `edit Employee_ID_prefix eid/Employee_ID` where Employee_ID is the new full string of a valid eid.

Examples:
*  `edit 1re p/91234567 e/johndoe@example.com` Edits the phone number and email address of the specified employee to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2dsf n/Betsy Crower t/` Edits the name of the specified employee to be `Betsy Crower` and clears all existing tags.
*  `edit 1sdg21 eid/3b9417cc-cf4e-4231-bc4d-4fd167c2abc6` Edits the employee id to be now `3b9417cc-cf4e-4231-bc4d-4fd167c2abc6` so long as no such employee id already exists.

---
### Undoing the last command: `undo`

Will undo to before the data is changed.

Format: `undo`

* Only works if any data in H'Reers has been changed.

#### Output:
* If data has been changed: `Undo successful!`
* No data changed: `No undo available!`

Examples:
* `undo` Will return the previous changed saved data.
* `undo 2` Will still return to the previous changed saved data as `undo` ignores all parameters after it.

---
### Locating employees: `find`

Finds employees whose names or/and job positions contain any of the given keywords.


<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
You can use this format in 3 ways!
</div>

Format 1 (Searching for name): `find n/KEYWORD [MORE_KEYWORDS]`

Format 2 (Searching for job positions): `find jp/KEYWORD [MORE_KEYWORDS]`

Format 3 (Searching for both name and job positions): `find n/KEYWORD [MORE_KEYWORDS] jp/KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* [For **Job Positions**] Only full words will be matched e.g. `Mana` will not match `Manager`
* [For **Name**] Partial words can be matched e.g. `Han` will match `Hans`

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the find command:**<br>

* When you search within a single field (like n/ for name or jp/ for job position), you only need one of the keywords to match — it's an OR search.

    * For example: find `n/Hans Bo` will find anyone with "Hans" or "Bo" in their name, like `Hans Gruber` or `Bo Yang`.

* When you use multiple fields together, the command finds people who match all of them — it becomes an AND search.

    * For example: `find n/Hans jp/engineer` finds people whose name includes "Hans" and whose job position includes "engineer".

* In the case of multiple fields and keywords, you will only see a person on the list if they match at least one keyword from each field.

    * So `find n/Hans Bo jp/dev manager` finds people whose name contains "Hans" or "Bo", and whose job position has the word "dev" or "manager".
</div>

Examples:
* `find n/li` returns `David Li` and `Real Li`
* `find n/david Li` returns `David Li` and `Real Li`
* `find n/li ri jp/ dev manager` returns `David Li`, `Real Ri` and `Real Li`<br>


![result for 'find n/li ri jp/ dev manager'](images/FindLiRiDevManagerResult.png)

---
### Deleting an employee: `delete`

Deletes the specified employee from H'Reers.

Format: `delete Employee_ID_prefix`

* Deletes the employee of the specified Employee ID.
* The Employee ID **must be valid and unique**

Examples:
* `list` followed by `delete Employee_ID_prefix` deletes the specified employee.

---
## Anniversary Commands

---
### Adding Anniversaries:  `addAnni`
You can use `addAnni` to add an anniversary to an employee's record in the address book.
This command can create custom Anniversaries that were otherwise not supported within the AddPerson Command.

#### Command Format
``` plaintext
addAnni eid/EMPLOYEE_ID_PREFIX d/DATE an/ANNIVERSARY_NAME at/ANNIVERSARY_TYPE [ad/DESCRIPTION] [atdesc/TYPE_DESCRIPTION]
```

| **Prefix** | **Meaning**                           | **Required?** | **Example Value**                   |
|------------|---------------------------------------|---------------|-------------------------------------|
| `e/`       | A partial prefix of the Employee ID   | Required      | `0c2414da`                          |
| `d/`       | The date of the anniversary           | Required      | `2025-03-13`                        |
| `n/`       | A short name for the anniversary      | Required      | `Silver Wedding`                    |
| `t/`       | The main category/type of the event   | Required      | `Wedding`                           |
| `desc/`    | A text description of the anniversary | Optional      | `Celebrating 25 years`             |
| `td/`      | Additional type descriptors           | Optional      | `Personal`, `Work`, etc. (repeatable) |

> **Note**: Brackets `[ ]` indicate an optional field. The prefix `td/` can appear multiple times to supply multiple type descriptors.

#### Example Usage
```plaintext 
addAnni eid/0c2414da d/2025-03-13 an/Silver Wedding at/Wedding ad/Celebrating 25 years atdesc/Personal
```
- Employee ID prefix: `0c2414da`
- Date: `2025-03-13`
- Anniversary Name: `Silver Wedding`
- Anniversary Type: `Wedding`
- Description: `Celebrating 25 years` (optional)
- Additional Type: `Personal` (optional)

If exactly one employee’s ID starts with `0c2414da`, this will create a `Silver Wedding` anniversary of the type `Wedding` for that employee, with an optional description and additional type descriptors.

<details>
<summary>Quirks & Edge Cases</summary>

**1. Employee ID Prefix Ambiguity**
- If the prefix matches multiple employees, an error displays.
- If the prefix does not match any employees, an error displays.

**2. Duplicate Anniversary Detection**
- The command checks existing anniversaries for the same date, name, description, and type.

**3. Multiple Type Descriptors**
- `atdesc/` can be repeated multiple times.

**4. Prefix Order**
- Required prefixes must appear: `eid/`, `d/`, `an/`, `at/`.

**5. Date Formatting**
- Must be in `YYYY-MM-DD` format.

**6. Description Field**
- `ad/` is optional.

**7. Successful Addition**
- A success message is displayed upon completion.
</details>

<details>
<summary>Common Errors & Messages</summary>

- **Multiple employees found with prefix**
    - Provide a longer Employee ID prefix.

- **Employee ID prefix not found**
    - Check for typos or confirm the employee exists.

- **Anniversary already exists**
    - Change at least one detail to avoid duplicates.

- **Invalid Command Format**
    - Ensure all required prefixes are present.
</details>

<details>
<summary>Tips</summary>

- **Use Unique Descriptions**: Helps differentiate otherwise similar anniversaries.
- **Check Date Validity**: Watch out for invalid or non-existent dates (e.g., leap years).
- **Provide Enough ID Prefix**: So only the intended employee is matched.
</details>

---
### **Showing anniversaries:** `showAnni`

You can use this command to view all anniversaries linked to a specific employee, based on their Employee ID.

Format: `showAnni eid/Employee_ID`

What will you see:

* A new window will open showing the employee’s anniversaries.
* You’ll see details like the date, name, and description of each anniversary.
* If you’re new to the app, you can also use the button in the GUI to do the same thing.

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
The Employee ID is the unique identifier assigned to each person in H'Reers — either one you provided when adding them, or one that was auto-generated.
</div>

Examples:
* showAnni eid/e22e5292-0353-49a9-9281-5a76e53bc94f

---
### **Deleting Anniversaries:** `deleteAnni`
You can use `deleteAnni` to remove a specific anniversary from an existing employee’s record, based on the anniversary's
order within the Employee's list of anniversaries.
If successful, the chosen anniversary will no longer appear in that employee’s list of anniversaries.


#### **Command Format**
```plaintext 
deleteAnniversary eid/EMPLOYEE_ID ad/INDEX
```
#### **Parameters**

| **Prefix** | **Meaning**                                                   | **Required?** | **Example**  |
|------------|---------------------------------------------------------------|---------------|-------------|
| `eid/`     | A partial (or full) prefix of the Employee ID                | Required      | `0c2414da`  |
| `ad/`      | The 1-based index of the anniversary you wish to remove      | Required      | `1`         |

#### **Example Usage**
```plaintext
deleteAnniversary eid/0c2414da ad/1
```


<details>
<summary>Quirks & Edge Cases</summary>

- **Employee ID Prefix Ambiguity**  
  If multiple employees share the same prefix, an error is thrown, prompting you to use a longer or full ID.

- **Employee Not Found**  
  If no employee matches the given prefix, an error is displayed.

- **Index Out of Bounds**  
  If the given index is greater than the number of anniversaries in the record, an error is displayed.
</details>

<details>
<summary>Common Errors & Messages</summary>

- **Multiple employees found with prefix**  
  Provide a longer or more specific ID prefix.

- **Employee ID prefix not found**  
  Check for typos or confirm that the employee exists.

- **Anniversary index out of bounds**  
  The index must be within the valid range of the employee’s anniversary list.
</details>

<details>
<summary>Tips</summary>

- **Confirm the Anniversary Index**  
  Because the code internally uses zero-based indexing, but the user command typically uses one-based indexing, verify that you’re specifying the correct anniversary number.

- **Disambiguate Employee IDs**  
  If you suspect multiple employees share a prefix, provide a longer portion of the ID.
</details>

---
## Reminder Commands

---

### Viewing upcoming birthdays: `reminder bd`

Filters and displays a list of employees whose birthdays are occurring within the next 3 days.

**Format:**
`reminder bd`

**What happens:**
- A panel will show up on the right side of the UI under “🎂 Birthday”.
- Each reminder card will show:
    - The employee’s **name**
    - **Job position**
    - **Birthday date**
    - **Days remaining** until the birthday

> 💡 The command only affects display; no data is modified.

---

### Viewing upcoming work anniversaries: `reminder wa`

Filters and displays a list of employees whose work anniversaries are occurring within the next 3 days.

**Format:**
`reminder wa`

**What happens:**
- A panel will show up next to the birthday panel under “🎉 Work Anniversary”.
- Each reminder card will include:
    - The employee’s **name**
    - **Job position**
    - **Work anniversary date**
    - **Days remaining** until the anniversary

---

Below is an example of how the reminders appear on the UI:

![reminderListUI.png](images/reminderListUI.png)

Each panel updates when you enter `reminder bd` or `reminder wa`.

---
## Quality of Life Features

---
### Clearing all entries: `clear`

Clears all entries from H'Reers.

Format: `clear`

---
### Exiting the program: `exit`

Exits the program.

Format: `exit`

---
### Saving the data

H'Reers data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

---
### Editing the data file

H'Reers data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, H'Reers will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the H'Reers to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

---
## Import and Export

---
### **Importing data:** `import`
You can use `import` to bring external data (in CSV or JSON) into your current address book.
Depending on the write mode (`append` or `overwrite`), you can either merge the new data with your existing records or replace them entirely.

#### **Command Format**
```plaintext
import ft/FILE_TYPE fp/FILE_PATH fn/FILE_NAME wm/WRITE_MODE
```

### **Parameters**

| **Prefix** | **Meaning**                                       | **Required?**                          | **Example Value**           |
|------------|---------------------------------------------------|----------------------------------------|-----------------------------|
| `ft/`      | File type to import (`json` or `csv`)             | **Required**                           | `json` / `csv`             |
| `fp/`      | Optional base directory or file path              | At least one of `fp/` or `fn/` required| `./data` / `C:\Users\John\`|
| `fn/`      | Optional filename (extension can be auto-added)   | At least one of `fp/` or `fn/` required| `myData.json`               |
| `wm/`      | Write mode (`append` or `overwrite`)              | **Required**                           | `append` / `overwrite`      |

### **Example Usage**
```plaintext 
import ft/json fp/data/ fn/contacts wm/append
```

<details>
<summary>Quirks & Edge Cases</summary>

1. **File Type Validation**
    - Supported only `json` or `csv`.

2. **File Path & Filename Usage**
    - You can provide a complete file path with extension via `fp/`.
    - If you also supply `fn/`, ensure `fp/` is just a directory (or the import will fail).

3. **Write Mode**
    - `append` merges new records but skips conflicts.
    - `overwrite` clears existing data entirely before importing.

4. **Extension Enforcement**
    - If you use `fn/` without an extension, the system may automatically append `.json` or `.csv` based on `ft/`.
    - Mismatched extensions will raise an error.
</details>

<details>
<summary>Common Errors & Messages</summary>

- **`Invalid file type`**  
  Provide either `json` or `csv` in `ft/`.

- **`Write mode must be specified as either 'append' or 'overwrite'`**  
  Ensure `wm/` is one of the two valid modes.

- **`Provide either a full file path or a filename, not both`**  
  This occurs if you pass `fp/` that includes a filename and also use `fn/`.

- **`Filename must be provided if path is just a directory`**  
  If `fp/` is a directory, you must specify a filename (`fn/`).
</details>

<details>
<summary>Tips</summary>

- **Check Extension Conflicts**: If your file says `.json` but you specify `ft/csv`, it will fail.
- **Ensure Proper Permissions**: The path must be writable or readable for the import to succeed.
- **Use Overwrite Cautiously**: This mode replaces all current data, so confirm backups if needed.
</details>

---
### **Exporting data:** `export`
You can use `export` to save the currently visible list of people in the address book to a file (JSON or CSV).
If you provide a specific directory path (`fp/`), the system will export the file there.
If you also include a file name (`fn/`), any missing extension is automatically appended based on the file type (`ft/`) chosen
This means that you do **not** need to include the extension behind the file name.

#### **Command Format**
```plaintext
export ft/FILE_TYPE [fp/FILE_PATH] [fn/FILE_NAME]
```

### **Parameters**

| **Prefix** | **Meaning**                                     | **Required?**              | **Example Value**     |
|------------|-------------------------------------------------|----------------------------|------------------------|
| `ft/`      | The file type to export (`json` or `csv`)       | **Required**               | `json` or `csv`       |
| `fp/`      | The optional file path (directory or full path) | Optional if `fn/` is used | `./output/`           |
| `fn/`      | The optional filename (extension auto-added)    | Optional if `fp/` is used | `contacts`, `data.csv`|

### **Example Usage**
```plaintext
export ft/json fp/data/ fn/contacts
```


<details>
<summary>Quirks & Edge Cases</summary>

1. **File Type Restrictions**
    - You must specify either `json` or `csv` using `ft/`.

2. **Empty Records**
    - If the current list of displayed people is empty, export fails with an error.

3. **File Path & Filename**
    - Provide either just a path (e.g., `fp/output/`) and let the system use a default file name, or specify both a path and a filename.
    - If you specify a full file path including a filename, do not also use `fn/`.

4. **Auto-Extension**
    - If you use `fn/contacts` but `ft/json`, the resulting file is `contacts.json`.

5. **Error Handling**
    - Mismatched file types or inaccessible directories (e.g., read/write issues) will cause an error.
</details>

<details>
<summary>Common Errors & Messages</summary>

- **`Invalid filetype`**  
  Occurs if you use something other than `json` or `csv` with `ft/`.

- **`No people to export`**  
  Raised if no records are currently displayed for export.

- **`Provide either a full file path or a filename, not both`**  
  If `fp/` already includes a filename and `fn/` is also used.

- **`Error exporting data`**  
  If an IOException or other issue happens during file creation.
</details>

<details>
<summary>Tips</summary>

- **Confirm the Output Path**: If none is provided, a default location may be used.
- **Check the Exported File**: Verify the correct file extension and contents.
- **Filtering**: Only exports the currently visible list of people, so use commands like `filter` or `search` beforehand.
</details>

---
## Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous H'Reers home folder.<br>

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Help** | `help`
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL jp/JOB [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com jb/Crypto Farmer t/friend t/colleague bd/2001-07-08 wa/2025-08-15`
**List** | `list`
**Edit** | `edit Employee_ID_Prefix [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [jb/JOB] [t/TAG]…​`<br> e.g.,`edit 12sde n/James Lee e/jameslee@example.com`
**Undo**| `undo`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**Delete** | `delete Employee_ID_Prefix`
**Clear** | `clear`
**addAnni** | `addAnni eid/EMPLOYEE_ID_PREFIX d/DATE an/ANNIVERSARY_NAME at/ANNIVERSARY_TYPE [ad/DESCRIPTION] [atdesc/TYPE_DESCRIPTION]`<br> e.g., `addAnni eid/0c2414da d/2025-03-13 an/Silver Wedding at/Wedding ad/Celebrating 25 years atdesc/Personal`
**showAnni** | `showAnni eid/Empoyee_ID`<br> e.g., `showAnni eid/e22e5292-0353-49a9-9281-5a76e53bc94f`
**deleteAnni** | `deleteAnniversary eid/EMPLOYEE_ID ad/INDEX`<br> e.g., `deleteAnniversary eid/0c2414da ad/1`
**import** | `import ft/FILE_TYPE fp/FILE_PATH fn/FILE_NAME wm/WRITE_MODE`<br> e.g., `import ft/json fp/data/ fn/contacts wm/append`
**export** | `export ft/json fp/data/ fn/contacts`<br> e.g., `export ft/json fp/data/ fn/contacts`

---
## Glossary
* CLI (Command Line Interface): A text-based interface used to type commands
* GUI (Graphical User Interface): A user interface that allows interaction with the software through visual elements like buttons and icons.
