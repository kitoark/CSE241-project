import java.time.LocalDate;

abstract public class Staff {
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private Role role;
    private Schedule workingHours;


    public Staff(String username,String password,LocalDate dateOfBirth,Role role,Schedule workingHours){
        this.username=username;
        this.password=password;
        this.dateOfBirth=dateOfBirth;
        this.role=role;
        this.workingHours = workingHours;
    }

    public void viewAllGuests(){
        System.out.println("Viewing "+ HotelDatabase.guests.size()+" guests");
    }

    public void viewAllRooms(){
    System.out.println("viewing"+ HotelDatabase.rooms.size()+"rooms");
    }

    public void viewAllReservations(){
        System.out.println("viewing"+ HotelDatabase.reservations.size()+"reservations");
    }

    public String getUsername(){
        return username;
    }

    public Role getRole(){
        return role;
    }

    public String getPassword(){
        return password;
    }

    public LocalDate getDateOfBirth(){
        return dateOfBirth;
    }

    public Schedule getWorkingHours(){
        return workingHours;
    }

    public void setUsername(String username){
        this.username=username;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setRole(Role role){
        this.role=role;
    }

    public void setDateOfBirth(Role role){
        this.role=role;
    }

}

