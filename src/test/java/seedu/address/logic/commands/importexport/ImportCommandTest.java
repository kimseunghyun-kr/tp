package seedu.address.logic.commands.importexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Employee;
import seedu.address.storage.JsonAdaptedPerson;
import seedu.address.storage.JsonSerializableAddressBook;
import seedu.address.testutil.AddressBookBuilder;

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
        addressBookUnique = new AddressBookBuilder().withEmployee(AMY).withEmployee(BOB).build();
        addressBookDuplicate = new AddressBookBuilder().withEmployee(ALICE).build();
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

            // Create mock JsonAdaptedPerson objects
            JsonAdaptedPerson mockJsonPerson1 = mock(JsonAdaptedPerson.class);
            JsonAdaptedPerson mockJsonPerson2 = mock(JsonAdaptedPerson.class);
            List<JsonAdaptedPerson> mockJsonPersons = Arrays.asList(mockJsonPerson1, mockJsonPerson2);

            // Mock getPersons() to return our list of JsonAdaptedPersons
            when(jsonSerializableAddressBook.getPersons()).thenReturn(mockJsonPersons);

            // Mock the toModelType() for each JsonAdaptedPerson
            when(mockJsonPerson1.toModelType()).thenReturn(addressBookUnique.getEmployeeList().get(0));
            when(mockJsonPerson2.toModelType()).thenReturn(addressBookUnique.getEmployeeList().get(1));

            // Mock empty response list for employeeId lookups
            ObservableList<Employee> emptyList = FXCollections.observableArrayList();
            when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(emptyList);

            importCommand = new ImportCommand("csv", csvFilePathNormalCase, "append");
            CommandResult result = importCommand.execute(model);

            verify(model, times(2)).addEmployee(any(Employee.class));
            assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_APPEND, 2, 0, "Conflicting records found:\n"),
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

            // Create mock JsonAdaptedPerson objects
            JsonAdaptedPerson mockJsonPerson1 = mock(JsonAdaptedPerson.class);
            JsonAdaptedPerson mockJsonPerson2 = mock(JsonAdaptedPerson.class);
            List<JsonAdaptedPerson> mockJsonPersons = Arrays.asList(mockJsonPerson1, mockJsonPerson2);

            // Mock getPersons() to return our list of JsonAdaptedPersons
            when(jsonSerializableAddressBook.getPersons()).thenReturn(mockJsonPersons);

            // Mock the toModelType() for each JsonAdaptedPerson
            when(mockJsonPerson1.toModelType()).thenReturn(addressBookUnique.getEmployeeList().get(0)); // AMY
            when(mockJsonPerson2.toModelType()).thenReturn(addressBookUnique.getEmployeeList().get(1)); // BOB
            when(mockJsonPerson1.getEmployeeId()).thenReturn(String.valueOf(AMY.getEmployeeId()));
            when(mockJsonPerson2.getEmployeeId()).thenReturn(String.valueOf(BOB.getEmployeeId()));

            // Create mock employee that will conflict with BOB
            Employee mockConflictingEmployee = mock(Employee.class);
            when(mockConflictingEmployee.isSameEmployee(BOB)).thenReturn(true);
            when(mockConflictingEmployee.hasSameDetails(BOB)).thenReturn(false);

            // Use doReturn/when syntax which is more lenient with argument matching
            ObservableList<Employee> emptyList = FXCollections.observableArrayList();
            ObservableList<Employee> bobConflictList = FXCollections.observableArrayList(mockConflictingEmployee);

            // AMY behavior - no conflict
            doReturn(emptyList).when(model).getFilteredByEmployeeIdPrefixList(AMY.getEmployeeId());

            // Specific behavior for BOB
            doReturn(bobConflictList).when(model).getFilteredByEmployeeIdPrefixList(BOB.getEmployeeId());

            importCommand = new ImportCommand("csv", csvFilePathNormalCase, "append");
            CommandResult result = importCommand.execute(model);

            // Only AMY should be added, BOB should be skipped
            verify(model, times(1)).addEmployee(any(Employee.class));

            // Check feedback message contains expected values
            String feedback = result.getFeedbackToUser();
            assertTrue(feedback.contains("Successfully imported 1 contacts, skipped 1"));
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
     * Tests how the getAnniversaries().addAll() method is called when contacts with same details are found.
     * @throws Exception
     */
    @Test
    public void execute_importJsonAppendWithMergedAnniversaries() throws Exception {
        try (MockedStatic<AddressBookFormatConverter> formatConverterMock =
                     mockStatic(AddressBookFormatConverter.class)) {
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromJson(jsonFilePathNormalCase))
                    .thenReturn(jsonSerializableAddressBook);
            // Setup imported data with test data containing anniversaries
            List<Employee> actualEmployees = addressBookDuplicate.getEmployeeList();
            List<JsonAdaptedPerson> mockJsonPersons = new ArrayList<>();

            for (Employee employee : actualEmployees) {
                JsonAdaptedPerson mockJsonPerson = mock(JsonAdaptedPerson.class);
                when(mockJsonPerson.toModelType()).thenReturn(employee);
                when(mockJsonPerson.getEmployeeId()).thenReturn(String.valueOf(employee.getEmployeeId()));
                mockJsonPersons.add(mockJsonPerson);
            }

            // Mock getPersons() to return the controlled list
            when(jsonSerializableAddressBook.getPersons()).thenReturn(mockJsonPersons);

            // Create a mock Employee
            Employee mockExistingEmployee = mock(Employee.class);
            when(mockExistingEmployee.isSameEmployee(any())).thenReturn(true);
            when(mockExistingEmployee.hasSameDetails(any())).thenReturn(true);

            // Create a separate mock for the anniversaries list
            @SuppressWarnings("unchecked")
            ObservableList<Anniversary> mockAnniversaryList = mock(ObservableList.class);
            when(mockExistingEmployee.getAnniversaries()).thenReturn(mockAnniversaryList);

            // Return our mock when searching for ALICE
            ObservableList<Employee> matchList = FXCollections.observableArrayList(mockExistingEmployee);
            when(model.getFilteredByEmployeeIdPrefixList(ALICE.getEmployeeId()))
                    .thenReturn(matchList);

            importCommand = new ImportCommand("json", jsonFilePathNormalCase, "append");
            CommandResult result = importCommand.execute(model);

            // Use ArgumentCaptor to specify the Collection version of addAll
            ArgumentCaptor<Collection<Anniversary>> captor = ArgumentCaptor.forClass(Collection.class);
            verify(mockAnniversaryList, times(1)).addAll(captor.capture());

            assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_APPEND, 1, 0, "Conflicting records found:\n"),
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

            // Set up mock return for importFromCsv
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromCsv(csvFilePathDuplicateCase))
                    .thenReturn(jsonSerializableAddressBook);

            // Create actual persons for the conflict test
            AddressBook conflictBook = new AddressBookBuilder()
                    .withEmployee(ALICE).withEmployee(BOB).build();
            // Mock JsonAdaptedPerson list
            List<JsonAdaptedPerson> mockJsonPersons = new ArrayList<>();

            JsonAdaptedPerson mockAliceJson = mock(JsonAdaptedPerson.class);
            when(mockAliceJson.toModelType()).thenReturn(ALICE);
            when(mockAliceJson.getEmployeeId()).thenReturn(ALICE.getEmployeeId().toString());
            mockJsonPersons.add(mockAliceJson);

            JsonAdaptedPerson mockBobJson = mock(JsonAdaptedPerson.class);
            when(mockBobJson.toModelType()).thenReturn(BOB);
            when(mockBobJson.getEmployeeId()).thenReturn(BOB.getEmployeeId().toString());
            mockJsonPersons.add(mockBobJson);

            when(jsonSerializableAddressBook.getPersons()).thenReturn(mockJsonPersons);

            // Mock a conflicting employee for ALICE
            Employee mockConflictEmployee = mock(Employee.class);
            when(mockConflictEmployee.getName()).thenReturn(ALICE.getName());
            when(mockConflictEmployee.isSameEmployee(any())).thenReturn(true);
            when(mockConflictEmployee.hasSameDetails(any())).thenReturn(false);

            when(model.getFilteredByEmployeeIdPrefixList(ALICE.getEmployeeId()))
                    .thenReturn(FXCollections.observableArrayList(mockConflictEmployee));

            when(model.getFilteredByEmployeeIdPrefixList(BOB.getEmployeeId()))
                    .thenReturn(FXCollections.observableArrayList());

            importCommand = new ImportCommand("csv", csvFilePathDuplicateCase, "append");
            CommandResult result = importCommand.execute(model);

            // Only BOB should be added
            verify(model, times(1)).addEmployee(eq(BOB));
            assertTrue(result.getFeedbackToUser().contains(mockConflictEmployee.getName().toString()));
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

            // Setup imported JSON data
            formatConverterMock.when(() -> AddressBookFormatConverter.importFromJson(jsonFilePathDuplicateCase))
                    .thenReturn(jsonSerializableAddressBook);

            // Prepare real model data — employee with same ID but different details
            AddressBook modifiedBook = new AddressBookBuilder().withEmployee(ALICE).build();
            // Create and mock a JsonAdaptedPerson to return ALICE
            JsonAdaptedPerson mockAliceJson = mock(JsonAdaptedPerson.class);
            when(mockAliceJson.toModelType()).thenReturn(ALICE);
            when(mockAliceJson.getEmployeeId()).thenReturn(ALICE.getEmployeeId().toString());
            when(jsonSerializableAddressBook.getPersons()).thenReturn(List.of(mockAliceJson));

            // Simulate existing employee with same ID but different details
            Employee mockExistingEmployee = mock(Employee.class);
            when(mockExistingEmployee.getName()).thenReturn(ALICE.getName());
            when(mockExistingEmployee.getEmployeeId()).thenReturn(ALICE.getEmployeeId());
            when(mockExistingEmployee.isSameEmployee(any())).thenReturn(true);
            when(mockExistingEmployee.hasSameDetails(any())).thenReturn(false);

            // When model is queried for existing employee, return this mock
            ObservableList<Employee> matchList = FXCollections.observableArrayList(mockExistingEmployee);
            when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(matchList);

            // Execute command
            importCommand = new ImportCommand("json", jsonFilePathDuplicateCase, "append");
            CommandResult result = importCommand.execute(model);

            // Verify no addition happens due to conflict
            verify(model, times(0)).addEmployee(any(Employee.class));
            assertTrue(result.getFeedbackToUser().contains(mockExistingEmployee.getName().toString()));
            assertTrue(result.getFeedbackToUser().contains(mockExistingEmployee.getEmployeeId().toString()));
        }
    }

}
