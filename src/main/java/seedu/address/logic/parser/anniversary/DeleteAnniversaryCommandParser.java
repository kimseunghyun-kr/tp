package seedu.address.logic.parser.anniversary;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.anniversary.DeleteAnniversaryCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EmployeeId;

/**
 * parses a deleteAnniversaryCommand
 */
public class DeleteAnniversaryCommandParser implements Parser<DeleteAnniversaryCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DeleteAnniversaryCommand parse(String args) throws ParseException {
        // tokenize using relevant prefixes
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_EMPLOYEEID, PREFIX_ANNIVERSARY_INDEX
        );

        // Basic validation to ensure required prefixes exist
        if (argMultimap.getValue(PREFIX_EMPLOYEEID).isEmpty()
                || argMultimap.getValue(PREFIX_ANNIVERSARY_INDEX).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAnniversaryCommand.MESSAGE_USAGE)
            );
        }
        try {
            EmployeeId employeeIdPrefix = ParserUtil.parseEmployeeIdPrefix(
                    argMultimap.getValue(PREFIX_EMPLOYEEID).get()
            );
            Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_ANNIVERSARY_DATE).get());
            return new DeleteAnniversaryCommand(index, employeeIdPrefix);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }
}
