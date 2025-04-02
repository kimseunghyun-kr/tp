package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.person.Employee;
import seedu.address.testutil.EmployeeBuilder;

public class FindCommandParserTest {

    private Model model;
    private FindCommandParser parser = new FindCommandParser();

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(Model.class);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validNameArgs_filtersCorrectly() throws ParseException {
        // no leading and trailing whitespaces
        FindCommand command = parser.parse(" n/Alice Bob");

        Employee matching1 = new EmployeeBuilder().withName("Alice Johnson").build();
        Employee matching2 = new EmployeeBuilder().withName("Bob Marley").build();
        Employee nonMatching = new EmployeeBuilder().withName("Charlie Brown").build();

        ObservableList<Employee> baseList =
                FXCollections.observableArrayList(matching1, matching2, nonMatching);
        FilteredList<Employee> filteredList = new FilteredList<>(baseList);

        //stub creation
        when(model.getFilteredEmployeeList()).thenReturn(filteredList);

        doAnswer(invocation -> {
            Predicate<Employee> predicate = invocation.getArgument(0);
            filteredList.setPredicate(predicate);
            return null;
        }).when(model).updateFilteredEmployeeList(any());

        command.execute(model);

        assertEquals(List.of(matching1, matching2), filteredList);

        //reset list
        filteredList.setPredicate(unused -> true);

        // multiple whitespaces between keywords
        FindCommand whitespaceCommand = parser.parse(" \n n/Alice \n \t Bob  \t");

        whitespaceCommand.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredEmployeeList());
    }

    @Test
    public void parse_jobPositionArgs_filtersCorrectly() throws Exception {
        // Parse command with standard input
        FindCommand command = parser.parse(" jp/engineer manager");

        Employee matching1 = new EmployeeBuilder().withJobPosition("Software Engineer").build();
        Employee matching2 = new EmployeeBuilder().withJobPosition("Product Manager").build();
        Employee nonMatching = new EmployeeBuilder().withJobPosition("Sales Associate").build();

        ObservableList<Employee> baseList =
                FXCollections.observableArrayList(matching1, matching2, nonMatching);
        FilteredList<Employee> filteredList = new FilteredList<>(baseList);

        when(model.getFilteredEmployeeList()).thenReturn(filteredList);

        doAnswer(invocation -> {
            Predicate<Employee> predicate = invocation.getArgument(0);
            filteredList.setPredicate(predicate);
            return null;
        }).when(model).updateFilteredEmployeeList(any());

        command.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredEmployeeList());

        //reset list
        filteredList.setPredicate(unused -> true);

        // Parse command with messy whitespace
        FindCommand whitespaceCommand = parser.parse(" \n jp/engineer \t manager  \n");

        whitespaceCommand.execute(model);

        assertEquals(List.of(matching1, matching2), model.getFilteredEmployeeList());
    }

    @Test
    public void parse_nameAndJobPositionArgs_filtersCorrectly() throws Exception {
        // Parse command with both name and job position
        FindCommand command = parser.parse(" n/jack jp/engineer");

        Employee matching = new EmployeeBuilder()
                .withName("Jack Daniel")
                .withJobPosition("Software Engineer")
                .build();

        Employee wrongName = new EmployeeBuilder()
                .withName("Alice")
                .withJobPosition("Software Engineer")
                .build();

        Employee wrongJob = new EmployeeBuilder()
                .withName("Jack")
                .withJobPosition("Chef")
                .build();

        Employee completelyOff = new EmployeeBuilder()
                .withName("Bob")
                .withJobPosition("Accountant")
                .build();

        ObservableList<Employee> baseList =
                FXCollections.observableArrayList(matching, wrongName, wrongJob, completelyOff);
        FilteredList<Employee> filteredList = new FilteredList<>(baseList);

        when(model.getFilteredEmployeeList()).thenReturn(filteredList);

        doAnswer(invocation -> {
            Predicate<Employee> predicate = invocation.getArgument(0);
            filteredList.setPredicate(predicate);
            return null;
        }).when(model).updateFilteredEmployeeList(any());

        command.execute(model);

        assertEquals(List.of(matching), model.getFilteredEmployeeList());

        //reset list
        filteredList.setPredicate(unused -> true);

        // Messy whitespace version
        FindCommand messyCommand = parser.parse(" \n n/jack  \n jp/engineer \t");

        messyCommand.execute(model);

        assertEquals(List.of(matching), model.getFilteredEmployeeList());
    }

    @Test
    public void parse_emptyFieldsWithPrefixes_throwsParseException() {
        assertParseFailure(parser, " n/   jp/   ",
                seedu.address.logic.Messages.MESSAGE_EMPTY_FIELD_WITH_PREFIX);
    }
    
    @Test
    public void parse_preamble_throwsParseException() {
        assertParseFailure(parser, " 2134rt n/jack jp/engineer",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    // Should pass as it will skip the empty field
    @Test
    public void parse_oneBlankField_stillParses() throws Exception {
        FindCommand command = parser.parse(" n/   jp/engineer ");
        assertEquals(FindCommand.class, command.getClass());
    }

    // Should pass as it will skip the empty field
    @Test
    public void parse_multipleBlankField_stillParses() throws Exception {
        FindCommand command = parser.parse(" n/   n/jack jp/   \n jp/engineer ");
        assertEquals(FindCommand.class, command.getClass());
    }

}
