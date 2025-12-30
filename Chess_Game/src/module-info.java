module Chess_Game {
    requires javafx.controls;
    requires javafx.graphics;
    
    // You MUST open all 3 packages that JavaFX needs to access
    opens chess_application.model to javafx.graphics;
    opens chess_application.view to javafx.graphics;
    opens chess_application.controller to javafx.graphics;
}