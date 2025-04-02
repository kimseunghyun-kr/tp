package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_EMPLOYEE_NOT_FOUND;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Employee;

/**
 * Shows the list of anniversary of an existing employee with the specified employee ID.
 */
public class ShowAnniversaryCommand extends Command {

    public static final String COMMAND_WORD = "showAnni"; // to be changed

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the list of anniversary to the employee with the "
            + "specified employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID\n"
            + "Example: " + COMMAND_WORD + " "
            + "eid/0c2414da-fafb-4e05-b4f7-befb22385381";

    public static final String MESSAGE_SUCCESS = "Anniversaries shown for employee: %s!";

    private final String employeeIdToFind;

    /**
     * Creates an ShowAnniversaryCommand with the given employee ID.
     *
     * @param employeeId the employee ID to find
     */
    public ShowAnniversaryCommand(String employeeId) {
        requireNonNull(employeeId);
        this.employeeIdToFind = employeeId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Employee employeeToShow = model.getFilteredEmployeeList().stream()
                .filter(p -> p.getEmployeeId().toString().equals(employeeIdToFind))
                .findFirst()
                .orElseThrow(() -> new CommandException(String.format(MESSAGE_EMPLOYEE_NOT_FOUND, employeeIdToFind)));

        return new CommandResult(String.format(MESSAGE_SUCCESS, employeeToShow.getName()), true,
                employeeToShow.getEmployeeIdAsString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ShowAnniversaryCommand)) {
            return false;
        }

        ShowAnniversaryCommand otherShowEmployeeCommand = (ShowAnniversaryCommand) other;
        return employeeIdToFind.equals(otherShowEmployeeCommand.employeeIdToFind);
    }
}
