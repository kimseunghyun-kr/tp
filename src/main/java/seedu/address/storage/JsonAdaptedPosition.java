package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import seedu.address.model.employee.Position;

/**
 * Jackson-friendly version of {@link Position}.
 */
@Data
public class JsonAdaptedPosition {
    private final String positionName;
    private final String positionDescription;

    @JsonCreator
    public JsonAdaptedPosition(@JsonProperty("positionName") String positionName,
                               @JsonProperty("positionDescription") String positionDescription) {
        this.positionName = positionName;
        this.positionDescription = positionDescription;
    }

    public JsonAdaptedPosition(Position source) {
        this.positionName = source.getPositionName();
        this.positionDescription = source.getPositionDescription();
    }

    public Position toModelType() {
        return new Position(positionName, positionDescription);
    }
}
