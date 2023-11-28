package model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// REVISION 2 CHECK

/** PRODUCT CLASS
 * The model for a Product object which includes several methods and attributes
 * to manage parts associated with the Product, associatedParts methods.
 * @author Dalton Shultz
 */

public class Product {
    /**
     * The id of the Product, a unique identifier for each Product instance.
     */
    private int id;
    /**
     * The name of the Product.
     */
    private String name;
    /**
     * The price for the Product.
     */
    private double price;
    /**
     * The stock/inventory of the Product.
     */
    private int stock;
    /**
     * The minimum stock/inventory of the Product.
     */
    private int min;
    /**
     * The maximum stock/inventory of the Product.
     */
    private int max;
    /**
     * Parts associated with the Product.
     */
    private ObservableList<Part> associatedParts;

    /** PRODUCT CONSTRUCTOR
     * The constructor for a Product instance.
     * @param id uniquely identifies each instance of Product.
     * @param name the formal name for the Product.
     * @param price the price for the Product.
     * @param stock the stock/inventory of the Product.
     * @param min the minimum stock/inventory for the Product.
     * @param max the maximum stock/inventory for the Product.
     * @param associatedParts a list of Parts associated with producing the Product.
     */

    public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts) {
        // Use conditional to prevent NullPointerException by initializing with FXC if null.
        this.associatedParts = (associatedParts != null) ? associatedParts : FXCollections.observableArrayList();
        // Set other params
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    /** GET ID
     * Getter for id
     * @return id of the Product instance.
     */
    public int getId() {
        return id;
    }

    /** SET ID
     * Setter for id
     * @param id of the Product instance.
     */
    public void setId(int id) {
        this.id = id;
    }

    /** GET NAME
     * Getter for name
     * @return name of the Product instance.
     */
    public String getName() {
        return name;
    }

    /** SET NAME
     * Setter for name
     * @param name of the Product instance.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** GET PRICE
     * Getter for price
     * @return price of the Product instance.
     */
    public double getPrice() {
        return price;
    }

    /** SET PRICE
     * Setter for price
     * @param price of the Product instance.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /** GET STOCK
     * Getter for stock
     * @return stock of the Product instance.
     */
    public int getStock() {
        return stock;
    }

    /** SET STOCK
     * Setter for stock
     * @param stock of the Product instance.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** GET MIN
     * Getter for min
     * @return min of the Product instance.
     */
    public int getMin() {
        return min;
    }

    /** SET MIN
     * Setter for min
     * @param min of the Product instance.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /** GET MAX
     * Getter for max
     * @return max of the Product instance.
     */
    public int getMax() {
        return max;
    }

    /** SET MAX
     * Setter for max
     * @param max of the Product instance.
     */
    public void setMax(int max) {
        this.max = max;
    }

    // AssociatedParts (list) methods
    /** GET ASSOCIATED PARTS
     * Getter for associatedParts
     * @return List of Parts associated with the Product.
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /** SET ASSOCIATED PARTS
     * Setter for associatedParts
     * @param associatedParts Parts associated with the Product.
     */
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    /** ADD ASSOCIATED PART
     * Adds a Part to the List of Parts (AssociatedParts) associated with the Product.
     * @param selectedPart identifies which Part to add to AssociatedParts.
     */
    public void addAssociatedPart(Part selectedPart) {
        this.associatedParts.add(selectedPart);
    }

    /** DELETE ASSOCIATED PART
     * Deletes a Part from the List of Parts (AssociatedParts) associated with the Product.
     * @param selectedPart identifies which Part to delete from AssociatedParts.
     * @return boolean
     */
    public boolean deleteAssociatedParts(Part selectedPart) {
        if (associatedParts.contains(selectedPart)) {
            associatedParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }
}