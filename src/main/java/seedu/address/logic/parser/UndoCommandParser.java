package seedu.address.logic.parser;

import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

public class UndoCommandParser implements Parser<UndoCommand> {

    @Override
    public UndoCommand parse(String args) throws ParseException {
        if (args.isEmpty()) {
            return new UndoCommand();
        } else {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT);
        }
    }
}
