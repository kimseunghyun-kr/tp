package seedu.address.logic.commands.anniversary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_ANNIVERSARY;
import static seedu.address.logic.Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND;
import static seedu.address.logic.Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class AddAnniversaryCommandTest {
    private Model model;
    private Anniversary validAnniversary;
    private EmployeeId validEmployeeId;
    private Employee baseEmployee;

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(Model.class);
        validEmployeeId = EmployeeId.fromString("00000000-0000-0000-0000-000000000001");
        validAnniversary = new Anniversary(
                LocalDate.of(2025, 3, 13),
                new AnniversaryType("Personal", "Family"),
                "Sample Description",
                "Sample Anniversary"
        );
        baseEmployee = Employee.builder()
                .employeeId(validEmployeeId)
                .name(new Name("Alice"))
                .phone(new Phone("12345678"))
                .email(new Email("alice@example.com"))
                .jobPosition(new JobPosition("Developer"))
                .tags(new HashSet<>())
                .anniversaries(new ArrayList<>())
                .build();
    }

    @Test
    public void execute_addValidAnniversary_success() throws Exception {
        // Arrange
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList(baseEmployee));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        // Act
        var result = command.execute(model);

        // Assert
        Mockito.verify(model).setEmployee(Mockito.eq(baseEmployee), Mockito.any(Employee.class));
        assertEquals("New anniversary added: " + validAnniversary, result.getFeedbackToUser());
    }

    @Test
    public void execute_noMatchingEmployee_throwsCommandException() {
        // Arrange
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList());
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        // Act & Assert
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND, validEmployeeId), ex.getMessage());
    }

    @Test
    public void execute_multipleEmployeesFound_throwsCommandException() {
        // Arrange
        Employee secondEmployee = Employee.builder()
                .employeeId(EmployeeId.fromString("11111111-1111-1111-1111-111111111111"))
                .name(new Name("Bob"))
                .phone(new Phone("87654321"))
                .email(new Email("bob@example.com"))
                .jobPosition(new JobPosition("Tester"))
                .tags(new HashSet<>())
                .anniversaries(new ArrayList<>())
                .build();
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList(baseEmployee, secondEmployee));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        // Act & Assert
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX, validEmployeeId), ex.getMessage());
    }

    @Test
    public void execute_duplicateAnniversary_throwsCommandException() {
        // Arrange
        ArrayList<Anniversary> existing = new ArrayList<>();
        existing.add(validAnniversary);
        Employee employeeWithAnniversary = Employee.builder()
                .employeeId(validEmployeeId)
                .name(baseEmployee.getName())
                .phone(baseEmployee.getPhone())
                .email(baseEmployee.getEmail())
                .jobPosition(baseEmployee.getJobPosition())
                .tags(baseEmployee.getTags())
                .anniversaries(existing)
                .build();
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList(employeeWithAnniversary));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        // Act & Assert
        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(MESSAGE_DUPLICATE_ANNIVERSARY, ex.getMessage());
    }

    @Test
    public void execute_anniversaryWithEmptyNameAndDescription_success() throws Exception {
        Anniversary minimalAnniversary = new Anniversary(
                LocalDate.of(2025, 3, 13),
                new AnniversaryType("", ""), // Type name and description are empty
                "", // Description
                "" // Name
        );

        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList(basePerson));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, minimalAnniversary);

        var result = command.execute(model);

        Mockito.verify(model).setPerson(Mockito.eq(basePerson), Mockito.argThat(updated ->
                updated.getAnniversaries().contains(minimalAnniversary)
        ));
        assertEquals("New anniversary added: " + minimalAnniversary, result.getFeedbackToUser());
    }

    @Test
    public void execute_sameDateDifferentName_success() throws Exception {
        Anniversary existingAnni = new Anniversary(
                LocalDate.of(2025, 3, 13),
                new AnniversaryType("Personal", "Family"),
                "Old Description",
                "Old Name"
        );
        Anniversary newAnni = new Anniversary(
                LocalDate.of(2025, 3, 13),
                new AnniversaryType("Work", "Promotion"),
                "New Description",
                "New Name"
        );
        ArrayList<Anniversary> existingList = new ArrayList<>();
        existingList.add(existingAnni);
        Person personWithAnni = Person.builder()
                .employeeId(validEmployeeId)
                .name(basePerson.getName())
                .phone(basePerson.getPhone())
                .email(basePerson.getEmail())
                .jobPosition(basePerson.getJobPosition())
                .tags(basePerson.getTags())
                .anniversaries(existingList)
                .build();

        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList(personWithAnni));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, newAnni);

        var result = command.execute(model);

        Mockito.verify(model).setPerson(Mockito.eq(personWithAnni), Mockito.argThat(updated ->
                updated.getAnniversaries().contains(newAnni)
        ));
        assertEquals("New anniversary added: " + newAnni, result.getFeedbackToUser());
    }

    @Test
    public void execute_addAnniversary_returnsNewPersonObject() throws Exception {
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(FXCollections.observableArrayList(basePerson));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        command.execute(model);

        Mockito.verify(model).setPerson(Mockito.eq(basePerson), Mockito.argThat(updated ->
                updated != basePerson && updated.getAnniversaries().contains(validAnniversary)
        ));
    }

    @Test
    public void constructor_nullAnniversary_throwsException() {
        assertThrows(NullPointerException.class, () -> new AddAnniversaryCommand(validEmployeeId, null));
    }

    @Test
    public void constructor_nullEmployeeId_throwsException() {
        assertThrows(NullPointerException.class, () -> new AddAnniversaryCommand(null, validAnniversary));
    }


}
