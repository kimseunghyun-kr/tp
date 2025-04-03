package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.reminder.Reminder;

/**
 * Filters and shows upcoming anniversaries (birthdays, work, or custom) within the next few days.
 */
public class ReminderCommand extends Command {
    public static final String COMMAND_WORD = "reminder";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Displays a list of upcoming anniversaries (within 3 days).\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Displayed employees with upcoming anniversaries! \n"
            + "Only employees with anniversaries upcoming within 3 days will appear on the panel.";

    private final Logger logger = LogsCenter.getLogger(ReminderCommand.class);


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateReminderList();
        List<Reminder> reminders = model.getReminderList();

        logger.fine("Reminder list contains " + reminders.size() + " items.");
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
