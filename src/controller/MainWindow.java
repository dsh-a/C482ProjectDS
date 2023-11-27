package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.lang.*;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.*;

// REVISION 2 CHECK

/** MAIN WINDOW CONTROLLER
 * MainWindow class, manages logic needed to perform user tasks on the MainWindow window.
 * @author Dalton Shultz
 */
public class MainWindow implements Initializable {

    @FXML private TableColumn<Part, Integer> mainPartIdCol;
    @FXML private TableColumn<Part, String> mainPartNameCol;
    @FXML private TableColumn<Part, Integer> mainPartInventoryCol;
    @FXML private TableColumn<Part, Double> mainPartPriceCol;
    @FXML private TableView<Part> mainPartTableView;
    @FXML private TextField mainPartSearchInput;
    @FXML private TableColumn<Product, Integer> mainProductIdCol;
    @FXML private TableColumn<Product, String> mainProductCol;
    @FXML private TableColumn<Product, Integer> mainProductInventoryCol;
    @FXML private TableColumn<Product, Double> mainProductPriceCol;
    @FXML private TableView<Product> mainProductTableView;
    @FXML private TextField mainProductSearchInput;

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

    /** LOAD ADD PART WINDOW
     * Method loads the AddPart window when the Add Button is clicked in the Parts section.
     * @param event user clicks the Add button in the Part section.
     * @throws IOException
     */
    @FXML
    void addPart(ActionEvent event) throws IOException {
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"))));
        stage.show();
    }

    /** SEARCH PARTS
     * Method searches the Parts in Inventory when the Search button is clicked in the Parts section.
     * @param event user clicks the Search button in the Part section.
     */
    @FXML
    void searchPart(KeyEvent event) {
        // Input stored in local variable with case removed
        String searchInput = mainPartSearchInput.getText().toLowerCase();
        ObservableList<Part> partList;
        // If the search input is empty, refresh the list with all products
        if (searchInput.isEmpty()) {
            mainPartTableView.setItems(Inventory.getAllParts());
            return;
        }
        // Filter partList based on results of getId OR getName
        // Checking for integer value in input
        if (searchInput.matches("\\d+")) {
            partList = FXCollections.observableArrayList(
                    Inventory.lookupPart(Integer.parseInt(searchInput)));
        } else {
            partList = FXCollections.observableArrayList(Inventory.lookupPart(searchInput));
        }
        // Set the partList to display
        mainPartTableView.setItems(partList);
        // If no products are found, display warning
        if (partList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "No product found with that name or ID.");
        }
    }

    /** LOAD MODIFY PARTS WINDOW
     * Method loads the ModifyPart window when the Modify button in the Parts section is clicked.
     * @param event user selected a Part and then clicked the Modify button in the Parts section.
     * @throws IOException
     */
    @FXML
    void modifyPart(ActionEvent event) throws IOException {
        Part selectedPart = mainPartTableView.getSelectionModel().getSelectedItem();
        // If no part selected, display warning and do not proceed to Mod Part window
        if (selectedPart == null) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "No part selected.");
        // Proceed to Mod Part window
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();
            ModifyPart modPartControl = loader.getController();
            modPartControl.sendPart(selectedPart);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.getRoot()));
            stage.show();
        }
    }

    /** DELETE PART
     * Method to delete a Part from Inventory on clicking the Delete button in the Parts section.
     * @param event user clicks the Delete button in the Parts section after selecting a Part.
     * @throws IOException
     */
    @FXML
    void deletePart(ActionEvent event) throws IOException {
        Part part = mainPartTableView.getSelectionModel().getSelectedItem();
        // Check for no part selected, alert, stay on page
        if (mainPartTableView.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Error!", "Select a part to be deleted.");
        } else {
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,
                    "Confirm Deletion", "Deleted parts are not recoverable, are you sure you want to proceed?");
            if (result.orElse(null) == ButtonType.OK) {
                Button sourceButton = (Button) event.getSource();
                Stage stage = (Stage) sourceButton.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
                Inventory.deletePart(part);
            } else {
                Button sourceButton = (Button) event.getSource();
                Stage stage = (Stage) sourceButton.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
            }
        }
    }

    /** LOAD ADD PRODUCTS WINDOW
     * Method loads the AddProduct window on clicking the Add Button in the Products section.
     * @param event user clicks the Add button in the Products section.
     * @throws IOException
     */
    @FXML
    void addProduct(ActionEvent event) throws IOException {
        Button sourceButton = (Button) event.getSource();
        Stage stage = (Stage) sourceButton.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"))));
        stage.show();
    }

    /** SEARCH PRODUCT
     * Method to search the Inventory for Products on input to textField; includes exact id search and fuzzy match on name.
     * @param event user inputs characters in textField in the Products section.
     */
    @FXML
    void searchProduct(KeyEvent event) {
        String searchInput = mainProductSearchInput.getText().toLowerCase();
        ObservableList<Product> productList;
        // If the search input is empty, return all
        if (searchInput.isEmpty()) {
            mainProductTableView.setItems(Inventory.getAllProducts());
            return;
        }
        // Check for integer values in input
        if (searchInput.matches("\\d+")) {
            productList = FXCollections.observableArrayList(
                    Inventory.lookupProduct(Integer.parseInt(searchInput)));
        } else {
            productList = FXCollections.observableArrayList(Inventory.lookupProduct(searchInput));
        }
        mainProductTableView.setItems(productList);
        // If no products found, alert
        if (productList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "No product found with that name or ID.");
        }
    }

    /** LOAD MODIFY PRODUCT WINDOW
     * Method loads the ModifyProduct window on clicking the Modify button in the Product section.
     * @param event user clicks the Modify button in the Products section while a Product is selected.
     * @throws IOException
     */
    @FXML
    void modifyProduct(ActionEvent event) throws IOException {
        Product selectedProduct = mainProductTableView.getSelectionModel().getSelectedItem();
        // When no product selected, alert, return to MainWindow
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "You haven't selected a product.");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();
            ModifyProduct modProductControl = loader.getController();
            modProductControl.sendProduct(selectedProduct);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.getRoot()));
            stage.show();
        }
    }

    /** DELETE PRODUCT
     * Method to delete a Product in Inventory when Delete button is clicked in Product section.
     * @param event user clicks the Delete button while a Product is selected.
     * @throws IOException
     */
    @FXML
    void deleteProduct(ActionEvent event) throws IOException {
        Product product = mainProductTableView.getSelectionModel().getSelectedItem();
        if (mainProductTableView.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.ERROR, "Error!", "Select a part to be deleted.");
        } else {
            // Warn user, data is not recoverable
            Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION,
                    "Confirm Deletion", "Deleted products are not recoverable, are you sure you want to proceed?");
            if (result.orElse(null) == ButtonType.OK) {
                Button sourceButton = (Button) event.getSource();
                Stage stage = (Stage) sourceButton.getScene().getWindow();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"))));
                stage.show();
                // Check for associated parts, alert on non-empty list
                if (product.getAssociatedParts().isEmpty()) {
                    Inventory.deleteProduct(product);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error!", "The product has associated parts.");
                }
            }
        }
    }

    /** EXIT APPLICATION
     * Method exits the application on clicking the Exit Button.
     * @param event user clicks the
     */
    @FXML
    void exitApp(ActionEvent event) {
        Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Confirm Exit", "Close application?");
        if (result.orElse(null) == ButtonType.OK) {
            System.exit(0);
        }
    }

    /** TABLES INITIALIZE
     * Method to set all Parts in the table view and all Products in the table view.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load all Parts & Products
        mainPartTableView.setItems(Inventory.getAllParts());
        mainProductTableView.setItems(Inventory.getAllProducts());
        // Fill columns with part data
        mainPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Fill columns with product data
        mainProductIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
