package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.Person;



/**
 * Shows the list of anniversary of an existing person with the specified employee ID.
 */
public class ShowAnniversaryCommand extends Command {

    public static final String COMMAND_WORD = "showAnni"; // to be changed

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the list of anniversary to the person with the "
            + "specified employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID\n"
            + "Example: " + COMMAND_WORD + " "
            + "eid/0c2414da-fafb-4e05-b4f7-befb22385381";

    public static final String MESSAGE_SUCCESS = "Anniversaries shown! for employeeId %s";

    private EmployeeId employeeIdToFind;

    /**
     * Creates an ShowAnniversaryCommand with the given employee ID.
     *
     * @param employeeId the employee ID to find
     */
    public ShowAnniversaryCommand(EmployeeId employeeId) {
        requireNonNull(employeeId);
        this.employeeIdToFind = employeeId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> personToEdit = model.getFilteredByEmployeeIdPrefixList(employeeIdToFind);
        if (personToEdit.size() > 1) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX,
                    employeeIdToFind
            ));
        }
        if (personToEdit.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_PERSON_PREFIX_NOT_FOUND,
                    employeeIdToFind
            ));
        }
        String foundUserEmployeeId = personToEdit.get(0).getEmployeeIdAsString();
        return new CommandResult(String.format(MESSAGE_SUCCESS, foundUserEmployeeId), true,
                foundUserEmployeeId);
    }
}
