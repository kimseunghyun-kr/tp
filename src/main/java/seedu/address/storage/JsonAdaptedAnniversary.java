package seedu.address.storage;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import seedu.address.model.anniversary.Anniversary;

/**
 * Jackson-friendly version of {@link Anniversary}.
 */
@Data
public class JsonAdaptedAnniversary {
    private final LocalDate date;
    private final Set<JsonAdaptedAnniversaryType> types;
    private final String description;
    private final String name;

    /**
     * creates the jsonadaptedAnniversary
     * @param date date
     * @param types types
     * @param description description
     * @param name name
     */
    @JsonCreator
    public JsonAdaptedAnniversary(@JsonProperty("date") LocalDate date,
                                  @JsonProperty("types") Set<JsonAdaptedAnniversaryType> types,
                                  @JsonProperty("description") String description,
                                  @JsonProperty("name") String name) {
        this.date = date;
        this.types = types;
        this.description = description;
        this.name = name;
    }

    /**
     * creates Json object from source anniversary object
     * @param source source
     */
    public JsonAdaptedAnniversary(Anniversary source) {
        this.date = source.getDate();
        this.types = source.getType().stream()
                .map(JsonAdaptedAnniversaryType::new)
                .collect(java.util.stream.Collectors.toSet());
        this.description = source.getDescription();
        this.name = source.getName();
    }

    /**
     * converts Anniversary Object from data object
     * @return jsonAdaptedAnniversary
     */
    public Anniversary toModelType() {
        return new Anniversary(
                date,
                types.stream().map(JsonAdaptedAnniversaryType::toModelType)
                        .collect(java.util.stream.Collectors.toSet()),
                description,
                name
        );
    }

}
