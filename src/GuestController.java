import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class GuestController {

    public static Guest loggedInGuest;
    @FXML private Label nameLabel;
    @FXML private Label balanceLabel;
    @FXML private TextField searchField;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private ComboBox<String> amenityFilter;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colRoomNumber;
    @FXML private TableColumn<Room, String>  colType;
    @FXML private TableColumn<Room, Double>  colPrice;
    @FXML private TableColumn<Room, String>  colAmenities;
    @FXML private DatePicker checkInPicker;
    @FXML private DatePicker checkOutPicker;
    @FXML private ListView<String> reservationsList;
    @FXML private Label            reservationStatusLabel;

    @FXML
    public void initialize() {
        if (loggedInGuest == null) return;

        nameLabel.setText("Welcome, " + loggedInGuest.getUsername());
        balanceLabel.setText(String.format("%.2f EGP", loggedInGuest.getBalance()));

        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colType.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getRoomType().getTypeName()));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPricePerNight"));
        colAmenities.setCellValueFactory(cell -> {
            String amenities = cell.getValue().getAmenities().stream()
                    .map(Amenity::getName)
                    .collect(Collectors.joining(", "));
            return new javafx.beans.property.SimpleStringProperty(
                    amenities.isEmpty() ? "None" : amenities);
        });

        checkInPicker.setValue(LocalDate.now());
        checkOutPicker.setValue(LocalDate.now().plusDays(1));

        ObservableList<String> amenityNames = FXCollections.observableArrayList();
        amenityNames.add("All Amenities");
        HotelDatabase.amenitiesDatabase.forEach(a -> amenityNames.add(a.getName()));
        amenityFilter.setItems(amenityNames);
        amenityFilter.setValue("All Amenities");

        refreshRooms();
        refreshReservations();
        startRealTimeRoomUpdates();
    }


    @FXML
    private void handleFilter() {
        String typeSearch  = searchField.getText().toLowerCase().trim();
        String selectedAmenity = amenityFilter.getValue();

        double minPrice = 0;
        double maxPrice = Double.MAX_VALUE;
        try { minPrice = Double.parseDouble(minPriceField.getText().trim()); } catch (NumberFormatException ignored) {}
        try { maxPrice = Double.parseDouble(maxPriceField.getText().trim()); } catch (NumberFormatException ignored) {}

        final double fMin = minPrice;
        final double fMax = maxPrice;

        ObservableList<Room> filtered = FXCollections.observableArrayList();
        for (Room room : HotelDatabase.rooms) {
            if (!room.isAvailable()) continue;

            if (!typeSearch.isEmpty() &&
                    !room.getRoomType().getTypeName().toLowerCase().contains(typeSearch)) continue;

            double price = room.getTotalPricePerNight();
            if (price < fMin || price > fMax) continue;

            if (selectedAmenity != null && !selectedAmenity.equals("All Amenities")) {
                boolean hasAmenity = room.getAmenities().stream()
                        .anyMatch(a -> a.getName().equalsIgnoreCase(selectedAmenity));
                if (!hasAmenity) continue;
            }

            filtered.add(room);
        }
        roomTable.setItems(filtered);
    }

    @FXML
    private void handleSearch() {
        handleFilter();
    }

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        minPriceField.clear();
        maxPriceField.clear();
        amenityFilter.setValue("All Amenities");
        refreshRooms();
    }


    @FXML
    private void handleBookRoom() {
        Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();
        if (selectedRoom == null) {
            showAlert("Error", "Please select a room.", Alert.AlertType.WARNING);
            return;
        }

        LocalDate checkIn  = checkInPicker.getValue();
        LocalDate checkOut = checkOutPicker.getValue();

        if (checkIn == null || checkOut == null) {
            showAlert("Error", "Please select check-in and check-out dates.", Alert.AlertType.WARNING);
            return;
        }
        if (!checkOut.isAfter(checkIn)) {
            showAlert("Error", "Check-out date must be after check-in date.", Alert.AlertType.WARNING);
            return;
        }
        if (checkIn.isBefore(LocalDate.now())) {
            showAlert("Error", "Check-in date cannot be in the past.", Alert.AlertType.WARNING);
            return;
        }

        try {
            loggedInGuest.makeReservation(selectedRoom, checkIn, checkOut);
            showAlert("Success", "Room booked successfully!", Alert.AlertType.INFORMATION);
            refreshRooms();
            refreshReservations();
            balanceLabel.setText(String.format("%.2f EGP", loggedInGuest.getBalance()));
        } catch (Exception e) {
            showAlert("Booking Failed", e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    @FXML
    private void handleCancelReservation() {
        int idx = reservationsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            setReservationStatus("Please select a reservation first.", false);
            return;
        }

        Reservation res = loggedInGuest.getMyReservations().get(idx);

        if (res.getStatus() == ReservationStatus.COMPLETED ||
            res.getStatus() == ReservationStatus.CANCELLED) {
            setReservationStatus("Cannot cancel a " + res.getStatus() + " reservation.", false);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancel reservation for Room " + res.getRoom().getRoomNumber() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.setTitle("Confirm Cancellation");
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                loggedInGuest.cancelReservation(res);
                refreshRooms();
                refreshReservations();
                setReservationStatus("Reservation cancelled.", true);
            }
        });
    }

    @FXML
    private void handleCheckout(ActionEvent event) {
        int idx = reservationsList.getSelectionModel().getSelectedIndex();
        if (idx < 0) {
            setReservationStatus("Please select a reservation first.", false);
            return;
        }

        Reservation res = loggedInGuest.getMyReservations().get(idx);

        if (res.getStatus() != ReservationStatus.CONFIRMED) {
            setReservationStatus(
                "Only CONFIRMED reservations can be checked out.\nStatus: " + res.getStatus(),
                false);
            return;
        }

        CheckoutPaymentController.reservationToCheckout = res;
        SceneSwitcher.goTo(event, "Checkout.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        loggedInGuest = null;
        SceneSwitcher.goTo(event, "login.fxml");
    }

    private void refreshRooms() {
        ObservableList<Room> available = FXCollections.observableArrayList();
        for (Room room : HotelDatabase.rooms) {
            if (room.isAvailable()) available.add(room);
        }
        roomTable.setItems(available);
    }

    private void refreshReservations() {
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Reservation res : loggedInGuest.getMyReservations()) {
            items.add("Room " + res.getRoom().getRoomNumber()
                    + " | " + res.getCheckIn() + " → " + res.getCheckOut()
                    + " | " + res.getStatus());
        }
        reservationsList.setItems(items);
    }

    private void setReservationStatus(String message, boolean success) {
        reservationStatusLabel.setText(message);
        reservationStatusLabel.setStyle(success
                ? "-fx-text-fill: #16a34a; -fx-font-weight: bold;"
                : "-fx-text-fill: #dc2626; -fx-font-weight: bold;");
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void startRealTimeRoomUpdates() {
        Task<Void> updater = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (!isCancelled()) {
                    Thread.sleep(5000);
                    Platform.runLater(() -> {
                        if (!searchField.getText().isEmpty()
                                || !minPriceField.getText().isEmpty()
                                || !maxPriceField.getText().isEmpty()
                                || (amenityFilter.getValue() != null
                                    && !amenityFilter.getValue().equals("All Amenities"))) {
                            handleFilter();
                        } else {
                            refreshRooms();
                        }
                    });
                }
                return null;
            }
        };
        Thread t = new Thread(updater);
        t.setDaemon(true);
        t.start();
    }
}
