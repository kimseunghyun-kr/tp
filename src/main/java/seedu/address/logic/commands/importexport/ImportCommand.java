package seedu.address.logic.commands.importexport;

import static seedu.address.logic.parser.CliSyntax.PREFIX_FILEPATH;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WRITE_MODE;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import lombok.Getter;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.storage.JsonSerializableAddressBook;

/**
 * Imports data from a file.
 */
@Getter
public class ImportCommand extends Command {
    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_USAGE = "import "
            + PREFIX_FILETYPE + "<json/csv> "
            + PREFIX_FILEPATH + "<path> "
            + PREFIX_WRITE_MODE + "<append/overwrite>";
    public static final String MESSAGE_SUCCESS_OVERWRITE = "Successfully imported %d contacts,"
            + " overwriting existing data.";
    public static final String MESSAGE_SUCCESS_APPEND = """
            Successfully imported %d contacts, skipped %d.\s
            Please resolve conflicts manually\s
             + %s""";
    public static final String MESSAGE_INVALID_FILETYPE = "Invalid filetype. Use 'json' or 'csv'.";
    public static final String MESSAGE_INVALID_MODE = "Invalid mode. Use 'append' or 'overwrite'.";
    public static final String MESSAGE_INVALID_DATA = "Invalid data in import file: %s";
    private static final Logger logger = LogsCenter.getLogger(ImportCommand.class);
    public final String filetype;
    public final Path path;
    public final String mode;

    /**
     * Creates an ImportCommand to import the specified {@code Person}
     * @param filetype json or csv source file
     * @param path path to the file
     * @param mode append or replace
     */
    public ImportCommand(String filetype, Path path, String mode) {
        this.filetype = filetype;
        this.path = path;
        this.mode = mode;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        try {
            // Import data from file
            JsonSerializableAddressBook importedData;
            if (filetype.equalsIgnoreCase("json")) {
                importedData = AddressBookFormatConverter.importFromJson(path);
                logger.info(String.format("Importing from JSON + %s, %s", path , importedData));
            } else if (filetype.equalsIgnoreCase("csv")) {
                importedData = AddressBookFormatConverter.importFromCsv(path);
                logger.info(String.format("Importing from CSV + %s, %s", path , importedData));
            } else {
                throw new CommandException(MESSAGE_INVALID_FILETYPE);
            }

            // Handle different import modes
            if (mode.equalsIgnoreCase("overwrite")) {
                return handleOverwriteMode(model, importedData);
            } else if (mode.equalsIgnoreCase("append")) {
                return handleAppendMode(model, importedData);
            } else {
                throw new CommandException(MESSAGE_INVALID_MODE);
            }
        } catch (IOException e) {
            throw new CommandException("Error reading file: " + e.getMessage());
        } catch (DataLoadingException e) {
            throw new CommandException("Error loading data: " + e.getMessage());
        }
    }

    /**
     * Handles the overwrite mode by replacing the entire address book.
     */
    private CommandResult handleOverwriteMode(Model model, JsonSerializableAddressBook importedData)
            throws CommandException {
        try {
            model.setAddressBook(importedData.toModelType());
            int importedCount = model.getAddressBook().getPersonList().size();
            return new CommandResult(String.format(MESSAGE_SUCCESS_OVERWRITE, importedCount));
        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_DATA, e.getMessage()));
        }
    }

    /**
     * Handles the append mode by adding persons individually, showing the skipped persons at the end.
     */
    private CommandResult handleAppendMode(Model model, JsonSerializableAddressBook importedData)
            throws CommandException {
        List<List<Person>> importStats = processImportedPersonsWhenAppend(model, importedData);
        int importedCount = importStats.get(0).size();
        List<Person> skippedList = importStats.get(1);
        int skippedCount = skippedList.size();

        // Build skipped details string
        StringBuilder skippedDetails = new StringBuilder();
        if (skippedCount > 0) {
            skippedDetails.append("\nSkipped contacts:");
            for (Person person : skippedList) {
                skippedDetails.append(String.format("\n- %s (%s)",
                        person.getName(), person.getEmployeeId()));
            }
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS_APPEND,
                importedCount, skippedCount, skippedDetails));
    }

    /**
     * Processes each person in the imported data, adding them to the model if valid.
     * the anniversary list is appended if the person already exists in the model.
     * Returns a list of unsuccessfully imported persons.
     */
    private List<List<Person>> processImportedPersonsWhenAppend(Model model, JsonSerializableAddressBook importedData)
            throws CommandException {
        List<Person> importedPersons = new ArrayList<>();
        List<Person> omittedPersons = new ArrayList<>();
        try {
            for (Person person : importedData.toModelType().getPersonList()) {
                Person matchInModel = model.getFilteredByEmployeeIdPrefixList(person.getEmployeeId()).stream()
                        .filter(p -> p.isSamePerson(person))
                        .findFirst()
                        .orElse(null);
                // model does not have eid of the person
                if (matchInModel == null) {
                    model.addPerson(person);
                    importedPersons.add(person);

                } else if (matchInModel.hasSameDetails(person)) {
                    // model has the person and have same details, then append the anniversarylist
                    matchInModel.getAnniversaries().addAll(person.getAnniversaries());
                } else {
                    // there is a conflict. ask user to resolve
                    omittedPersons.add(person);
                }
            }
            logger.info(String.format("omitted persons, %s", omittedPersons));
            return List.of(importedPersons, omittedPersons);
        } catch (IllegalValueException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_DATA, e.getMessage()));
        }
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ImportCommand otherCommand)) {
            return false;
        }
        return filetype.equals(otherCommand.filetype)
                && path.equals(otherCommand.path)
                && mode.equals(otherCommand.mode);
    }
}
