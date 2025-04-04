package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.anniversary.DeleteAnniversaryCommand;
import seedu.address.logic.parser.anniversary.DeleteAnniversaryCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.EmployeeId;

public class DeleteAnniversaryCommandParserTest {
    private static final String PREFIX_EID_PARSABLE = " " + PREFIX_EMPLOYEEID;
    private DeleteAnniversaryCommandParser parser;

    @BeforeEach
    public void setUp() {
        parser = new DeleteAnniversaryCommandParser();
    }

    /**
     * Valid equivalence partition: proper employee id and anniversary index.
     * Boundary: minimal valid anniversary index ("1").
     */
    @Test
    public void parse_validInput_success() throws ParseException {
        String userInput = PREFIX_EID_PARSABLE + "00000000-0000-0000-0000-000000000001 "
                + PREFIX_ANNIVERSARY_INDEX + "1";
        DeleteAnniversaryCommand command = parser.parse(userInput);
        assertEquals(EmployeeId.fromString("00000000-0000-0000-0000-000000000001"),
                command.getEmployeeIdPrefix());
        assertEquals(Index.fromOneBased(1), command.getTargetIndex());
    }

    @Test
    public void parse_missingEmployeeId_throwsParseException() {
        String userInput = PREFIX_ANNIVERSARY_INDEX + "1";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAnniversaryCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "00000000-0000-0000-0000-000000000001";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteAnniversaryCommand.MESSAGE_USAGE), ex.getMessage());
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "00000000-0000-0000-0000-000000000001 "
                + PREFIX_ANNIVERSARY_INDEX + "abc";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    @Test
    public void parse_extraArguments_stillValidOrThrowsParseException() {
        // If the parser strictly validates extra args, this may throw ParseException. Adjust as needed.
        String userInput = PREFIX_EID_PARSABLE + "00000000-0000-0000-0000-000000000001 "
                + PREFIX_ANNIVERSARY_INDEX + "1 extra/ignored";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

        /**
         * Valid equivalence partition: input with extra whitespace.
         * Boundary: tests that trimming is correctly applied.
         */
        @Test
        public void parse_validInput_extraWhitespace_success() throws Exception {
            String input = "   eid/E123    ai/1   ";
            DeleteAnniversaryCommand expectedCommand = new DeleteAnniversaryCommand(
                    Index.fromOneBased(1), EmployeeId.fromString("E123"));
            DeleteAnniversaryCommand command = parser.parse(input);
            assertEquals(expectedCommand, command);
        }

        /**
         * Valid equivalence partition: prefixes provided in a different order.
         * The parser should correctly tokenize and parse regardless of order.
         */
        @Test
        public void parse_validInput_differentOrder_success() throws Exception {
            String input = "ai/1 eid/E123";
            DeleteAnniversaryCommand expectedCommand = new DeleteAnniversaryCommand(
                    Index.fromOneBased(1), EmployeeId.fromString("E123"));
            DeleteAnniversaryCommand command = parser.parse(input);
            assertEquals(expectedCommand, command);
        }

        /**
         * Invalid equivalence partition: missing employee id prefix.
         * Boundary: input with only the anniversary index.
         */
        @Test
        public void parse_missingEmployeeIdPrefix_throwsParseException() {
            String input = "ai/1";
            assertThrows(ParseException.class, () -> parser.parse(input));
        }

        /**
         * Invalid equivalence partition: missing anniversary index prefix.
         * Boundary: input with only the employee id.
         */
        @Test
        public void parse_missingAnniversaryIndex_throwsParseException() {
            String input = "eid/E123";
            assertThrows(ParseException.class, () -> parser.parse(input));
        }

        /**
         * Invalid equivalence partition: employee id that contains a space.
         * This tests the rule that employee ids must not contain internal spaces.
         */
        @Test
        public void parse_invalidEmployeeId_throwsParseException() {
            String input = "eid/E 123 ai/1";
            assertThrows(ParseException.class, () -> parser.parse(input));
        }

        /**
         * Invalid equivalence partitions: anniversary index is invalid.
         * Boundaries: "0" (not a non-zero unsigned integer) and non-numeric input.
         */
        @Test
        public void parse_invalidAnniversaryIndex_throwsParseException() {
            String inputZero = "eid/E123 ai/0";
            String inputNonNumeric = "eid/E123 ai/abc";
            assertThrows(ParseException.class, () -> parser.parse(inputZero));
            assertThrows(ParseException.class, () -> parser.parse(inputNonNumeric));
        }

        /**
         * (Optional) Null input test.
         * Although command parsers usually assume non-null inputs,
         * this verifies that a null input triggers a NullPointerException.
         */
        @Test
        public void parse_nullInput_throwsNullPointerException() {
            String input = null;
            assertThrows(NullPointerException.class, () -> parser.parse(input));
        }
    }


