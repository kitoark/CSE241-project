package Booking;

public interface Payable {
    public void Payment(double amount,PaymentMethod method) throws InvalidPaymentException;
}
