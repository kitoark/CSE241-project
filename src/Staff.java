import java.time.LocalDate;

abstract public class Staff {
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private Role role;
    private Schedule workingHours;


public Staff(String username,String password,LocalDate dateOfBirth,Role role,String workingHours2){
this.username=username;
this.password=password;
this.dateOfBirth=dateOfBirth;
this.role=role;
this.workingHours=workingHours;
}
public void viewAllGuests(){
System.out.println("Viewing"+guests.size()+"guests");
}
public void viewAllRooms(){
System.out.println("viewing"+rooms.size()+"rooms");
}
public void viewAllREservations(){
System.out.println("viewing"+reservations.size()+"reservations");
}
public String getUsername(){
    return username;
}
public Role getRole(){
    return role;
}
public String getpassword(){
    return password;
}
public LocalDate getdateOfBirth(){
    return dateOfBirth;
}
public Schedule getworkinghours(){
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
public void setdateOfBirth(Role role){
    this.role=role;
}

}

