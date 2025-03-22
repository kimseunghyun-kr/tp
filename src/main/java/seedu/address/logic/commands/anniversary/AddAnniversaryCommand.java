// AddAnniversaryCommand.java

package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_ANNIVERSARY;
import static seedu.address.logic.Messages.MESSAGE_SUCCESS;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;

/**
 * Adds an anniversary to an existing Person in the address book.
 */
public class AddAnniversaryCommand extends Command {

    public static final String COMMAND_WORD = "anniversary";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an anniversary to the person identified by "
            + "a prefix of their Employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID_PREFIX "
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
    private final Anniversary toAdd;
    private final EmployeeId employeeIdPrefix;

    /**
     * Creates an AddAnniversaryCommand to add the specified {@code Anniversary} to the person with given employeeId.
     */
    public AddAnniversaryCommand(EmployeeId employeeIdPrefix, Anniversary anniversary) {
        requireNonNull(employeeIdPrefix);
        requireNonNull(anniversary);
        this.employeeIdPrefix = employeeIdPrefix;
        this.toAdd = anniversary;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> matchedEmployees = model.getFilteredByEmployeeIdPrefixList(employeeIdPrefix);

        if (matchedEmployees.size() > 1) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX,
                    employeeIdPrefix
            ));
        }

        if (matchedEmployees.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_PERSON_PREFIX_NOT_FOUND,
                    employeeIdPrefix
            ));
        }

        Person personToEdit = matchedEmployees.get(0);

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
                .jobPosition(personToEdit.getJobPosition())
                .email(personToEdit.getEmail())
                .phone(personToEdit.getPhone())
                .tags(personToEdit.getTags())
                .anniversaries(anniversaryList).build();

        // update the model
        model.setPerson(personToEdit, updatedPerson);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
