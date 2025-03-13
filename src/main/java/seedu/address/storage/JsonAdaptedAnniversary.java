package seedu.address.storage;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;

/**
 * Jackson-friendly version of {@link Anniversary}.
 */
@Data
public class JsonAdaptedAnniversary {

    private final String uuid;
    private final LocalDate date;
    private final Set<JsonAdaptedAnniversaryType> types;
    private final String description;
    private final String name;

    // If you want a reference to the person, you can add this:
    private final String personId; // optional

    /**
     * Creates a Jackson-friendly Anniversary.
     */
    @JsonCreator
    public JsonAdaptedAnniversary(@JsonProperty("uuid") String uuid,
                                  @JsonProperty("date") LocalDate date,
                                  @JsonProperty("types") Set<JsonAdaptedAnniversaryType> types,
                                  @JsonProperty("description") String description,
                                  @JsonProperty("name") String name,
                                  @JsonProperty("personId") String personId) {
        this.uuid = uuid;
        this.date = date;
        this.types = types;
        this.description = description;
        this.name = name;
        this.personId = personId;
    }

    /**
     * Converts a given {@code Anniversary} into this class for Jackson use.
     */
    public JsonAdaptedAnniversary(Anniversary source) {
        this.uuid = source.getUuid().toString();
        this.date = source.getDate();
        this.types = source.getType().stream()
                .map(JsonAdaptedAnniversaryType::new)
                .collect(Collectors.toSet());
        this.description = source.getDescription();
        this.name = source.getName();
        this.personId = null; // or fill in as needed
    }

    /**
     * Converts this Jackson-friendly adapted object back into the model's {@code Anniversary}.
     */
    public Anniversary toModelType() {
        UUID annUuid = (uuid == null) ? UUID.randomUUID() : UUID.fromString(uuid);

        // Convert the stored JSONAdaptedAnniversaryTypes back to real AnniversaryTypes
        Set<AnniversaryType> anniversaryTypes = types.stream()
                .map(JsonAdaptedAnniversaryType::toModelType)
                .collect(Collectors.toSet());

        return new Anniversary(
                annUuid,
                date,
                anniversaryTypes,
                description,
                name
        );
    }
}
