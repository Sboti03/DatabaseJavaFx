package hu.petrik.databasejavafx;

import hu.petrik.data.Dog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DogUpdateController extends Controller {

    @javafx.fxml.FXML
    private TextField nameField;
    @javafx.fxml.FXML
    private Spinner<Integer> ageField;
    @javafx.fxml.FXML
    private TextField breedField;

    private Dog dog;
    private DogDB dogDB;


    @FXML
    private void initialize() {
        ageField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 50, 1));
    }

    public void handleCancel(ActionEvent actionEvent) {
        Stage stage =(Stage) nameField.getScene().getWindow();
        stage.close();
    }

    public void handleUpdate(ActionEvent actionEvent) {
        if (nameField.getText().isEmpty() || breedField.getText().isEmpty()) {
            error("Please fill all fields");
            return;
        }
        try {
            dog.setAge(ageField.getValue());
            dog.setBreed(breedField.getText().trim());
            dog.setName(nameField.getText().trim());
            if (dogDB.updateDog(dog)) {
                alert(Alert.AlertType.INFORMATION, "Success", "");
                Stage stage =(Stage) nameField.getScene().getWindow();
                stage.close();
            } else {
                error("Unsuccessful update!");
            }
        } catch (SQLException e) {
            error("Error", e.getSQLState());
        }

    }

    public Dog getDog() {
        return dog;
    }

    public void setDogDB(DogDB dogDB) {
        this.dogDB = dogDB;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
        nameField.setText(dog.getName());
        ageField.getValueFactory().setValue(dog.getAge());
        breedField.setText(dog.getBreed());
    }
}
