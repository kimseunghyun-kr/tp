package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.EmployeeNotFoundException;
import seedu.address.model.util.EmployeeIdPrefixValidationUtils;

/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * An employee is considered unique by comparing using {@code Employee#isSameEmployee(Employee)}.
 * As such, adding and updating of persons uses Employee#isSameEmployee(Employee)
 * for equality so as to ensure that the employee being added or updated is
 * unique in terms of identity in the UniqueEmployeeList.
 * However, the removal of a employee uses Employee#equals(Object) so
 * as to ensure that the employee with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Employee#isSameEmployee(Employee)
 */
public class UniqueEmployeeList implements Iterable<Employee> {

    private final ObservableList<Employee> internalList = FXCollections.observableArrayList();
    private final ObservableList<Employee> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent employee as the given argument.
     */
    public boolean contains(Employee toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameEmployee);
    }

    /**
     * Sorts the list of persons by their next upcoming date (birthday or work anniversary).
     * - Persons with the nearest upcoming date will appear first.
     * - Persons without a birthday or anniversary will be placed at the end of the list.
     */
    public void sortByUpcomingDate() {
        List<Employee> sortedList = internalList.stream()
                .sorted(Comparator
                        .comparing(Employee::getNextUpcomingDate,
                                Comparator.nullsLast(Comparator.naturalOrder()))
                )
                .collect(Collectors.toList());

        // Update the list after sorting
        internalList.setAll(sortedList);
    }
    /**
     * Calculates the number of days until the next occurrence of a birthday.
     *
     * @param birthday The date of the birthday.
     * @return Number of days until the next birthday, or {@code null} if the birthday is {@code null}.
     */
    private Integer daysUntilNextBirthday(LocalDate birthday) {
        if (birthday == null) {
            return null;
        }

        LocalDate today = LocalDate.now();
        LocalDate nextBirthday = birthday.withYear(today.getYear());

        // If the birthday has already passed this year, adjust to next year
        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }

        // Return the number of days until the next birthday
        return (int) ChronoUnit.DAYS.between(today, nextBirthday);
    }

    /**
     * Adds a employee to the list.
     * The employee must not already exist in the list.
     */
    public void add(Employee toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicatePersonException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the employee {@code target} in the list with {@code editedEmployee}.
     * {@code target} must exist in the list.
     * The employee identity of {@code editedEmployee} must not be the same as another existing employee in the list.
     */
    public void setPerson(Employee target, Employee editedEmployee) {
        requireAllNonNull(target, editedEmployee);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EmployeeNotFoundException();
        }

        if (!target.isSameEmployee(editedEmployee) && contains(editedEmployee)) {
            throw new DuplicatePersonException();
        }

        internalList.set(index, editedEmployee);
    }

    /**
     * Removes the equivalent employee from the list.
     * The employee must exist in the list.
     */
    public void remove(Employee toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new EmployeeNotFoundException();
        }
    }

    public void setPersons(UniqueEmployeeList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code employees}.
     * {@code employees} must not contain duplicate employees.
     */
    public void setPersons(List<Employee> employees) {
        requireAllNonNull(employees);
        if (!personsAreUnique(employees)) {
            throw new DuplicatePersonException();
        }
        internalList.setAll(employees);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Employee> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Employee> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueEmployeeList)) {
            return false;
        }

        UniqueEmployeeList otherUniqueEmployeeList = (UniqueEmployeeList) other;
        return internalList.equals(otherUniqueEmployeeList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code employees} contains only unique employees.
     */
    private boolean personsAreUnique(List<Employee> employees) {
        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = i + 1; j < employees.size(); j++) {
                if (employees.get(i).isSameEmployee(employees.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a list of pairs of employee IDs that have prefix conflicts.
     * @return list of pairs of employee IDs with prefix conflicts
     */
    public List<Pair<EmployeeId, EmployeeId>> getPrefixConflictingPairs() {
        return EmployeeIdPrefixValidationUtils.getPrefixConflictingPairs(internalList);
    }

    /**
     * Checks if there exist a employee specifically duplicated
     * @param toCheck the employee to check
     * @return true if there exists more than one employee that are duplicated
     */
    public boolean hasDuplicatePersonDetails(Employee toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(checkPerson -> checkPerson.hasSameDetails(toCheck));
    }
    /**
     * Checks if there is an employee ID in the list that has a prefix conflict
     * with the given employee ID. A prefix conflict occurs when one employee ID
     * is a prefix of another one.
     *
     * @param employeeId the employee ID to check for conflicts.
     *                   Must not be null.
     * @return true if a prefix conflict is found, false otherwise.
     */
    public boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId) {
        requireNonNull(employeeId);
        return internalList.stream().anyMatch(person -> person.getEmployeeId().hasPrefixConflict(employeeId));
    }

    /**
     * Checks if there is an employee ID in the list that has a prefix conflict
     * with the given employee ID, while ignoring a specific employee ID.
     * A prefix conflict occurs when one employee ID is a prefix of another.
     *
     * @param employeeId the employee ID to check for conflicts.
     * @param toIgnore   the employee ID to be ignored during the check.
     * @return true if a prefix conflict is found excluding the specified employee ID to ignore, false otherwise.
     */
    public boolean hasEmployeeIdPrefixConflictIgnoringSpecific(EmployeeId employeeId, EmployeeId toIgnore) {
        requireNonNull(employeeId);
        requireNonNull(toIgnore);
        return internalList.stream()
                .filter(person -> !person.getEmployeeId().equals(toIgnore))
                .anyMatch(person -> person.getEmployeeId().hasPrefixConflict(employeeId));
    }


}
