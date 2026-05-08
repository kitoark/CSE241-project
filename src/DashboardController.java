import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DashboardController {

    @FXML private Label userLabel;
    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label occupiedRoomsLabel;
    @FXML private Label totalGuestsLabel;
    @FXML private Label tableStatusLabel;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private TableView<Room> roomTable;

    @FXML
    public void initialize() {
        statusFilter.setItems(FXCollections.observableArrayList("All", "AVAILABLE", "RESERVED", "OCCUPIED", "UNDER_MAINTENANCE"));
        refreshTable();
    }

    @FXML
    private void refreshTable() {
        ObservableList<Room> rooms = FXCollections.observableArrayList(HotelDatabase.rooms);
        roomTable.setItems(rooms);

        long available = HotelDatabase.rooms.stream()
                .filter(r -> r.getStatus() == Room.RoomStatus.AVAILABLE).count();
        long occupied  = HotelDatabase.rooms.stream()
                .filter(r -> r.getStatus() == Room.RoomStatus.OCCUPIED).count();

        totalRoomsLabel.setText(String.valueOf(HotelDatabase.rooms.size()));
        availableRoomsLabel.setText(String.valueOf(available));
        occupiedRoomsLabel.setText(String.valueOf(occupied));
        totalGuestsLabel.setText(String.valueOf(HotelDatabase.guests.size()));
        tableStatusLabel.setText("Showing " + rooms.size() + " rooms");
    }

    @FXML
    private void filterRooms() {
        String query  = searchField.getText().toLowerCase();
        String status = statusFilter.getValue();

        ObservableList<Room> filtered = FXCollections.observableArrayList(
            HotelDatabase.rooms.stream()
                .filter(r -> query.isEmpty() ||
                        String.valueOf(r.getRoomNumber()).contains(query) || r.getRoomType().getTypeName().toLowerCase().contains(query))
                .filter(r -> status == null || "All".equals(status) || r.getStatus().name().equals(status)).toList());

        roomTable.setItems(filtered);
        tableStatusLabel.setText("Showing " + filtered.size() + " rooms");
    }

    @FXML private void openAddRoomDialog(){
        System.out.println("Open Add Room dialog");
    }
    @FXML private void exportRooms(){
        System.out.println("Export rooms to CSV");
    }
    @FXML private void showDashboard(){
        System.out.println("Dashboard");
    }
    @FXML private void showRooms(){
        System.out.println("Rooms");
    }
    @FXML private void showReservations(){
        System.out.println("Reservations");
    }
    @FXML private void showGuests(){
        System.out.println("Guests");
    }
    @FXML private void showInvoices(){
        System.out.println("Invoices");
    }
    @FXML private void handleLogout(){
        System.out.println("Logout");
    }
}
