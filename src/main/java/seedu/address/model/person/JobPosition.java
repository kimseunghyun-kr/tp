package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Employee's job position in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidJobPosition(String)}
 */
public class JobPosition {

    public static final String MESSAGE_CONSTRAINTS = "Job positions can take any values, and they should not be blank.";

    /**
     * The first character of the job position must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     * This regex also allows for periods and apostrophes.
     */
    public static final String VALIDATION_REGEX = "^[\\p{L}\\p{N} .\\-']+";

    public final String value;

    /**
     * Constructs a {@code JobPosition}.
     *
     * @param jobPosition A valid job position.
     */
    public JobPosition(String jobPosition) {
        requireNonNull(jobPosition);
        checkArgument(isValidJobPosition(jobPosition), MESSAGE_CONSTRAINTS);
        value = jobPosition;
    }

    /**
     * Returns true if a given string is a valid job position.
     */
    public static boolean isValidJobPosition(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof JobPosition)) {
            return false;
        }

        JobPosition otherJobPosition = (JobPosition) other;
        return value.equals(otherJobPosition.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

