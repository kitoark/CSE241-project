import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {
    public static String errorLabelText;

    @FXML private Button registerbutton;
    @FXML private TextField registerUsernameBox;
    @FXML private TextField roomPrefrences;
    @FXML private PasswordField registerPasswordBox;
    @FXML private TextField addressBox;
    @FXML private Label errorLabel;
    @FXML private DatePicker dobselector;
    @FXML private RadioButton maleR;
    @FXML private RadioButton femaleR;
    @FXML private Spinner<Double> balanceBox;

    public void register(ActionEvent event){
        String username = registerUsernameBox.getText();
        String password = registerPasswordBox.getText();
        LocalDate dob = dobselector.getValue();
        String address = addressBox.getText();
        String prefs = roomPrefrences.getText();
        Gender gender = null;
        if (maleR.isSelected()){
            gender = Gender.MALE;
        } else if (femaleR.isSelected()){
            gender = Gender.FEMALE;
        } else {
            errorLabel.setText("Please select your gender.");
            return;
        }
        double balance = balanceBox.getValue();
        try {
            Guest guest = new Guest(username, password, dob, balance, address, gender, prefs);
            if (guest.register()) {
                System.out.println("Registration successful! You can now log in.");
                loginMenu(event);
            } else {
                errorLabel.setText("Username already taken. Try another one.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
        errorLabel.setText(errorLabelText);
    }


    public void loginMenu(ActionEvent event){
        SceneSwitcher.goTo(event,"login.fxml");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0,999999.9);
        balanceBox.setEditable(true);
        valueFactory.setValue(0.0);
        balanceBox.setValueFactory(valueFactory);
        balanceBox.getEditor().setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches("\\d*(\\.\\d*)?") ? c : null));
        balanceBox.focusedProperty().addListener((o, old, isFocused) -> {
            if (!isFocused) balanceBox.increment(0);
        });
    }
}