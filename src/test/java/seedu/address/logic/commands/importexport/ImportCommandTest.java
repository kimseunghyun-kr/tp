package seedu.address.logic.commands.importexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.ALICE;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.AMY;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.BOB;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonSerializableAddressBook;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.TypicalPersonsWithoutAnniversaries;

@ExtendWith(MockitoExtension.class)
public class ImportCommandTest {

    @Mock
    private Model model;

    @Mock
    private JsonSerializableAddressBook jsonSerializableAddressBook;

    @InjectMocks
    private ImportCommand importCommand;

    private Path jsonFilePathNormalCase;
    private Path jsonFilePathDuplicateCase;
    private Path csvFilePathNormalCase;
    private Path csvFilePathDuplicateCase;
    private Path csvFilePathAggregateCase;
    private AddressBook addressBookUnique;
    private AddressBook addressBookDuplicate;

    @BeforeEach
    public void setUp() {
        jsonFilePathNormalCase = Paths.get("src/test/data/ImportCommandTest/test.json");
        jsonFilePathDuplicateCase = Paths.get("src/test/data/ImportCommandTest/testDuplicateInvalid.json");
        csvFilePathNormalCase = Paths.get("src/test/data/ImportCommandTest/test.csv");
        csvFilePathDuplicateCase = Paths.get("src/test/data/ImportCommandTest/testDuplicateInvalid.csv");
        csvFilePathAggregateCase = Paths.get("src/test/data/ImportCommandTest/testAggregate.csv");
        addressBookUnique = new AddressBookBuilder().withPerson(AMY).withPerson(BOB).build();
        addressBookDuplicate = new AddressBookBuilder().withPerson(ALICE).build();
    }

    /**
     * Tests the handleOverwriteMode() method of ImportCommand,
     * verifying it replaces the entire address book with JSON data.
     * @throws Exception
     */
    @Test
    public void execute_importJsonOverwrite_success() throws Exception {
        // Use mockStatic to mock the static method
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Mock the static method
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromJson(jsonFilePathNormalCase))
                    .thenReturn(jsonSerializableAddressBook);

            // Setup the rest of the test
            when(jsonSerializableAddressBook.toModelType()).thenReturn(addressBookUnique);
            when(model.getAddressBook()).thenReturn(addressBookUnique);

            ImportCommand importCommand = new ImportCommand("json", jsonFilePathNormalCase, "overwrite");
            CommandResult result = importCommand.execute(model);

            verify(model).setAddressBook(any(AddressBook.class));
            assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_OVERWRITE, 2), result.getFeedbackToUser());
        }
    }

    /**
     * Tests handleAppendMode() and processImportedPersonsWhenAppend()
     * methods with non-conflicting contacts from CSV.
     * @throws Exception
     */
    @Test
    public void execute_importCsvAppend_success() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Setup base mocks
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromCsv(csvFilePathNormalCase))
                    .thenReturn(jsonSerializableAddressBook);
            when(jsonSerializableAddressBook.toModelType()).thenReturn(addressBookUnique);

            // Mock empty response list - only mock what will actually be called
            ObservableList<Person> emptyList = FXCollections.observableArrayList();
            when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(emptyList);

            importCommand = new ImportCommand("csv", csvFilePathNormalCase, "append");
            CommandResult result = importCommand.execute(model);

            verify(model, times(2)).addPerson(any(Person.class));
            assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_APPEND, 2, 0, ""),
                    result.getFeedbackToUser());
        }
    }

    /**
     * Tests how processImportedPersonsWhenAppend() handles contacts that conflict with existing ones.
     * @throws Exception
     */
    @Test
    public void execute_importCsvAppendWithSkippedContacts() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Setup imported data with AMY and BOB
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromCsv(csvFilePathNormalCase))
                    .thenReturn(jsonSerializableAddressBook);
            when(jsonSerializableAddressBook.toModelType()).thenReturn(addressBookUnique);

            // Create mock person that will conflict with BOB
            Person mockConflictingPerson = mock(Person.class);
            when(mockConflictingPerson.isSamePerson(any())).thenReturn(true);
            when(mockConflictingPerson.hasSameDetails(any())).thenReturn(false);

            // Return conflict list only for BOB's employeeId, empty list for AMY's
            when(model.getFilteredByEmployeeIdPrefixList(BOB.getEmployeeId()))
                    .thenReturn(FXCollections.observableArrayList(mockConflictingPerson));
            when(model.getFilteredByEmployeeIdPrefixList(AMY.getEmployeeId()))
                    .thenReturn(FXCollections.observableArrayList());

            importCommand = new ImportCommand("csv", csvFilePathNormalCase, "append");
            CommandResult result = importCommand.execute(model);

            // Only AMY should be added, BOB should be skipped
            verify(model, times(1)).addPerson(any(Person.class));

            // Check feedback message contains expected values
            String feedback = result.getFeedbackToUser();
            assertTrue(feedback.contains("Successfully imported 1 contacts, skipped 1"));
            assertTrue(feedback.contains("Skipped contacts:"));
            assertTrue(feedback.contains(BOB.getName().toString()));
            assertTrue(feedback.contains(BOB.getEmployeeId().toString()));
        }
    }


    @Test
    public void execute_importInvalidFileType_throwsCommandException() {
        importCommand = new ImportCommand("invalid", jsonFilePathNormalCase, "overwrite");
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }


    @Test
    public void execute_importInvalidMode_throwsCommandException() {
        importCommand = new ImportCommand("json", jsonFilePathNormalCase, "invalid");
        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }


    @Test
    public void execute_importJsonWithInvalidData_throwsCommandException() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromJson(jsonFilePathNormalCase))
                    .thenReturn(jsonSerializableAddressBook);
            when(jsonSerializableAddressBook.toModelType()).thenThrow(new IllegalValueException("Invalid data"));

            importCommand = new ImportCommand("json", jsonFilePathNormalCase, "overwrite");
            assertThrows(CommandException.class, () -> importCommand.execute(model));
        }
    }

    @Test
    public void execute_importCsvWithIoException_throwsCommandException() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromCsv(csvFilePathNormalCase))
                    .thenThrow(new IOException("IO error"));

            importCommand = new ImportCommand("csv", csvFilePathNormalCase, "append");
            assertThrows(CommandException.class, () -> importCommand.execute(model));
        }
    }

    /**
     * Tests that when multiple entries for the same person exist in a CSV file,
     * they're properly consolidated into a single person with all anniversaries merged.
     * @throws Exception
     */
    @Test
    public void execute_importCsvWithDuplicatesInFile() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Create a JsonSerializableAddressBook with duplicates
            JsonSerializableAddressBook realImportedData = mock(JsonSerializableAddressBook.class);
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromCsv(csvFilePathAggregateCase))
                    .thenReturn(realImportedData);
            Person alice = TypicalPersonsWithoutAnniversaries.ALICE;
            // Create Person with all anniversaries from CSV
            Person aliceWithAllAnniversaries = new PersonBuilder(TypicalPersonsWithoutAnniversaries.ALICE)
                    .withAnniversary("2025-03-13", "Family",
                            "\"Silver Wedding\"", "\"Celebrating 25 years\"")
                    .withAnniversary("2025-02-25", "wedding", "silver wedding", "things")
                    .withAnniversary("2025-02-25", "Work Anniversary",
                            "Birthday", "alex's Birthday")
                    .build();

            // Create address book with the consolidated person
            AddressBook dedupedAddressBook = new AddressBookBuilder()
                    .withPerson(aliceWithAllAnniversaries)
                    .build();

            // Return the address book with consolidated entries
            when(realImportedData.toModelType()).thenReturn(dedupedAddressBook);

            // Mock empty filtered list so we add new person
            ObservableList<Person> emptyList = FXCollections.observableArrayList();
            when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(emptyList);

            importCommand = new ImportCommand("csv", csvFilePathAggregateCase, "append");
            CommandResult result = importCommand.execute(model);

            // Verify only one person was added
            ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);
            verify(model, times(1)).addPerson(personCaptor.capture());

            // Verify the added person has all the anniversaries from the CSV
            Person addedPerson = personCaptor.getValue();
            assertEquals(3, addedPerson.getAnniversaries().size());

            assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_APPEND, 1, 0, ""),
                    result.getFeedbackToUser());
        }
    }

    /**
     * Tests how the getAnniversaries().addAll() method is called when contacts with same details are found.
     * @throws Exception
     */
    @Test
    public void execute_importJsonAppendWithMergedAnniversaries() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Setup imported data with test data containing anniversaries
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromJson(jsonFilePathNormalCase))
                    .thenReturn(jsonSerializableAddressBook);
            when(jsonSerializableAddressBook.toModelType()).thenReturn(addressBookDuplicate);

            // Create a mock Person
            Person mockExistingPerson = mock(Person.class);
            when(mockExistingPerson.isSamePerson(any())).thenReturn(true);
            when(mockExistingPerson.hasSameDetails(any())).thenReturn(true);

            // Create a separate mock for the anniversaries list
            @SuppressWarnings("unchecked")
            ObservableList<Anniversary> mockAnniversaryList = mock(ObservableList.class);
            when(mockExistingPerson.getAnniversaries()).thenReturn(mockAnniversaryList);

            // Return our mock when searching for ALICE
            ObservableList<Person> matchList = FXCollections.observableArrayList(mockExistingPerson);
            when(model.getFilteredByEmployeeIdPrefixList(ALICE.getEmployeeId()))
                    .thenReturn(matchList);

            importCommand = new ImportCommand("json", jsonFilePathNormalCase, "append");
            CommandResult result = importCommand.execute(model);

            // Use ArgumentCaptor to specify the Collection version of addAll
            ArgumentCaptor<Collection<Anniversary>> captor = ArgumentCaptor.forClass(Collection.class);
            verify(mockAnniversaryList, times(1)).addAll(captor.capture());

            assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_APPEND, 0, 0, ""),
                    result.getFeedbackToUser());
        }
    }

    /**
     * Tests importing some contacts while skipping others due to conflicts[same eid, different details].
     * @throws Exception
     */
    @Test
    public void execute_importCsvWithPartialConflicts() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Setup imported data with two people
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromCsv(csvFilePathDuplicateCase))
                    .thenReturn(jsonSerializableAddressBook);

            // Create address book with two conflicting entries
            AddressBook conflictBook = new AddressBookBuilder()
                    .withPerson(ALICE).withPerson(BOB).build();
            when(jsonSerializableAddressBook.toModelType()).thenReturn(conflictBook);

            // First person exists but with conflict
            Person mockConflictPerson = mock(Person.class);
            when(mockConflictPerson.getName()).thenReturn(ALICE.getName());
            when(mockConflictPerson.isSamePerson(any())).thenReturn(true);
            when(mockConflictPerson.hasSameDetails(any())).thenReturn(false);

            // Return conflict for ALICE, but empty for BOB
            when(model.getFilteredByEmployeeIdPrefixList(ALICE.getEmployeeId()))
                    .thenReturn(FXCollections.observableArrayList(mockConflictPerson));
            when(model.getFilteredByEmployeeIdPrefixList(BOB.getEmployeeId()))
                    .thenReturn(FXCollections.observableArrayList());

            importCommand = new ImportCommand("csv", csvFilePathDuplicateCase, "append");
            CommandResult result = importCommand.execute(model);

            // Only BOB should be added
            verify(model, times(1)).addPerson(any(Person.class));
            assertTrue(result.getFeedbackToUser().contains(mockConflictPerson.getName().toString()));
        }
    }

    /**
     * Tests importing contacts with the same employee ID but different details.
     * this is the same as csvDuplicateCase but with JSON data.
     * @throws Exception
     */
    @Test
    public void execute_importJsonAppendWithExistingEmployeeIdButDifferentDetails() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            // Setup imported data
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromJson(jsonFilePathDuplicateCase))
                    .thenReturn(jsonSerializableAddressBook);

            // Create book with a person having same ID but different details
            AddressBook modifiedBook = new AddressBookBuilder().withPerson(ALICE).build();
            when(jsonSerializableAddressBook.toModelType()).thenReturn(modifiedBook);

            // Create a mock for an existing person with same ID but different details
            Person mockExistingPerson = mock(Person.class);
            when(mockExistingPerson.getName()).thenReturn(ALICE.getName());
            when(mockExistingPerson.getEmployeeId()).thenReturn(ALICE.getEmployeeId());
            when(mockExistingPerson.isSamePerson(any())).thenReturn(true);
            when(mockExistingPerson.hasSameDetails(any())).thenReturn(false);

            // Return the mock when searching
            ObservableList<Person> matchList = FXCollections.observableArrayList(mockExistingPerson);
            when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(matchList);

            importCommand = new ImportCommand("json", jsonFilePathDuplicateCase, "append");
            CommandResult result = importCommand.execute(model);

            // No one should be added
            verify(model, times(0)).addPerson(any(Person.class));
            assertTrue(result.getFeedbackToUser().contains(mockExistingPerson.getName().toString()));
            assertTrue(result.getFeedbackToUser().contains(mockExistingPerson.getEmployeeId().toString()));
        }
    }
}
