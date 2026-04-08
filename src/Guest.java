import java.time.LocalDate;
import java.util.Date;

public class Guest {
    //username (String), password (String, validated), dateOfBirth (LocalDate or Date),
    //balance (double), address (String), gender (must be an enum: MALE, FEMALE),
    //roomPreferences (e.g., preferred room type, floor, etc.)
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private double balance;
    private String address;
    private Gender gender;
    private RoomType roomPrefrences;

}
