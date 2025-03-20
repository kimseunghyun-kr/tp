package seedu.address.storage;

import java.time.LocalDate;

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
    private final LocalDate date;
    private final AnniversaryType type;
    private final String description;
    private final String name;

    /**
     * creates the jsonadaptedAnniversary from json
     * @param date date
     * @param type type
     * @param description description
     * @param name name
     */
    @JsonCreator
    public JsonAdaptedAnniversary(@JsonProperty("date") LocalDate date,
                                  @JsonProperty("type") JsonAdaptedAnniversaryType type,
                                  @JsonProperty("description") String description,
                                  @JsonProperty("name") String name) {
        this.date = date;
        this.type = type.toModelType();
        this.description = description;
        this.name = name;
    }

    /**
     * creates Json object from source anniversary object
     * @param source source
     */
    public JsonAdaptedAnniversary(Anniversary source) {
        this.date = source.getDate();
        this.type = source.getType();
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
                type,
                description,
                name
        );
    }

}
