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

/** ADD PRODUCT CONTROLLER
 * AddProduct controller, manages logic needed to perform user tasks on the AddProduct window.
 * @author Dalton Shultz
 */

public class AddProduct implements Initializable {

    /**
     * First Id for new Product instances.
     */
    private static int productId = 1000;
    /**
     * The list of Parts associated with the Product; associatedParts.
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    /**
     * Default parameters for new Product instances.
     */
    private Product newProduct = new Product(0, "", 0.00, 0, 0, 0, null);

    Stage stage;

    @FXML private TextField addProductId;
    @FXML private TextField addProductName;
    @FXML private TextField addProductPrice;
    @FXML private TextField addProductInventory;
    @FXML private TextField addProductMin;
    @FXML private TextField addProductMax;
    @FXML private TextField addProductSearch;
    @FXML private TableColumn<Part, Integer> addProductAddPartIdCol;
    @FXML private TableColumn<Part, String> addProductAddPartNameCol;
    @FXML private TableColumn<Part, Double> addProductAddPartPriceCol;
    @FXML private TableColumn<Part, Integer> addProductAddPartInventoryCol;
    @FXML private TableColumn<Part, Integer> addProductDeletePartIdCol;
    @FXML private TableColumn<Part, String> addProductDeletePartNameCol;
    @FXML private TableColumn<Part, Integer> addProductDeletePartInventoryCol;
    @FXML private TableColumn<Part, Double> addProductDeletePartPriceCol;
    @FXML private TableView<Part> addProductAddPartTableView;
    @FXML private TableView<Part> addProductDeletePartTableView;

    /** ALERT METHOD
     * Method to show an Alert when the user adds input that doesn't fit acceptable parameters, reduces redundancy.
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

    /** GENERATE NEW PRODUCT ID
     * Method to get new product id beginning at 1000
     * @return productId for new Product instance.
     */
    public static int getProductIdCount() {
        productId++;
        return productId;
    }

    /** ADD PART TO PRODUCT
     * Method to add a Part to the new Product created when the Add button is clicked and the Part is selected.
     * @param event user clicks the Add Part button.
     */
    @FXML
    void addPartsAddProduct(ActionEvent event) {
        Part selectedPart = addProductAddPartTableView.getSelectionModel().getSelectedItem();
        // Handle null part selection
        if (selectedPart == null) {
            showAlert(Alert.AlertType.ERROR, "Error!", "No part selected.");
            // Handle duplicate attachments of parts to products
        } else if (associatedParts.contains(selectedPart)) {
            showAlert(Alert.AlertType.ERROR, "Error!", "Part already attached to product.");
        } else {
            associatedParts.add(selectedPart);
            addProductDeletePartTableView.setItems(associatedParts);
        }
    }

    /** SEARCH PART
     * Method to search the Parts in Inventory when text is entered. Searches with both partId and fuzzy match on partName.
     * @param event user pressed a key
     */
    @FXML
    void searchPartsAddProduct(KeyEvent event) {
        String searchText = addProductSearch.getText();
        try {
            int partId = Integer.parseInt(searchText);
            ObservableList<Part> partsList = Inventory.getAllParts().filtered(part ->
                    String.valueOf(part.getId()).contains(searchText));
            if (partsList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "No part found with that ID.");
            }
            addProductAddPartTableView.setItems(partsList);
        } catch (NumberFormatException e) {
            // If the input is not a valid integer, perform a partial match search by name
            ObservableList<Part> partsList = Inventory.getAllParts().filtered(part ->
                    part.getName().toLowerCase().contains(searchText.toLowerCase()));
            if (partsList.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "No part found with that name.");
            }
            addProductAddPartTableView.setItems(partsList);
        }
    }

    /** DELETE PART FROM PRODUCT
     * Method to remove an associated part from the new product being created when the user clicks the remove associated part button.
     * @param event user clicks the Remove Associated Part Button
     */
    @FXML
    void deletePartAddProduct(ActionEvent event) {
        Part selectedPart = addProductDeletePartTableView.getSelectionModel().getSelectedItem();
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Delete Associated Part", "Remove part from this product?");
        if (result.orElse(null) == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            associatedParts.remove(selectedPart);
            addProductDeletePartTableView.setItems(associatedParts);
        }
    }

    /** SAVE NEW PRODUCT
     * Method to save a new product when the user clicks the Save button. Creates new Product using the input information, save new Product to Inventory.
     * @param event the user clicks on the 'Save' button.
     * @throws IOException
     */
    @FXML
    void saveAddProduct(ActionEvent event) throws IOException {
        // Use try in case of input errors like missing values
        try {
            int id = Integer.parseInt(addProductId.getText());
            String name = addProductName.getText();
            double price = Double.parseDouble(addProductPrice.getText());
            int stock = Integer.parseInt(addProductInventory.getText());
            int max = Integer.parseInt(addProductMax.getText());
            int min = Integer.parseInt(addProductMin.getText());
            ObservableList<Part> list = associatedParts;

            // Warnings for invalid inputs
            // Empty name error
            if (name.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "A name is required.");
            }
            // Inventory input invalidated by min/max input
            else if (stock > max || stock < min) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "Inventory is not between the minimum and maximum.");
            }
            // Min/max invalidated by max/min input
            else if (min > max) {
                showAlert(Alert.AlertType.WARNING, "Warning!", "The minimum can't be greater than the maximum.");
            } else {
                // Increment id for new product, implemented here so that saved Products have new Ids, unsaved Products don't interrupt sequence
                productId = getProductIdCount();
                // Add the new Product data
                Inventory.addProduct(new Product(id, name, price, stock, min, max, list));
                // Return to Main Window
                Button sourceButton = (Button) event.getSource();
                Stage stage = (Stage) sourceButton.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
            }
        }
        // If other values missing
        catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Some fields are missing values.");
        }
    }

    /** CANCEL CHANGES, RETURN TO MAIN WINDOW
     * Method to go back to Main Window on clicking the cancel button without saving the new product.
     * @param event the user clicks on the 'Cancel' button.
     * @throws IOException
     */
    @FXML
    void cancel(ActionEvent event) throws IOException {
        // Confirm cancel action
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Cancel", "Cancel adding new product?");
        // User clicked 'OK' button, return to Main Window
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
        stage.show();
    }

    /** SETUP TABLEVIEW
     * Method to set all top parts in table view and bottom associated parts in table view, get new product id, and set new product id to id field.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get all products to fill-in AddPart table & DeletePart table which are components of AddProduct window
        addProductAddPartTableView.setItems(Inventory.getAllParts());
        addProductAddPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAddPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAddPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAddPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductDeletePartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductDeletePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductDeletePartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductDeletePartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Get next productId for new product
        addProductId.setText(String.valueOf(productId));

    }
}