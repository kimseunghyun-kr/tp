package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import seedu.address.model.anniversary.AnniversaryType;

/**
 * Jackson-friendly version of {@link AnniversaryType}.
 */
@Data
public class JsonAdaptedAnniversaryType {
    private final String name;
    private final String description;

    @JsonCreator
    public JsonAdaptedAnniversaryType(@JsonProperty("name") String name,
                                      @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }

    public JsonAdaptedAnniversaryType(AnniversaryType source) {
        this.name = source.getName();
        this.description = source.getDescription();
    }

    public AnniversaryType toModelType() {
        return new AnniversaryType(name, description);
    }
}
