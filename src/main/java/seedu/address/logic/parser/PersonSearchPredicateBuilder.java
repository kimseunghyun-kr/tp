package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Employee;
import seedu.address.model.person.JobPositionContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Builds a predicate based on the given {@code ArgumentMultimap}.
 */
public class PersonSearchPredicateBuilder {

    private static final Logger logger = LogsCenter.getLogger(PersonSearchPredicateBuilder.class);

    /**
     * Builds a {@code Predicate<Employee>} based on the given {@code ArgumentMultimap}.
     * Within the Prefixes, the keywords are combined with OR logic.
     * Between the Prefixes, the keywords are combined with AND logic.
     * @param argMultimap the argument multimap
     * @return the predicate
     */
    public static Predicate<Employee> buildPredicate(ArgumentMultimap argMultimap) {
        Predicate<Employee> combinedPredicate = person -> true;

        boolean hasNonEmptyName = !argMultimap.getAllValues(PREFIX_NAME).stream().allMatch(String::isBlank);
        boolean hasNonEmptyJp = !argMultimap.getAllValues(PREFIX_JOBPOSITION).stream().allMatch(String::isBlank);

        // Name Search
        if (hasNonEmptyName) {
            Predicate<Employee> namePredicate = buildNamePredicate(argMultimap);
            combinedPredicate = combinedPredicate.and(namePredicate);
        }

        // Job Position Search
        if (hasNonEmptyJp) {
            Predicate<Employee> jpPredicate = buildJobPositionPredicate(argMultimap);
            combinedPredicate = combinedPredicate.and(jpPredicate);
        }
        return combinedPredicate;
    }

    private static List<String> buildEachPredicate(ArgumentMultimap argMultimap, Prefix prefixToSearch) {
        List<String> keywords = argMultimap.getAllValues(prefixToSearch);
        List<String> combinedKeywords = new ArrayList<>();
        for (String keyword : keywords) {
            if (keyword.isBlank()) {
                continue;
            }
            keyword = keyword.trim();
            String[] splitKeywords = keyword.split("\\s+");
            combinedKeywords.addAll(Arrays.asList(splitKeywords));
        }
        return combinedKeywords;
    }

    private static Predicate<Employee> buildNamePredicate(ArgumentMultimap argMultimap) {
        List<String> combinedNameKeywords = buildEachPredicate(argMultimap, PREFIX_NAME);
        Predicate<Employee> namePredicate = new NameContainsKeywordsPredicate(combinedNameKeywords);
        logger.info("Predicate added: " + namePredicate);
        return namePredicate;
    }

    private static Predicate<Employee> buildJobPositionPredicate(ArgumentMultimap argMultimap) {
        List<String> combinedJpKeywords = buildEachPredicate(argMultimap, PREFIX_JOBPOSITION);;
        Predicate<Employee> combinedJpPredicate = new JobPositionContainsKeywordsPredicate(combinedJpKeywords);
        logger.info("Predicate added: " + combinedJpPredicate);
        return combinedJpPredicate;
    }
}
