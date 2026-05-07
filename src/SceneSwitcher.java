import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneSwitcher {
    public static void goTo(ActionEvent event, String fxmlFile) {
        try {
            // 1. Load the FXML
            Parent root = FXMLLoader.load(SceneSwitcher.class.getResource(fxmlFile));

            // 2. Get the current Stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Set and Show
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error: Could not find " + fxmlFile);
            e.printStackTrace();
        }
    }
}