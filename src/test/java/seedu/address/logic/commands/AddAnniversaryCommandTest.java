package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_ANNIVERSARY;
import static seedu.address.logic.Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX;
import static seedu.address.logic.Messages.MESSAGE_PERSON_PREFIX_NOT_FOUND;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;

public class AddAnniversaryCommandTest {

    private Model model;
    private Anniversary validAnniversary;
    private EmployeeId validEmployeeId;

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(Model.class);
        Person existingPerson = Mockito.mock(Person.class);

        validEmployeeId = EmployeeId.fromString("00000000-0000-0000-0000-000000000001");
        validAnniversary = new Anniversary(
                LocalDate.of(2025, 3, 13),
                new AnniversaryType("Personal", "Family"),
                "Sample Description",
                "Sample Anniversary"
        );

        // Mock a single result for the employee ID prefix
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(Collections.singletonList(existingPerson));
        Mockito.when(existingPerson.getAnniversaries())
                .thenReturn(Collections.emptyList());
    }

    @Test
    public void execute_addValidAnniversary_success() throws Exception {
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);
        CommandResult result = command.execute(model);
        // Expect success message to contain the added anniversary
        Mockito.verify(model).setPerson(Mockito.any(Person.class), Mockito.any(Person.class));
        assertEquals(String.format("Anniversary added: %s", validAnniversary.toString()), result.getFeedbackToUser());
    }

    @Test
    public void execute_duplicateAnniversary_throwsCommandException() {
        // Simulate the existing person already having the same anniversary
        Person existingPerson = Mockito.mock(Person.class);
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(Collections.singletonList(existingPerson));
        Mockito.when(existingPerson.getAnniversaries())
                .thenReturn(Collections.singletonList(validAnniversary));

        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);
        assertThrows(CommandException.class, () -> command.execute(model), MESSAGE_DUPLICATE_ANNIVERSARY);
    }

    @Test
    public void execute_noMatchingEmployee_throwsCommandException() {
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(Collections.emptyList());
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_PERSON_PREFIX_NOT_FOUND, validEmployeeId), ex.getMessage());
    }

    @Test
    public void execute_multipleMatches_throwsCommandException() {
        // Simulate finding multiple employees with the same ID prefix
        Person p1 = Mockito.mock(Person.class);
        Person p2 = Mockito.mock(Person.class);
        Mockito.when(model.getFilteredByEmployeeIdPrefixList(validEmployeeId))
                .thenReturn(java.util.Arrays.asList(p1, p2));
        AddAnniversaryCommand command = new AddAnniversaryCommand(validEmployeeId, validAnniversary);

        CommandException ex = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals(String.format(MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX, validEmployeeId), ex.getMessage());
    }
}