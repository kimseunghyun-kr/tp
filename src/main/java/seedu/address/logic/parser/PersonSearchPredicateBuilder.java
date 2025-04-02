package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;

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
     * @param argMultimap the argument multimap
     * @return the predicate
     */
    public static Predicate<Employee> buildPredicate(ArgumentMultimap argMultimap) {
        Predicate<Employee> combinedPredicate = person -> true;

        boolean hasNonEmptyName = argMultimap.getValue(PREFIX_NAME)
                .map(name -> !name.isBlank()).orElse(false);
        boolean hasNonEmptyJob = argMultimap.getValue(PREFIX_JOBPOSITION)
                .map(jp -> !jp.isBlank()).orElse(false);

        if (hasNonEmptyName) {
            List<String> names = argMultimap.getAllValues(PREFIX_NAME);
            Predicate<Employee> combinedNamePredicate = person -> false;
            for(String name : names) {
                if (name.isBlank()) {
                    continue;
                }
                name = name.trim();
                String[] nameKeywords = name.split("\\s+");
                Predicate<Employee> namePredicate = new NameContainsKeywordsPredicate(Arrays.asList(nameKeywords));
                logger.info("Predicate added: " + namePredicate);
                combinedNamePredicate = combinedNamePredicate.or(namePredicate);
            }
            combinedPredicate = combinedPredicate.and(combinedNamePredicate);
        }

        if (hasNonEmptyJob) {
            List<String> jobs = argMultimap.getAllValues(PREFIX_JOBPOSITION);
            Predicate<Employee> combinedJpPredicate = person -> false;
            for(String job : jobs) {
                if (job.isBlank()) {
                    continue;
                }
                job = job.trim();
                String[] jobKeywords = job.split("\\s+");
                Predicate<Employee> jpPredicate = new JobPositionContainsKeywordsPredicate(Arrays.asList(jobKeywords));
                logger.info("Predicate added: " + jpPredicate);
                combinedJpPredicate = combinedJpPredicate.or(jpPredicate);
            }
            combinedPredicate = combinedPredicate.and(combinedJpPredicate);
        }

        return combinedPredicate;
    }
}
