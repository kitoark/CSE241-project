package Booking;

public class InvalidPaymentException extends Exception {
//Constructor To get the error Message
    public InvalidPaymentException(String message) {
        super(message);
    }
}
