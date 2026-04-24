import java.time.LocalDate;

public abstract class Staff {

    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private Role role;
    private Schedule workingHours;

    public Staff(String username, String password, LocalDate dateOfBirth, Role role, Schedule workingHours) {
        setUsername(username);
        setPassword(password);
        setDateOfBirth(dateOfBirth);
        this.role = role;
        this.workingHours = workingHours;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }
    public Role getRole(){
        return role;
    }
    public Schedule getWorkingHours() {
        return workingHours;
    }

    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }
        this.username = username.trim();
    }

    public void setPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        this.password = password;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth must be a past date");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public void setRole(Role role){
        this.role = role;
    }
    public void setWorkingHours(Schedule schedule){
        this.workingHours = schedule;
    }

    public void viewAllGuests() {
        System.out.println("\n--- All Guests (" + HotelDatabase.guests.size() + ") ---");
        for (Guest guest : HotelDatabase.guests){
            guest.printProfile();
        }
    }

    public void viewAllRooms() {
        System.out.println("\n--- All Rooms (" + HotelDatabase.rooms.size() + ") ---");
        for (Room room : HotelDatabase.rooms) {
            room.printInfo();
        }
    }

    public void viewAllReservations() {
        System.out.println("\n--- All Reservations (" + HotelDatabase.reservations.size() + ") ---");
        for (Reservation reservation : HotelDatabase.reservations) {
            reservation.printInfo();
        }
    }

    public static Staff login(String username, String password) {
        for (Staff staff : HotelDatabase.staffs) {
            if (staff.getUsername().equalsIgnoreCase(username) && staff.getPassword().equals(password)) {
                System.out.println("Staff login successful. Welcome, " + staff.getUsername() + " [" + staff.getRole() + "]");
                return staff;
            }
        }
        System.out.println("Staff login failed: invalid credentials.");
        return null;
    }

    public boolean staffRegister() {
        for (Staff staff : HotelDatabase.staffs) {
            if (staff.getUsername().equalsIgnoreCase(this.username)) {
                System.out.println("Registration failed: staff '" + username + "' already exists.");
                return false;
            }
        }
        HotelDatabase.staffs.add(this);
        System.out.println("staff '" + username + "' registered successfully.");
        return true;
    }
}
