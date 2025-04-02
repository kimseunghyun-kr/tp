package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.anniversary.Anniversary;

/**
 * Controller for a separate Anniversary window.
 * Since we extend UiPart<Region/>, the FXML's top-level node is a Region
 * (like a VBox), not a Stage. Therefore, we manually create a Stage here
 * and place that Region inside it, so we can show it as a separate window.
 */
public class AnniversaryWindow extends UiPart<Region> {

    private static final Logger logger = LogsCenter.getLogger(AnniversaryWindow.class);
    private static final String FXML = "AnniversaryWindow.fxml"; // your F.fxml file

    // We'll hold onto a Stage so we can show/hide/focus it
    private final Stage windowStage;

    @FXML
    private ListView<Anniversary> anniversaryListView;

    /**
     * Creates a new AnniversaryWindow.
     *
     * The FXML's top-level node is a Region (e.g. VBox),
     * so here we create a new Stage and place that Region inside it.
     */
    public AnniversaryWindow() {
        super(FXML);

        // Create an actual Stage and put the FXML's root in a Scene
        windowStage = new Stage();
        windowStage.setTitle("Anniversaries"); // Title can be anything
        windowStage.setScene(new Scene(getRoot()));
    }

    // ========================================================
    // Methods to show/hide/focus the separate window
    // ========================================================

    /**
     * Shows the anniversary window.
     */
    public void show() {
        logger.fine("Showing anniversary page about the employee.");
        windowStage.show();
        windowStage.centerOnScreen();
    }

    /**
     * Returns true if the anniversary window is currently being shown.
     */
    public boolean isShowing() {
        return windowStage.isShowing();
    }

    /**
     * Hides the anniversary window.
     */
    public void hide() {
        windowStage.hide();
    }

    /**
     * Focuses on the anniversary window.
     */
    public void focus() {
        windowStage.requestFocus();
    }

    // ========================================================
    // Logic for populating the ListView
    // ========================================================

    /**
     * Updates the ListView with the given list of anniversaries.
     */
    public void setAnniversaryList(List<Anniversary> anniversaries) {
        // If you prefer, you can do: anniversaryListView.setItems(FXCollections.observableList(anniversaries));
        // And if you want a custom cell:
        anniversaryListView.setCellFactory(listView -> new ListCell<Anniversary>() {
            @Override
            protected void updateItem(Anniversary item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // For example: "2025-03-13 (Silver Wedding): Celebrating 25 years"
                    String display = item.getDate() + " (" + item.getName() + "): " + item.getDescription();
                    setText(display);
                }
            }
        });

        // if you want to store the data in the listView:
        anniversaryListView.getItems().setAll(anniversaries);
    }

    /**
     * Called by the "Close" button in the FXML (if you have one).
     * Simply hides the window.
     */
    @FXML
    private void handleClose() {
        hide();
    }
}
