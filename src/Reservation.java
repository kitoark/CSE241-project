import java.time.LocalDate;

public class Reservation{
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private ReservationStatus status;
    private int reservationId;
    //Reservation Parameterized Constructor
    public Reservation(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut, ReservationStatus status, int reservationId) {
        this.guest=guest;
        this.room=room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status=ReservationStatus.PENDING;
        this.reservationId=reservationId;
    }

    //Reservation Getters
    public Room getRoom()
    {
        return room;
    }
    public Guest getGuest()
    {
        return guest;
    }
    public LocalDate getCheckIn()
    {
        return checkIn;
    }
    public LocalDate getCheckOut()
    {
        return checkOut;
    }
    public ReservationStatus getStatus()
    {
        return status;
    }
    public int getReservationId()
    {
        return reservationId;
    }
    //Reservation Setters
    public void setRoom(Room room)
    {
        this.room=room;
    }
    public void setGuest(Guest guest)
    {
        this.guest=guest;
    }
    public void setCheckIn(LocalDate checkIn)
    {
        this.checkIn = checkIn;
    }
    public void setCheckOut(LocalDate checkOut)
    {
        if (checkIn != null && checkIn.isAfter(checkOut))
        {
            System.out.println("Error: Checkin must be before checkout.");
            return;
        }
        this.checkOut = checkOut;
    }
    public void setStatus(ReservationStatus status)
    {
        this.status=status;
    }

    public void printInfo() {
        System.out.println("  Reservation #" + reservationId + " | Room: " + room.getRoomNumber() + " | " + checkIn + " → " + checkOut + " | Status: " + status);
    }
}