module hu.petrik.databasejavafx {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens hu.petrik.databasejavafx to javafx.fxml;
    exports hu.petrik.databasejavafx;
}