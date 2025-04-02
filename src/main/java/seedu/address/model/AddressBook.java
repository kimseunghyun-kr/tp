package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.UniqueEmployeeList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameEmployee comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueEmployeeList employees;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        employees = new UniqueEmployeeList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the employee list with {@code employees}.
     * {@code employees} must not contain duplicate employees.
     */
    public void setEmployees(List<Employee> employees) {
        this.employees.setPersons(employees);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);
        setEmployees(newData.getEmployeeList());
    }

    //// employee-level operations

    /**
     * Returns true if a employee with the same identity as {@code employee} exists in the address book.
     */
    public boolean hasPerson(Employee employee) {
        requireNonNull(employee);
        return employees.contains(employee);
    }

    /**
     * Returns true if a employee with the same identity as {@code employee} exists in the address book.
     */
    public boolean hasDuplicatePersonDetails(Employee employee) {
        requireNonNull(employee);
        return employees.hasDuplicatePersonDetails(employee);
    }
    /**
     * Checks whether the given {@code EmployeeId} has a prefix conflict with any existing employee ID
     * in the address book. A prefix conflict occurs when one employee ID is a prefix of another one.
     */
    public boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId) {
        requireNonNull(employeeId);
        return employees.hasEmployeeIdPrefixConflict(employeeId);
    }

    /**
     * Checks if there is an employee ID in the address book that has a prefix conflict
     * with the given employee ID, while ignoring a specific employee ID.
     * A prefix conflict occurs when one employee ID is a prefix of another employee ID.
     *
     * @param employeeId the employee ID to check for conflicts.
     * @param toIgnore the employee ID to be ignored during the conflict check.
     * @return true if a prefix conflict is found, excluding the specified employee ID to ignore; false otherwise.
     */
    public boolean hasEmployeeIdPrefixConflictIgnoringSpecific(EmployeeId employeeId, EmployeeId toIgnore) {
        requireNonNull(employeeId);
        return employees.hasEmployeeIdPrefixConflictIgnoringSpecific(employeeId, toIgnore);
    }

    /**
     * Adds a employee to the address book.
     * The employee must not already exist in the address book.
     */
    public void addPerson(Employee p) {
        employees.add(p);
    }

    /**
     * Replaces the given employee {@code target} in the list with {@code editedEmployee}.
     * {@code target} must exist in the address book.
     * The employee identity of {@code editedEmployee} must not be
     * the same as another existing employee in the address book.
     */
    public void setPerson(Employee target, Employee editedEmployee) {
        requireNonNull(editedEmployee);
        employees.setPerson(target, editedEmployee);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Employee key) {
        employees.remove(key);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employees", employees)
                .toString();
    }

    @Override
    public ObservableList<Employee> getEmployeeList() {
        return employees.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return employees.equals(otherAddressBook.employees);
    }

    @Override
    public int hashCode() {
        return employees.hashCode();
    }
}
