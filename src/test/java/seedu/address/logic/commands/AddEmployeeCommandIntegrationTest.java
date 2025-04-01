package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Employee;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddEmployeeCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() {
        Employee validEmployee = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validEmployee);

        assertCommandSuccess(new AddPersonCommand(validEmployee), model,
                String.format(AddPersonCommand.MESSAGE_SUCCESS, Messages.format(validEmployee)),
                expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Employee employeeInList = model.getAddressBook().getEmployeeList().get(0);
        assertCommandFailure(new AddPersonCommand(employeeInList), model,
                AddPersonCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
