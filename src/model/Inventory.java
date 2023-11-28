package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

// REVISION 2 CHECK

/** INVENTORY CLASS
 * Model for Inventory objects
 * Built class Inventory.java
 * @author Dalton Shultz
 * Creates an object Inventory composed of CRUD methods and two Lists of Product & Part objects.
 *
 * <p><b>RUNTIME ERROR:</b> When I added the "addTestProducts()" method for my testing purposes, I inadvertently created
 * a runtime error. I initialized the test products with 'null' as the associatedParts value; when trying to modify
 * those products later an error was thrown in my IntelliJ terminal because the getAssociatedParts method returned null.
 * This caused a NullPointerException, so I fixed that by adding a conditional statement in the Product class on the
 * variable to return FXCollection.observableArrayList instead of associatedParts to avoid the NPE while preserving
 * function for the method.</p>
 */
public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /** ALERT METHOD
     * Method to show an Alert when the user adds input that doesn't fit acceptable parameters.
     * @param type
     * @param title
     * @param dialog
     * @return showAndWait()
     */
    private static Optional<ButtonType> showAlert(Alert.AlertType type, String title, String dialog) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(dialog);
        return alert.showAndWait();
    }

    // Add Methods
    /** ADD PART
     * Method for adding a Part to Inventory
     * @param newPart the new instance of Part to be added.
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /** ADD PRODUCT
     * Method for adding a Product to Inventory
     * @param newProduct the new instance of Product to be added.
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    // Search Methods
    /** SEARCH PART BY ID
     * Method to search for Parts by id.
     * When called, the method searches the Parts in Inventory by partId and returns matches if any exist.
     * @param partId argument in the search for Part(s).
     * @return Part(s) from argument partId.
     */
    public static ObservableList<Part> lookupPart(int partId) {
        return allParts.filtered(part -> Integer.toString(part.getId()).contains(Integer.toString(partId)));
    }

    /** SEARCH PART BY NAME
     * Method to search Parts by name.
     * When called, the method searches the Parts in Inventory by name and returns matches if any exist.
     * @param partName argument in the search for Part(s).
     * @return Part(s) from argument partName.
     */
    public static ObservableList<Part> lookupPart(String partName) {
        return allParts.filtered(part -> part.getName().toLowerCase().contains(partName.toLowerCase()));
    }

    /** SEARCH PRODUCT BY ID
     * Method to search for Product matching argument productId
     * When called, the method searches the Products in Inventory by productId and returns matches if any exist.
     * @param productId argument in the search for Product(s).
     * @return Product from the argument productId.
     */
    public static ObservableList<Product> lookupProduct(int productId) {
        return allProducts.filtered(product -> Integer.toString(product.getId()).contains(Integer.toString(productId)));
    }

    /** SEARCH PRODUCT BY NAME
     * Method to search for Products by name.
     * When called, the method searches the Products in Inventory by name and returns matches if any exist.
     * @param productName argument in the search for Product(s).
     * @return Product(s) from argument productName.
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        return allProducts.filtered(product -> product.getName().toLowerCase().contains(productName.toLowerCase()));
    }

    // Get methods
    /** GET ALL PARTS
     * Method to get all Part(s) in Inventory.
     * When called, the method returns all Part(s) in Inventory.
     * @return All Part(s) in Inventory.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** GET ALL PRODUCTS
     * Method to get all Product(s) in Inventory.
     * When called, the method returns all Product(s) in Inventory.
     * @return All Product(s) in Inventory.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    // Update Methods
    /** UPDATE PART
     * Method to update a Part, -1 is returned if the part is not found, if the Part is found
     * the part at that index is replaced with the new part.
     * @param id takes argument id of Part to update.
     * @param selectedPart represents the Part to update.
     */
    public static void updatePart(int id, Part selectedPart) {
        int index = getAllParts().indexOf(lookupPart(id));
        // Part found, replace part, -1 is only returned when part not found
        if (index != -1) {
            getAllParts().set(index, selectedPart);
        }
    }

    /** UPDATE PRODUCT
     * Method to update a Product, -1 is returned if the product is not found, if the Product is found
     * the product at that index is replaced with the new product.
     * @param id, takes argument id of Product to update.
     * @param newProduct represents the Product to update.
     */
    public static void updateProduct(int id, Product newProduct) {
        int index = getAllProducts().indexOf(lookupProduct(id));
        // Product found, replace product, -1 is only returned when product not found
        if (index != -1) {
            getAllProducts().set(index, newProduct);
        }
    }

    // Delete Methods
    /** DELETE PART
     * Method to delete a Part from Inventory.
     * If the Part is associated with a Product in Inventory, an alert is displayed to warn the user.
     * @param selectedPart argument for which Part to delete.
     */
    public static void deletePart(Part selectedPart) {
        // Check if part is associated with a Product
        if (allProducts.stream().anyMatch(product -> product.getAssociatedParts().contains(selectedPart))) {
            showAlert(Alert.AlertType.WARNING, "Warning!", "Part is associated with an existing Product!");
        } else {
            allParts.remove(selectedPart);
        }
    }

    /** DELETE PRODUCT
     * Method to delete a Product from Inventory.
     * @param selectedProduct argument for which  Product to delete.
     */
    public static void deleteProduct(Product selectedProduct) {
        allProducts.remove(selectedProduct);
    }

    // Call testParts & testProducts
    static {
        addTestParts();
        addTestProducts();
    }

    // Test parts to initialize application with
    private static void addTestParts() {
        addPart(new InHouse(999, "In-House Sample A", 3.99, 4, 1, 5, 100));
        addPart(new InHouse(998, "In-House Sample B", 10.99, 5, 2, 10, 101));
        addPart(new InHouse(997, "In-House Sample C", 2.99, 2, 1, 3, 102));
        addPart(new Outsourced(996, "Outsourced Sample A", 3.99, 2, 1, 3, "SampleMart"));
        addPart(new Outsourced(995, "Outsourced Sample B", 4.99, 3, 1, 6, "Samples.com"));
        addPart(new Outsourced(994, "Outsourced Sample C", 11.99, 5, 0, 20, "SampleScientific, Inc."));
    }
    // Test products to initialize application with
    private static void addTestProducts() {
        addProduct(new Product(1999, "Product Sample A", 43.99, 7, 1, 10, null));
        addProduct(new Product(1998, "Product Sample B", 110.99, 5, 2, 10, null));
        addProduct(new Product(1997, "Product Sample C", 12.99, 4, 1, 13, null));
    }
}