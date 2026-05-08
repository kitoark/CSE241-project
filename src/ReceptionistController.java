import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.time.LocalDate;

public class ReceptionistController {

    // متغير لتخزين موظف الاستقبال الذي سجل دخوله
    public static Staff currentStaff;

    @FXML private Label staffNameLabel, contentTitle;
    @FXML private Label statGuestsLabel, statRoomsLabel, statResLabel, feedbackLabel;
    @FXML private TextField searchField;
    @FXML private TableView mainTable;
    @FXML private Button btnGuests, btnRooms, btnRes;
    @FXML private HBox actionBox;
    @FXML private ComboBox<PaymentMethod> paymentMethodBox;

    private String currentMode = "RESERVATIONS";

    @FXML
    public void initialize() {
        if (currentStaff != null) {
            staffNameLabel.setText("Welcome, " + currentStaff.getUsername());
        }
        paymentMethodBox.setItems(FXCollections.observableArrayList(PaymentMethod.values()));
        showReservations(); // عرض الحجوزات كصفحة رئيسية
    }

    // دالة لتحديث الواجهة والجدول بناءً على الزر المضغوط
    private void updateUI(Button activeBtn, String title, String mode) {
        btnGuests.getStyleClass().remove("nav-btn-active");
        btnRooms.getStyleClass().remove("nav-btn-active");
        btnRes.getStyleClass().remove("nav-btn-active");

        activeBtn.getStyleClass().add("nav-btn-active");
        contentTitle.setText(title);
        currentMode = mode;
        searchField.clear();
        mainTable.getColumns().clear();
        feedbackLabel.setText("");

        // إظهار أزرار الـ Check-In و Check-Out فقط في صفحة الحجوزات
        actionBox.setVisible(mode.equals("RESERVATIONS"));
        actionBox.setManaged(mode.equals("RESERVATIONS"));

        refreshStats();
    }

    private void refreshStats() {
        int avail = 0;
        for (Room r : HotelDatabase.rooms) if (r.isAvailable()) avail++;
        statRoomsLabel.setText(String.valueOf(avail));
        statGuestsLabel.setText(String.valueOf(HotelDatabase.guests.size()));
        statResLabel.setText(String.valueOf(HotelDatabase.reservations.size()));
    }

    @FXML
    public void showGuests() {
        updateUI(btnGuests, "Guest Records", "GUESTS");

        TableColumn<Guest, String> colUser = new TableColumn<>("Username");
        colUser.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Guest, String> colAddress = new TableColumn<>("Address");
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Guest, Double> colBal = new TableColumn<>("Balance (EGP)");
        colBal.setCellValueFactory(new PropertyValueFactory<>("balance"));

        mainTable.getColumns().addAll(colUser, colAddress, colBal);
        mainTable.setItems(FXCollections.observableArrayList(HotelDatabase.guests));
    }

    @FXML
    public void showRooms() {
        updateUI(btnRooms, "Live Room Status", "ROOMS");

        TableColumn<Room, Integer> colNum = new TableColumn<>("Room #");
        colNum.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));

        TableColumn<Room, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRoomType().getTypeName()));

        TableColumn<Room, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getStatus().toString()));

        mainTable.getColumns().addAll(colNum, colType, colStatus);
        mainTable.setItems(FXCollections.observableArrayList(HotelDatabase.rooms));
    }

    @FXML
    public void showReservations() {
        updateUI(btnRes, "Manage Reservations", "RESERVATIONS");

        TableColumn<Reservation, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));

        TableColumn<Reservation, String> colGuest = new TableColumn<>("Guest");
        colGuest.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGuest().getUsername()));

        TableColumn<Reservation, Integer> colRoom = new TableColumn<>("Room #");
        colRoom.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getRoom().getRoomNumber()));

        TableColumn<Reservation, LocalDate> colIn = new TableColumn<>("Check-In");
        colIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));

        TableColumn<Reservation, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        mainTable.getColumns().addAll(colId, colGuest, colRoom, colIn, colStatus);
        mainTable.setItems(FXCollections.observableArrayList(HotelDatabase.reservations));
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase();
        if (currentMode.equals("GUESTS")) {
            ObservableList<Guest> filtered = FXCollections.observableArrayList();
            for (Guest g : HotelDatabase.guests) {
                if (g.getUsername().toLowerCase().contains(query)) filtered.add(g);
            }
            mainTable.setItems(filtered);
        } else if (currentMode.equals("ROOMS")) {
            ObservableList<Room> filtered = FXCollections.observableArrayList();
            for (Room r : HotelDatabase.rooms) {
                if (String.valueOf(r.getRoomNumber()).contains(query) || r.getStatus().toString().toLowerCase().contains(query)) {
                    filtered.add(r);
                }
            }
            mainTable.setItems(filtered);
        } else if (currentMode.equals("RESERVATIONS")) {
            ObservableList<Reservation> filtered = FXCollections.observableArrayList();
            for (Reservation r : HotelDatabase.reservations) {
                if (r.getGuest().getUsername().toLowerCase().contains(query) || String.valueOf(r.getReservationId()).contains(query)) {
                    filtered.add(r);
                }
            }
            mainTable.setItems(filtered);
        }
    }

    @FXML
    public void handleCheckIn() {
        Reservation selected = (Reservation) mainTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFeedback("Please select a reservation first.", false);
            return;
        }

        if (currentStaff instanceof Receptionist) {
            Receptionist rec = (Receptionist) currentStaff;
            rec.manageCheckIn(selected); // تنفيذ الدالة من كلاس Receptionist
            showReservations(); // تحديث الجدول
            refreshStats();
            showFeedback("Guest checked in successfully!", true);
        }
    }

    @FXML
    public void handleCheckOut() {
        Reservation selected = (Reservation) mainTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showFeedback("Please select a reservation first.", false);
            return;
        }

        PaymentMethod method = paymentMethodBox.getValue();
        if (method == null) {
            showFeedback("Please select a payment method.", false);
            return;
        }

        if (currentStaff instanceof Receptionist) {
            Receptionist rec = (Receptionist) currentStaff;
            try {
                rec.manageCheckOut(selected, method); // تنفيذ الخروج والدفع
                showReservations();
                refreshStats();
                showFeedback("Check-out and payment completed successfully!", true);
            } catch (Exception e) {
                showFeedback("Error: " + e.getMessage(), false);
            }
        }
    }

    private void showFeedback(String message, boolean isSuccess) {
        feedbackLabel.setText(message);
        if (isSuccess) {
            feedbackLabel.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;"); // لون أخضر للنجاح
        } else {
            feedbackLabel.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;"); // لون أحمر للخطأ
        }
    }

    @FXML
    public void openNewReservation(ActionEvent event) {
        SceneSwitcher.goTo(event, "Reservation.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event) {
        currentStaff = null;
        SceneSwitcher.goTo(event, "login.fxml");
    }
}