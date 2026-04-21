public class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException(String message) {
        super("Invalid payment");
    }
}
