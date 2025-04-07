package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ReminderCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains JUnit tests for {@code ReminderCommandParser}.
 */
public class ReminderCommandParserTest {

    private final ReminderCommandParser parser = new ReminderCommandParser();

    @Test
    public void parse_emptyArgs_returnsReminderCommand() throws ParseException {
        ReminderCommand command = parser.parse("");
        assertTrue(command instanceof ReminderCommand);
    }

    @Test
    public void parse_whitespaceArgs_returnsReminderCommand() throws ParseException {
        ReminderCommand command = parser.parse("   \t  ");
        assertTrue(command instanceof ReminderCommand);
    }

    @Test
    public void parse_extraArgs_returnsReminderCommand() throws ParseException {
        ReminderCommand command = parser.parse("something here");
        assertTrue(command instanceof ReminderCommand);
    }

    @Test
    public void parse_numericArgs_returnsReminderCommand() throws ParseException {
        ReminderCommand command = parser.parse("123 456");
        assertTrue(command instanceof ReminderCommand);
    }

    @Test
    public void parse_gibberishArgs_returnsReminderCommand() throws ParseException {
        ReminderCommand command = parser.parse("!@#$%^&*()_+=[]{}");
        assertTrue(command instanceof ReminderCommand);
    }

    @Test
    public void parse_longArgs_returnsReminderCommand() throws ParseException {
        ReminderCommand command = parser.parse("this is a very long string with many words to check if it still works fine");
        assertTrue(command instanceof ReminderCommand);
    }

    @Test
    public void parse_nullOrEdgeCase_returnsReminderCommand() {
        assertDoesNotThrow(() -> parser.parse(" "));
        assertDoesNotThrow(() -> parser.parse("reminder test"));
    }
}
