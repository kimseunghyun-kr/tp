package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Represents a command to undo the previous operation in the address book.
 * The command allows the user to revert the most recent change made to the address book if possible.
 */
public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful!";
    public static final String MESSAGE_FAILURE = "No undo available!";

    /**
     * Executes the undo command to revert the most recent change to the address book.
     * If an undo operation is available, it will be executed. Otherwise, a failure message is returned.
     *
     * @param model The model containing the address book data.
     * @return A CommandResult object indicating the result of the undo operation.
     * @throws CommandException If there is an error while executing the command.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        ModelManager modelManager = (ModelManager) model;
        if (modelManager.canUndoAddressBook()) {
            modelManager.undoAddressBook();
            return new CommandResult(MESSAGE_SUCCESS);
        } else {
            return new CommandResult(MESSAGE_FAILURE);
        }
    }

}
