package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import model.*;

// REVISION 2 CHECK

/** ADD PART CONTROLLER
 * AddPart class, manages logic needed to perform user tasks on the AddPart window.
 * @author Dalton Shultz
 *
 * <p><b>LOGICAL ERROR:</b> I declared a local variable 'id' inside the AddPart.saveAddPart method which prevented the
 * member variable 'id' from being incremented; I had thought to set the class member variable equal to this local
 * variable after the method's execution but I had several different logical errors in output so I removed the local
 * variable and changed references to the class member variable and saved each new increment in id instead of locally.
 * </p>
 */

public class AddPart implements Initializable {

    /**
     * First Id for new Part instances.
     */
    private static int id = 0000;

    @FXML private RadioButton addPartInhouse;
    @FXML private RadioButton addPartOutsourced;
    @FXML private Label addPartId;
    @FXML private TextField addPartName;
    @FXML private TextField addPartPrice;
    @FXML private TextField addPartInventory;
    @FXML private TextField addPartMin;
    @FXML private TextField addPartMax;
    @FXML private TextField addPartMachineId;
    @FXML private Label MachineOrCompany;

    /** ALERT METHOD
     * Method to show an Alert when the user adds input that doesn't fit acceptable parameters.
     * This method was implemented to improve readability and reduce repetitive alert statements.
     * @param type
     * @param title
     * @param dialog
     * @return showAndWait()
     */
    private Optional<ButtonType> showAlert(Alert.AlertType type, String title, String dialog) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(dialog);
        return alert.showAndWait();
    }

    /** SAVE NEW PART
     * Saves the text the user inputs for storage in the Inventory of Part(s).
     * @param event user clicks the Save button.
     * @throws IOException
     */
    @FXML
    void saveAddPart(ActionEvent event) throws IOException {
        try {
            String name = addPartName.getText();
            double price = Double.parseDouble(addPartPrice.getText());
            int stock = Integer.parseInt(addPartInventory.getText());
            int min = Integer.parseInt(addPartMin.getText());
            int max = Integer.parseInt(addPartMax.getText());

            // Handle input errors
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "A name is required.");
            } else if (stock > max || stock < min) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "Inventory is not between the minimum and maximum.");
            } else if (min > max) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "The minimum can't be greater or equal to the maximum.");
            } else {
                // In-House radio button is selected
                if (addPartInhouse.isSelected()) {
                    int machineId = Integer.parseInt(addPartMachineId.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                }
                // Outsourced radio button is selected
                else if (addPartOutsourced.isSelected()) {
                    String companyName = addPartMachineId.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                }
                // Return to main window
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Invalid text in submission fields.");
        }
    }

    /** CANCEL ADDING PART
     * Method to return to the main window when the Cancel button is clicked.
     * @param event user clicks the Cancel button.
     * @throws IOException
     */
    @FXML
    void cancelAddPart(ActionEvent event) throws IOException {
        showAlert(Alert.AlertType.CONFIRMATION, "Cancel Adding Part", "Cancel adding new part?");
        // Decrement partId on new Part cancelled
        id--;
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
        stage.show();
    }

    /** SET PARAMS FOR InHouse PART
     * Method to change the Machine ID/Company Name when the InHouse radio button is clicked.
     * @param event user clicks the In-House button.
     */
    @FXML
    void setPartInhouseParams(ActionEvent event) {
        MachineOrCompany.setText("Machine ID");
        addPartOutsourced.setSelected(false);
    }

    /** SET PARAMS FOR OUTSOURCED PART
     * Method to change the Machine ID/Company Name when the Outsourced radio button is clicked.
     * @param event user clicks the Outsourced button.
     */
    @FXML
    void setPartOutsourcedParams(ActionEvent event) {
        MachineOrCompany.setText("Company Name");
        addPartInhouse.setSelected(false);
    }

    /** GENERATE NEW PART ID
     * Method to get the new part id beginning at 0001.
     * @return id for new Part
     */
    public static int getPartIdCount() {
        id++;
        return id;
    }

    /** INITIALIZE WITH DEFAULTS
     * Method to set radio buttons to InHouse by default and set the new partId in the uneditable id field.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Increment id for new Part, implement on initialization, decrement on cancel
          presents new partId to user during part creation without interrupting sequence
         */
        id = getPartIdCount();
        // Set the id given the increment
        addPartId.setText(String.valueOf(id));
        // Set a default to escape scenario where Part is not created on param not set
        addPartInhouse.setSelected(true);
    }
}