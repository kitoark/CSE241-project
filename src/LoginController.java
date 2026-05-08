import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private ChoiceBox<String> loginChoiceBox;
    @FXML
    private Button loginbutton;
    @FXML
    private TextField loginUsernameBox;
    @FXML
    private PasswordField loginPasswordBox;
    @FXML
    private Label errorLabel;

    private final String[] roles = {"guest","staff"};
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginChoiceBox.getItems().addAll(roles);
        loginChoiceBox.setValue("guest");
    }

    public void login(ActionEvent e){
        if (loginChoiceBox.getValue().equals("guest")){
            String username = loginUsernameBox.getText();
            String password = loginPasswordBox.getText();
            Guest guest = Guest.login(username, password);
            if (guest != null) {
                guestMenu(e);
                return;
            }
            else {
                errorLabel.setText("Login failed: invalid username or password");
            }
        }
        else if (loginChoiceBox.getValue().equals("staff")){
            String username = loginUsernameBox.getText();
            String password = loginPasswordBox.getText();
            Staff staff = Staff.login(username, password);
            if (staff != null) {
                staffMenu(e);
                return;
            }
            else {
                errorLabel.setText("Login failed: invalid username or password");
            }
        }
    }

    public void registerMenu(ActionEvent event){
        SceneSwitcher.goTo(event,"register.fxml");
    }

    public void guestMenu(ActionEvent e){
        SceneSwitcher.goTo(e,"guest.fxml");
    }

    public void staffMenu(ActionEvent e) {
        SceneSwitcher.goTo(e,"staff.fxml");
    }
    public void handleLogin(ActionEvent actionEvent) {
    }
    public void handleGuestLogin(ActionEvent actionEvent) {
    }
}
