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

    @JsonCreator
    public JsonAdaptedAnniversary(@JsonProperty("date") LocalDate date,
                                  @JsonProperty("types") Set<JsonAdaptedAnniversaryType> types,
                                  @JsonProperty("description") String description) {
        this.date = date;
        this.types = types;
        this.description = description;
    }

    public JsonAdaptedAnniversary(Anniversary source) {
        this.date = source.getDate();
        this.types = source.getType().stream()
                .map(JsonAdaptedAnniversaryType::new)
                .collect(java.util.stream.Collectors.toSet());
        this.description = source.getDescription();
    }

    public Anniversary toModelType() {
        return new Anniversary(
                date,
                types.stream().map(JsonAdaptedAnniversaryType::toModelType)
                        .collect(java.util.stream.Collectors.toSet()),
                description
        );
    }

}
