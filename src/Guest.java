import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Guest {

    private String username;
    private String password;
    private String dateOfBirth;
    private double balance;
    private String address;
    private Gender gender;
    private String roomPreferences;
    private List<Reservation> myReservations;

    public Guest(){}
    public Guest(String username, String password, String dateOfBirth,
                 double balance, String address, Gender gender, String roomPreferences) {
        setUsername(username);
        setPassword(password);
        this.dateOfBirth = dateOfBirth;
        setBalance(balance);
        this.address = address;
        this.gender = gender;
        this.roomPreferences = roomPreferences;
        //this.myReservations = new ArrayList<>();
    }


    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        this.password = password;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            System.out.println("Balance cannot be negative");
        }
        this.balance = balance;
    }

    //  Guest Actions

     public void displayAvailableRooms() {
       System.out.println("Displaying all available rooms from Database...");
    }

     public void makeReservation(Room room, Date startDate, Date endDate) {

       System.out.println("Reservation made for room: " + room.getRoomNumber());
    }

    public void cancelReservation(Reservation reservation) {
       myReservations.remove(reservation);
     reservation.setStatus(ReservationStatus.CANCELLED);
    }


    public String getUsername() { return username; }
    public double getBalance() { return balance; }
    public List<Reservation> getMyReservations() { return myReservations; }
}
