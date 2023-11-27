module C482_Project_b {
    requires javafx.controls;
    requires javafx.fxml;

    opens wgu.c482_project_2 to javafx.fxml;
    opens controller to javafx.fxml;
    opens model to javafx.base;

    /* opens wgu.c482_project_2 to javafx.fxml; */
    /* exports wgu.c482_project_2; */
    exports controller;
    exports main;
    opens main to javafx.fxml;
}
