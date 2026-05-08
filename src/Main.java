import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        HotelDatabase.createDummyData();
        if (!HotelDatabase.guests.isEmpty()) {
            GuestController.loggedInGuest = HotelDatabase.guests.get(0);
        }
        Application.launch(Main.class, args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setTitle("Grand Hotel");
        stage.setScene(scene);
        stage.show();
    }
}
