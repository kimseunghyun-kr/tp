package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_REMINDER_TYPE;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

public class ReminderCommand extends Command {
    public static final String COMMAND_WORD = "reminder";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters a list of people with upcoming birthdays or work anniversaries within 3 days.\n"
            + "Parameters: TYPE (must be either 'bd' for birthday or 'wa' for work anniversary)\n"
            + "Example: " + COMMAND_WORD + " bd";

    public static final String MESSAGE_SUCCESS_BIRTHDAY = "Displayed employees with upcoming birthdays!";
    public static final String MESSAGE_SUCCESS_WORK_ANNIVERSARY = "Displayed employees with upcoming work anniversaries!";

    private final String type;

    public ReminderCommand(String type) {
        this.type = type;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        return switch (type) {
            case "bd" -> {
                model.updateBirthdayReminderList();
                yield new CommandResult(MESSAGE_SUCCESS_BIRTHDAY);
            }
            case "wa" -> {
                model.updateWorkAnniversaryReminderList();
                yield new CommandResult(MESSAGE_SUCCESS_WORK_ANNIVERSARY);
            }
            default -> throw new CommandException(MESSAGE_INVALID_REMINDER_TYPE);
        };
    }
}
