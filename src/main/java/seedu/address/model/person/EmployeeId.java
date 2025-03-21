package seedu.address.model.person;

/**
 * Represents a Person's employee ID in the address book.
 */
public class EmployeeId {

    public static final String MESSAGE_CONSTRAINTS =
            "Employee ID should be a string of letters and digits no longer than 36 characters.";

    public static final String VALIDATION_REGEX = "[a-zA-Z0-9]{1,36}";

    public final String value;

    /**
     * Constructs an {@code EmployeeId}.
     *
     * @param employeeId A valid employee ID.
     */
    public EmployeeId(String employeeId) {
        this.value = employeeId;
    }

    /**
     * Generates an employee ID from a String
     */
    public static EmployeeId fromString(String employeeId) {
        return new EmployeeId(employeeId);
    }

    /**
     * Generates a new employee ID as a random UUID.
     */
    public static EmployeeId generateNewEmployeeId() {
        return new EmployeeId(java.util.UUID.randomUUID().toString());
    }

    /**
     * Returns true if a given string is a valid employee ID.
     */
    public static boolean isValidEmployeeId(String test) {
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

        if (!(other instanceof EmployeeId)) {
            return false;
        }

        EmployeeId otherEmployeeId = (EmployeeId) other;
        return value.equals(otherEmployeeId.value);
    }
}
