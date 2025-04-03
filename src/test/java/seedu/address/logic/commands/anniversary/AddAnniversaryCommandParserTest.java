package seedu.address.logic.commands.anniversary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

import seedu.address.logic.parser.anniversary.AddAnniversaryCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.EmployeeId;

public class AddAnniversaryCommandParserTest {
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

    @Test
    public void parse_allFieldsPresent_returnsAddAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_ANNIVERSARY_DESC + VALID_DESCRIPTION + " "
                + PREFIX_ANNIVERSARY_TYPE_DESC + VALID_TYPE_DESC;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(VALID_NAME, anni.getName());
        assertEquals(VALID_DESCRIPTION, anni.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anni.getDate());
        assertEquals(VALID_TYPE, anni.getType().getName());
        assertEquals(VALID_TYPE_DESC, anni.getType().getDescription());
        assertEquals(validEmployeeId, cmd.getEmployeeIdPrefix());
    }

    @Test
    public void parse_optionalFieldsOmitted_returnsAddAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(VALID_NAME, anni.getName());
        assertEquals("", anni.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anni.getDate());
        assertEquals(VALID_TYPE, anni.getType().getName());
        assertEquals("", anni.getType().getDescription());
    }

    @Test
    public void parse_birthdayAnniversary_returnsAddAnniversaryCommand() throws Exception {
        // 생일 기념일 파싱 테스트
        String personName = "hong gil dong";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_BIRTHDAY + VALID_DATE + " "
                + PREFIX_NAME + personName;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(personName + "'s Birthday", anni.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anni.getDate());
        assertEquals("Birthday", anni.getType().getName());
    }

    @Test
    public void parse_workAnniversary_returnsAddAnniversaryCommand() throws Exception {
        String personName = "hong gil dong";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_WORK_ANNIVERSARY + VALID_DATE + " "
                + PREFIX_NAME + personName;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(personName + "'s work anniversary", anni.getDescription());
        assertEquals(LocalDate.parse(VALID_DATE), anni.getDate());
        assertEquals("Work Anniversary", anni.getType().getName());
    }

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

    @Test
    public void parse_differentFieldOrder_returnsAddAnniversaryCommand() throws Exception {
        String userInput = " " + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(VALID_NAME, anni.getName());
        assertEquals(LocalDate.parse(VALID_DATE), anni.getDate());
    }

    @Test
    public void parse_extraWhitespace_returnsAddAnniversaryCommand() throws Exception {
        // 추가 공백이 있는 경우
        String userInput = "  " + PREFIX_EMPLOYEEID + "  " + VALID_EMPLOYEE_ID + "  "
                + PREFIX_ANNIVERSARY_DATE + "  " + VALID_DATE + "  "
                + PREFIX_ANNIVERSARY_NAME + "  " + VALID_NAME + "  "
                + PREFIX_ANNIVERSARY_TYPE + "  " + VALID_TYPE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(VALID_NAME, anni.getName());
        assertEquals(LocalDate.parse(VALID_DATE), anni.getDate());
    }

    @Test
    public void parse_invalidDate_throwsParseException() {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + "2025/03/13" + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        String finalUserInput = userInput;
        ParseException exception = assertThrows(ParseException.class, () -> parser.parse(finalUserInput));
        assertEquals(MESSAGE_DATE_CONSTRAINTS, exception.getMessage());

        userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + "2025-02-30" + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        String finalUserInput1 = userInput;
        exception = assertThrows(ParseException.class, () -> parser.parse(finalUserInput1));
        assertEquals(MESSAGE_DATE_CONSTRAINTS, exception.getMessage());
    }

    @Test
    public void parse_longDescription_returnsAddAnniversaryCommand() throws Exception {
        String longDescription = "this is less than 1000 characters"
                .repeat(20);
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + VALID_NAME + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE + " "
                + PREFIX_ANNIVERSARY_DESC + longDescription;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(longDescription, anni.getDescription());
    }

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

    @Test
    public void parse_specialCharactersInName_returnsAddAnniversaryCommand() throws Exception {
        String specialName = "특별한 기념일! @#$%";
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + specialName + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        AddAnniversaryCommand cmd = parser.parse(userInput);
        Anniversary anni = cmd.getToAdd();

        assertEquals(specialName, anni.getName());
    }

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

    @Test
    public void parse_emptyFields_throwsParseException() {
        String userInput = " " + PREFIX_EMPLOYEEID + VALID_EMPLOYEE_ID + " "
                + PREFIX_ANNIVERSARY_DATE + VALID_DATE + " "
                + PREFIX_ANNIVERSARY_NAME + " " + " "
                + PREFIX_ANNIVERSARY_TYPE + VALID_TYPE;

        assertThrows(ParseException.class, () -> parser.parse(userInput));
    }
}
