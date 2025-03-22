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
}
