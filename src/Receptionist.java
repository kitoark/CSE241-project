import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Receptionist extends Staff {

    public Receptionist(String username, String password, LocalDate dateOfBirth, Schedule workingHours) {
        super(username, password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }
    public void manageCheckIn(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.PENDING || reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.getRoom().checkIn();
            System.out.println("Guest '" + reservation.getGuest().getUsername() + "' successfully checked in to room " + reservation.getRoom().getRoomNumber());
        } else {
            System.out.println("Cannot check in. Reservation #" + reservation.getReservationId() + " has status: " + reservation.getStatus());
        }
    }
    public void manageCheckOut(Reservation reservation, PaymentMethod method) {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            System.out.println("Cannot check out. Reservation #" + reservation.getReservationId() + " is not CONFIRMED. Current status: " + reservation.getStatus());
            return;
        }

        try {
            long nights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());

            double amountDue = reservation.getRoom().getTotalCostForStay((int) nights);

            Invoice invoice = new Invoice(reservation, amountDue);

            invoice.processPayment(amountDue, method);
            HotelDatabase.invoices.add(invoice);

            reservation.getRoom().checkOut();
            System.out.println("Check-out complete for reservation #" + reservation.getReservationId() + ". Room " + reservation.getRoom().getRoomNumber() + " is now available");
            invoice.printInfo();
        }
        catch (InvalidPaymentException e) {
            System.out.println("Check-out failed: " + e.getMessage());
        }
    }
}
