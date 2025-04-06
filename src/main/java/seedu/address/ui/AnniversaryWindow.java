package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    @FXML private TableView<Anniversary> anniversaryTable;
    @FXML private TableColumn<Anniversary, String> dateColumn;
    @FXML private TableColumn<Anniversary, String> nameColumn;
    @FXML private TableColumn<Anniversary, String> descriptionColumn;
    @FXML private TableColumn<Anniversary, String> typeColumn;
    @FXML private TableColumn<Anniversary, String> typeDescriptionColumn;


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
        windowStage.setMinWidth(500);
        windowStage.setMinHeight(600);
        anniversaryTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        dateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate().toString()));
        nameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        descriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));
        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType().getName()));
        typeDescriptionColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType().getDescription()));

        anniversaryTable.getItems().setAll(anniversaries);
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
