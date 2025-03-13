package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryBook;
import seedu.address.model.person.Person;



/**
 * Adds an anniversary to an existing Person in the address book (now stored in AnniversaryBook).
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
     * Creates an AddAnniversaryCommand to add the specified {@code Anniversary}
     * to the Person with the given employeeId.
     */
    public AddAnniversaryCommand(String employeeId, Anniversary anniversary) {
        requireNonNull(employeeId);
        requireNonNull(anniversary);
        this.employeeIdToFind = employeeId;
        this.toAdd = anniversary;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        // 1) Find the person by employeeId
        Person personToEdit = model.getFilteredPersonList().stream()
                .filter(p -> p.getEmployeeId().toString().equals(employeeIdToFind))
                .findFirst()
                .orElse(null);
        if (personToEdit == null) {
            throw new CommandException(String.format(MESSAGE_PERSON_NOT_FOUND, employeeIdToFind));
        }

        // 2) Check for duplication
        UUID pid = personToEdit.getEmployeeId();
        AnniversaryBook ab = model.getAnniversaryBook();
        if (ab.isDuplicateAnniversary(pid, toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_ANNIVERSARY);
        }

        // 3) Insert into the AnniversaryBook
        ab.addAnniversary(pid, toAdd);

        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

}
