package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;


import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Name;


/**
 * Unit tests for {@link AnniversaryParserUtils}.
 * Applies equivalence partitioning (EP) and boundary value analysis (BVA) to verify:
 * - Standard anniversary parsing
 * - Birthday/work anniversary recognition
 * - Date formatting
 * - Prefix presence/absence and error handling
 */
@MockitoSettings()
public class AnniversaryParserUtilsTest {

    /**
     * Tests that a standard anniversary with all fields present is parsed correctly.
     */
    @Test
    public void parseStandardAnniversary_validFields_success() throws Exception {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                " an/Birthday d/2025-04-02 at/Personal atdesc/Family ad/Remember to bring cake",
                PREFIX_ANNIVERSARY_NAME, PREFIX_ANNIVERSARY_DATE, PREFIX_ANNIVERSARY_TYPE,
                PREFIX_ANNIVERSARY_TYPE_DESC, PREFIX_ANNIVERSARY_DESC
        );

        // When
        Anniversary result = AnniversaryParserUtils.resolveAnniversaryInput(map);

        // Then
        assertEquals("Birthday", result.getName());
        assertEquals("Remember to bring cake", result.getDescription());
        assertEquals("Personal", result.getType().getName());
        assertEquals("Family", result.getType().getDescription());
    }

    /**
     * Tests that a birthday prefix with name parses into a proper Birthday anniversary.
     */
    @Test
    public void parseBirthday_validFields_success() throws Exception {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(" bd/1990-05-10 n/Alice", PREFIX_BIRTHDAY, PREFIX_NAME);

        // When
        Anniversary result = AnniversaryParserUtils.resolveAnniversaryInput(map);

        // Then
        assertEquals("Alice's Birthday", result.getDescription());
        assertEquals("Birthday", result.getName());
    }

    /**
     * Tests that a work anniversary prefix with name parses into a proper WorkAnniversary.
     */
    @Test
    public void parseWorkAnniversary_validFields_success() throws Exception {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(" wa/2020-09-01 n/Bob", PREFIX_WORK_ANNIVERSARY, PREFIX_NAME);

        // When
        Anniversary result = AnniversaryParserUtils.resolveAnniversaryInput(map);

        // Then
        assertEquals("Bob's work anniversary", result.getDescription());
        assertEquals("work anniversary", result.getName());
    }

    /**
     * Tests that providing both a standard anniversary and a birthday causes a conflict.
     */
    @Test
    public void resolveAnniversaryInput_conflictingTypes_throwsParseException() {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                " an/Birthday d/2025-04-02 at/Personal bd/1990-01-01 n/Alice",
                PREFIX_ANNIVERSARY_NAME, PREFIX_ANNIVERSARY_DATE, PREFIX_ANNIVERSARY_TYPE,
                PREFIX_BIRTHDAY, PREFIX_NAME
        );

        // When + Then
        assertThrows(ParseException.class, () -> AnniversaryParserUtils.resolveAnniversaryInput(map));
    }

    /**
     * Tests that an empty argument map (no recognized prefixes) throws a ParseException.
     */
    @Test
    public void resolveAnniversaryInput_missingAllTypes_throwsParseException() {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(" ");

        // When + Then
        assertThrows(ParseException.class, () -> AnniversaryParserUtils.resolveAnniversaryInput(map));
    }

    /**
     * Tests that an invalid date string for a standard anniversary results in ParseException.
     */
    @Test
    public void parseStandardAnniversary_invalidDate_throwsParseException() {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                " an/Bad d/not-a-date at/TestType",
                PREFIX_ANNIVERSARY_NAME, PREFIX_ANNIVERSARY_DATE, PREFIX_ANNIVERSARY_TYPE
        );

        // When + Then
        assertThrows(ParseException.class, () -> AnniversaryParserUtils.resolveAnniversaryInput(map));
    }

    /**
     * Tests that multiAddAnniversary returns a single birthday when birthday and name are provided.
     */
    @Test
    public void multiAddAnniversary_onlyBirthday_success() throws Exception {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(" bd/2000-01-01 n/Jane", PREFIX_BIRTHDAY, PREFIX_NAME);

        // When
        var list = AnniversaryParserUtils.multiAddAnniversary(map);

        // Then
        assertEquals(1, list.size());
        assertEquals("Jane's Birthday", list.get(0).getDescription());
    }

    /**
     * Tests that parseAnniversaryWithName throws a ParseException for an unknown type prefix.
     */
    @Test
    public void parseAnniversaryWithName_invalidPrefix_throwsParseException() {
        // Given
        Name dummy = new Name("Tom");

        // When + Then
        assertThrows(ParseException.class, () ->
                AnniversaryParserUtils.parseAnniversaryWithName(dummy, "2024-12-01", new Prefix("/x"))
        );
    }

    /**
     * Tests that a standard anniversary with an empty name uses the type name as a fallback.
     * BVA: Empty name string, fallback to type. Type must still be valid.
     */
    @Test
    public void parseStandardAnniversary_emptyName_success() throws Exception {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                " an/ d/2024-01-01 at/ValidType atdesc/StillValid",
                PREFIX_ANNIVERSARY_NAME, PREFIX_ANNIVERSARY_DATE, PREFIX_ANNIVERSARY_TYPE,
                PREFIX_ANNIVERSARY_TYPE_DESC
        );

        // When
        Anniversary result = AnniversaryParserUtils.resolveAnniversaryInput(map);

        // Then
        assertEquals("ValidType", result.getName()); // fallback to type
        assertEquals("StillValid", result.getType().getDescription());
    }


    /**
     * Tests a boundary value: the earliest valid ISO-8601 date (year 0001).
     * Ensures all inputs are valid for safe content checks.
     */
    @Test
    public void parseStandardAnniversary_earliestValidDate_success() throws Exception {
        // Given
        ArgumentMultimap map = ArgumentTokenizer.tokenize(
                " an/Epoch d/0001-01-01 at/Origin atdesc/StartOfTime",
                PREFIX_ANNIVERSARY_NAME, PREFIX_ANNIVERSARY_DATE, PREFIX_ANNIVERSARY_TYPE,
                PREFIX_ANNIVERSARY_TYPE_DESC
        );

        // When
        Anniversary result = AnniversaryParserUtils.resolveAnniversaryInput(map);

        // Then
        assertEquals("Epoch", result.getName());
        assertEquals("Origin", result.getType().getName());
    }

}
