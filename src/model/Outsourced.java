package model;

// REVISION 2 CHECK

/** OUTSOURCED CLASS
 * Model for Outsourced objects, an extension of the Part object.
 * @author Dalton Shultz
 */

public class Outsourced extends Part {
    /**
     * The name of the company who produced the Part.
     */
    private String companyName;

    /** OUTSOURCED CONSTRUCTOR
     * For new Outsourced Part instances
     * @param id of the Outsourced part.
     * @param name for the Outsourced part.
     * @param price for the Outsourced part.
     * @param stock in inventory of Outsourced part.
     * @param min minimum level of inventory for the Outsourced part.
     * @param max maximum level of inventory for the Outsourced part.
     * @param companyName name of the company who produced the part.
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    /** GET COMPANY NAME
     * Getter for companyName
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /** SET COMPANY NAME
     * Setter for companyName
     * @param companyName the identifier for the company who produced the Part
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}