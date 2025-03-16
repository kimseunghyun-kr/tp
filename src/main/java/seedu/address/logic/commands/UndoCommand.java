package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class UndoCommand extends Command {
    public static final String COMMAND_WORD = "undo";
    public static final String MESSAGE_SUCCESS = "Undo successful!";
    public static final String MESSAGE_FAILURE = "No undo available!";

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
