package seedu.address.logic.parser.anniversary;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public AddAnniversaryCommand parse(String args) throws ParseException {
        // tokenize using relevant prefixes
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_EMPLOYEEID, PREFIX_ANNIVERSARY_DATE, PREFIX_NAME,
                PREFIX_ANNIVERSARY_DESC, PREFIX_ANNIVERSARY_TYPE
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

        // Build a Set<AnniversaryType> from each at/ prefix
        // For simplicity, each token can be used as both the "type name" and "description," or
        // you might parse them differently. Adjust as needed.
        Set<AnniversaryType> types = new HashSet<>();
        for (String token : typeTokens) {
            // In a real app, you might want more advanced parsing
            types.add(new AnniversaryType(token, ""));
        }

        Anniversary newAnniversary = new Anniversary(
                date,
                types,
                descStr,
                nameStr
        );

        return new AddAnniversaryCommand(employeeId, newAnniversary);
    }
}
