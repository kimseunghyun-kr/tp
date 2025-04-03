package seedu.address.model.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.util.Pair;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;



/**
 * utility class to validate employee IDs.
 * Currently only harvests internal checks for prefix conflicts.
 */
public class EmployeeIdPrefixValidationUtils {

    /**
     * Gets all pairs of employee IDs in the list that have prefix conflicts.
     * @param employees the list of employees to check
     * @return list of conflicting pairs
     */
    public static List<Pair<EmployeeId, EmployeeId>> getPrefixConflictingPairs(List<Employee> employees) {
        Objects.requireNonNull(employees);
        List<Pair<EmployeeId, EmployeeId>> conflicts = new ArrayList<>();
        for (int i = 0; i < employees.size(); i++) {
            EmployeeId id1 = employees.get(i).getEmployeeId();
            for (int j = i + 1; j < employees.size(); j++) {
                EmployeeId id2 = employees.get(j).getEmployeeId();
                if (id1.hasPrefixConflict(id2)) {
                    conflicts.add(new Pair<>(id1, id2));
                }
            }
        }
        return conflicts;
    }
}
