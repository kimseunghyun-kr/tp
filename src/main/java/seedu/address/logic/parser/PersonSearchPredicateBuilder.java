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
            List<String> names = argMultimap.getAllValues(PREFIX_NAME);
            List<String> combinedNameKeywords = new ArrayList<>();
            for (String name : names) {
                if (name.isBlank()) {
                    continue;
                }
                name = name.trim();
                String[] nameKeywords = name.split("\\s+");
                combinedNameKeywords.addAll(Arrays.asList(nameKeywords));
            }
            Predicate<Employee> namePredicate = new NameContainsKeywordsPredicate(combinedNameKeywords);
            logger.info("Predicate added: " + namePredicate);
            combinedPredicate = combinedPredicate.and(namePredicate);
        }

        // Job Position Search
        if (hasNonEmptyJp) {
            List<String> jobs = argMultimap.getAllValues(PREFIX_JOBPOSITION);
            List<String> combinedJpKeywords = new ArrayList<>();
            for (String job : jobs) {
                if (job.isBlank()) {
                    continue;
                }
                job = job.trim();
                String[] jobKeywords = job.split("\\s+");
                combinedJpKeywords.addAll(Arrays.asList(jobKeywords));
            }
            Predicate<Employee> combinedJpPredicate = new JobPositionContainsKeywordsPredicate(combinedJpKeywords);
            logger.info("Predicate added: " + combinedJpPredicate);
            combinedPredicate = combinedPredicate.and(combinedJpPredicate);

        }
        return combinedPredicate;
    }
}
