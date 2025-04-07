package seedu.address.logic.parser;

import seedu.address.logic.commands.ReminderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ReminderCommand object.
 */
public class ReminderCommandParser implements Parser<ReminderCommand> {

    /**
     * Parses the given {@code String} of arguments and returns a ReminderCommand object.
     * Ignores any arguments provided.
     */
    public ReminderCommand parse(String args) throws ParseException {
        return new ReminderCommand();
    }
}

