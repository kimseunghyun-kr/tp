package seedu.address.logic.parser.anniversary;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import seedu.address.logic.commands.anniversary.ShowAnniversaryCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EmployeeId;

/**
 * Parses input arguments and creates a new ShowAnniversaryCommand object
 */
public class ShowAnniversaryCommandParser implements Parser<ShowAnniversaryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ShowAnniversaryCommand
     * and returns an ShowAnniversaryCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ShowAnniversaryCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_EMPLOYEEID);

        if (argMultimap.getValue(PREFIX_EMPLOYEEID).isEmpty() || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowAnniversaryCommand.MESSAGE_USAGE)
            );
        }

        EmployeeId employeeId = ParserUtil.parseEmployeeId(argMultimap.getValue(PREFIX_EMPLOYEEID).get());
        return new ShowAnniversaryCommand(employeeId);
    }
}
