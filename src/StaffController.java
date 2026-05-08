import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

public class StaffController implements Initializable {
    @FXML private Label staffRoleLabel;
    @FXML private Label staffNameLabel;
    @FXML private Label pageTitle;
    @FXML private Label errorLabel;
    @FXML private Button btnAddRoom;
    @FXML private Button btnDeleteRoom;
    @FXML private Button btnRoomTypes;
    @FXML private Button btnAmenities;
    @FXML private Label  adminSectionLabel;
    @FXML private VBox dashboardPane;
    @FXML private VBox guestsPane;
    @FXML private VBox roomsPane;
    @FXML private VBox reservationsPane;
    @FXML private VBox roomTypesPane;
    @FXML private VBox amenitiesPane;
    @FXML private TableView<Reservation>dashReservationTable;
    @FXML private TableColumn<Reservation,Integer>dashColId;
    @FXML private TableColumn<Reservation,String>dashColGuest;
    @FXML private TableColumn<Reservation,Integer>dashColRoom;
    @FXML private TableColumn<Reservation,LocalDate>dashColCheckIn;
    @FXML private TableColumn<Reservation,LocalDate>dashColCheckOut;
    @FXML private TableColumn<Reservation,ReservationStatus>dashColStatus;
    @FXML private Label statGuests;
    @FXML private Label statRooms;
    @FXML private Label statReservations;
    @FXML private Label statAvailable;
    @FXML private TextField guestSearchField;
    @FXML private TableView<Guest>guestTable;
    @FXML private TableColumn<Guest,String>gColUsername;
    @FXML private TableColumn<Guest,LocalDate>gColDob;
    @FXML private TableColumn<Guest,Gender>gColGender;
    @FXML private TableColumn<Guest,String>gColAddress;
    @FXML private TableColumn<Guest,Double>gColBalance;
    @FXML private TableColumn<Guest,String>gColPrefs;
    @FXML private ComboBox<String>roomStatusFilter;
    @FXML private TableView<Room>roomTable;
    @FXML private TableColumn<Room,Integer>rColNumber;
    @FXML private TableColumn<Room,Integer>rColFloor;
    @FXML private TableColumn<Room,String>rColType;
    @FXML private TableColumn<Room,Room.RoomStatus> rColStatus;
    @FXML private TableColumn<Room,Double>rColPrice;
    @FXML private TableColumn<Room,String>rColAmenities;
    @FXML private ComboBox<PaymentMethod>paymentMethodBox;
    @FXML private TableView<Reservation>reservationTable;
    @FXML private TableColumn<Reservation,Integer>resColId;
    @FXML private TableColumn<Reservation,String>resColGuest;
    @FXML private TableColumn<Reservation,Integer>resColRoom;
    @FXML private TableColumn<Reservation,LocalDate>resColCheckIn;
    @FXML private TableColumn<Reservation,LocalDate>resColCheckOut;
    @FXML private TableColumn<Reservation,ReservationStatus> resColStatus;
    @FXML private TableColumn<Reservation,Double>resColTotal;
    @FXML private TableView<RoomType>roomTypeTable;
    @FXML private TableColumn<RoomType,Integer>rtColId;
    @FXML private TableColumn<RoomType,String>rtColName;
    @FXML private TableColumn<RoomType,Integer>rtColCapacity;
    @FXML private TableColumn<RoomType,Double>rtColPrice;
    @FXML private TableView<Amenity>          amenityTable;
    @FXML private TableColumn<Amenity,Integer> amColId;
    @FXML private TableColumn<Amenity,String>  amColName;
    @FXML private TableColumn<Amenity,Double>  amColCost;

    public static Staff currentStaff;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentStaff != null) {
            staffNameLabel.setText("Welcome, " + currentStaff.getUsername());
            staffRoleLabel.setText(currentStaff.getRole().toString());
        }
        boolean isAdmin = (currentStaff instanceof Admin);
        btnAddRoom.setVisible(isAdmin);
        btnAddRoom.setManaged(isAdmin);
        btnDeleteRoom.setVisible(isAdmin);
        btnDeleteRoom.setManaged(isAdmin);
        btnRoomTypes.setVisible(isAdmin);
        btnRoomTypes.setManaged(isAdmin);
        btnAmenities.setVisible(isAdmin);
        btnAmenities.setManaged(isAdmin);
        adminSectionLabel.setVisible(isAdmin);
        adminSectionLabel.setManaged(isAdmin);
        setupDashboardTable();
        setupGuestTable();
        setupRoomTable();
        setupReservationTable();
        setupRoomTypeTable();
        setupAmenityTable();
        roomStatusFilter.getItems().addAll("ALL",
                Room.RoomStatus.AVAILABLE.toString(),
                Room.RoomStatus.RESERVED.toString(),
                Room.RoomStatus.OCCUPIED.toString(),
                Room.RoomStatus.UNDER_MAINTENANCE.toString());
        roomStatusFilter.setValue("ALL");

        paymentMethodBox.getItems().addAll(PaymentMethod.values());

        // Show dashboard by default
        showDashboard(null);
    }
    private void setupDashboardTable() {
        dashColId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        dashColGuest.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getGuest().getUsername()));
        dashColRoom.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getRoom().getRoomNumber()));
        dashColCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        dashColCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        dashColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupGuestTable() {
        gColUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        gColDob.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        gColGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        gColAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        gColBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        gColPrefs.setCellValueFactory(new PropertyValueFactory<>("roomPreferences"));
    }
    private void setupRoomTable() {
        rColNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        rColFloor.setCellValueFactory(new PropertyValueFactory<>("floorNumber"));
        rColType.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getRoomType().getTypeName()));
        rColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        rColPrice.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getTotalPricePerNight()));
        rColAmenities.setCellValueFactory(c -> {
            StringBuilder sb = new StringBuilder();
            for (Amenity a : c.getValue().getAmenities()) {
                if (sb.length() > 0) sb.append(", ");
                sb.append(a.getName());
            }
            return new javafx.beans.property.SimpleStringProperty(sb.toString());
        });
    }
    private void setupReservationTable() {
        resColId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        resColGuest.setCellValueFactory(c ->
                new javafx.beans.property.SimpleStringProperty(
                        c.getValue().getGuest().getUsername()));
        resColRoom.setCellValueFactory(c ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        c.getValue().getRoom().getRoomNumber()));
        resColCheckIn.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        resColCheckOut.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        resColStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        resColTotal.setCellValueFactory(c -> {
            Reservation r = c.getValue();
            long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
            double total = r.getRoom().getTotalCostForStay((int) nights);
            return new javafx.beans.property.SimpleObjectProperty<>(total);
        });
    }

    private void setupRoomTypeTable() {
        rtColId.setCellValueFactory(new PropertyValueFactory<>("typeId"));
        rtColName.setCellValueFactory(new PropertyValueFactory<>("typeName"));
        rtColCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        rtColPrice.setCellValueFactory(new PropertyValueFactory<>("basePricePerNight"));
    }

    private void setupAmenityTable() {
        amColId.setCellValueFactory(new PropertyValueFactory<>("amenityId"));
        amColName.setCellValueFactory(new PropertyValueFactory<>("name"));
        amColCost.setCellValueFactory(new PropertyValueFactory<>("extraCostPerNight"));
    }
    private void showOnly(VBox pane, String title) {
        dashboardPane.setVisible(false);
        dashboardPane.setManaged(false);
        guestsPane.setVisible(false);
        guestsPane.setManaged(false);
        roomsPane.setVisible(false);
        roomsPane.setManaged(false);
        reservationsPane.setVisible(false);
        reservationsPane.setManaged(false);
        roomTypesPane.setVisible(false);
        roomTypesPane.setManaged(false);
        amenitiesPane.setVisible(false);
        amenitiesPane.setManaged(false);

        pane.setVisible(true);
        pane.setManaged(true);
        pageTitle.setText(title);
        clearError();
    }

    @FXML
    public void showDashboard(ActionEvent e) {
        showOnly(dashboardPane, "Dashboard");
        statGuests.setText(String.valueOf(HotelDatabase.guests.size()));
        statRooms.setText(String.valueOf(HotelDatabase.rooms.size()));
        statReservations.setText(String.valueOf(HotelDatabase.reservations.size()));
        long available = HotelDatabase.rooms.stream()
                .filter(Room::isAvailable).count();
        statAvailable.setText(String.valueOf(available));
        dashReservationTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.reservations));
    }

    @FXML
    public void showGuests(ActionEvent e) {
        showOnly(guestsPane, "Guests");
        guestTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.guests));
    }

    @FXML
    public void showRooms(ActionEvent e) {
        showOnly(roomsPane, "Rooms");
        loadAllRooms();
    }

    @FXML
    public void showReservations(ActionEvent e) {
        showOnly(reservationsPane, "Reservations");
        reservationTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.reservations));
    }
    @FXML
    public void showRoomTypes(ActionEvent e) {
        showOnly(roomTypesPane, "Room Types");
        roomTypeTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.roomTypes));
    }
    @FXML
    public void showAmenities(ActionEvent e) {
        showOnly(amenitiesPane, "Amenities");
        amenityTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.amenitiesDatabase));
    }
    @FXML
    public void logout(ActionEvent e) {
        currentStaff = null;
        SceneSwitcher.goTo(e, "login.fxml");
    }
    @FXML
    public void searchGuest(ActionEvent e) {
        String query = guestSearchField.getText().trim().toLowerCase();
        ObservableList<Guest> results = FXCollections.observableArrayList();
        for (Guest g : HotelDatabase.guests) {
            if (g.getUsername().toLowerCase().contains(query)) {
                results.add(g);
            }
        }
        guestTable.setItems(results);
        if (results.isEmpty()) {
            showError("No guest found matching \"" + query + "\".");
        }
    }
    @FXML
    public void clearGuestSearch(ActionEvent e) {
        guestSearchField.clear();
        guestTable.setItems(FXCollections.observableArrayList(HotelDatabase.guests));
        clearError();
    }
    @FXML
    public void filterRoomsByStatus(ActionEvent e) {
        String filter = roomStatusFilter.getValue();
        if (filter == null || filter.equals("ALL")) {
            loadAllRooms();
            return;
        }
        ObservableList<Room> filtered = FXCollections.observableArrayList();
        for (Room r : HotelDatabase.rooms) {
            if (r.getStatus().toString().equals(filter)) {
                filtered.add(r);
            }
        }
        roomTable.setItems(filtered);
    }

    @FXML
    public void openAddRoom(ActionEvent e) {
        if (!(currentStaff instanceof Admin)) { showError("Admin access required."); return; }

        // Simple dialog to create a new room
        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Add New Room");
        dialog.setHeaderText("Enter room details");

        TextField roomNumField  = new TextField(); roomNumField.setPromptText("Room number");
        TextField floorField    = new TextField(); floorField.setPromptText("Floor number");
        ComboBox<RoomType> typeBox = new ComboBox<>();
        typeBox.getItems().addAll(HotelDatabase.roomTypes);
        typeBox.setPromptText("Select room type");

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Room Number:"), 0, 0); grid.add(roomNumField,  1, 0);
        grid.add(new Label("Floor:"),        0, 1); grid.add(floorField,    1, 1);
        grid.add(new Label("Room Type:"),    0, 2); grid.add(typeBox,       1, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == addBtn) {
                try {
                    int rn = Integer.parseInt(roomNumField.getText().trim());
                    int fl = Integer.parseInt(floorField.getText().trim());
                    RoomType rt = typeBox.getValue();
                    if (rt == null) return null;
                    return new Room(rn, rn, fl, rt);
                } catch (NumberFormatException ex) { return null; }
            }
            return null;
        });

        Optional<Room> result = dialog.showAndWait();
        result.ifPresent(room -> {
            ((Admin) currentStaff).createRoom(room);
            loadAllRooms();
        });
    }

    @FXML
    public void deleteRoom(ActionEvent e) {
        if (!(currentStaff instanceof Admin)) { showError("Admin access required."); return; }
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Please select a room to delete."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete Room " + selected.getRoomNumber() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                ((Admin) currentStaff).deleteRoom(selected.getRoomNumber());
                loadAllRooms();
            }
        });
    }
    @FXML
    public void checkIn(ActionEvent e) {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Select a reservation to check in."); return; }

        if (!(currentStaff instanceof Receptionist)) { showError("Receptionist access required."); return; }
        Receptionist rec = (Receptionist) currentStaff;
        rec.manageCheckIn(selected);
        refreshReservations();
    }

    @FXML
    public void checkOut(ActionEvent e) {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Select a reservation to check out."); return; }

        PaymentMethod method = paymentMethodBox.getValue();
        if (method == null) { showError("Please select a payment method."); return; }

        if (!(currentStaff instanceof Receptionist)) { showError("Receptionist access required."); return; }
        Receptionist rec = (Receptionist) currentStaff;
        try {
            rec.manageCheckOut(selected, method);
            refreshReservations();
            showError("Check-out successful!");
        } catch (InvalidPaymentException ex) {
            showError("Check-out failed: " + ex.getMessage());
        }
    }
    @FXML
    public void openAddRoomType(ActionEvent e) {
        if (!(currentStaff instanceof Admin)) { showError("Admin access required."); return; }

        Dialog<RoomType> dialog = new Dialog<>();
        dialog.setTitle("Add Room Type");
        TextField nameField  = new TextField();
        nameField.setPromptText("e.g. Deluxe");
        TextField capField   = new TextField();
        capField.setPromptText("Capacity");
        TextField priceField = new TextField();
        priceField.setPromptText("Base price/night");

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Name:"),     0, 0); grid.add(nameField,  1, 0);
        grid.add(new Label("Capacity:"), 0, 1); grid.add(capField,   1, 1);
        grid.add(new Label("Price:"),    0, 2); grid.add(priceField, 1, 2);
        dialog.getDialogPane().setContent(grid);

        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == addBtn) {
                try {
                    int id  = HotelDatabase.roomTypes.size() + 1;
                    int cap = Integer.parseInt(capField.getText().trim());
                    double price = Double.parseDouble(priceField.getText().trim());
                    return new RoomType(id, nameField.getText().trim(), cap, price);
                } catch (NumberFormatException ex) { return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(rt -> {
            ((Admin) currentStaff).createRoomType(rt);
            roomTypeTable.setItems(
                    FXCollections.observableArrayList(HotelDatabase.roomTypes));
        });
    }
    @FXML
    public void deleteRoomType(ActionEvent e) {
        if (!(currentStaff instanceof Admin)) { showError("Admin access required.");
            return;
        }
        RoomType selected = roomTypeTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Select a room type to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete room type \"" + selected.getTypeName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                ((Admin) currentStaff).deleteRoomType(selected.getTypeName());
                roomTypeTable.setItems(
                        FXCollections.observableArrayList(HotelDatabase.roomTypes));
            }
        });
    }
    @FXML
    public void openAddAmenity(ActionEvent e) {
        if (!(currentStaff instanceof Admin)) { showError("Admin access required.");
            return;
        }
        Dialog<Amenity> dialog = new Dialog<>();
        dialog.setTitle("Add Amenity");
        TextField nameField  = new TextField(); nameField.setPromptText("e.g. Pool");
        TextField costField  = new TextField(); costField.setPromptText("Extra cost/night (0 = free)");
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.add(new Label("Name:"), 0, 0); grid.add(nameField, 1, 0);
        grid.add(new Label("Cost:"), 0, 1); grid.add(costField, 1, 1);
        dialog.getDialogPane().setContent(grid);
        ButtonType addBtn = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addBtn, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> {
            if (bt == addBtn) {
                try {
                    int id = HotelDatabase.amenitiesDatabase.size() + 1;
                    double cost = Double.parseDouble(costField.getText().trim());
                    return new Amenity(id, nameField.getText().trim(), cost);
                } catch (NumberFormatException ex) { return null; }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(amenity -> {
            ((Admin) currentStaff).createAmenity(amenity);
            amenityTable.setItems(
                    FXCollections.observableArrayList(HotelDatabase.amenitiesDatabase));
        });
    }
    @FXML
    public void deleteAmenity(ActionEvent e) {
        if (!(currentStaff instanceof Admin)) { showError("Admin access required."); return; }
        Amenity selected = amenityTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showError("Select an amenity to delete."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete amenity \"" + selected.getName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                ((Admin) currentStaff).deleteAmenity(selected.getName());
                amenityTable.setItems(
                        FXCollections.observableArrayList(HotelDatabase.amenitiesDatabase));
            }
        });
    }
    private void loadAllRooms() {
        roomTable.setItems(FXCollections.observableArrayList(HotelDatabase.rooms));
    }

    private void refreshReservations() {
        reservationTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.reservations));
        clearError();
    }
    private void showError(String msg) {
        errorLabel.setText(msg);
    }

    private void clearError() {
        errorLabel.setText("");
    }
}
