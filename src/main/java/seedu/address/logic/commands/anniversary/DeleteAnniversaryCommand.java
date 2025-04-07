package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS;
import static seedu.address.logic.Messages.MESSAGE_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;

/**
 * Deletes Anniversaries from a employee
 */
@Getter
public class DeleteAnniversaryCommand extends Command {
    public static final String MESSAGE_SUCCESS = "anniversary deleted: %1$s";
    public static final String COMMAND_WORD = "deleteAnni";
    public static final Object MESSAGE_USAGE = COMMAND_WORD
            + ": deletes an anniversary to the employee identified by a "
            + "prefix of their Employee ID.\n"
            + "Parameters: "
            + PREFIX_EMPLOYEEID + "EMPLOYEE_ID "
            + PREFIX_ANNIVERSARY_INDEX + "index ";
    private final Index targetIndex;
    private final EmployeeId employeeIdPrefix;

    /**
     * constructs a deleteAnniversaryCommand
     * @param targetIndex tar
     * @param employeeIdPrefix emp
     */
    public DeleteAnniversaryCommand(Index targetIndex, EmployeeId employeeIdPrefix) {
        this.targetIndex = targetIndex;
        this.employeeIdPrefix = employeeIdPrefix;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.commitChanges();
        List<Employee> matchedEmployees = model.getFullFilteredByEmployeeIdPrefixListFromData(employeeIdPrefix);

        if (matchedEmployees.size() > 1) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX,
                    employeeIdPrefix
            ));
        }

        if (matchedEmployees.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND,
                    employeeIdPrefix
            ));
        }

        Employee employeeToEdit = matchedEmployees.get(0);
        List<Anniversary> anniversaryList = employeeToEdit.getAnniversaries();

        if (targetIndex.getZeroBased() >= anniversaryList.size()) {
            throw new CommandException(MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS);
        }

        // Save the state before any potential changes
        model.commitChanges();

        List<Anniversary> editAnniversaryList = new ArrayList<>();
        editAnniversaryList.addAll(anniversaryList);

        Anniversary anniversaryToDelete = anniversaryList.get(targetIndex.getZeroBased());
        editAnniversaryList.remove(anniversaryToDelete);
        Employee updatedEmployee = Employee.builder()
                .employeeId(employeeToEdit.getEmployeeId())
                .name(employeeToEdit.getName())
                .jobPosition(employeeToEdit.getJobPosition())
                .email(employeeToEdit.getEmail())
                .phone(employeeToEdit.getPhone())
                .tags(employeeToEdit.getTags())
                .anniversaries(editAnniversaryList).build();
        // update the model
        model.setEmployee(employeeToEdit, updatedEmployee);

        return new CommandResult(String.format(MESSAGE_SUCCESS, anniversaryToDelete), true,
                employeeToEdit.getEmployeeIdAsString());
    }
}
