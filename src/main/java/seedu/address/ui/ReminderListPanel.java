package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.reminder.Reminder;

/**
 * A UI panel that displays a list of upcoming reminders for employee anniversaries.
 *
 * This includes birthdays, work anniversaries, and custom anniversary types, each shown
 * using a {@link ReminderCard}. Each reminder entry is associated with a specific
 * {@link seedu.address.model.person.Person}, type, and upcoming date.
 */
public class ReminderListPanel extends UiPart<Region> {
    private static final String FXML = "ReminderListPanel.fxml";

    @FXML
    private ListView<Reminder> reminderListView;

    /**
     * Constructs a {@code ReminderListPanel} with the given list of reminders.
     *
     * @param reminderList The observable list of {@link Reminder} objects to display.
     */
    public ReminderListPanel(ObservableList<Reminder> reminderList) {
        super(FXML);
        reminderListView.setItems(reminderList);
        reminderListView.setCellFactory(listView -> new ReminderListViewCell());
    }

    /**
     * A custom {@link ListCell} used to render each {@link Reminder} as a {@link ReminderCard}.
     */
    class ReminderListViewCell extends ListCell<Reminder> {
        @Override
        protected void updateItem(Reminder reminder, boolean empty) {
            super.updateItem(reminder, empty);
            if (empty || reminder == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ReminderCard(reminder).getRoot());
            }
        }
    }
}
