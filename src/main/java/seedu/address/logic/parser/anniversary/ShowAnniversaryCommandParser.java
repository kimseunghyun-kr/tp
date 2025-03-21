package seedu.address.logic.parser.anniversary;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import seedu.address.logic.commands.anniversary.ShowAnniversaryCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;

public class ShowAnniversaryCommandParser implements Parser<ShowAnniversaryCommand> {

    @Override
    public ShowAnniversaryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_EMPLOYEEID);

        if (argMultimap.getValue(PREFIX_EMPLOYEEID).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowAnniversaryCommand.MESSAGE_USAGE)
            );
        }

        String employeeId = argMultimap.getValue(PREFIX_EMPLOYEEID).get();

        return new ShowAnniversaryCommand(employeeId);
    }
}
