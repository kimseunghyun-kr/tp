// AddAnniversaryCommand.java

package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_DUPLICATE_ANNIVERSARY;
import static seedu.address.logic.Messages.MESSAGE_SUCCESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ANNIVERSARY_TYPE_DESC;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;

/**
 * Adds an anniversary to an existing Employee in the address book.
 */
@Getter
public class AddAnniversaryCommand extends Command {

    public static final String COMMAND_WORD = "addAnni";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an anniversary to the employee identified by "
            + "a prefix of their Employee ID.\n"
            + "Parameters: [] indicates optional fields\n"
            + PREFIX_EMPLOYEEID + "EMPLOYEE_ID_PREFIX "
            + PREFIX_ANNIVERSARY_DATE + "DATE "
            + PREFIX_ANNIVERSARY_NAME + "ANNIVERSARY_NAME "
            + PREFIX_ANNIVERSARY_TYPE + "ANNIVERSARY_TYPE "
            + "[" + PREFIX_ANNIVERSARY_DESC + "DESCRIPTION] "
            + "[" + PREFIX_ANNIVERSARY_TYPE_DESC + "TYPE]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EMPLOYEEID + " 0c2414da-fafb-4e05-b4f7-befb22385381 "
            + PREFIX_ANNIVERSARY_DATE + "2025-03-13 "
            + PREFIX_ANNIVERSARY_NAME + "Silver Wedding "
            + PREFIX_ANNIVERSARY_TYPE + "Wedding "
            + PREFIX_ANNIVERSARY_DESC + "Celebrating 25 years "
            + PREFIX_ANNIVERSARY_TYPE_DESC + "Personal";
    private final Anniversary toAdd;
    private final EmployeeId employeeIdPrefix;

    /**
     * Creates an AddAnniversaryCommand to add the specified {@code Anniversary} to the employee with given employeeId.
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
        List<Employee> matchedEmployees = model.getFilteredByEmployeeIdPrefixList(employeeIdPrefix);

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

        // Check if the same anniversary already exists
        boolean duplicate = employeeToEdit.getAnniversaries().stream()
                .anyMatch(existing ->
                        existing.getDate().equals(toAdd.getDate())
                                && existing.getName().equals(toAdd.getName())
                                && existing.getDescription().equals(toAdd.getDescription())
                                && existing.getType().equals(toAdd.getType())
                );
        if (duplicate) {
            throw new CommandException(MESSAGE_DUPLICATE_ANNIVERSARY);
        }

        // Create a new Employee object with updated anniversaries
        List<Anniversary> anniversaryList = new ArrayList<>(employeeToEdit.getAnniversaries());
        anniversaryList.add(toAdd);
        Employee updatedEmployee = Employee.builder()
                .employeeId(employeeToEdit.getEmployeeId())
                .name(employeeToEdit.getName())
                .jobPosition(employeeToEdit.getJobPosition())
                .email(employeeToEdit.getEmail())
                .phone(employeeToEdit.getPhone())
                .tags(employeeToEdit.getTags())
                .anniversaries(anniversaryList).build();

        // update the model
        model.setEmployee(employeeToEdit, updatedEmployee);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }
}
