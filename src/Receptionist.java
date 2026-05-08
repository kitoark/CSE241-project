import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Receptionist extends Staff {

    public Receptionist(String username, String password, LocalDate dateOfBirth, Schedule workingHours) {
        super(username, password, dateOfBirth, Role.RECEPTIONIST, workingHours);
    }
    public void manageCheckIn(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.COMPLETED || reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Cannot check in. Reservation status is: " + reservation.getStatus());
            return;
        }
        reservation.setStatus(ReservationStatus.CONFIRMED);
        if (reservation.getRoom().getStatus() != Room.RoomStatus.OCCUPIED) {
            reservation.getRoom().setStatus(Room.RoomStatus.OCCUPIED);
        }
        System.out.println("Guest '" + reservation.getGuest().getUsername() + "' checked in to room " + reservation.getRoom().getRoomNumber());
    }
    public void manageCheckOut(Reservation reservation, PaymentMethod method) throws InvalidPaymentException {
        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new InvalidPaymentException("Cannot check out. Reservation status is: " + reservation.getStatus());
        }

        long nights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
        double amountDue = reservation.getRoom().getTotalCostForStay((int) nights);

        Invoice invoice = new Invoice(reservation, amountDue);
        invoice.processPayment(amountDue, method);

        if (method != PaymentMethod.CASH) {
            reservation.getGuest().setBalance(reservation.getGuest().getBalance() - amountDue);
        }

        HotelDatabase.invoices.add(invoice);
        reservation.getRoom().checkOut();
        System.out.println("Check-out complete for reservation #" + reservation.getReservationId());
        invoice.printInfo();
    }
}
