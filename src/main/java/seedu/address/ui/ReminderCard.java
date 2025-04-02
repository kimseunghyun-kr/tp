package seedu.address.ui;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Employee;

/**
 * A lightweight UI component that displays a birthday or work anniversary reminder for a {@code Employee}.
 */
public class ReminderCard extends UiPart<Region> {
    private static final String FXML = "ReminderListCard.fxml";

    public final Employee employee;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label jobPosition;
    @FXML
    private Label dateInfo;

    /**
     * Creates a {@code ReminderCard} with the given {@code Employee}.
     * Highlights either the birthday or work anniversary depending on type.
     */
    public ReminderCard(Employee employee, boolean isBirthday) {
        super(FXML);
        this.employee = employee;
        name.setText(employee.getName().fullName);
        jobPosition.setText(employee.getJobPosition().value);

        LocalDate today = LocalDate.now();
        LocalDate nextDate = isBirthday
                ? employee.getNextUpcomingBirthdayDate()
                : employee.getNextUpcomingWorkAnniversaryDate();

        if (nextDate != null) {
            long daysLeft = ChronoUnit.DAYS.between(today, nextDate);
            String label = isBirthday ? "🎂 Birthday" : "🎉 Work Anniversary";
            dateInfo.setText(label + ": " + nextDate + " (in " + daysLeft + " day" + (daysLeft == 1 ? "" : "s") + ")");
        } else {
            assert false : "nextDate should not be null when employee is in reminder list";
            dateInfo.setText("No upcoming " + (isBirthday ? "birthday." : "work anniversary."));
        }
    }
}
