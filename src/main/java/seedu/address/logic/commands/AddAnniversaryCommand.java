// AddAnniversaryCommand.java

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Person;

/**
 * Adds an anniversary to an existing Person in the address book.
 */
public class AddAnniversaryCommand extends Command {

    public static final String COMMAND_WORD = "anniversary";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an anniversary to the person with the "
            + "specified employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID "
            + "d/DATE "
            + "n/ANNIVERSARY_NAME "
            + "[ad/DESCRIPTION] "
            + "[at/TYPE]...\n"
            + "Example: " + COMMAND_WORD + " "
            + "eid/0c2414da-fafb-4e05-b4f7-befb22385381 "
            + "d/2025-03-13 "
            + "n/Silver Wedding "
            + "ad/Celebrating 25 years "
            + "at/Personal "
            + "at/Family";

    public static final String MESSAGE_SUCCESS = "New anniversary added: %1$s";
    public static final String MESSAGE_PERSON_NOT_FOUND = "No person found with employeeId = %s";
    public static final String MESSAGE_DUPLICATE_ANNIVERSARY =
            "This exact anniversary (date + name + type + description) already exists for that person.";

    private final Anniversary toAdd;
    private final String employeeIdToFind;

    /**
     * Creates an AddAnniversaryCommand to add the specified {@code Anniversary} to the person with given employeeId.
     */
    public AddAnniversaryCommand(String employeeId, Anniversary anniversary) {
        requireNonNull(employeeId);
        requireNonNull(anniversary);
        this.employeeIdToFind = employeeId;
        this.toAdd = anniversary;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // Attempt to find the person in model by employeeId
        Person personToEdit = model.getFilteredPersonList().stream()
                .filter(p -> p.getEmployeeId().toString().equals(employeeIdToFind))
                .findFirst()
                .orElse(null);

        if (personToEdit == null) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, employeeIdToFind));
        }

        // Check if the same anniversary already exists
        boolean duplicate = personToEdit.getAnniversaries().stream()
                .anyMatch(existing ->
                        existing.getDate().equals(toAdd.getDate())
                                && existing.getName().equals(toAdd.getName())
                                && existing.getDescription().equals(toAdd.getDescription())
                                && existing.getType().equals(toAdd.getType())
                );
        if (duplicate) {
            throw new CommandException(MESSAGE_DUPLICATE_ANNIVERSARY);
        }

        // Create a new Person object with updated anniversaries
        List<Anniversary> anniversaryList = new ArrayList<>(personToEdit.getAnniversaries());
        anniversaryList.add(toAdd);
        Person updatedPerson = Person.builder()
                .employeeId(personToEdit.getEmployeeId())
                .name(personToEdit.getName())
                .address(personToEdit.getAddress())
                .email(personToEdit.getEmail())
                .phone(personToEdit.getPhone())
                .tags(personToEdit.getTags())
                .anniversaries(anniversaryList).build();

        // update the model
        model.setPerson(personToEdit, updatedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
