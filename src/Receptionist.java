import java.time.LocalDate;

public class Receptionist extends Staff{
    public Receptionist(String username, String password, LocalDate dateOfBirth, Schedule workingHours){
        super(username, password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }

    public void managecheckin(Reservation reservation){
        if(reservation.getStatus() == ReservationStatus.PENDING || reservation.getStatus() == ReservationStatus.CONFIRMED){
            reservation.getRoom().setStatus(Room.RoomStatus.AVAILABLE);
            System.out.println("Successfully checked in guest for reservation: " + reservation.getReservationId());
        }
        else {
            System.out.println("Cannot check in. Reservation status is currently: " + reservation.getStatus());
        
        }
    }

    public void manageCheckOut(Reservation reservation, PaymentMethod method) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) return;
        try {
            double amountDue = reservation.getRoom().getRoomType().getBasePricePerNight();
            Invoice invoice = new Invoice(reservation, amountDue);
            
            invoice.processPayment(amountDue, method);
            HotelDatabase.invoices.add(invoice);
            
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservation.getRoom().setStatus(Room.RoomStatus.RESERVED);
            
            System.out.println("Check-out complete. Room " + reservation.getRoom().getRoomNumber() + " is available.");
        }
        catch (InvalidPaymentException e) {
            System.out.println(e.getMessage());
        }
    }
}
