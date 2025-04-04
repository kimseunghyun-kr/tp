package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.AnniversaryParserUtils.INVALID_ANNIVERSARY;
import static seedu.address.logic.parser.AnniversaryParserUtils.MESSAGE_DATE_CONSTRAINTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.anniversary.AddAnniversaryCommand;
import seedu.address.logic.parser.anniversary.AddAnniversaryCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.EmployeeId;

public class AddAnniversaryCommandParserTest {

    // Constants for valid input
    private static final String PREFIX_EID_PARSABLE = " " + PREFIX_EMPLOYEEID;
    private static final String VALID_EMPLOYEE_ID = "00000000-0000-0000-0000-000000000001";
    private static final String VALID_DATE = "2025-03-13";
    private static final String VALID_NAME = "anniversary name";
    private static final String VALID_TYPE = "Personal";
    private static final String VALID_DESCRIPTION = "anniversary description";
    private static final String VALID_TYPE_DESC = "anniversary type description";

    private AddAnniversaryCommandParser parser;
    private EmployeeId validEmployeeId;

    @BeforeEach
    public void setUp() throws Exception {
        parser = new AddAnniversaryCommandParser();
        validEmployeeId = EmployeeId.fromString(VALID_EMPLOYEE_ID);
    }

    /**
     * EP: Standard valid input with all fields present
     * @throws Exception
     */
    @Test
    public void parse_validInput_returnsAddAnniversaryCommand() throws Exception {
        String userInput = PREFIX_EID_PARSABLE + "00000000-0000-0000-0000-000000000001 "
                + PREFIX_ANNIVERSARY_DATE + "2025-03-13 "
                + PREFIX_ANNIVERSARY_NAME + "BirthdayName "
                + PREFIX_ANNIVERSARY_TYPE + "Personal "
                + PREFIX_ANNIVERSARY_TYPE_DESC + "Family "
                + PREFIX_ANNIVERSARY_DESC + "Sample Description";
        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();
        assertEquals("BirthdayName", anniv.getName());
        assertEquals("Sample Description", anniv.getDescription());
    }

    /**
     * All optional fields included (name, type, desc, typeDesc)
     * @throws Exception
     */
    @Test
    public void parse_allFieldsPresent_returnsAddAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_ANNIVERSARY_DESC + VALID_DESCRIPTION + " "
                + PREFIX_ANNIVERSARY_TYPE_DESC + VALID_TYPE_DESC;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(VALID_NAME, anniv.getName());
        assertEquals(VALID_DESCRIPTION, anniv.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anniv.getDate());
        assertEquals(VALID_TYPE, anniv.getType().getName());
        assertEquals(VALID_TYPE_DESC, anniv.getType().getDescription());
        assertEquals(validEmployeeId, cmd.getEmployeeIdPrefix());
    }

    /**
     * EP: Omits optional fields (desc, typeDesc)
     * @throws Exception
     */
    @Test
    public void parse_optionalFieldsOmitted_returnsAddAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(VALID_NAME, anniv.getName());
        assertEquals("", anniv.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anniv.getDate());
        assertEquals(VALID_TYPE, anniv.getType().getName());
        assertEquals("", anniv.getType().getDescription());
    }

    /**
     * EP: Birthday shortcut mode (auto-generates type and desc)
     * @throws Exception
     */
    @Test
    public void parse_birthdayAnniversary_returnsAddAnniversaryCommand() throws Exception {
        String personName = "hong gil dong";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_BIRTHDAY + VALID_DATE + " "
                + PREFIX_NAME + personName;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(personName + "'s Birthday", anniv.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anniv.getDate());
        assertEquals("Birthday", anniv.getType().getName());
    }

    /**
     * EP: Work anniversary shortcut (also auto-generates)
     * @throws Exception
     */
    @Test
    public void parse_workAnniversary_returnsAddAnniversaryCommand() throws Exception {
        String personName = "hong gil dong";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_WORK_ANNIVERSARY + VALID_DATE + " "
                + PREFIX_NAME + personName;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(personName + "'s work anniversary", anniv.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anniv.getDate());
        assertEquals("Work Anniversary", anniv.getType().getName());
    }

    /**
     * EP: Valid input in different field order
     * @throws Exception
     */
    @Test
    public void parse_differentFieldOrder_returnsAddAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(VALID_NAME, anniv.getName());
        assertEquals(LocalDate.parse(VALID_DATE), anniv.getDate());
    }

    /**
     * EP: Valid input with extra whitespace between tokens
     * @throws Exception
     */
    @Test
    public void parse_extraWhitespace_returnsAddAnniversaryCommand() throws Exception {
        String userInput = "  " + PREFIX_EMPLOYEEID + "  " + VALID_EMPLOYEE_ID + "  "
                + PREFIX_ANNIVERSARY_DATE + "  " + VALID_DATE + "  "
                + PREFIX_ANNIVERSARY_NAME + "  " + VALID_NAME + "  "
                + PREFIX_ANNIVERSARY_TYPE + "  " + VALID_TYPE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(VALID_NAME, anniv.getName());
        assertEquals(LocalDate.parse(VALID_DATE), anniv.getDate());
    }

    /**
     * EP: Valid input description at max allowed length (1000 chars)
     * @throws Exception
     */
    @Test
    public void parse_longDescription_returnsAddAnniversaryCommand() throws Exception {
        String longDescription = "this is less than 1000 characters".repeat(20);
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_ANNIVERSARY_DESC + longDescription;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(longDescription, anniv.getDescription());
    }

    /**
     * EP: Special characters allowed in name field
     * @throws Exception
     */
    @Test
    public void parse_specialCharactersInName_returnsAddAnniversaryCommand() throws Exception {
        String specialName = "특별한 기념일! @#$%";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + specialName + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anniv = cmd.getToAdd();

        assertEquals(specialName, anniv.getName());
    }

    // *******************************
    // Invalid Input Tests
    // *******************************

    /**
     * EP: Missing required field - employee ID
     */
    @Test
    public void parse_missingEmployeeId_throwsParseException() {
        String userInput = " " + PREFIX_ANNIVERSARY_DATE + "2025-03-13 "
                + PREFIX_NAME + "MissingEid "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    /**
     * EP: Missing required field - date
     */
    @Test
    public void parse_missingDate_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_NAME + "NoDate "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    /**
     * EP: Missing required field - name
     */
    @Test
    public void parse_missingName_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "2025-01-01 "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }

    /**
     * EP + Format validation: Completely invalid date, malformed format, and invalid day (Feb 30)
     */
    @Test
    public void parse_invalidDate_throwsParseException() {
        // Test with completely invalid date string
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "invaliddate "
                + PREFIX_NAME + "InvalidDate "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        String finalUserInput = userInput;
        assertThrows(ParseException.class, () -> parser.parse(finalUserInput));

        // Test with incorrect format and invalid calendar date
        userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + "2025/03/13" + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;
        String finalUserInput2 = userInput;
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(finalUserInput2));
        assertEquals(MESSAGE_DATE_CONSTRAINTS, exception.getMessage());

        userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + "2025-02-30" + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;
        String finalUserInput1 = userInput;
        exception = assertThrows(ParseException.class, () -> parser.parse(finalUserInput1));
        assertEquals(MESSAGE_DATE_CONSTRAINTS, exception.getMessage());
    }

    /**
     * EP: Missing anniversary type
     */
    @Test
    public void parse_missingAnniversaryType_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "2025-02-25 "
                + PREFIX_NAME + "NoType ";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
    /**
     * EP: Too many anniversary type fields (logical conflict)
     */
    @Test
    public void parse_tooManyAnniversaryType_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "2025-03-25 "
                + PREFIX_NAME + "TooManyTypes "
                + PREFIX_ANNIVERSARY_TYPE + "One "
                + PREFIX_ANNIVERSARY_TYPE + "Two "
                + PREFIX_ANNIVERSARY_TYPE + "Three";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, INVALID_ANNIVERSARY), ex.getMessage());
    }
    /**
     * EP: Completely invalid format (garbage input)
     */
    @Test
    public void parse_invalidFormat_throwsParseException() {
        String userInput = "some_invalid_input";
        ParseException ex = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAnniversaryCommand.MESSAGE_USAGE),
                ex.getMessage());
    }
    /**
     * Format validation: Space-containing date token
     */
    @Test
    public void parse_invalidAnniversaryDateWithSpaces_throwsParseException() {
        String userInput = PREFIX_EID_PARSABLE + "abc "
                + PREFIX_ANNIVERSARY_DATE + "invalid_date "
                + PREFIX_NAME + "AnnivName "
                + PREFIX_ANNIVERSARY_TYPE + "Personal";
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
    /**
     * Conflict case: Both birthday and work anniversary specified
     */
    @Test
    public void parse_birthdayAndWorkAnniversaryTogether_throwsException() {
        String personName = "hong gil dong";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_BIRTHDAY + VALID_DATE + " "
                + PREFIX_WORK_ANNIVERSARY + VALID_DATE + " "
                + PREFIX_NAME + personName;
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals("birthday cannot be created with work anniversary for addanniversaryCommand.",
                exception.getMessage());
    }
    /**
     * Conflict case: Custom + birthday/work fields present together
     */
    @Test
    public void parse_customWithBirthdayOrWork_throwsException() {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_BIRTHDAY + VALID_DATE + " "
                + PREFIX_NAME + "hong-gil-dong";
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals("custom anniversaries cannot be used with birthday or work anniversary.",
                exception.getMessage());
    }
    /**
     * BVA: Description exceeds max (1001 chars)
     */
    @Test
    public void parse_tooLongDescription_throwsParseException() {
        String tooLongDescription = "X".repeat(1001);
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_ANNIVERSARY_DESC + tooLongDescription;
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals("The anniversary description is too long (max 1000 characters).",
                exception.getMessage());
    }
    /**
     * Security/Injection: Dangerous SQL-like input
     */
    @Test
    public void parse_dangerousInput_throwsParseException() {
        String dangerousName = "DROP TABLE employees;";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + dangerousName + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(userInput));
        assertEquals("The anniversary name contains potentially dangerous content.",
                exception.getMessage());
    }
    /**
     * EP: Required fields present but name content is empty
     */
    @Test
    public void parse_emptyFields_throwsParseException() {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + " " + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;
        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
