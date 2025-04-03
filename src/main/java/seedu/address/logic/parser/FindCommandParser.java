package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_EMPTY_FIELD_WITH_PREFIX;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Employee;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_JOBPOSITION);

        // Handles if the user inputs an empty string or has no prefix
        if (!areAnyPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_JOBPOSITION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        boolean hasEmptyName = argMultimap.getAllValues(PREFIX_NAME).stream().allMatch(String::isBlank);
        boolean hasEmptyJp = argMultimap.getAllValues(PREFIX_JOBPOSITION).stream().allMatch(String::isBlank);

        // Handles if both fields are present but empty (e.g. n/   jp/   )
        if (hasEmptyName && hasEmptyJp) {
            throw new ParseException(MESSAGE_EMPTY_FIELD_WITH_PREFIX);
        }

        //buildPredicate will skip the empty field if one of them is empty
        Predicate<Employee> predicate = PersonSearchPredicateBuilder.buildPredicate(argMultimap);

        return new FindCommand(predicate);
    }

    /**
     * Returns true if at least one of the prefixes contains a non-empty {@code Optional} value in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
