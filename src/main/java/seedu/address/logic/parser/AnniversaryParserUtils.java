package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Name;

/**
 * Contains utility methods for parsing anniversary information.
 */
public class AnniversaryParserUtils {

    /**
     * Parses the anniversary information from the given {@code ArgumentMultimap}.
     *
     * @param argMultimap the argument multimap containing the parsed arguments
     * @param name the name of the person
     * @return a list of anniversaries
     * @throws ParseException if the anniversary information is invalid
     */
    public static List<Anniversary> parseAnniversaries(ArgumentMultimap argMultimap, Name name) throws ParseException {
        List<Anniversary> anniversaryList = new ArrayList<>();

        boolean hasAnniversaryType = arePrefixesPresent(argMultimap,
                PREFIX_ANNIVERSARY_TYPE, PREFIX_ANNIVERSARY_TYPE_DESC);
        boolean hasBirthday = argMultimap.getValue(PREFIX_BIRTHDAY).isPresent();
        boolean hasWorkAnniversary = argMultimap.getValue(PREFIX_WORK_ANNIVERSARY).isPresent();

        if (hasAnniversaryType && (hasBirthday || hasWorkAnniversary)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPersonCommand.MESSAGE_USAGE));
        }

        if (hasAnniversaryType) {
            Anniversary anniversary = ParserUtil.parseAnniversary(name,
                    argMultimap.getValue(PREFIX_ANNIVERSARY_DATE).get(),
                    argMultimap.getValue(PREFIX_ANNIVERSARY_TYPE).get(),
                    argMultimap.getValue(PREFIX_ANNIVERSARY_TYPE_DESC).get());
            anniversaryList.add(anniversary);
        } else {
            if (hasBirthday) {
                Anniversary birthday = ParserUtil.parseAnniversary(name,
                        argMultimap.getValue(PREFIX_BIRTHDAY).get(), "Birthday", null);
                anniversaryList.add(birthday);
            }
            if (hasWorkAnniversary) {
                Anniversary workAnniversary = ParserUtil.parseAnniversary(name,
                        argMultimap.getValue(PREFIX_WORK_ANNIVERSARY).get(),
                        "Work Anniversary", null);
                anniversaryList.add(workAnniversary);
            }
        }

        return anniversaryList;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
