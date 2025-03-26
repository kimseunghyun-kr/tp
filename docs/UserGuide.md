---
layout: page
title: User Guide
---

H'Reers is a **desktop application** for *HRs* to keep details and anniversaries of their employees. While it has a GUI, most of the user interactions happen using a CLI (Command Line Interface).

* Table of Contents 
{:toc}

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/se-edu/addressbook-level3/releases).

1. Copy the file to the folder you want to use as the _home folder_ for your AddressBook.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar addressbook.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/MockUI.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `add n/John Doe p/98765432 e/johnd@example.com jb/Crypto Minor bd/2000-01-01 wa/2014-12-12` : Adds a contact named `John Doe` to the Address Book.

   * `delete [employee id prefix]` : Deletes the specified employee contact. _Note: Employee ID prefix has to pinpoint only one Employee for delete to work._

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command format:**<br>

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

### Viewing help: `help`

Shows a message explaning how to access the help page.

![help message](images/HelpU.png)

Format: `help`

### Adding a person: `add`

Adds a person to the address book.

Format: `add n/NAME p/PHONE_NUMBER e/EMAIL jp/JOB [t/TAG]… bd/DATE wa/DATE​`

Date format: `YYYY-MM-DD`
<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
A person can have any number of tags (including 0)
</div>

<div markdown="span" class="alert alert-primary">:bulb: **Tip:**
bd/ stands for birthday and wa/ stands for work anniversary.

Both are optional.

Both of which are standard anniversaries that the app will make for you
with just the date!
Other anniversaries can be added as well with `Add Anniversary` command below.
</div>

Examples:
* `add n/John Doe p/98765432 e/johnd@example.com jp/President bd/2001-01-01 wa/2020-07-08`
* `add n/Betsy Crowe t/friend e/betsycrowe@example.com jp/Cleaner p/1234567 t/criminal bd/2005-12-01 wa/2025-05-21`

### Listing all persons: `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person: `edit`

Edits an existing person in the address book.

Format: `edit [Employee ID prefix] [n/NAME] [eid/EMPLOYEE_ID] [p/PHONE] [e/EMAIL] [jp/JOB] [t/TAG]…​`

* Edits the specified employee. The Employee ID can be shortened down and not necessarily needed to type in the full ID. The Employee ID prefix **must be Unique.**
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* When editing tags, the existing tags of the person will be removed i.e adding of tags is not cumulative.
* You can remove all the person’s tags by typing `t/` without
    specifying any tags after it.
* You can change the employee id by typing edit [Employee ID prefix] eid/[new Employee ID]

Examples:
*  `edit 1re p/91234567 e/johndoe@example.com` Edits the phone number and email address of the specified employee to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2dsf n/Betsy Crower t/` Edits the name of the specified employee to be `Betsy Crower` and clears all existing tags.
*  `edit 1sdg21 eid/3b9417cc-cf4e-4231-bc4d-4fd167c2abc6` Edits the employee id to be now `3b9417cc-cf4e-4231-bc4d-4fd167c2abc6` so long as no such employee id already exists.

### Locating persons by name: `find`

Finds persons whose names contain any of the given keywords.

Format: `find KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `find John` returns `john` and `John Doe`
* `find alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Deleting a person: `delete`

Deletes the specified person from the address book.

Format: `delete [Employee ID prefix]`

* Deletes the person of the specified Employee ID.
* The command only works if the Employee ID is unique.
* The Employee ID **must be valid and unique**

Examples:
* `list` followed by `delete [Employee ID prefix]` deletes the specified employee.

### Show anniversaries: `showAnni`

Shows the list of anniversary details of the person specified by the Employee ID.

Format: `showAnni eid/Empoyee_ID`

* Displays a new window with a list of anniversaries of the person specified by the Employee ID
* Details like dates, description and name will be displayed as well.
* Beginners can use the button in the GUI to do the same thing.
* The employee ID refers to the ID of the person in the address book, that you have specified when adding or generated if you did not.

Examples:
* showAnni eid/e22e5292-0353-49a9-9281-5a76e53bc94f

### Clearing all entries: `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program: `exit`

Exits the program.

Format: `exit`

### Saving the data

AddressBook data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

AddressBook data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<div markdown="span" class="alert alert-warning">:exclamation: **Caution:**
If your changes to the data file makes its format invalid, AddressBook will discard all data and start with an empty data file at the next run. Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the AddressBook to behave in unexpected ways (e.g., if a value entered is outside of the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</div>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous AddressBook home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

Action | Format, Examples
--------|------------------
**Add** | `add n/NAME p/PHONE_NUMBER e/EMAIL jp/JOB [t/TAG]…​` <br> e.g., `add n/James Ho p/22224444 e/jamesho@example.com jb/Crypto Farmer t/friend t/colleague bd/2001-07-08 wa/2025-08-15`
**Clear** | `clear`
**Delete** | `delete Employee_ID_Prefix`
**Edit** | `edit Employee_ID_Prefix [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [jb/JOB] [t/TAG]…​`<br> e.g.,`edit 12sde n/James Lee e/jameslee@example.com`
**Find** | `find KEYWORD [MORE_KEYWORDS]`<br> e.g., `find James Jake`
**List** | `list`
**Help** | `help`
**showAnni** | `showAnni eid/Empoyee_ID`<br> e.g., `showAnni eid/e22e5292-0353-49a9-9281-5a76e53bc94f`
