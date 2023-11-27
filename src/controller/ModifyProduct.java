package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import model.Part;
import model.Inventory;

// REVISION 2 CHECK

/** MODIFY PRODUCT CONTROLLER
 * ModifyProduct controller, manages logic needed to perform user tasks on the ModifyProduct window.
 * @author Dalton Shultz
 */

public class ModifyProduct implements Initializable {

    /** DECLARE ASSOCIATED PARTS
     * The list of Parts associated with the Product; associatedParts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product modProduct;
    Stage stage;

    @FXML private TextField modProductId;
    @FXML private TableColumn<Part, Integer> modProductAddPartIdCol;
    @FXML private TableColumn<Part, String> modProductAddPartNameCol;
    @FXML private TableColumn<Part, Integer> modProductAddPartInventoryCol;
    @FXML private TableColumn<Part, Double> modProductAddPartPriceCol;
    @FXML private TableColumn<Part, Integer> modProductDeletePartIdCol;
    @FXML private TableColumn<Part, String> modProductDeletePartNameCol;
    @FXML private TableColumn<Part, Integer> modProductDeletePartInventoryCol;
    @FXML private TableColumn<Part, Double> modProductDeletePartPriceCol;
    @FXML private TableView<Part> modProductAddPartTableView;
    @FXML private TableView<Part> modProductDeletePartTableView;
    @FXML private TextField modProductSearch;
    @FXML private TextField modProductInventory;
    @FXML private TextField modProductMax;
    @FXML private TextField modProductMin;
    @FXML private TextField modProductName;
    @FXML private TextField modProductPrice;

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

    /** ADD PART TO PRODUCT
     * Method to add an associated Part to the Product being modified when clicking the Add button.
     * Handles errors in method including duplicate part additions, null pull selections.
     * @param event user clicks the Modify Product button
     */
    @FXML
    void addPartModProduct(ActionEvent event) {
        Part selectedPart = modProductAddPartTableView.getSelectionModel().getSelectedItem();
        // Handle null part selection
        if (selectedPart == null) {
            showAlert(Alert.AlertType.ERROR, "Error!", "Select a part to proceed.");
        // Handle duplicate attachments of parts to products
        } else if (modProduct.getAssociatedParts().contains(selectedPart)) {
                showAlert(Alert.AlertType.ERROR, "Error!", "Part already attached to product.");
        // Add part to product, no errors detected
        } else {
            modProduct.addAssociatedPart(selectedPart);
            modProduct.setAssociatedParts(modProduct.getAssociatedParts());
        }
    }

    /** SEARCH PART
     * Method to search the Parts in Inventory when text is entered. Searches with both partId and fuzzy match on partName.
     * @param event user pressed a key
     */
    @FXML
    void searchPartsModProduct(KeyEvent event) {
        String searchText = modProductSearch.getText();
        try {
            int partId = Integer.parseInt(searchText);
            ObservableList<Part> partsList = Inventory.getAllParts().filtered(part ->
                    String.valueOf(part.getId()).contains(searchText));
            if (partsList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "No part found with that ID.");
            }
            modProductAddPartTableView.setItems(partsList);
        } catch (NumberFormatException e) {
            // If the input is not a valid integer, perform a partial match search by name
            ObservableList<Part> partsList = Inventory.getAllParts().filtered(part ->
                    part.getName().toLowerCase().contains(searchText.toLowerCase()));
            if (partsList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "No part found with that name.");
            }
            modProductAddPartTableView.setItems(partsList);
        }
    }

    /** DELETE PART FROM PRODUCT
     * Method to remove an associated Part from the Product being modified when the Remove Associated part button is clicked.
     * @param event user clicks the Remove Associated Part button
     */
    @FXML
    void deletePartModProduct(ActionEvent event) {
        Part selectedPart = modProductDeletePartTableView.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,
                "Delete Associated Part","Remove part from this product?");
        if (result.orElse(null) == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            modProduct.deleteAssociatedParts(selectedPart);
            modProduct.setAssociatedParts(modProduct.getAssociatedParts());
        }
    }

    /** SAVE PRODUCT CHANGES
     * Method to modify an existing Product on Save button click. Updates existing Product with data input
     * from ModifyProduct window.
     * @param event user clicks the Save button.
     * @throws IOException
     */
    @FXML
    void saveProduct(ActionEvent event) throws IOException {
        try {
            modProduct.setId(Integer.parseInt(modProductId.getText()));
            modProduct.setName(modProductName.getText());
            modProduct.setPrice(Double.parseDouble(modProductPrice.getText()));
            modProduct.setStock(Integer.parseInt(modProductInventory.getText()));
            modProduct.setMin(Integer.parseInt(modProductMin.getText()));
            modProduct.setMax(Integer.parseInt(modProductMax.getText()));
            modProduct.setAssociatedParts(modProduct.getAssociatedParts());

            if (modProductName.getText().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "A name is required.");
            }
            else if (Integer.parseInt(modProductMin.getText()) > Integer.parseInt(modProductMax.getText())) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "The minimum can't be greater than the maximum.");
            }
            else if (Integer.parseInt(modProductInventory.getText()) > Integer.parseInt(modProductMax.getText())
                || Integer.parseInt(modProductInventory.getText()) < Integer.parseInt(modProductMin.getText())) {
                    showAlert(Alert.AlertType.WARNING, "Warning!", "Inventory is not between the minimum and maximum.");
            } else {
                Inventory.updateProduct(modProduct.getId(), modProduct);
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Some fields are missing values.");
        }
    }

    /** CANCEL CHANGES, RETURN TO MAIN WINDOW
     * Method to return to the Main Window when the Cancel button is clicked, no data saved.
     * @param event user clicks Cancel button
     * @throws IOException
     */
    @FXML
    void cancelModProduct(ActionEvent event) throws IOException {
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,
                "Cancel Modifying Product","This will clear all changes, do you want to continue?");
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
        stage.show();
    }

    /** SEND SELECTED PRODUCT TO MODIFY PRODUCT WINDOW
     * Method selects the Product on the Main Window to modify and send the Product data to the fields on the ModifyProduct window.
     * @param product selected on Main Window.
     */
    public void sendProduct(Product product) {
        // Pass the Product selected to a variable
        modProduct = product;
        // Set the values from the object to the window fields
        modProductId.setText(String.valueOf(modProduct.getId()));
        modProductName.setText(modProduct.getName());
        modProductPrice.setText(String.valueOf(modProduct.getPrice()));
        modProductInventory.setText(String.valueOf(modProduct.getStock()));
        modProductMax.setText(String.valueOf(modProduct.getMax()));
        modProductMin.setText(String.valueOf(modProduct.getMin()));
        modProductDeletePartTableView.setItems(modProduct.getAssociatedParts());
    }

    /** INITIALIZE TABLEVIEWS
     * FIXME EXPLAIN - ADD TO PARAM EXPLANATIONS
     * Method to setup all Parts table view and the bottom associated Parts table view.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Top table view for Add Part functions
        modProductAddPartTableView.setItems(Inventory.getAllParts());
        modProductAddPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductAddPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductAddPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductAddPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Bottom table view for Remove Part functions
        modProductDeletePartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductDeletePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductDeletePartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProductDeletePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}