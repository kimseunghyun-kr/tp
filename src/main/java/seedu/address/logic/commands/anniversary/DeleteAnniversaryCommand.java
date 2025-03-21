package seedu.address.logic.commands.anniversary;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS;
import static seedu.address.logic.Messages.MESSAGE_PERSON_NOT_FOUND;
import static seedu.address.logic.commands.anniversary.AddAnniversaryCommand.COMMAND_WORD;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Person;

/**
 * Deletes Anniversaries from a person
 */
public class DeleteAnniversaryCommand extends Command {
    public static final String MESSAGE_SUCCESS = "anniversary added: %1$s";
    public static final String COMMAND_WORD = "deleteAnniversary";
    public static final Object MESSAGE_USAGE = COMMAND_WORD + ": deletes an anniversary to the person with the "
            + "specified employee ID.\n"
            + "Parameters: "
            + "eid/EMPLOYEE_ID "
            + "ad/index ";
    private final Index targetIndex;
    private final String employeeIdToFind;

    /**
     * constructs a deleteAnniversaryCommand
     * @param targetIndex tar
     * @param employeeIdToFind emp
     */
    public DeleteAnniversaryCommand(Index targetIndex, String employeeIdToFind) {
        this.targetIndex = targetIndex;
        this.employeeIdToFind = employeeIdToFind;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        // Attempt to find the person in model by employeeId
        Person personToEdit = model.getFilteredPersonList().stream()
                .filter(p -> p.getEmployeeId().toString().equals(employeeIdToFind))
                .findFirst()
                .orElseThrow(()->new CommandException(MESSAGE_PERSON_NOT_FOUND));
        List<Anniversary> anniversaryList = personToEdit.getAnniversaries();

        if (targetIndex.getZeroBased() >= anniversaryList.size()) {
            throw new CommandException(MESSAGE_ANNIVERSARY_OUT_OF_BOUNDS);
        }

        Anniversary anniversaryToDelete = anniversaryList.get(targetIndex.getZeroBased());
        anniversaryList.remove(anniversaryToDelete);
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

        return new CommandResult(String.format(MESSAGE_SUCCESS, anniversaryToDelete));
    }
}
