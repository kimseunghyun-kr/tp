package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Employee> filteredEmployees;
    private int currentStatePointer = 0;
    private List<AddressBook> addressBookStates = new ArrayList<>();

    private final FilteredList<Employee> birthdayReminderList;

    private final FilteredList<Employee> workAnniversaryReminderList;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);

        filteredEmployees = new FilteredList<>(this.addressBook.getEmployeeList());

        // Apply default filtering
        filteredEmployees.setPredicate(employee -> true);

        this.birthdayReminderList = new FilteredList<>(this.addressBook.getEmployeeList());
        this.birthdayReminderList.setPredicate(employee -> false);

        this.workAnniversaryReminderList = new FilteredList<>(this.addressBook.getEmployeeList());
        this.workAnniversaryReminderList.setPredicate(employee -> false);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
        commitAddressBook();
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public void updateBirthdayReminderList() {
        birthdayReminderList.setPredicate(employee -> {
            boolean isUpcoming = employee.isUpcomingWithinDays("Birthday", 3);
            System.out.println(employee.getName() + ": " + isUpcoming);
            return isUpcoming;
        });
        System.out.println("Filtered birthday list: " + birthdayReminderList.size());
    }

    @Override
    public void updateWorkAnniversaryReminderList() {
        workAnniversaryReminderList.setPredicate(employee -> {
            boolean isUpcoming = employee.isUpcomingWithinDays("Work Anniversary", 3);
            System.out.println(employee.getName() + ": " + isUpcoming);
            return isUpcoming;
        });
        System.out.println("Filtered wa list: " + workAnniversaryReminderList.size());
    }

    @Override
    public boolean hasEmployee(Employee employee) {
        requireNonNull(employee);
        return addressBook.hasPerson(employee);
    }

    @Override
    public boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId) {
        requireNonNull(employeeId);
        return addressBook.hasEmployeeIdPrefixConflict(employeeId);
    }

    @Override
    public boolean hasEmployeeIdPrefixConflictIgnoringSpecific(EmployeeId employeeId, EmployeeId toIgnore) {
        requireAllNonNull(employeeId, toIgnore);
        return addressBook.hasEmployeeIdPrefixConflictIgnoringSpecific(employeeId, toIgnore);
    }

    @Override
    public boolean hasDuplicateEmployeeDetails(Employee employee) {
        requireNonNull(employee);
        return addressBook.hasDuplicatePersonDetails(employee);
    }

    @Override
    public void deleteEmployee(Employee target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addEmployee(Employee employee) {
        addressBook.addPerson(employee);
        updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
    }

    @Override
    public void setEmployee(Employee target, Employee editedEmployee) {
        requireAllNonNull(target, editedEmployee);
        addressBook.setPerson(target, editedEmployee);
    }

    //=========== Filtered Employee List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Employee} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Employee> getFilteredEmployeeList() {
        return filteredEmployees;
    }

    @Override
    public ObservableList<Employee> getFilteredByEmployeeIdPrefixList(EmployeeId employeeIdPrefix) {
        requireNonNull(employeeIdPrefix);
        return new FilteredList<>(
                filteredEmployees, employee -> employeeIdPrefix.isPrefixOf(employee.getEmployeeId())
        );
    }

    @Override
    public void updateFilteredEmployeeList(Predicate<Employee> predicate) {
        requireNonNull(predicate);
        filteredEmployees.setPredicate(predicate);
    }

    @Override
    public ObservableList<Employee> getBirthdayReminderList() {
        return birthdayReminderList;
    }

    @Override
    public ObservableList<Employee> getWorkAnniversaryReminderList() {
        return workAnniversaryReminderList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredEmployees.equals(otherModelManager.filteredEmployees);
    }

    /**
     * Checks if the address book can be undone.
     * This is determined by whether the current state pointer is greater than 0,
     * indicating that there is at least one previous state to revert to.
     *
     * @return True if the address book has a previous state to undo, otherwise false.
     */
    public boolean canUndoAddressBook() {
        return currentStatePointer > 0;
    }

    /**
     * Undoes the most recent change to the address book.
     * This reverts the address book to its previous state based on the current state pointer.
     * The state pointer is decremented, and the address book is updated with the previous state.
     *
     * @throws IllegalStateException If no undo is available (i.e., no previous state exists).
     */
    public void undoAddressBook() {
        if (canUndoAddressBook()) {
            currentStatePointer--;
            addressBook.resetData(addressBookStates.get(currentStatePointer));
        }
    }

    /**
     * Commits the current state of the address book to the history.
     * This creates a new snapshot of the current address book and adds it to the list of address book states.
     * The current state pointer is incremented to reflect the new committed state.
     */
    public void commitAddressBook() {
        addressBookStates.add(new AddressBook(addressBook));
        currentStatePointer++;
    }

    /**
     * Commits the current changes to the address book.
     * This is a wrapper method that calls {@link #commitAddressBook()}.
     */
    @Override
    public void commitChanges() {
        commitAddressBook();
    }

}