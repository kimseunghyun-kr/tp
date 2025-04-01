package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.reminder.Reminder;

/**
 * A lightweight UI component that displays an upcoming anniversary reminder for a
 * {@link seedu.address.model.person.Person}.
 *
 * This reminder could represent a birthday, work anniversary, or a custom anniversary type.
 * The card includes the employee's name, job position, type/description of anniversary, and the date.
 */
public class ReminderCard extends UiPart<Region> {
    private static final String FXML = "ReminderListCard.fxml";

    public final Reminder reminder;

    @FXML
    private Label name;

    @FXML
    private Label jobPosition;

    @FXML
    private Label type;

    @FXML
    private Label typeDescription;

    @FXML
    private Label anniversaryDescription;

    @FXML
    private Label dateInfo;

    /**
     * Creates a {@code ReminderCard} with the given {@code Reminder} object.
     *
     * @param reminder The reminder to display in this card.
     */
    public ReminderCard(Reminder reminder) {
        super(FXML);
        this.reminder = reminder;

        name.setText(reminder.getPerson().getName().fullName);
        jobPosition.setText(reminder.getPerson().getJobPosition().value);

        type.setText("🎉 " + reminder.getType().getName()); // at/
        typeDescription.setText("📌 " + reminder.getType().getDescription()); // atdesc/
        anniversaryDescription.setText("📝 " + reminder.getDescription()); // ad/

        long daysLeft = reminder.getDaysLeft();
        dateInfo.setText("📅 " + reminder.getDate() + " (in " + daysLeft + " day" + (daysLeft == 1 ? "" : "s") + ")");
    }
}
