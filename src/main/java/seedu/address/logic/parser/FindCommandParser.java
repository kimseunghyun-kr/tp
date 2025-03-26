package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.JobPositionContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;

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

        // Handles if the user inputs an empty string or has no prefix or prefix is empty
        if (!areAnyPrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_JOBPOSITION)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        boolean hasName = argMultimap.getValue(PREFIX_NAME).isPresent();
        boolean hasJob = argMultimap.getValue(PREFIX_JOBPOSITION).isPresent();

        Predicate<Person> combinedPredicate = person -> true;

        if (hasName) {
            String name = argMultimap.getValue(PREFIX_NAME).get().trim();
            String[] nameKeywords = name.split("\\s+");
            Predicate<Person> namePredicate = new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords));
            combinedPredicate = combinedPredicate.and(namePredicate);
        }

        if (hasJob) {
            String job = argMultimap.getValue(PREFIX_JOBPOSITION).get().trim();
            String[] jobKeywords = job.split("\\s+");
            Predicate<Person> jpPredicate = new JobPositionContainsKeywordsPredicate(Arrays.asList(jobKeywords));
            combinedPredicate = combinedPredicate.and(jpPredicate);
        }

        return new FindCommand(combinedPredicate);
    }

    /**
     * Returns true if at least one of the prefixes contains a non-empty {@code Optional} value in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
