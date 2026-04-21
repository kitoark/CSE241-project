import java.time.LocalDate;
public class Reservation {
    private Guest guest;
    private Room room;
    private LocalDate checkin;
    private LocalDate checkout;
    private ReservationStatus status;
    private int reservationId;
    //Reservation Parameterized Constructor
    public Reservation(Guest guest,Room room,LocalDate checkin,LocalDate checkout,ReservationStatus status,int reservationId)
    {
        this.guest=guest;
        this.room=room;
        this.checkin=checkin;
        this.checkout=checkout;
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
    public LocalDate getCheckin()
    {
        return checkin;
    }
    public LocalDate getCheckout()
    {
        return checkout;
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
    public void setCheckin(LocalDate checkin)
    {
        this.checkin=checkin;
    }
    public void setCheckout(LocalDate checkout)
    {
        if (checkin != null && checkin.isBefore(checkout))
        {
            System.out.println("Error: Checkin must be before checkout.");
            return;
        }
        this.checkout=checkout;
    }
    public void setStatus(ReservationStatus status)
    {
        this.status=status;
    }
}