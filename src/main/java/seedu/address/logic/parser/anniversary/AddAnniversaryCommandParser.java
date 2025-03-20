package seedu.address.logic.parser.anniversary;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import seedu.address.logic.commands.anniversary.AddAnniversaryCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;

/**
 * Parses input arguments and creates a new AddAnniversaryCommand object
 */
public class AddAnniversaryCommandParser implements Parser<AddAnniversaryCommand> {
    public static final String MESSAGE_ANNIVERSARY_TYPE_PARSE = "Anniversary type must be in the format of "
            + "at/TYPE1 atdesc/description";

    @Override
    public AddAnniversaryCommand parse(String args) throws ParseException {
        // tokenize using relevant prefixes
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_EMPLOYEEID, PREFIX_ANNIVERSARY_DATE, PREFIX_NAME,
                PREFIX_ANNIVERSARY_DESC, PREFIX_ANNIVERSARY_TYPE, PREFIX_ANNIVERSARY_TYPE_DESC
        );

        // Basic validation to ensure required prefixes exist
        if (argMultimap.getValue(PREFIX_EMPLOYEEID).isEmpty()
                || argMultimap.getValue(PREFIX_ANNIVERSARY_DATE).isEmpty()
                || argMultimap.getValue(PREFIX_NAME).isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAnniversaryCommand.MESSAGE_USAGE)
            );
        }

        // parse required fields
        String employeeId = argMultimap.getValue(PREFIX_EMPLOYEEID).get();
        String dateStr = argMultimap.getValue(PREFIX_ANNIVERSARY_DATE).get();
        String nameStr = argMultimap.getValue(PREFIX_NAME).get();

        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            throw new ParseException(Anniversary.MESSAGE_DATE_CONSTRAINTS);
        }

        // parse optional fields
        String descStr = argMultimap.getValue(PREFIX_ANNIVERSARY_DESC).orElse("");
        List<String> typeTokens = argMultimap.getAllValues(PREFIX_ANNIVERSARY_TYPE);

        // Build a AnniversaryType from each at/ prefix
        if (typeTokens.isEmpty()) {
            throw new ParseException(MESSAGE_ANNIVERSARY_TYPE_PARSE);
        }
        if (typeTokens.size() > 2) {
            throw new ParseException(MESSAGE_ANNIVERSARY_TYPE_PARSE);
        }

        AnniversaryType type = new AnniversaryType(typeTokens.get(0), typeTokens.get(1));

        Anniversary newAnniversary = new Anniversary(
                date,
                type,
                descStr,
                nameStr
        );

        return new AddAnniversaryCommand(employeeId, newAnniversary);
    }
}
