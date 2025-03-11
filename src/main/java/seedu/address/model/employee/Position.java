package seedu.address.model.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Denotes which position / group a person is in
 */
@Data
@AllArgsConstructor
public class Position {
    private String positionName;
    private String positionDescription;
}
