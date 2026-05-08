import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;


public class CheckoutPaymentController implements Initializable {

    public static Reservation reservationToCheckout;

    @FXML
    private TextArea invoiceTextArea;
    @FXML
    private ComboBox<PaymentMethod> paymentMethodBox;
    @FXML
    private Button confirmButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentMethodBox.setItems(FXCollections.observableArrayList(PaymentMethod.values()));

        if (reservationToCheckout != null) {
            Reservation r = reservationToCheckout;
            long nights = ChronoUnit.DAYS.between(r.getCheckIn(), r.getCheckOut());
            double total = r.getRoom().getTotalCostForStay((int) nights);

            String invoiceText = "--- INVOICE ---\n" +
                    "Guest: " + r.getGuest().getUsername() + "\n" +
                    "Room: " + r.getRoom().getRoomNumber() + " (" + r.getRoom().getRoomType().getTypeName() + ")\n" +
                    "Check-In: " + r.getCheckIn() + "\n" +
                    "Check-Out: " + r.getCheckOut() + "\n" +
                    "Nights: " + nights + "\n" +
                    "Total: " + total + " EGP";
            invoiceTextArea.setText(invoiceText);
        } else {
            invoiceTextArea.setText("No reservation selected.");
        }
    }

    @FXML
    private void handleConfirmPayment(ActionEvent event) {
        PaymentMethod selectedMethod = paymentMethodBox.getValue();

        if (selectedMethod == null) {
            showFeedback("Selection Required", "Please select a payment method before proceeding.", Alert.AlertType.WARNING);
            return;
        }

        if (reservationToCheckout == null) {
            showFeedback("Error", "No reservation to checkout.", Alert.AlertType.ERROR);
            return;
        }

        try {
            reservationToCheckout.getGuest().checkout(reservationToCheckout, selectedMethod);
            showFeedback("Payment Successful", "Your payment via " + selectedMethod + " has been confirmed.", Alert.AlertType.INFORMATION);
            reservationToCheckout = null;
            SceneSwitcher.goTo(event, "receptionistDashboard.fxml");
        } catch (InvalidPaymentException e) {
            showFeedback("Payment Failed", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showFeedback(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}