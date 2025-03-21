package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_PERSON_NOT_FOUND;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

public class ShowAnniversaryCommand extends Command {

    public static final String COMMAND_WORD = "showAnni"; // to be changed

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows the list of anniversary to the person with the "
            + "specified employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID\n"
            + "Example: " + COMMAND_WORD + " "
            + "eid/0c2414da-fafb-4e05-b4f7-befb22385381";

    public static final String MESSAGE_SUCCESS = "Anniversaries shown: %1$s!";

    private String employeeIdToFind;

    public ShowAnniversaryCommand(String employeeId) {
        requireNonNull(employeeId);
        this.employeeIdToFind = employeeId;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = model.getFilteredPersonList().stream()
                .filter(p -> p.getEmployeeId().toString().equals(employeeIdToFind))
                .findFirst()
                .orElse(null);

        if (personToEdit == null) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, employeeIdToFind));
        }
        return null;
    }
}
