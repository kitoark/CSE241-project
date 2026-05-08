import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.concurrent.Task;


public class GuestController {

    public static Guest loggedInGuest;

    @FXML private Label nameLabel;
    @FXML private Label balanceLabel;
    @FXML private TextField searchField;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colRoomNumber;
    @FXML private TableColumn<Room, String> colType;
    @FXML private TableColumn<Room, Double> colPrice;
    @FXML private ListView<String> reservationsList;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;

    @FXML
    public void initialize() {
        if (loggedInGuest != null) {
            nameLabel.setText("Welcome, " + loggedInGuest.getUsername());
            balanceLabel.setText(loggedInGuest.getBalance() + " EGP");

            colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
            colType.setCellValueFactory(cellData ->
                    new javafx.beans.property.SimpleStringProperty(cellData.getValue().getRoomType().getTypeName()));
            colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPricePerNight"));

            checkInPicker.setValue(LocalDate.now());
            checkOutPicker.setValue(LocalDate.now().plusDays(1));

            refreshRooms();
            refreshReservations();
            startRealTimeRoomUpdates();
        }
    }

    private void refreshRooms() {
        ObservableList<Room> availableRooms = FXCollections.observableArrayList();
        for (Room room : HotelDatabase.rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }
        roomTable.setItems(availableRooms);
    }

    private void refreshReservations() {
        ObservableList<String> resData = FXCollections.observableArrayList();
        for (Reservation res : loggedInGuest.getMyReservations()) {
            resData.add("Room " + res.getRoom().getRoomNumber() + " | " + res.getCheckIn() + " -> " + res.getCheckOut() + " | " + res.getStatus());
        }
        reservationsList.setItems(resData);
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Room> filteredList = FXCollections.observableArrayList();

        for (Room room : HotelDatabase.rooms) {
            if (room.isAvailable() && room.getRoomType().getTypeName().toLowerCase().contains(searchText)) {
                filteredList.add(room);
            }
        }
        roomTable.setItems(filteredList);
    }

    @FXML
    private void handleBookRoom() {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();

        if (selectedRoom == null) {
            showAlert("Error", "Please select a room", Alert.AlertType.WARNING);
            return;
        }

        LocalDate checkIn = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();

        if (checkIn == null || checkOut == null) {
            showAlert("Error", "Please select check-in and check-out dates", Alert.AlertType.WARNING);
            return;
        }

        if (!checkOut.isAfter(checkIn)) {
            showAlert("Error", "Check-out date must be after check-in date", Alert.AlertType.WARNING);
            return;
        }

        if (checkIn.isBefore(LocalDate.now())) {
            showAlert("Error", "Check-in date cannot be in the past", Alert.AlertType.WARNING);
            return;
        }

        try {
            loggedInGuest.makeReservation(selectedRoom, checkIn, checkOut);
            showAlert("Success", "Room booked successfully!", Alert.AlertType.INFORMATION);
            refreshRooms();
            refreshReservations();
            balanceLabel.setText(loggedInGuest.getBalance() + " EGP");
        } catch (Exception e) {
            showAlert("Failed", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleLogout(javafx.event.ActionEvent event) {
        loggedInGuest = null;
        SceneSwitcher.goTo(event, "login.fxml");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void startRealTimeRoomUpdates() {
        Task<Void> backgroundUpdater = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!isCancelled()) {
                    Thread.sleep(5000);
                    Platform.runLater(() -> {
                        refreshRooms();
                    });
                }
                return null;
            }
        };

        Thread updaterThread = new Thread(backgroundUpdater);
        updaterThread.setDaemon(true);
        updaterThread.start();
    }
}