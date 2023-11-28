package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** DOCUMENTATION
 * <h1> Inventory Management System </h1>
 * This class creates an application that allows the user to manage an inventory composed of Parts and Products.
 * @author Dalton Shultz
 *
 * <p><b>FUTURE ENHANCEMENT:</b> Implementing a 'Tab' at the top of the Main Window or a sidebar that lets the users select
 * through TableViews focused on, 1. All Parts & Products, 2. Products, & 3. Parts, would make implementing a Filter
 * field and button more practical, and increase work efficiency for larger inventories.</p>
 *
 * <p><b>FUTURE ENHANCEMENT:</b> Implementing a 'LocationId' field on Inventory would enable quickly finding quantities of Parts
 * to ship out of the facility by linking the Inventory for the Part(s)/Product(s) to a new model 'Location'.</p>
 *
 * <p><b>FUTURE ENHANCEMENT:</b> Adding - and + buttons to the left and right of the Min, Max, & Inventory fields could
 * enhance the user experience by increasing the ergonomics of the window and reducing typos.</p>
 *
 * <p><b>LOGICAL ERROR:</b> I declared a local variable 'id' inside the AddPart.saveAddPart method which prevented the
 * member variable 'id' from being incremented; I had thought to set the class member variable equal to this local
 * variable after the method's execution but I had several different logical errors in output so I removed the local
 * variable and changed references to the class member variable and saved each new increment in id instead of locally.
 * </p>
 *
 * <p><b>RUNTIME ERROR:</b> When I added the "addTestProducts()" method for my testing purposes, I inadvertently created
 * a runtime error. I initialized the test products with 'null' as the associatedParts value; when trying to modify
 * those products later an error was thrown in my IntelliJ terminal because the getAssociatedParts method returned null.
 * This caused a NullPointerException, so I fixed that by adding a conditional statement in the Product class on the
 * variable to return FXCollection.observableArrayList instead of associatedParts to avoid the NPE while preserving
 * function for the method.</p>
 */

public class App extends Application {

    /** APP TITLE
     * Added a variable in case title changes for maintainability.
     */
    private static final String MAIN_TITLE = "Inventory Management System";

    /** LOAD MAIN WINDOW
     * The Main Window is loaded.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainWindow.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setTitle(MAIN_TITLE);
        primaryStage.show();
    }

    /** START APP
     * The method that starts the application.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
