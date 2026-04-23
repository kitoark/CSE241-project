public interface Payable {
    public void processPayment(double amount,PaymentMethod method) throws InvalidPaymentException;
}
