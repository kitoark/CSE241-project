import java.time.LocalDateTime;

public class Invoice implements Payable {

    private int invoiceId;
    private Reservation reservation;
    private double totalAmount;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private boolean paid;

    //Invoice Parameterized Constructor
    public Invoice(Reservation reservation, double totalAmount) {
        if (reservation  == null)    throw new IllegalArgumentException("Reservation cannot be null");
        if (totalAmount  <  0)       throw new IllegalArgumentException("Total amount cannot be negative");
        this.invoiceId    = HotelDatabase.invoices.size() + 1;
        this.reservation  = reservation;
        this.totalAmount  = totalAmount;
        this.paid         = false;
    }

    public int getInvoiceId(){
        return invoiceId;
    }
    public Reservation getReservation(){
        return reservation;
    }
    public double getTotalAmount(){
        return totalAmount;
    }
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    public LocalDateTime getPaymentDate(){
        return paymentDate;
    }
    public boolean isPaid(){
        return paid;
    }

    //To check amount is valid or not
    @Override
    public void processPayment(double amount, PaymentMethod method) throws InvalidPaymentException {
        if (paid) {
            throw new InvalidPaymentException("This invoice has already been paid");
        }
        if (amount < 0) {
            throw new InvalidPaymentException("Payment amount cannot be negative");
        }
        if (amount < totalAmount) {
            throw new InvalidPaymentException("Insufficient payment. Required: " + totalAmount + " EGP, Provided: " + amount + " EGP");
        }
        if (method == null) {
            throw new InvalidPaymentException("Payment method must be specified");
        }

        if (method != PaymentMethod.CASH) {
            if (reservation.getGuest().getBalance() < totalAmount) {
                throw new InvalidPaymentException("Insufficient guest balance. Required: " + totalAmount + " EGP, Available: " + reservation.getGuest().getBalance() + " EGP");
            }
        }

        this.paymentMethod = method;
        this.paymentDate = LocalDateTime.now();
        this.paid = true;
        reservation.setStatus(ReservationStatus.COMPLETED);

        System.out.println("Payment of " + totalAmount + " EGP processed via " + method);
        System.out.println("Payment date: " + paymentDate);
    }
}
