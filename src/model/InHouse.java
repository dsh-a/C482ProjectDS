package model;

// REVISION 1 CHECK

/** INHOUSE CLASS
 * Model for InHouse objects, an extension of the Part object
 * Built class InHouse.java
 * @author Dalton Shultz
 */

public class InHouse extends Part {
    /**
     * The machine id for the machine that produced the Part.
     */
    private int machineId;

    /** INHOUSE CONSTRUCTOR
     * Constructor for new InHouse Part instance.
     * @param id of the InHouse Part.
     * @param name for the InHouse Part.
     * @param price for the InHouse Part.
     * @param stock in inventory of InHouse Part.
     * @param min minimum level of inventory for the InHouse Part.
     * @param max maximum level of inventory for the InHouse Part.
     * @param machineId for the machine which produced the Part.
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /** GET MACHINEID
     * Getter for machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /** SET MACHINEID
     * Setter for machineId
     * @param machineId the unique identifier of the machine where the Part was made.
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}