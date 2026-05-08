import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        setDateOfBirth(dateOfBirth);
        setBalance(balance);
        setAddress(address);
        this.gender = gender;
        setRoomPreferences(roomPreferences);
        this.myReservations = new ArrayList<>();
    }

    //  Setters

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            RegisterController.errorLabelText ="Username cannot be empty.";
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }
    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            RegisterController.errorLabelText ="Address cannot be empty.";
            throw new IllegalArgumentException("Address cannot be empty.");
        }
        this.address = address.trim();
    }
    public void setPassword(String password) throws IllegalArgumentException{
        if (password == null || password.length() < 6) {
            RegisterController.errorLabelText ="Password must be at least 6 characters.";
            throw new IllegalArgumentException("Password must be at least 6 characters.");
        }
        this.password = password;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            RegisterController.errorLabelText ="Date of birth must be a past date.";
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
        if (prefs == null || prefs.trim().isEmpty()) {
            RegisterController.errorLabelText ="Preferences cannot be empty.";
            throw new IllegalArgumentException("Preferences cannot be empty.");
        }
        this.roomPreferences = prefs.trim();
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
                System.out.println("Registration failed: username '" + username + "' is already taken");
                return false;
            }
        }
        HotelDatabase.guests.add(this);
        System.out.println("Guest '" + username + "' registered successfully");
        return true;
    }

    public static Guest login(String username, String password) {
        for (Guest guest : HotelDatabase.guests) {
            if (guest.getUsername().equalsIgnoreCase(username) && guest.getPassword().equals(password)) {
                return guest;
            }
        }
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
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        double totalCost = room.getTotalCostForStay((int) nights);
        if (this.balance < totalCost) {
            throw new IllegalArgumentException("Insufficient balance. Required: " + totalCost + " EGP, Available: " + this.balance + " EGP");
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
            System.out.println("You have no reservations");
            return;
        }
        for (Reservation reservation : myReservations) {
            reservation.printInfo();
        }
    }

    public void cancelReservation(Reservation reservation) {
        if (!myReservations.contains(reservation)) {
            System.out.println("Reservation not found in your account");
            return;
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED
                || reservation.getStatus() == ReservationStatus.CANCELLED) {
            System.out.println("Cannot cancel a reservation with status: " + reservation.getStatus());
            return;
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.getRoom().cancelReservation();
        System.out.println("Reservation #" + reservation.getReservationId() + " cancelled");
    }

    public void checkout(Reservation reservation, PaymentMethod method) throws InvalidPaymentException {

        if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
            throw new InvalidPaymentException("Only CONFIRMED reservations can be checked out. Current status: " + reservation.getStatus());
        }

        long nights = ChronoUnit.DAYS.between(reservation.getCheckIn(), reservation.getCheckOut());
        double amount = reservation.getRoom().getTotalCostForStay((int) nights);

        //Assuming the guest always has enough when selecting cash.
        if (method != PaymentMethod.CASH && balance < amount) {
            throw new InvalidPaymentException("Insufficient balance. Required: " + amount + " EGP, Available: " + balance + " EGP");
        }

        Invoice invoice = new Invoice(reservation, amount);
        invoice.processPayment(amount, method);

        if (method != PaymentMethod.CASH) {
            this.balance -= amount;
        }

        reservation.getRoom().checkOut();
        HotelDatabase.invoices.add(invoice);
        System.out.println("Checkout complete. Invoice total: " + amount + " EGP");
    }


    @Override
    public String toString() {
        return username;
    }

    public void printProfile() {
        System.out.println("---- Guest Profile ----");
        System.out.println("  Username     : " + username);
        System.out.println("  Birthday     : " + dateOfBirth);
        System.out.println("  Gender       : " + gender);
        System.out.println("  Address      : " + address);
        System.out.println("  Balance      : " + balance + " EGP");
        System.out.println("  Preferences  : " + roomPreferences);
        System.out.println("  Reservations : " + myReservations.size());
    }
}
