package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Employee;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_EMPTY_FIELD_WITH_PREFIX = "At least one non-empty field is required.";
    public static final String MESSAGE_EMPLOYEES_LISTED_OVERVIEW = "%1$d employees listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    public static final String MESSAGE_SUCCESS = "New anniversary added: %1$s";
    public static final String MESSAGE_WARNING_ANNI_AFTER_TODAY = "WARNING: You are inputting an anniversary happening "
            + "after today. H'Reers won't stop you from doing that as we can still track it, but please be aware.";
    public static final String MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND = "No employee found with employeeId starting with %s";
    public static final String MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX =
            "Found multiple employees with employeeId starting with %s";
    public static final String MESSAGE_DUPLICATE_ANNIVERSARY =
            "This exact anniversary (date + name + type + description) already exists for that employee.";
    public static final String MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS =
            "The index you are searching for is out of bounds for the anniversary.";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code employee} for display to the user.
     */
    public static String format(Employee employee) {
        final StringBuilder builder = new StringBuilder();
        builder.append(employee.getName())
                .append("; Phone: ")
                .append(employee.getPhone())
                .append("; Email: ")
                .append(employee.getEmail())
                .append("; Job: ")
                .append(employee.getJobPosition())
                .append("; Tags: ");
        employee.getTags().forEach(builder::append);
        return builder.toString();
    }

}
