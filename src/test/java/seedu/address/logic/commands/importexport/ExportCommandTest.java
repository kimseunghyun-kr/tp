package seedu.address.logic.commands.importexport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.CsvTestUtil;
import seedu.address.testutil.JsonTestUtil;
import seedu.address.testutil.TypicalPersonsWithAnniversaries;
import seedu.address.testutil.TypicalPersonsWithoutAnniversaries;

public class ExportCommandTest {

    @TempDir
    public Path testFolder;

    private Path jsonPathWithAnniversaries;
    private Path csvPathWithAnniversaries;
    private Path jsonPathWithoutAnniversaries;
    private Path csvPathWithoutAnniversaries;

    @BeforeEach
    public void setUp() {
        jsonPathWithAnniversaries = testFolder.resolve("withAnniversaries.json");
        csvPathWithAnniversaries = testFolder.resolve("withAnniversaries.csv");
        jsonPathWithoutAnniversaries = testFolder.resolve("withoutAnniversaries.json");
        csvPathWithoutAnniversaries = testFolder.resolve("withoutAnniversaries.csv");
    }

    @Test
    public void executeExportEmployeesWithAnniversariesToJson_success() throws Exception {
        // Setup model with anniversary data
        Model model = new ModelManager(TypicalPersonsWithAnniversaries.getTypicalAddressBook(), new UserPrefs());

        // Execute export command
        ExportCommand exportCommand = new ExportCommand("json", jsonPathWithAnniversaries);
        CommandResult result = exportCommand.execute(model);

        // Verify file contents match expected output
        JsonTestUtil.assertFileContentMatch(jsonPathWithAnniversaries,
                Paths.get("src/test/data/exportCommandTest/withAnniversary.json"));

        // Verify correct success message
        String expectedMessage = String.format("Exported %d employees in json format to %s",
                model.getFilteredEmployeeList().size(), jsonPathWithAnniversaries);
        assertEquals(expectedMessage,
                result.getFeedbackToUser().replaceAll("\\[.*\\]",
                        Integer.toString(model.getFilteredEmployeeList().size())));
    }

    @Test
    public void executeExportEmployeesWithAnniversariesToCsv_success() throws Exception {
        // Setup model with anniversary data
        Model model = new ModelManager(TypicalPersonsWithAnniversaries.getTypicalAddressBook(), new UserPrefs());

        // Execute export command
        ExportCommand exportCommand = new ExportCommand("csv", csvPathWithAnniversaries);
        CommandResult result = exportCommand.execute(model);

        // Verify file contents match expected output
        CsvTestUtil.assertFileContentMatch(csvPathWithAnniversaries,
                Paths.get("src/test/data/exportCommandTest/withAnniversary.csv"));

        // Verify correct success message
        String expectedMessage = String.format("Exported %d employees in csv format to %s",
                model.getFilteredEmployeeList().size(), csvPathWithAnniversaries);
        assertEquals(expectedMessage,
                result.getFeedbackToUser().replaceAll("\\[.*\\]",
                        Integer.toString(model.getFilteredEmployeeList().size())));
    }

    @Test
    public void executeExportEmployeesWithoutAnniversariesToJson_success() throws Exception {
        // Setup model without anniversary data
        Model model = new ModelManager(TypicalPersonsWithoutAnniversaries.getTypicalAddressBook(), new UserPrefs());

        // Execute export command
        ExportCommand exportCommand = new ExportCommand("json", jsonPathWithoutAnniversaries);
        CommandResult result = exportCommand.execute(model);

        // Verify file contents match expected output
        JsonTestUtil.assertFileContentMatch(jsonPathWithoutAnniversaries,
                Paths.get("src/test/data/exportCommandTest/noAnniversary.json"));

        // Verify correct success message
        String expectedMessage = String.format("Exported %d employees in json format to %s",
                model.getFilteredEmployeeList().size(), jsonPathWithoutAnniversaries);
        assertEquals(expectedMessage,
                result.getFeedbackToUser().replaceAll("\\[.*\\]",
                        Integer.toString(model.getFilteredEmployeeList().size())));
    }

    @Test
    public void executeExportEmployeesWithoutAnniversariesToCsv_success() throws Exception {
        // Setup model without anniversary data
        Model model = new ModelManager(TypicalPersonsWithoutAnniversaries.getTypicalAddressBook(), new UserPrefs());

        // Execute export command
        ExportCommand exportCommand = new ExportCommand("csv", csvPathWithoutAnniversaries);
        CommandResult result = exportCommand.execute(model);

        // Verify file contents match expected output
        CsvTestUtil.assertFileContentMatch(csvPathWithoutAnniversaries,
                Paths.get("src/test/data/exportCommandTest/noAnniversary.csv"));

        // Verify correct success message
        String expectedMessage = String.format("Exported %d employees in csv format to %s",
                model.getFilteredEmployeeList().size(), csvPathWithoutAnniversaries);
        assertEquals(expectedMessage,
                result.getFeedbackToUser().replaceAll("\\[.*\\]",
                        Integer.toString(model.getFilteredEmployeeList().size())));
    }

    @Test
    public void execute_invalidFiletype_throwsCommandException() {
        Model model = new ModelManager(TypicalPersonsWithAnniversaries.getTypicalAddressBook(), new UserPrefs());
        Path outputPath = testFolder.resolve("export.invalid");

        ExportCommand exportCommand = new ExportCommand("invalid", outputPath);

        assertThrows(CommandException.class, () -> exportCommand.execute(model));
    }

    @Test
    public void execute_emptyList_throwsCommandException() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        Path outputPath = testFolder.resolve("empty.json");

        ExportCommand exportCommand = new ExportCommand("json", outputPath);

        assertThrows(CommandException.class, () -> exportCommand.execute(model));
    }
}
