import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationController {
    @FXML private Label reservationIdLabel;
    @FXML private ComboBox<Guest> guestComboBox;
    @FXML private TextField guestUsernameField;
    @FXML private TextField guestBalanceField;
    @FXML private TextField preferencesField;
    @FXML private ComboBox<RoomType>roomTypeComboBox;
    @FXML private ComboBox<Room>roomComboBox;
    @FXML private DatePicker checkinPicker;
    @FXML private DatePicker checkoutPicker;
    @FXML private TextField nightsField;
    @FXML private ComboBox<PaymentMethod> paymentMethodComboBox;
    @FXML private CheckBox cbWifi, cbBreakfast, cbParking, cbSpa, cbPool;
    @FXML private Label basePriceLabel;
    @FXML private Label amenitiesPriceLabel;
    @FXML private Label nightsSummaryLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label balanceWarningLabel;
    @FXML private Label formErrorLabel;

    @FXML
    public void initialize() {
        reservationIdLabel.setText("REF: RES-" + (HotelDatabase.reservations.size() + 1001));
        guestComboBox.setItems(FXCollections.observableArrayList(HotelDatabase.guests));
        roomTypeComboBox.setItems(FXCollections.observableArrayList(HotelDatabase.roomTypes));
        paymentMethodComboBox.setItems(FXCollections.observableArrayList(PaymentMethod.values()));
        guestComboBox.setOnAction(e -> {
            Guest g = guestComboBox.getValue();
            if (g != null) {
                guestUsernameField.setText(g.getUsername());
                guestBalanceField.setText(String.format("%.2f EGP", g.getBalance()));
            }
        });
    }
    @FXML
    private void filterRoomsByType() {
        RoomType selectedType = roomTypeComboBox.getValue();
        if (selectedType == null) {
            return;
        }
        var availableRooms = HotelDatabase.rooms.stream()
                .filter(r -> r.getRoomType().getTypeId() == selectedType.getTypeId() && r.isAvailable()).toList();
        roomComboBox.setItems(FXCollections.observableArrayList(availableRooms));
        updatePriceSummary();
    }

    @FXML
    private void updatePriceSummary() {
        Room room = roomComboBox.getValue();
        LocalDate checkin = checkinPicker.getValue();
        LocalDate checkout = checkoutPicker.getValue();

        if (room == null) {
            return;
        }
        double basePrice = room.getRoomType().getBasePricePerNight();
        basePriceLabel.setText(String.format("%.2f EGP", basePrice));

        double amenitiesCost = 0;
        if (cbWifi.isSelected()) {
            amenitiesCost += 50;
        }
        if (cbBreakfast.isSelected()){
            amenitiesCost += 120;
        }
        if (cbParking.isSelected()){
            amenitiesCost += 80;
        }
        if (cbSpa.isSelected()) {
            amenitiesCost += 200;
        }
        if (cbPool.isSelected()) {
            amenitiesCost += 100;
        } amenitiesPriceLabel.setText(String.format("%.2f EGP", amenitiesCost));

        if (checkin != null && checkout != null && checkout.isAfter(checkin)) {
            long nights = ChronoUnit.DAYS.between(checkin, checkout);
            nightsField.setText(nights + " night(s)");
            nightsSummaryLabel.setText(String.valueOf(nights));
            double total = (basePrice + amenitiesCost) * nights;
            totalAmountLabel.setText(String.format("%.2f EGP", total));

            Guest g = guestComboBox.getValue();
            if (g != null && g.getBalance() < total) {
                balanceWarningLabel.setText("⚠ Guest balance (" +
                        String.format("%.2f", g.getBalance()) +
                        " EGP) is insufficient for this reservation.");
            } else {
                balanceWarningLabel.setText("");
            }
        }
    }
    @FXML private void handleConfirmReservation() {
        Guest g = guestComboBox.getValue();
        Room  room = roomComboBox.getValue();
        LocalDate cin = checkinPicker.getValue();
        LocalDate cout = checkoutPicker.getValue();
        PaymentMethod pm = paymentMethodComboBox.getValue();
        if (g == null || room == null || cin == null || cout == null || pm == null) {
            formErrorLabel.setText("Please fill in all required fields (*).");
            return;
        }
        if (!cout.isAfter(cin)) {
            formErrorLabel.setText("Check-out date must be after check-in date.");
            return;
        }
        Reservation reservation = new Reservation(g, room, cin, cout, ReservationStatus.PENDING, HotelDatabase.reservations.size() + 1001);
        HotelDatabase.addReservation(reservation);
        room.reserve();
        System.out.println("Reservation confirmed: " + reservation.getReservationId());
        formErrorLabel.setText("");
        goBack();
    }
    @FXML
    private void goBack() {
        System.out.println("Navigate back to Dashboard");
    }
}


