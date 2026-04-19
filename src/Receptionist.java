import java.time.LocalDate;

public class Receptionist extends Staff{
    public Receptionist(String username, String password, LocalDate dateOfBirth, String workingHours){
  super(username, password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }
    public void managecheckin(Reservation reservation){
        if(reservation.getstatus()== ReservationStatus.PENDING || reservation.getStatus() == ReservationStatus.CONFIRMED){
            reservation.getRoom().setAvailable(false)
        System.out.println("Successfully checked in guest for reservation: " + reservation.getReservationId());
        } else {
            System.out.println("Cannot check in. Reservation status is currently: " + reservation.getStatus());
        
    }
}
public void manageCheckOut(Reservation reservation, PaymentMethod method) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) return;

        try {
            double amountDue = reservation.getRoom().getRoomType().getBasePrice(); 
            Invoice invoice = new Invoice("INV-" + reservation.getReservationId(), amountDue);
            
            invoice.processPayment(amountDue, method);
            HotelDatabase.invoices.add(invoice);
            
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservation.getRoom().setAvailable(true);
            
            System.out.println("Check-out complete. Room " + reservation.getRoom().getRoomNumber() + " is available.");
        } catch (InvalidPaymentException e) {
            System.out.println(e.getMessage());
        }
    }
}
}