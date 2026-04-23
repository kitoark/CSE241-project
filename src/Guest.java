import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Guest {

    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private double balance;
    private String address;
    private Gender gender;
    private String roomPreferences;
    private List<Reservation> myReservations;

    public Guest(){}
    public Guest(String username, String password, LocalDate dateOfBirth,
                 double balance, String address, Gender gender, String roomPreferences) {
        setUsername(username);
        setPassword(password);
        this.dateOfBirth = dateOfBirth;
        setBalance(balance);
        this.address = address;
        this.gender = gender;
        this.roomPreferences = roomPreferences;
        this.myReservations = new ArrayList<>();
    }

    //  Setters

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }
    public void setPassword(String password) throws IllegalArgumentException{
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        this.password = password;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must be a past date.");
        }
        this.dateOfBirth = dateOfBirth;
    }
    public void setBalance(double balance) throws IllegalArgumentException{
        if (balance < 0) {
            System.out.println("Balance cannot be negative");
        }
        this.balance = balance;
    }
    public void setGender(Gender gender){
        this.gender = gender;
    }
    public void setRoomPreferences(String prefs){
        this.roomPreferences = prefs;
    }

    //  Getters

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }
    public double getBalance(){
        return balance;
    }
    public String getAddress(){
        return address;
    }
    public Gender getGender(){
        return gender;
    }
    public String getRoomPreferences(){
        return roomPreferences;
    }
    public List<Reservation> getMyReservations(){
        return myReservations;
    }

    //  Guest Actions

    public boolean register() {
        for (Guest guest : HotelDatabase.guests) {
            if (guest.getUsername().equalsIgnoreCase(this.username)) {
                System.out.println("Registration failed: username '" + username + "' is already taken.");
                return false;
            }
        }
        HotelDatabase.guests.add(this);
        System.out.println("Guest '" + username + "' registered successfully.");
        return true;
    }

    public static Guest login(String username, String password) {
        for (Guest guest : HotelDatabase.guests) {
            if (guest.getUsername().equalsIgnoreCase(username) && guest.getPassword().equals(password)) {
                System.out.println("Login successful. Welcome, " + guest.getUsername() + "!");
                return guest;
            }
        }
        System.out.println("Login failed: invalid username or password.");
        return null;
    }

    public void viewAvailableRooms() {
        System.out.println("--- Available Rooms ---");
        boolean found = false;
        for (Room room : HotelDatabase.rooms) {
            if (room.isAvailable()) {
                room.printInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No rooms are currently available");
    }

    public Reservation makeReservation(Room room, LocalDate checkIn, LocalDate checkOut) throws RoomNotAvailableException {
        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is not available");
        }

        if (checkIn == null || checkOut == null || checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Check-in date must be before check-out date");
        }

        if (checkIn.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Check-in date cannot be in the past");
        }

        int newId = HotelDatabase.reservations.size() + 1;
        Reservation reservation = new Reservation(this, room, checkIn, checkOut, ReservationStatus.PENDING, newId);
        room.reserve();
        myReservations.add(reservation);
        HotelDatabase.reservations.add(reservation);
        System.out.println("Reservation #" + newId + " created for room " + room.getRoomNumber() + " (From :" + checkIn + " To :" + checkOut + ")");
        return reservation;
    }

    public void viewReservations() {
        System.out.println("\n=== Your Reservations ===");
        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }
        for (Reservation reservation : myReservations) {
            reservation.printInfo();
        }
    }

    public void cancelReservation(Reservation reservation) {
        if (!myReservations.contains(reservation)) {
            System.out.println("Reservation not found in your account.");
            return;
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED
                || reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Cannot cancel a reservation with status: " + reservation.getStatus());
            return;
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getRoom().cancelReservation();
        System.out.println("Reservation #" + reservation.getReservationId() + " cancelled.");
    }

    public void printProfile() {
        System.out.println("--- Guest Profile ---");
        System.out.println("  Username     : " + username);
        System.out.println("  Date of Birth: " + dateOfBirth);
        System.out.println("  Gender       : " + gender);
        System.out.println("  Address      : " + address);
        System.out.println("  Balance      : " + balance + " EGP");
        System.out.println("  Preferences  : " + roomPreferences);
        System.out.println("  Reservations : " + myReservations.size());
    }
}
