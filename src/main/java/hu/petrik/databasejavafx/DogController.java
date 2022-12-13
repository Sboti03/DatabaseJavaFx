package hu.petrik.databasejavafx;

import hu.petrik.data.Dog;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.LightBase;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DogController extends Controller {

    @FXML
    private TableView<Dog> dogTable;
    @FXML
    private TableColumn<Dog, String> nameCol;
    @FXML
    private TableColumn<Dog, Integer> ageCol;
    @FXML
    private TableColumn<Dog, String> breedCol;

    private DogDB database;
    @FXML
    private Button insertBtn;
    @FXML
    private Button updateBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField nameField;
    @FXML
    private Spinner<Integer> ageField;
    @FXML
    private TextField breedField;


    @FXML
    private void initialize() {
        ageField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,50, 1));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed"));

        Platform.runLater(() -> {
            try {
                loadDogsFromServer();
            } catch (Exception e) {
                error("Nem sikerült betölteni a szervert", e.getMessage());
                Platform.exit();
            }
        });
    }

    private void loadDogsFromServer() throws SQLException {
        dogTable.getItems().clear();
        database = new DogDB();
        List<Dog> dogs = database.getDogs();
        for (Dog d : dogs) {
            dogTable.getItems().add(d);
        }
    }

    public void handleUpdate(ActionEvent actionEvent) {
        int selectedIndex = dogTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Please select a dog from the list!");
            return;
        }
        Dog selectedDog = dogTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("dog-update-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Update dog");

            DogUpdateController controller = fxmlLoader.getController();
            controller.setDog(selectedDog);
            controller.setDogDB(database);

            stage.setScene(scene);
            stage.show();
            insertBtn.setDisable(true);
            updateBtn.setDisable(true);
            deleteBtn.setDisable(true);
            stage.setOnHidden(e -> {
                insertBtn.setDisable(false);
                updateBtn.setDisable(false);
                deleteBtn.setDisable(false);
                try {
                    loadDogsFromServer();
                } catch (SQLException ex) {
                    error("An error occurred while communicating with the server");
                }
            });

        } catch (Exception e) {
            error("Could not load form", e.getMessage());
        }
    }

    public void handleDelete(ActionEvent actionEvent) {
        int selectedIndex = dogTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Please select a dog from the list!");
            return;
        }
        Dog selectedDog = dogTable.getSelectionModel().getSelectedItem();
        try {
            database.deleteDog(selectedDog.getId());
            loadDogsFromServer();
        } catch (SQLException e) {
            error("Error: ", e.getSQLState());
        }

    }

    public void handleInsert(ActionEvent actionEvent) {
        if (nameField.getText().isEmpty() || breedField.getText().isEmpty()) {
            warning("Please field all fields");
            return;
        }
        Dog dog = new Dog(nameField.getText().trim(), ageField.getValue(), breedField.getText().trim());
        try {
            database.insertDog(dog);
            loadDogsFromServer();
        } catch (SQLException e) {
            error("Error: ", e.getSQLState());
        }
    }
}