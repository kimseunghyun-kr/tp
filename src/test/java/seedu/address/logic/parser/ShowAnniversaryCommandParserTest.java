package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.anniversary.ShowAnniversaryCommand;
import seedu.address.logic.parser.anniversary.ShowAnniversaryCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;

public class ShowAnniversaryCommandParserTest {

    private final ShowAnniversaryCommandParser parser = new ShowAnniversaryCommandParser();
    private final String employeeId = "0c2414da-fafb-4e05-b4f7-befb22385381";

    @Test
    public void parse_validArgs_returnsShowAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_EMPLOYEEID + " " + employeeId;
        ShowAnniversaryCommand result = parser.parse(userInput);

        assertEquals(new ShowAnniversaryCommand(employeeId), result);
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        String userInput = " " + employeeId;

        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowAnniversaryCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }

    //Solve in another branch
    @Disabled
    @Test
    public void parse_emptyEidValue_throwsParseException() {
        String userInput = " " + PREFIX_EMPLOYEEID + " ";
        ParseException thrown = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ShowAnniversaryCommand.MESSAGE_USAGE),
                thrown.getMessage());
    }
}
