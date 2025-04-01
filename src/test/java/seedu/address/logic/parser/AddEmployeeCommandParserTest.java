package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.BIRTHDAY_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_BIRTHDAY_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_JOBPOSTION_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_WORK_ANNIVERSARY_DATE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.JOB_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.JOB_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOBPOSITION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.WORK_ANNIVERSARY_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.WORK_ANNIVERSARY_DESC_BOB;
import static seedu.address.logic.parser.AnniversaryParserUtils.MESSAGE_DATE_CONSTRAINTS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertFieldEqualityFirst;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.AMY;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddEmployeeCommand;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EmployeeBuilder;

public class AddEmployeeCommandParserTest {
    private AddEmployeeCommandParser parser = new AddEmployeeCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Employee expectedEmployee = new EmployeeBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertFieldEqualityFirst(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + JOB_DESC_BOB + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                new AddEmployeeCommand(expectedEmployee));

        // multiple tags - all accepted
        Employee expectedEmployeeMultipleTags = new EmployeeBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertFieldEqualityFirst(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB + TAG_DESC_HUSBAND
                        + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                new AddEmployeeCommand(expectedEmployeeMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedEmployeeString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + JOB_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, JOB_DESC_AMY + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_JOBPOSITION));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedEmployeeString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + JOB_DESC_AMY
                        + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME,
                        PREFIX_JOBPOSITION, PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_JOBPOSTION_DESC + validExpectedEmployeeString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_JOBPOSITION));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedEmployeeString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedEmployeeString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedEmployeeString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedEmployeeString + INVALID_JOBPOSTION_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_JOBPOSITION));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Employee expectedEmployee = new EmployeeBuilder(AMY).withTags().build();
        assertFieldEqualityFirst(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + JOB_DESC_AMY
                + BIRTHDAY_DESC_AMY + WORK_ANNIVERSARY_DESC_AMY,
                new AddEmployeeCommand(expectedEmployee));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + JOB_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_JOBPOSITION_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_JOBPOSITION_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + JOB_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + JOB_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_JOBPOSTION_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                JobPosition.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_JOBPOSTION_DESC
                + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB, Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + JOB_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + WORK_ANNIVERSARY_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));

        //invalid birthday date
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + INVALID_BIRTHDAY_DATE_DESC + WORK_ANNIVERSARY_DESC_BOB,
                MESSAGE_DATE_CONSTRAINTS);

        //invalid work anniversary date
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + JOB_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND + BIRTHDAY_DESC_BOB + INVALID_WORK_ANNIVERSARY_DATE_DESC,
                MESSAGE_DATE_CONSTRAINTS);
    }
}
