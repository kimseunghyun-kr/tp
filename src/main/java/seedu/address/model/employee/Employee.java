package seedu.address.model.employee;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import seedu.address.model.anniversary.Anniversary;


/**
 * aggregate class denoting an employee
 */
@Data
@AllArgsConstructor
public class Employee {
    private Position position;
    private UUID employeeId;
    private List<Anniversary> anniversaryList;
}
