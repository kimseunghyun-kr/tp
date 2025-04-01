package seedu.address.testutil;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.person.Employee;

/**
 * A utility class for test cases.
 */
public class TestUtil {

    /**
     * Folder used for temp files created during testing. Ignored by Git.
     */
    private static final Path SANDBOX_FOLDER = Paths.get("src", "test", "data", "sandbox");

    /**
     * Appends {@code fileName} to the sandbox folder path and returns the resulting path.
     * Creates the sandbox folder if it doesn't exist.
     */
    public static Path getFilePathInSandboxFolder(String fileName) {
        try {
            Files.createDirectories(SANDBOX_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SANDBOX_FOLDER.resolve(fileName);
    }

    /**
     * Returns the middle index of the employee in the {@code model}'s employee list.
     */
    public static Index getMidIndex(Model model) {
        return Index.fromOneBased(model.getFilteredEmployeeList().size() / 2);
    }

    /**
     * Returns the last index of the employee in the {@code model}'s employee list.
     */
    public static Index getLastIndex(Model model) {
        return Index.fromOneBased(model.getFilteredEmployeeList().size());
    }

    /**
     * Returns the employee in the {@code model}'s employee list at {@code index}.
     */
    public static Employee getEmployee(Model model, Index index) {
        return model.getFilteredEmployeeList().get(index.getZeroBased());
    }

    /**
     * checks if the Persons are the same excluding their employee id
     */
    public static void assertPersonEqualsIgnoringEmployeeId(Employee expected, Employee actual) {
        if (expected == actual) {
            return;
        }
        if (expected == null || actual == null) {
            fail("One of the persons is null");
        }
        if (!Objects.equals(expected.getName(), actual.getName())
                || !Objects.equals(expected.getPhone(), actual.getPhone())
                || !Objects.equals(expected.getEmail(), actual.getEmail())
                || !Objects.equals(expected.getJobPosition(), actual.getJobPosition())
                || !Objects.equals(expected.getTags(), actual.getTags())) {
            fail("Persons are not equal except for employeeId.\nExpected: " + expected + "\nActual  : " + actual);
        }
    }
}
