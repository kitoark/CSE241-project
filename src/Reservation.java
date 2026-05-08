import java.time.LocalDate;

public class Reservation {

    private int reservationId;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;

    public Reservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, ReservationStatus status, int reservationId) {
        if (guest == null)  throw new IllegalArgumentException("Guest cannot be null.");
        if (room  == null)  throw new IllegalArgumentException("Room cannot be null.");
        validateDates(checkIn, checkOut);
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = ReservationStatus.PENDING;
        this.reservationId = reservationId;
    }

    public int getReservationId() {
        return reservationId;
    }
    public Guest getGuest(){
        return guest;
    }
    public Room getRoom(){
        return room;
    }
    public LocalDate getCheckIn(){
        return checkIn;
    }
    public LocalDate getCheckOut(){
        return checkOut;
    }
    public ReservationStatus getStatus(){
        return status;
    }


    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setCheckIn(LocalDate checkIn) {
        validateDates(checkIn, this.checkOut);
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        validateDates(this.checkIn, checkOut);
        this.checkOut = checkOut;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    private static void validateDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null)  throw new IllegalArgumentException("Check-in date cannot be null.");
        if (checkOut == null) throw new IllegalArgumentException("Check-out date cannot be null.");
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Check-in date (" + checkIn + ") must be before check-out date (" + checkOut + ").");
        }
    }

    public void printInfo() {
        System.out.println("  Reservation #" + reservationId + " | Room: " + room.getRoomNumber() + " | " + checkIn + " -> " + checkOut + " | Status: " + status);
    }
}
