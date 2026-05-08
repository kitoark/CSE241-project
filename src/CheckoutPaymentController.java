import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;



public class CheckoutPaymentController implements Initializable {

     @FXML
     private TextArea invoiceTextArea;
     @FXML
     private ComboBox<String> paymentMethodBox;
     private Button confirmButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        paymentMethodBox.getItems().addAll("Credit Card", "Debit Card", "PayPal", "Cash");
        invoiceTextArea.setText("--- INVOICE ---\nRoom: 302 (Deluxe)\nNights: 3\nTotal: $450.00");
    }

    @FXML
    private void handleConfirmPayment(ActionEvent event) {
        String selectedMethod = paymentMethodBox.getValue();

        if (selectedMethod == null) {
            showFeedback("Selection Required", "Please select a payment method before proceeding.", Alert.AlertType.WARNING);
        } else {
            processPayment(selectedMethod);
            showFeedback("Payment Successful", "Your payment via " + selectedMethod + " has been confirmed.", Alert.AlertType.INFORMATION);

        }
    }

    private void processPayment(String method) {
        System.out.println("Processing " + method + " payment...");
    }

    private void showFeedback(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}