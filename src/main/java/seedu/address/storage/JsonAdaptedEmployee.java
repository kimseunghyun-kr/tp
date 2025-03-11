package seedu.address.storage;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import seedu.address.model.employee.Employee;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Employee}.
 */
@Data
public class JsonAdaptedEmployee {

    private final UUID employeeId;
    private final JsonAdaptedPosition position;
    private final List<JsonAdaptedAnniversary> anniversaryList;

    @JsonCreator
    public JsonAdaptedEmployee(
            @JsonProperty("employeeId") UUID employeeId,
            @JsonProperty("position") JsonAdaptedPosition position,
            @JsonProperty("anniversaryList") List<JsonAdaptedAnniversary> anniversaryList) {
        this.employeeId = employeeId;
        this.position = position;
        this.anniversaryList = anniversaryList;
    }

    public JsonAdaptedEmployee(Employee source) {
        this.employeeId = source.getEmployeeId();
        this.position = new JsonAdaptedPosition(source.getPosition());
        this.anniversaryList = source.getAnniversaryList().stream()
                .map(JsonAdaptedAnniversary::new)
                .toList();
    }

    public Employee toModelType() {
        return new Employee(
                position.toModelType(),
                employeeId,
                anniversaryList.stream().map(JsonAdaptedAnniversary::toModelType).toList()
        );
    }
}
