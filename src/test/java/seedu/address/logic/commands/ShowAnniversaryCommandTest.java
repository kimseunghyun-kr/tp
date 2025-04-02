package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static seedu.address.logic.commands.anniversary.ShowAnniversaryCommand.MESSAGE_SUCCESS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.logic.commands.anniversary.ShowAnniversaryCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.testutil.EmployeeBuilder;

//For some reason, this test can't be found when in Anniversary package
public class ShowAnniversaryCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(Model.class);
    }

    @Test
    public void execute_validEmployeeId_success() throws Exception {
        Employee mockEmployee = new EmployeeBuilder().build();
        String validId = mockEmployee.getEmployeeId().toString();


        ObservableList<Employee> baseList =
                FXCollections.observableArrayList(mockEmployee);
        FilteredList<Employee> filteredList = new FilteredList<>(baseList);

        Mockito.when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(filteredList);

        ShowAnniversaryCommand command = new ShowAnniversaryCommand(EmployeeId.fromString(validId));
        CommandResult result = command.execute(model);

        assertEquals(result, new CommandResult(String.format(MESSAGE_SUCCESS, mockEmployee.getEmployeeId()),
                true, validId));
    }

    @Test
    public void execute_employeeNotFound_throwsCommandException() {
        String nonExistentId = "fake-id";

        ObservableList<Employee> baseList =
                FXCollections.observableArrayList();
        FilteredList<Employee> filteredList = new FilteredList<>(baseList);

        Mockito.when(model.getFilteredByEmployeeIdPrefixList(any())).thenReturn(filteredList); // no employees

        ShowAnniversaryCommand command = new ShowAnniversaryCommand(EmployeeId.fromString(nonExistentId));

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    @Test
    public void constructor_nullId_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new ShowAnniversaryCommand(null));
    }
}
