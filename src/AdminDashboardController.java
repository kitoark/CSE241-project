import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminDashboardController {

    /** Set this before navigating to the admin dashboard (e.g. in LoginController). */
    public static Staff loggedInAdmin;

    // ── NAV BUTTONS ───────────────────────────────────────────────────────────
    @FXML private Button btnDashboard;
    @FXML private Button btnRooms;
    @FXML private Button btnReservations;
    @FXML private Button btnGuests;
    @FXML private Button btnInvoices;

    // ── PAGES ─────────────────────────────────────────────────────────────────
    @FXML private VBox dashboardPage;
    @FXML private VBox roomsPage;
    @FXML private VBox reservationsPage;
    @FXML private VBox guestsPage;
    @FXML private VBox invoicesPage;

    // ── SIDEBAR ───────────────────────────────────────────────────────────────
    @FXML private Label userLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label occupiedRoomsLabel;
    @FXML private Label totalGuestsLabel;
    @FXML private Label totalReservationsLabel;

    // ── ROOMS PAGE ────────────────────────────────────────────────────────────
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Integer> colRoomId;
    @FXML private TableColumn<Room, Integer> colRoomNumber;
    @FXML private TableColumn<Room, Integer> colFloor;
    @FXML private TableColumn<Room, String>  colType;
    @FXML private TableColumn<Room, String>  colStatus;
    @FXML private TableColumn<Room, Double>  colPrice;
    @FXML private TableColumn<Room, String>  colAmenities;
    @FXML private Label tableStatusLabel;

    // ── DASHBOARD PAGE ────────────────────────────────────────────────────────
    @FXML private TableView<Reservation> recentReservationsTable;
    @FXML private TableColumn<Reservation, Integer> colResId;
    @FXML private TableColumn<Reservation, String>  colResGuest;
    @FXML private TableColumn<Reservation, Integer> colResRoom;
    @FXML private TableColumn<Reservation, String>  colResCheckIn;
    @FXML private TableColumn<Reservation, String>  colResCheckOut;
    @FXML private TableColumn<Reservation, String>  colResStatus;

    // ── RESERVATIONS PAGE ─────────────────────────────────────────────────────
    @FXML private TextField resSearchField;
    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> colFullResId;
    @FXML private TableColumn<Reservation, String>  colFullResGuest;
    @FXML private TableColumn<Reservation, Integer> colFullResRoom;
    @FXML private TableColumn<Reservation, String>  colFullResCheckIn;
    @FXML private TableColumn<Reservation, String>  colFullResCheckOut;
    @FXML private TableColumn<Reservation, String>  colFullResStatus;
    @FXML private Label resStatusLabel;

    // ── GUESTS PAGE ───────────────────────────────────────────────────────────
    @FXML private TextField guestSearchField;
    @FXML private TableView<Guest> guestsTable;
    @FXML private TableColumn<Guest, String>  colGuestName;
    @FXML private TableColumn<Guest, String>  colGuestGender;
    @FXML private TableColumn<Guest, String>  colGuestDOB;
    @FXML private TableColumn<Guest, Double>  colGuestBalance;
    @FXML private TableColumn<Guest, String>  colGuestAddress;
    @FXML private TableColumn<Guest, Integer> colGuestReservations;
    @FXML private Label guestStatusLabel;

    // ── INVOICES PAGE ─────────────────────────────────────────────────────────
    @FXML private TableView<Invoice> invoicesTable;
    @FXML private TableColumn<Invoice, Integer> colInvId;
    @FXML private TableColumn<Invoice, String>  colInvGuest;
    @FXML private TableColumn<Invoice, String>  colInvRoom;
    @FXML private TableColumn<Invoice, Double>  colInvAmount;
    @FXML private TableColumn<Invoice, String>  colInvMethod;
    @FXML private TableColumn<Invoice, String>  colInvDate;
    @FXML private TableColumn<Invoice, String>  colInvStatus;
    @FXML private Label invoiceStatusLabel;

    // ═════════════════════════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════════════════════════

    @FXML
    public void initialize() {
        setupRoomsTable();
        setupDashboardTable();
        setupReservationsTable();
        setupGuestsTable();
        setupInvoicesTable();

        statusFilter.setItems(FXCollections.observableArrayList(
                "All", "AVAILABLE", "RESERVED", "OCCUPIED", "UNDER_MAINTENANCE"));

        if (loggedInAdmin != null) {
            userLabel.setText(loggedInAdmin.getUsername());
        }

        refreshAll();

        // Auto-refresh every 8 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(8), e -> refreshAll()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TABLE COLUMN SETUP
    // ═════════════════════════════════════════════════════════════════════════

    private void setupRoomsTable() {
        colRoomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        colRoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("floorNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colType.setCellValueFactory(cell -> {
            RoomType t = cell.getValue().getRoomType();
            return new SimpleStringProperty(t != null ? t.getTypeName() : "N/A");
        });
        colPrice.setCellValueFactory(new PropertyValueFactory<>("totalPricePerNight"));
        colAmenities.setCellValueFactory(cell -> {
            List<Amenity> list = cell.getValue().getAmenities();
            return new SimpleStringProperty(
                    list.stream().map(Amenity::getName).collect(Collectors.joining(", ")));
        });
    }

    private void setupDashboardTable() {
        colResId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colResGuest.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getGuest().getUsername()));
        colResRoom.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getRoom().getRoomNumber()).asObject());
        colResCheckIn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCheckIn().toString()));
        colResCheckOut.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCheckOut().toString()));
        colResStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus().toString()));
    }

    private void setupReservationsTable() {
        colFullResId.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        colFullResGuest.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getGuest().getUsername()));
        colFullResRoom.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getRoom().getRoomNumber()).asObject());
        colFullResCheckIn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCheckIn().toString()));
        colFullResCheckOut.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCheckOut().toString()));
        colFullResStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getStatus().toString()));
    }

    private void setupGuestsTable() {
        colGuestName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colGuestGender.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getGender() != null ? cell.getValue().getGender().toString() : "N/A"));
        colGuestDOB.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDateOfBirth().toString()));
        colGuestBalance.setCellValueFactory(new PropertyValueFactory<>("balance"));
        colGuestAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colGuestReservations.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getMyReservations().size()).asObject());
    }

    private void setupInvoicesTable() {
        colInvId.setCellValueFactory(new PropertyValueFactory<>("invoiceId"));
        colInvGuest.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getReservation().getGuest().getUsername()));
        colInvRoom.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        String.valueOf(cell.getValue().getReservation().getRoom().getRoomNumber())));
        colInvAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        colInvMethod.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getPaymentMethod() != null
                                ? cell.getValue().getPaymentMethod().toString() : "N/A"));
        colInvDate.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getPaymentDate() != null
                                ? cell.getValue().getPaymentDate().toString() : "N/A"));
        colInvStatus.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().isPaid() ? "PAID" : "UNPAID"));
    }

    // ═════════════════════════════════════════════════════════════════════════
    // REFRESH
    // ═════════════════════════════════════════════════════════════════════════

    /** Refreshes stats sidebar + all table pages. */
    private void refreshAll() {
        refreshStats();
        refreshRoomsTable();
        refreshDashboardTable();
        refreshReservationsTable();
        refreshGuestsTable();
        refreshInvoicesTable();
    }

    private void refreshStats() {
        long available = HotelDatabase.rooms.stream()
                .filter(r -> r.getStatus() == Room.RoomStatus.AVAILABLE).count();
        long occupied  = HotelDatabase.rooms.stream()
                .filter(r -> r.getStatus() == Room.RoomStatus.OCCUPIED).count();
        totalRoomsLabel.setText(String.valueOf(HotelDatabase.rooms.size()));
        availableRoomsLabel.setText(String.valueOf(available));
        occupiedRoomsLabel.setText(String.valueOf(occupied));
        totalGuestsLabel.setText(String.valueOf(HotelDatabase.guests.size()));
        totalReservationsLabel.setText(String.valueOf(HotelDatabase.reservations.size()));
    }

    private void refreshRoomsTable() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(HotelDatabase.rooms);
        roomTable.setItems(rooms);
        tableStatusLabel.setText("Showing " + rooms.size() + " rooms");
    }

    private void refreshDashboardTable() {
        recentReservationsTable.setItems(
                FXCollections.observableArrayList(HotelDatabase.reservations));
    }

    private void refreshReservationsTable() {
        ObservableList<Reservation> list =
                FXCollections.observableArrayList(HotelDatabase.reservations);
        reservationsTable.setItems(list);
        resStatusLabel.setText("Showing " + list.size() + " reservations");
    }

    private void refreshGuestsTable() {
        ObservableList<Guest> list = FXCollections.observableArrayList(HotelDatabase.guests);
        guestsTable.setItems(list);
        guestStatusLabel.setText("Showing " + list.size() + " guests");
    }

    private void refreshInvoicesTable() {
        ObservableList<Invoice> list = FXCollections.observableArrayList(HotelDatabase.invoices);
        invoicesTable.setItems(list);
        invoiceStatusLabel.setText("Showing " + list.size() + " invoices");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // NAVIGATION
    // ═════════════════════════════════════════════════════════════════════════

    /** Shows the given page and hides all others; updates nav-button styles. */
    private void showPage(VBox target) {
        VBox[] pages = {dashboardPage, roomsPage, reservationsPage, guestsPage, invoicesPage};
        Button[] btns = {btnDashboard, btnRooms, btnReservations, btnGuests, btnInvoices};

        for (int i = 0; i < pages.length; i++) {
            boolean active = pages[i] == target;
            pages[i].setVisible(active);
            pages[i].setManaged(active);

            btns[i].getStyleClass().removeAll("nav-btn-active", "nav-btn");
            btns[i].getStyleClass().add(active ? "nav-btn-active" : "nav-btn");
        }
        refreshAll();
    }

    @FXML public void showDashboard()    { showPage(dashboardPage); }
    @FXML public void showRooms()        { showPage(roomsPage); }
    @FXML public void showReservations() { showPage(reservationsPage); }
    @FXML public void showGuests()       { showPage(guestsPage); }
    @FXML public void showInvoices()     { showPage(invoicesPage); }

    // ═════════════════════════════════════════════════════════════════════════
    // FILTERS
    // ═════════════════════════════════════════════════════════════════════════

    @FXML
    private void filterRooms() {
        String query  = searchField.getText().toLowerCase();
        String status = statusFilter.getValue();

        ObservableList<Room> filtered = FXCollections.observableArrayList(
                HotelDatabase.rooms.stream()
                        .filter(r -> query.isEmpty()
                                || String.valueOf(r.getRoomNumber()).contains(query)
                                || r.getRoomType().getTypeName().toLowerCase().contains(query))
                        .filter(r -> status == null || "All".equals(status)
                                || r.getStatus().name().equals(status))
                        .toList());

        roomTable.setItems(filtered);
        tableStatusLabel.setText("Showing " + filtered.size() + " rooms");
    }

    @FXML
    private void filterReservations() {
        String q = resSearchField.getText().toLowerCase();
        ObservableList<Reservation> filtered = FXCollections.observableArrayList(
                HotelDatabase.reservations.stream()
                        .filter(r -> q.isEmpty()
                                || r.getGuest().getUsername().toLowerCase().contains(q)
                                || String.valueOf(r.getRoom().getRoomNumber()).contains(q)
                                || r.getStatus().toString().toLowerCase().contains(q))
                        .toList());
        reservationsTable.setItems(filtered);
        resStatusLabel.setText("Showing " + filtered.size() + " reservations");
    }

    @FXML
    private void filterGuests() {
        String q = guestSearchField.getText().toLowerCase();
        ObservableList<Guest> filtered = FXCollections.observableArrayList(
                HotelDatabase.guests.stream()
                        .filter(g -> q.isEmpty()
                                || g.getUsername().toLowerCase().contains(q)
                                || g.getAddress().toLowerCase().contains(q))
                        .toList());
        guestsTable.setItems(filtered);
        guestStatusLabel.setText("Showing " + filtered.size() + " guests");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ROOM CRUD
    // ═════════════════════════════════════════════════════════════════════════

    @FXML
    private void openAddRoomDialog() {
        // Build form fields
        TextField roomNumField = new TextField();
        roomNumField.setPromptText("e.g. 305");

        TextField floorField = new TextField();
        floorField.setPromptText("e.g. 3");

        ComboBox<RoomType> typeCombo = buildRoomTypeCombo(null);

        GridPane grid = buildGrid(
                new String[]{"Room Number:", "Floor:", "Room Type:"},
                new javafx.scene.Node[]{roomNumField, floorField, typeCombo}
        );

        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Add New Room");
        dialog.setHeaderText("Enter details for the new room");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            try {
                int roomNum = Integer.parseInt(roomNumField.getText().trim());
                int floor   = Integer.parseInt(floorField.getText().trim());
                RoomType type = typeCombo.getValue();
                if (type == null) { showAlert("Validation Error", "Please select a room type.", Alert.AlertType.ERROR); return null; }

                int newId = HotelDatabase.rooms.stream().mapToInt(r -> r.getRoomId()).max().orElse(0) + 1;
                return new Room(newId, roomNum, floor, type);
            } catch (NumberFormatException ex) {
                showAlert("Validation Error", "Room number and floor must be whole numbers.", Alert.AlertType.ERROR);
                return null;
            }
        });

        dialog.showAndWait().ifPresent(room -> {
            HotelDatabase.rooms.add(room);
            refreshAll();
        });
    }

    @FXML
    private void editSelectedRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a room to edit.", Alert.AlertType.WARNING);
            return;
        }

        // Form fields pre-populated
        TextField roomNumField = new TextField(String.valueOf(selected.getRoomNumber()));
        TextField floorField   = new TextField(String.valueOf(selected.getFloorNumber()));
        ComboBox<RoomType> typeCombo   = buildRoomTypeCombo(selected.getRoomType());
        ComboBox<String>   statusCombo = new ComboBox<>(FXCollections.observableArrayList(
                "AVAILABLE", "RESERVED", "OCCUPIED", "UNDER_MAINTENANCE"));
        statusCombo.setValue(selected.getStatus().name());

        GridPane grid = buildGrid(
                new String[]{"Room Number:", "Floor:", "Room Type:", "Status:"},
                new javafx.scene.Node[]{roomNumField, floorField, typeCombo, statusCombo}
        );

        Dialog<Room> dialog = new Dialog<>();
        dialog.setTitle("Edit Room #" + selected.getRoomNumber());
        dialog.setHeaderText("Update room details");
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt != ButtonType.OK) return null;
            try {
                int roomNum = Integer.parseInt(roomNumField.getText().trim());
                int floor   = Integer.parseInt(floorField.getText().trim());
                RoomType type = typeCombo.getValue();
                if (type == null) { showAlert("Validation Error", "Please select a room type.", Alert.AlertType.ERROR); return null; }

                Room updated = new Room(selected.getRoomId(), roomNum, floor, type);
                // Preserve amenities
                for (Amenity a : selected.getAmenities()) updated.addAmenity(a);
                // Apply chosen status
                if (statusCombo.getValue() != null) {
                    updated.setStatus(Room.RoomStatus.valueOf(statusCombo.getValue()));
                }
                return updated;
            } catch (NumberFormatException ex) {
                showAlert("Validation Error", "Room number and floor must be whole numbers.", Alert.AlertType.ERROR);
                return null;
            }
        });

        dialog.showAndWait().ifPresent(updated -> {
            int idx = HotelDatabase.rooms.indexOf(selected);
            if (idx >= 0) HotelDatabase.rooms.set(idx, updated);
            refreshAll();
        });
    }

    @FXML
    private void deleteSelectedRoom() {
        Room selected = roomTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a room to delete.", Alert.AlertType.WARNING);
            return;
        }

        boolean hasActive = HotelDatabase.reservations.stream().anyMatch(r ->
                r.getRoom() == selected &&
                r.getStatus() != ReservationStatus.CANCELLED &&
                r.getStatus() != ReservationStatus.COMPLETED);
        if (hasActive) {
            showAlert("Cannot Delete", "Room #" + selected.getRoomNumber() + " has active reservations. Cancel them first.", Alert.AlertType.ERROR);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete Room #" + selected.getRoomNumber() + "? This cannot be undone.",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText(null);

        confirm.showAndWait().filter(bt -> bt == ButtonType.OK).ifPresent(bt -> {
            HotelDatabase.rooms.remove(selected);
            refreshAll();
        });
    }

    /** Refresh button on rooms page. */
    @FXML
    private void refreshTable() {
        refreshAll();
    }

    // ═════════════════════════════════════════════════════════════════════════
    // RESERVATION ACTIONS
    // ═════════════════════════════════════════════════════════════════════════

    @FXML
    private void confirmReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a reservation to confirm.", Alert.AlertType.WARNING);
            return;
        }
        if (selected.getStatus() != ReservationStatus.PENDING) {
            showAlert("Cannot Confirm",
                    "Only PENDING reservations can be confirmed.\nCurrent status: " + selected.getStatus(),
                    Alert.AlertType.WARNING);
            return;
        }
        selected.setStatus(ReservationStatus.CONFIRMED);
        selected.getRoom().setStatus(Room.RoomStatus.RESERVED);
        reservationsTable.refresh();
        roomTable.refresh();
        recentReservationsTable.refresh();
        refreshStats();
        showAlert("Success",
                "Reservation #" + selected.getReservationId() + " confirmed. Room marked as RESERVED.",
                Alert.AlertType.INFORMATION);
    }

    @FXML
    private void cancelReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a reservation to cancel.", Alert.AlertType.WARNING);
            return;
        }
        if (selected.getStatus() == ReservationStatus.COMPLETED
                || selected.getStatus() == ReservationStatus.CANCELLED) {
            showAlert("Cannot Cancel",
                    "Reservation already has status: " + selected.getStatus(),
                    Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancel Reservation #" + selected.getReservationId() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirm Cancellation");
        confirm.setHeaderText(null);

        confirm.showAndWait().filter(bt -> bt == ButtonType.OK).ifPresent(bt -> {
            selected.setStatus(ReservationStatus.CANCELLED);
            selected.getRoom().setStatus(Room.RoomStatus.AVAILABLE);
            reservationsTable.refresh();
            roomTable.refresh();
            recentReservationsTable.refresh();
            refreshStats();
        });
    }

    // ═════════════════════════════════════════════════════════════════════════
    // LOGOUT
    // ═════════════════════════════════════════════════════════════════════════

    public void loginMenu(ActionEvent event) {
        loggedInAdmin = null;
        SceneSwitcher.goTo(event, "login.fxml");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // HELPERS
    // ═════════════════════════════════════════════════════════════════════════

    /** Builds a ComboBox for RoomType with a display name converter. */
    private ComboBox<RoomType> buildRoomTypeCombo(RoomType selected) {
        ComboBox<RoomType> combo = new ComboBox<>(
                FXCollections.observableArrayList(HotelDatabase.roomTypes));
        combo.setConverter(new StringConverter<RoomType>() {
            @Override public String toString(RoomType rt)   { return rt != null ? rt.getTypeName() : ""; }
            @Override public RoomType fromString(String s)  { return null; }
        });
        if (selected != null) combo.setValue(selected);
        combo.setPrefWidth(200);
        return combo;
    }

    /** Builds a labeled GridPane from parallel label-text / node arrays. */
    private GridPane buildGrid(String[] labels, javafx.scene.Node[] fields) {
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 60, 10, 10));
        for (int i = 0; i < labels.length; i++) {
            grid.add(new Label(labels[i]), 0, i);
            grid.add(fields[i], 1, i);
        }
        return grid;
    }

    /** Shows a simple alert dialog. */
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}