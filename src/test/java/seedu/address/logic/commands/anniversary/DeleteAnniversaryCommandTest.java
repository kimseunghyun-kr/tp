package seedu.address.logic.commands.anniversary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS;
import static seedu.address.logic.Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND;
import static seedu.address.logic.Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import seedu.address.commons.core.index.Index;
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

public class DeleteAnniversaryCommandTest {

    private Model model;
    private Employee baseEmployee;
    private EmployeeId employeeId;
    private Anniversary anniversaryOne;
    private Anniversary anniversaryTwo;

    @BeforeEach
    void setUp() {
        model = Mockito.mock(Model.class);
        employeeId = EmployeeId.fromString("00000000-0000-0000-0000-000000000001");
        anniversaryOne = new Anniversary(LocalDate.of(2023, 10, 15), new AnniversaryType("TestType", "Desc"),
                "My Anniversary", "Type1");
        anniversaryTwo = new Anniversary(LocalDate.of(2025, 3, 13), new AnniversaryType("AnotherType", "Desc2"),
                "Another Anniversary", "Type2");
        ArrayList<Anniversary> anniversaries = new ArrayList<>();
        anniversaries.add(anniversaryOne);
        anniversaries.add(anniversaryTwo);
        baseEmployee = Employee.builder()
                .employeeId(employeeId)
                .name(new Name("Example"))
                .phone(new Phone("12345"))
                .email(new Email("ex@example.com"))
                .jobPosition(new JobPosition("Tester"))
                .tags(new HashSet<>())
                .anniversaries(anniversaries)
                .build();
    }

    @Test
    void execute_deleteAnniversary_success() throws CommandException {
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(employeeId))
                .thenReturn(FXCollections.observableArrayList(baseEmployee));
        DeleteAnniversaryCommand cmd = new DeleteAnniversaryCommand(Index.fromOneBased(1), employeeId);

        String feedback = cmd.execute(model).getFeedbackToUser();
        Mockito.verify(model).setEmployee(Mockito.eq(baseEmployee), Mockito.any(Employee.class));
        assertEquals("anniversary deleted: " + anniversaryOne, feedback);
    }

    @Test
    void execute_noMatchingEmployee_throwsCommandException() {
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(employeeId))
                .thenReturn(FXCollections.observableArrayList());
        DeleteAnniversaryCommand cmd = new DeleteAnniversaryCommand(Index.fromOneBased(1), employeeId);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND, employeeId), ex.getMessage());
    }

    @Test
    void execute_multipleEmployeesFound_throwsCommandException() {
        Employee otherEmployee = Employee.builder()
                .employeeId(EmployeeId.fromString("11111111-1111-1111-1111-111111111111"))
                .name(new Name("Second"))
                .phone(new Phone("54321"))
                .email(new Email("second@example.com"))
                .jobPosition(new JobPosition("Tester2"))
                .tags(new HashSet<>())
                .anniversaries(Collections.singletonList(anniversaryOne))
                .build();
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(employeeId))
                .thenReturn(FXCollections.observableArrayList(baseEmployee, otherEmployee));
        DeleteAnniversaryCommand cmd = new DeleteAnniversaryCommand(Index.fromOneBased(1), employeeId);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(String.format(MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX, employeeId), ex.getMessage());
    }

    @Test
    void execute_outOfBoundsIndex_throwsCommandException() {
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(employeeId))
                .thenReturn(FXCollections.observableArrayList(baseEmployee));
        DeleteAnniversaryCommand cmd = new DeleteAnniversaryCommand(Index.fromOneBased(999), employeeId);

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertEquals(MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS, ex.getMessage());
    }
}
