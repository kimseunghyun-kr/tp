package seedu.address.logic.commands.importexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ImportCommandIntegrationTest {

    public static final Path TEST_DATA_FOLDER = Paths.get("src",
            "test", "data", "ImportCommandTest");
    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_importValidCsv_success() throws Exception {
        Path testCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        ImportCommand importCommand = new ImportCommand("csv", testCsvPath, "overwrite");

        CommandResult commandResult = importCommand.execute(model);

        assertEquals(String.format(ImportCommand.MESSAGE_SUCCESS_OVERWRITE, 7), commandResult.getFeedbackToUser());
        assertEquals(7, model.getAddressBook().getPersonList().size());
    }

    @Test
    public void execute_importAggregateCsv_success() throws Exception {
        // First import base data
        Path testCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        ImportCommand importCommand = new ImportCommand("csv", testCsvPath, "overwrite");
        importCommand.execute(model);

        // Then import aggregate data in append mode
        Path aggregateCsvPath = TEST_DATA_FOLDER.resolve("testAggregate.csv");
        ImportCommand appendCommand = new ImportCommand("csv", aggregateCsvPath, "append");

        CommandResult commandResult = appendCommand.execute(model);

        // Should have imported 1 new person and skipped/merged others
        assertTrue(commandResult.getFeedbackToUser().contains("Successfully imported"));
        assertFalse(commandResult.getFeedbackToUser().contains("Skipped contacts"));
    }

    @Test
    public void execute_importDuplicateInvalidCsv_handlesConflicts() throws Exception {
        // First import base data with Alice
        Path testCsvPath = TEST_DATA_FOLDER.resolve("test.csv");
        ImportCommand importCommand = new ImportCommand("csv", testCsvPath, "overwrite");
        importCommand.execute(model);

        // Then import data with conflicting Alice records
        Path duplicatePath = TEST_DATA_FOLDER.resolve("testDuplicateInvalid.csv");
        ImportCommand appendCommand = new ImportCommand("csv", duplicatePath, "append");
        CommandResult commandResult = appendCommand.execute(model);

        // Should mention skipped contacts due to conflicts
        assertTrue(commandResult.getFeedbackToUser().contains("Skipped contacts"));
        assertTrue(commandResult.getFeedbackToUser().contains("Alice Pauline"));
        assertTrue(commandResult.getFeedbackToUser().contains("Alice Paulina"));
    }

    @Test
    public void execute_importInvalidData_throwsCommandException() {
        Path invalidPath = Paths.get("nonexistent.csv");
        ImportCommand importCommand = new ImportCommand("csv", invalidPath, "overwrite");

        assertThrows(CommandException.class, () -> importCommand.execute(model));
    }
}
