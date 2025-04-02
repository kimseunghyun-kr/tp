package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import seedu.address.logic.commands.AddEmployeeCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains helper methods for testing command parsers.
 */
public class CommandParserTestUtil {

    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is successful and the command created
     * equals to {@code expectedCommand}.
     */
    public static void assertParseSuccess(Parser<? extends Command> parser, String userInput,
            Command expectedCommand) {
        try {
            Command command = parser.parse(userInput);
            assertEquals(expectedCommand, command);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is successful and the command created
     * equals to {@code expectedCommand}.
     */
    public static void assertFieldEqualityFirst(Parser<? extends Command> parser, String userInput,
                                          Command expectedCommand) {
        try {
            Command command = parser.parse(userInput);
            assertFieldEqualityFirst(expectedCommand, command);
        } catch (ParseException pe) {
            throw new IllegalArgumentException("Invalid userInput.", pe);
        }
    }

    /**
     * Asserts that the {@code command} equals to {@code expectedCommand} in Fields before comparing equals
     */
    public static void assertFieldEqualityFirst(Command expectedCommand, Command command) {
        if (command instanceof AddEmployeeCommand && expectedCommand instanceof AddEmployeeCommand) {
            // Compare while ignoring employeeId
            assertTrue(((AddEmployeeCommand) expectedCommand).hasSameDetails((AddEmployeeCommand) command),
                    "Commands do not match when ignoring employeeId.");
        } else if (command instanceof EditCommand && expectedCommand instanceof EditCommand) {
            assertTrue(((EditCommand) expectedCommand).hasSameDetails((EditCommand) command),
                    "Commands do not match when ignoring employeeId.");
        } else {
            assertEquals(expectedCommand, command);
        }
    }


    /**
     * Asserts that the parsing of {@code userInput} by {@code parser} is unsuccessful and the error message
     * equals to {@code expectedMessage}.
     */
    public static void assertParseFailure(Parser<? extends Command> parser, String userInput, String expectedMessage) {
        try {
            parser.parse(userInput);
            throw new AssertionError("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(expectedMessage, pe.getMessage());
        }
    }
}
