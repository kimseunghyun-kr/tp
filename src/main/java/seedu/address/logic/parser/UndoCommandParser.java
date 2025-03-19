package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments for the "undo" command and creates an instance of the UndoCommand.
 * This class ensures that the correct format is followed for the "undo" command input.
 */
public class UndoCommandParser implements Parser<UndoCommand> {

    /**
     * Parses the given input arguments to create an UndoCommand.
     * If the input is empty (indicating the "undo" command with no additional arguments),
     * an UndoCommand instance is created. If there is any extra input, a ParseException is thrown.
     *
     * @param args The arguments provided by the user for the "undo" command.
     * @return An instance of UndoCommand if the input is valid.
     * @throws ParseException If the input format is invalid or contains extra arguments.
     */
    @Override
    public UndoCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            return new UndoCommand();
        } else {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }
}
