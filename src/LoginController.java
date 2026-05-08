import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private ComboBox<String> loginChoiceBox;
    @FXML private TextField        loginUsernameBox;
    @FXML private PasswordField    loginPasswordBox;
    @FXML private Label            errorLabel;

    private final String[] roles = {"guest", "staff"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginChoiceBox.getItems().addAll(roles);
        loginChoiceBox.setValue("guest");
    }

    public void login(ActionEvent e) {
        String username = loginUsernameBox.getText().trim();
        String password = loginPasswordBox.getText();

        if (loginChoiceBox.getValue().equals("guest")) {
            Guest guest = Guest.login(username, password);
            if (guest != null) {
                GuestController.loggedInGuest = guest;
                SceneSwitcher.goTo(e, "guest.fxml");
            } else {
                errorLabel.setText("Login failed: invalid username or password.");
            }

        } else { // staff
            Staff staff = Staff.login(username, password);

            if (staff != null && staff.getRole() == Role.ADMIN) {
                // ── Pass the logged-in admin to the dashboard controller ──
                AdminDashboardController.loggedInAdmin = staff;
                SceneSwitcher.goTo(e, "adminDashboard.fxml");

            } else if (staff != null && staff.getRole() == Role.RECEPTIONIST) {
                SceneSwitcher.goTo(e, "receptionistDashboard.fxml");

            } else {
                errorLabel.setText("Login failed: invalid username or password.");
            }
        }
    }

    public void registerMenu(ActionEvent event) {
        SceneSwitcher.goTo(event, "register.fxml");
    }
}