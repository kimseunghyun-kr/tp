package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_filtersCorrectly() throws ParseException {
        // no leading and trailing whitespaces
        FindCommand command = parser.parse(" n/Alice Bob");

        Person matching1 = new PersonBuilder().withName("Alice Johnson").build();
        Person matching2 = new PersonBuilder().withName("Bob Marley").build();
        Person nonMatching = new PersonBuilder().withName("Charlie Brown").build();

        ModelStubWithFilterablePersons model =
                new ModelStubWithFilterablePersons(List.of(matching1, matching2, nonMatching));

        command.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredPersonList());

        // multiple whitespaces between keywords
        FindCommand whitespaceCommand = parser.parse(" \n n/Alice \n \t Bob  \t");

        ModelStubWithFilterablePersons whitespaceModel =
                new ModelStubWithFilterablePersons(List.of(matching1, matching2, nonMatching));

        whitespaceCommand.execute(whitespaceModel);

        assertEquals(List.of(matching1, matching2), whitespaceModel.getFilteredPersonList());
    }

    @Test
    public void parse_jobPositionArgs_filtersCorrectly() throws Exception {
        // Parse command with standard input
        FindCommand command = parser.parse(" jp/engineer manager");

        Person matching1 = new PersonBuilder().withJobPosition("Software Engineer").build();
        Person matching2 = new PersonBuilder().withJobPosition("Product Manager").build();
        Person nonMatching = new PersonBuilder().withJobPosition("Sales Associate").build();

        ModelStubWithFilterablePersons model =
                new ModelStubWithFilterablePersons(List.of(matching1, matching2, nonMatching));

        command.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredPersonList());

        // Parse command with messy whitespace
        FindCommand whitespaceCommand = parser.parse(" \n jp/engineer \t manager  \n");

        ModelStubWithFilterablePersons whitespaceModel =
                new ModelStubWithFilterablePersons(List.of(matching1, matching2, nonMatching));

        whitespaceCommand.execute(whitespaceModel);

        assertEquals(List.of(matching1, matching2), whitespaceModel.getFilteredPersonList());
    }

    @Test
    public void parse_nameAndJobPositionArgs_filtersCorrectly() throws Exception {
        // Parse command with both name and job position
        FindCommand command = parser.parse(" n/jack jp/engineer");

        Person matching = new PersonBuilder()
                .withName("Jack Daniel")
                .withJobPosition("Software Engineer")
                .build();

        Person wrongName = new PersonBuilder()
                .withName("Alice")
                .withJobPosition("Software Engineer")
                .build();

        Person wrongJob = new PersonBuilder()
                .withName("Jack")
                .withJobPosition("Chef")
                .build();

        Person completelyOff = new PersonBuilder()
                .withName("Bob")
                .withJobPosition("Accountant")
                .build();

        ModelStubWithFilterablePersons model =
                new ModelStubWithFilterablePersons(List.of(matching, wrongName, wrongJob, completelyOff));

        command.execute(model);

        assertEquals(List.of(matching), model.getFilteredPersonList());

        // Messy whitespace version
        FindCommand messyCommand = parser.parse(" \n n/jack  \n jp/engineer \t");

        ModelStubWithFilterablePersons messyModel =
                new ModelStubWithFilterablePersons(List.of(matching, wrongName, wrongJob, completelyOff));

        messyCommand.execute(messyModel);

        assertEquals(List.of(matching), messyModel.getFilteredPersonList());
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
        public void addPerson(Person person) {
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
        public boolean hasPerson(Person person) {
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
        public boolean hasDuplicatePersonDetails(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredByEmployeeIdPrefixList(EmployeeId employeeIdPrefix) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitChanges() {
            // Stub implementation, no-op
        }
    }

    private class ModelStubWithFilterablePersons extends ModelStub {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final FilteredList<Person> filteredPersons;

        ModelStubWithFilterablePersons(List<Person> initialPersons) {
            persons.addAll(initialPersons);
            filteredPersons = new FilteredList<>(persons);
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            filteredPersons.setPredicate(predicate);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return filteredPersons;
        }
    }

}
