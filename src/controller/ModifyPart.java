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

/** MODIFY PART CONTROLLER
 * ModifyPart controller, manages logic needed to perform user tasks on the ModifyPart window.
 * @author Dalton Shultz
 */

public class ModifyPart implements Initializable {

    @FXML private Label modPartId;
    @FXML private Label machineIdOrCompanyName;
    @FXML private RadioButton modPartInhouse;
    @FXML private RadioButton modPartOutsourced;
    @FXML private TextField modPartMachineId;
    @FXML private TextField modPartInventory;
    @FXML private TextField modPartMax;
    @FXML private TextField modPartMin;
    @FXML private TextField modPartName;
    @FXML private TextField modPartPrice;


    /** ALERT METHOD
     * Method to show an Alert when the user adds input that doesn't fit acceptable parameters.
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
    /** SAVE PART CHANGES
     * Method to modify exist Parts when the Save button is clicked.
     * @param event user clicks the Save button.
     * @throws IOException
     */
    @FXML
    void saveModPart(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modPartId.getText());
            String name = modPartName.getText();
            double price = Double.parseDouble(modPartPrice.getText());
            int stock = Integer.parseInt(modPartInventory.getText());
            int min = Integer.parseInt(modPartMin.getText());
            int max = Integer.parseInt(modPartMax.getText());

            // Handle errors
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "A name is required.");
            } else if (min > max) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "The minimum must be less than or equal to the maximum.");
            } else if (stock > max || stock < min) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "Inventory is not between the minimum and maximum.");
            // No errors in input
            } else {
                // InHouse radio button is selected
                if (modPartInhouse.isSelected()) {
                    // Get machineId to local variable
                    int machineId = Integer.parseInt(modPartMachineId.getText());
                    // Overwrite the existing Part
                    Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machineId));
                }
                // Outsourced radio button is selected
                else if (modPartOutsourced.isSelected()) {
                    // Get companyName to local variable
                    String companyName = modPartMachineId.getText();
                    // Overwrite the existing Part
                    Inventory.updatePart(id, new Outsourced(id, name, price, stock, min, max, companyName));
                }
                // Return to Main Window
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Fill out all fields to proceed.");
        }
    }

    /** CANCEL CHANGES, RETURN TO MAIN WINDOW
     * Method to return to main window on clicking cancel button, no input data is saved.
     * @param event user clicks the Cancel button.
     * @throws IOException
     */
    @FXML
    void cancelModPart(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,
                "Cancel Modifying Part", "Do you want to cancel modifying the part?");
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
        stage.show();
    }

    /** SET PARAMS FOR INHOUSE PARTS
     * Method to change the Machine ID/Company Name field to Machine ID when clicking the InHouse radio button.
     * @param event user clicks the In-House button.
     */
    @FXML
    void setPartInhouseParams(ActionEvent event) {
        machineIdOrCompanyName.setText("Machine Id");
        modPartOutsourced.setSelected(false);
    }

    /** SET PARAMS FOR OUTSOURCED PARTS
     * Method to change the Machine ID/Company Name field to Company Name when the user clicks the Outsourced button.
     * @param event user clicks the Outsourced button.
     */
    @FXML
    void setPartOutsourcedParams(ActionEvent event) {
        machineIdOrCompanyName.setText("Company Name");
        modPartInhouse.setSelected(false);
    }

    /** SEND PART DATA TO MODIFY PART WINDOW
     * Method takes the Part selected on the Main Window to be modified and sends the part data to the fields on the ModifyPart window.
     * @param part selected on Main Window.
     */
    public void sendPart(Part part) {
        // set part details on window
        modPartId.setText(String.valueOf(part.getId()));
        modPartName.setText(part.getName());
        modPartPrice.setText(String.valueOf(part.getPrice()));
        modPartInventory.setText(String.valueOf(part.getStock()));
        modPartMin.setText(String.valueOf(part.getMin()));
        modPartMax.setText(String.valueOf(part.getMax()));

        // Get InHouse or Outsourced value
        if (part instanceof InHouse) {
            modPartInhouse.setSelected(true);
            machineIdOrCompanyName.setText("Machine Id");
            modPartMachineId.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else if (part instanceof Outsourced) {
            modPartOutsourced.setSelected(true);
            machineIdOrCompanyName.setText("Company Name");
            modPartMachineId.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
    }

    /** INITIALIZE MODIFY PART
     * Initializes the PartModify class
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}

}
