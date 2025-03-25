package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A key for a person that is used to determine if two persons are equal.
 * @param name the name of the person
 * @param phone the phone number of the person
 * @param email the email of the person
 * @param jobPosition the address of the person
 * @param employeeId the employee ID of the person
 * @param tags the tags of the person
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
     * Creates a JsonAdaptedPerson from the PersonKey.
     * @return the JsonAdaptedPerson created from the PersonKey
     */
    public JsonAdaptedPerson toJsonAdaptedPerson() {
        return new JsonAdaptedPerson(
                name,
                phone,
                email,
                jobPosition,
                employeeId,
                tags.stream().map(JsonAdaptedTag::new).collect(Collectors.toList()),
                new ArrayList<>() // we’ll add anniversaries later
        );
    }
}
