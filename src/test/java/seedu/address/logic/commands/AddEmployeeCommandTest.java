package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.testutil.PersonBuilder;

public class AddEmployeeCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddPersonCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Employee validEmployee = new PersonBuilder().build();

        CommandResult commandResult = new AddPersonCommand(validEmployee).execute(modelStub);

        assertEquals(String.format(AddPersonCommand.MESSAGE_SUCCESS, Messages.format(validEmployee)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validEmployee), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Employee validEmployee = new PersonBuilder().build();
        AddPersonCommand addPersonCommand = new AddPersonCommand(validEmployee);
        ModelStub modelStub = new ModelStubWithPerson(validEmployee);

        assertThrows(CommandException.class,
                AddPersonCommand.MESSAGE_DUPLICATE_PERSON, () -> addPersonCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Employee alice = new PersonBuilder().withName("Alice").build();
        Employee bob = new PersonBuilder().withName("Bob").build();
        AddPersonCommand addAliceCommand = new AddPersonCommand(alice);
        AddPersonCommand addBobCommand = new AddPersonCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddPersonCommand addAliceCommandCopy = new AddPersonCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different employee -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddPersonCommand addPersonCommand = new AddPersonCommand(ALICE);
        String expected = AddPersonCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addPersonCommand.toString());
    }

    /**
     * A default model stub that have all the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Employee employee) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Employee employee) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasEmployeeIdPrefixConflictIgnoringSpecific(EmployeeId employeeId, EmployeeId toIgnore) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasDuplicatePersonDetails(Employee employee) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Employee target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Employee target, Employee editedEmployee) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Employee> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Employee> getFilteredByEmployeeIdPrefixList(EmployeeId employeeIdPrefix) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Employee> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Employee> getBirthdayReminderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Employee> getWorkAnniversaryReminderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitChanges() {
            // Stub implementation, no-op
        }

        @Override
        public void updateBirthdayReminderList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateWorkAnniversaryReminderList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single employee.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Employee employee;

        ModelStubWithPerson(Employee employee) {
            requireNonNull(employee);
            this.employee = employee;
        }

        @Override
        public boolean hasPerson(Employee employee) {
            requireNonNull(employee);
            return this.employee.isSameEmployee(employee);
        }
    }

    /**
     * A Model stub that always accept the employee being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Employee> personsAdded = new ArrayList<>();

        @Override
        public boolean hasPerson(Employee employee) {
            requireNonNull(employee);
            return personsAdded.stream().anyMatch(employee::isSameEmployee);
        }

        @Override
        public void addPerson(Employee employee) {
            requireNonNull(employee);
            personsAdded.add(employee);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }

        @Override
        public boolean hasEmployeeIdPrefixConflict(EmployeeId employeeId) {
            return false;
        }

        @Override
        public void updateBirthdayReminderList() {
            // Stub implementation, no-op
        }

        @Override
        public void updateWorkAnniversaryReminderList() {
            // Stub implementation, no-op
        }
    }
}
