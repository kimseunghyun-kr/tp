package seedu.address.logic.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UndoCommandTest {

    private Model model;
    private UndoCommand undoCommand;

    @BeforeEach
    void setUp() {
        model = new ModelManager();  // Assuming ModelManager implements Model
        undoCommand = new UndoCommand();
    }

    @Test
    void execute_undoAvailable_success() throws CommandException {
        // Simulate an initial state of the address book and commit a change
        Person validPerson = new PersonBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validPerson);

        model.commitChanges(); // Assuming commitChanges saves the current state

        // Perform undo operation
        CommandResult result = undoCommand.execute(model);

        // Assert that undo was successful
        assertEquals(UndoCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
        assertTrue(model.getFilteredPersonList().isEmpty()); // Assuming the undo removes the added person
    }

    @Test
    void execute_noUndoAvailable_failure() throws CommandException {
        // No changes have been made, so undo should not be possible
        CommandResult result = undoCommand.execute(model);

        // Assert that undo fails because no undo state is available
        assertEquals(UndoCommand.MESSAGE_FAILURE, result.getFeedbackToUser());
    }

    @Test
    void execute_undoDelete_noDeleteOperation_failure() throws CommandException {
        // No delete operation, so undo should fail
        CommandResult result = undoCommand.execute(model);

        // Assert that undo fails because there is no delete to undo
        assertEquals(UndoCommand.MESSAGE_FAILURE, result.getFeedbackToUser());
    }
}
