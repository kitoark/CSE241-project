package Booking;

import java.time.LocalDateTime;

public class Invoice implements Payable {
    private Reservation reservation;
    private double totalAmount;
    private PaymentMethod method;
    private LocalDateTime paymentDate;

    //Invoice Parameterized Constructor
    public Invoice(Reservation reservation,double amount)
    {
        this.reservation=reservation;
        this.totalAmount=amount;
    }
    @Override
    //To check amount is valid or not
    public void Payment(double amount , PaymentMethod method) throws InvalidPaymentException {
        if (amount < 0) {
            throw new InvalidPaymentException("Error:Negative Amount is Invalid Amount.");
        }

        // To check if Guest's Balance after Booking is Sufficient or not When using Online or CreditCard
        if (reservation.getGuest().getBalance() < amount) {
            throw new InvalidPaymentException("Error: Insufficient Guest Balance");

        }
        this.method = method;
        this.paymentDate= LocalDateTime.now();
        this.reservation.setStatus(ReservationStatus.COMPLETED);
        System.out.println("Payment of:"+amount+"EGP"+"\n"+"Processed via:"+method);
    }
}
