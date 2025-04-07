package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Employee;

/**
 * An UI component that displays information of a {@code Employee}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String EMPLOYEEID_PREFIX = "employeeId : ";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Employee employee;
    private final MainWindow mainWindow; // Store reference to MainWindow

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label job;
    @FXML
    private Label email;
    @FXML
    private Label employeeId;
    @FXML
    private FlowPane tags;
    @FXML
    private Button showAnniversariesButton; // New button

    /**
     * Creates a {@code PersonCode} with the given {@code Employee} and index to display.
     */
    public PersonCard(Employee employee, int displayedIndex, MainWindow mainWindow) {
        super(FXML);
        this.employee = employee;
        this.mainWindow = mainWindow;
        id.setText(displayedIndex + ". ");
        name.setText(employee.getName().fullName);
        name.setTooltip(new Tooltip(employee.getName().fullName));
        phone.setText(employee.getPhone().value);
        job.setText(employee.getJobPosition().value);
        job.setTooltip(new Tooltip(employee.getJobPosition().value));
        email.setText(employee.getEmail().value);
        email.setTooltip(new Tooltip(employee.getEmail().value));
        employeeId.setText(employee.getEmployeeId().toString());
        employeeId.setTooltip(new Tooltip(EMPLOYEEID_PREFIX + employee.getEmployeeId().toString()));
        employee.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> {
                    Label tagLabel = new Label(tag.tagName);
                    tagLabel.setMaxWidth(150); // limit width in px
                    tagLabel.setTooltip(new Tooltip(tag.tagName)); // show full name on hover
                    tags.getChildren().add(tagLabel);
                });
    }

    @FXML
    void handleShowAnniversariesClick() {
        mainWindow.handleShowAnniversaries(employee.getEmployeeId().toString());
    }

}
