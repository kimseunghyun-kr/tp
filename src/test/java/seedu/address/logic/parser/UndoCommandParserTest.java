package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.address.logic.parser.UndoCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.commands.UndoCommand;

class UndoCommandParserTest {

    private UndoCommandParser parser;

    @BeforeEach
    void setUp() {
        parser = new UndoCommandParser(); // Initialize the UndoCommandParser
    }

    @Test
    void parse_emptyArgs_returnsUndoCommand() throws ParseException {
        // Test case where args is an empty string
        String args = "";
        UndoCommand command = parser.parse(args);

        // Assert that the correct command instance is returned
        assertNotNull(command);
        assertTrue(command instanceof UndoCommand); // Ensure the returned command is of type UndoCommand
    }

    @Test
    void parse_nonEmptyArgs_throwsParseException() {
        // Test case where args is non-empty
        String args = "extra argument";  // Any extra string should throw an exception

        // Assert that a ParseException is thrown
        assertThrows(ParseException.class, () -> parser.parse(args));
    }
}
