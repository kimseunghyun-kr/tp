package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EMPLOYEE;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_JOBPOSITION = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";
    private static final String INVALID_DATE = "2020-20-20";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_JOBPOSITION = "Hiring Manager";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_DATE = "2020-10-10";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_EMPLOYEE, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_EMPLOYEE, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parseJobPosition_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseJobPosition((String) null));
    }

    @Test
    public void parseJobPosition_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseJobPosition(INVALID_JOBPOSITION));
    }

    @Test
    public void parsejobPosition_validValueWithoutWhitespace_returnsJobPosition() throws Exception {
        JobPosition expectedjobPosition = new JobPosition(VALID_JOBPOSITION);
        assertEquals(expectedjobPosition, ParserUtil.parseJobPosition(VALID_JOBPOSITION));
    }

    @Test
    public void parsejobPosition_validValueWithWhitespace_returnsTrimmedJobPosition() throws Exception {
        String jobPositionWithWhitespace = WHITESPACE + VALID_JOBPOSITION + WHITESPACE;
        JobPosition expectedjobPosition = new JobPosition(VALID_JOBPOSITION);
        assertEquals(expectedjobPosition, ParserUtil.parseJobPosition(jobPositionWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    /**
     * EP: Valid string within safe content and length limits
     */
    @Test
    public void validateSafeContent_validInput_success() throws Exception {
        ParserUtil.validateSafeContent("Valid input name", "test field", false, false);
    }

    /**
     * EP: Null input with isNullPossible = true → allowed
     */
    @Test
    public void validateSafeContent_nullInputAllowed_success() throws Exception {
        ParserUtil.validateSafeContent(null, "optional field", true , false);
        ParserUtil.validateSafeContent(null, "optional field", true , true);
    }

    /**
     * EP: Null input with isNullPossible = false → should fail
     */
    @Test
    public void validateSafeContent_nullInputNotAllowed_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent(null, "required field", false, true));
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent(null, "required field", false, false));
    }

    /**
     * EP: Empty input string with isNullPossible = false → should fail
     */
    @Test
    public void validateSafeContent_emptyInputNotAllowed_throwsParseException() {
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent("   ", "required field", true, false));
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent("   ", "required field", false, false));
    }

    /**
     * BVA: Input exactly at MAX_ALLOWED_LENGTH → should pass
     */
    @Test
    public void validateSafeContent_maxLength_success() throws Exception {
        String input = "a".repeat(ParserUtil.MAX_ALLOWED_LENGTH);
        ParserUtil.validateSafeContent(input, "boundary field", false, false);
    }

    /**
     * BVA: Input exceeding MAX_ALLOWED_LENGTH → should fail
     */
    @Test
    public void validateSafeContent_exceedsMaxLength_throwsParseException() {
        String input = "a".repeat(ParserUtil.MAX_ALLOWED_LENGTH + 1);
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent(input, "length field", false, false));
    }

    /**
     * EP: Input containing dangerous content (e.g., HTML/JS injection) → should fail
     */
    @Test
    public void validateSafeContent_dangerousContent_throwsParseException() {
        String input = "<script>alert('x')</script>";
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent(input, "sanitization field", false, false));
    }

    /**
     * EP: Input containing control characters → should fail
     */
    @Test
    public void validateSafeContent_controlCharacters_throwsParseException() {
        String input = "Hello\u0007World"; // Bell character (non-printable)
        assertThrows(ParseException.class, () ->
                ParserUtil.validateSafeContent(input, "control char field", false, false));
    }

}
