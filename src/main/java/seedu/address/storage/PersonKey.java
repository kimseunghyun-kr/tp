package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.model.person.Employee;
import seedu.address.model.tag.Tag;

/**
 * A key for a employee that is used to determine if two persons are equal.
 * @param name the name of the employee
 * @param phone the phone number of the employee
 * @param email the email of the employee
 * @param jobPosition the address of the employee
 * @param employeeId the employee ID of the employee
 * @param tags the tags of the employee
 */
public record PersonKey(
        String name,
        String phone,
        String email,
        String jobPosition,
        String employeeId,
        List<String> tags
) {
    /**
     * Creates a PersonKey from a JsonAdaptedPerson.
     * @param person the JsonAdaptedPerson to create the PersonKey from
     * @return the PersonKey created from the JsonAdaptedPerson
     */
    public static PersonKey from(JsonAdaptedPerson person) {
        return new PersonKey(
                person.getName(),
                person.getPhone(),
                person.getEmail(),
                person.getJobposition(),
                person.getEmployeeId(),
                person.getTags().stream()
                        .map(JsonAdaptedTag::getTagName).collect(Collectors.toList())
        );
    }

    /**
     * Creates a PersonKey from a Employee.
     * @param employee the Employee to create the PersonKey from
     * @return the PersonKey created from the Employee
     */
    public static PersonKey from(Employee employee) {
        return new PersonKey(
                employee.getName().toString(),
                employee.getPhone().toString(),
                employee.getEmail().toString(),
                employee.getJobPosition().toString(),
                employee.getEmployeeId().toString(),
                employee.getTags().stream()
                        .map(Tag::getTagName).collect(Collectors.toList())
        );
    }

    /**
     * Creates a JsonAdaptedPerson from the PersonKey.
     * EXTREME CAUTION TO THE ORDERING - this causes bugs if not done correctly
     * @return the JsonAdaptedPerson created from the PersonKey
     */
    public JsonAdaptedPerson toJsonAdaptedPerson() {
        return new JsonAdaptedPerson(
                employeeId,
                name,
                phone,
                email,
                jobPosition,
                tags.stream().map(JsonAdaptedTag::new).collect(Collectors.toList()),
                new ArrayList<>() // we’ll add anniversaries later
        );
    }
}
