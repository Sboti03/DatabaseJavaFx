module hu.petrik.databasejavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens hu.petrik.databasejavafx to javafx.fxml;
    opens hu.petrik.data to javafx.base;

    exports hu.petrik.databasejavafx;
}