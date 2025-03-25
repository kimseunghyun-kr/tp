package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FILETYPE;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.nio.file.Path;
import java.nio.file.Paths;

import seedu.address.logic.commands.importexport.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;



/**
 * Utility class to resolve file paths.
 */
public class FilePathResolverUtils {

    /**
     * Resolves the final file path based on optional filepath, filename, and fileType.
     * If both filepath and filename include filenames, throws an error.
     * If extension does not match fileType, appends the correct extension.
     */
    public static Path resolveFilePath(String filePath, String filename, String fileType) throws ParseException {
        Path path = null;

        // Normalize fileType extension
        String expectedExtension = "." + fileType.toLowerCase();

        if (filePath != null) {
            Path filePathObj = Paths.get(filePath);
            boolean filePathHasFilename = looksLikeFile(filePathObj);

            if (filePathHasFilename && filename != null) {
                throw new ParseException("Provide either a full file path or a filename, not both.");
            }

            if (!filePathHasFilename && filename != null) {
                filename = ensureCorrectExtension(filename, expectedExtension);
                path = filePathObj.resolve(filename);
            } else if (filePathHasFilename) {
                String actualName = filePathObj.getFileName().toString();
                if (!actualName.toLowerCase().endsWith(expectedExtension)) {
                    path = Paths.get(filePath + expectedExtension); // auto-fix extension
                } else {
                    path = filePathObj;
                }
            } else {
                // filePath is a folder and filename is null → use default
                throw new ParseException("Filename must be provided if path is just a directory.");
            }
        } else if (filename != null) {
            filename = ensureCorrectExtension(filename, expectedExtension);
            path = Paths.get(filename);
        } else {
            throw new IllegalArgumentException("❗ Either file path or filename must be provided.");
        }

        return path;
    }

    private static boolean looksLikeFile(Path path) {
        String name = path.getFileName().toString();
        return name.contains(".") && !name.endsWith(".");
    }

    private static String ensureCorrectExtension(String name, String requiredExt) {
        if (!name.toLowerCase().endsWith(requiredExt)) {
            return name + requiredExt;
        }
        return name;
    }

    /**
     * Verifies that the file type is present and valid.
     * @param argMultimap Contains user arguments.
     * @param blank A blank string.
     * @throws ParseException If the file type is missing or invalid.
     */
    public static void verifyFileTypePresentAndValid(ArgumentMultimap argMultimap, String blank) throws ParseException {
        if (!arePrefixesPresent(argMultimap, PREFIX_FILETYPE)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }
        if (!argMultimap.getValue(PREFIX_FILETYPE).orElse(blank).equals("json")
                && !argMultimap.getValue(PREFIX_FILETYPE).orElse(blank).equals("csv")) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }
    }

}
