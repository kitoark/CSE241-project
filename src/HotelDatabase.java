import java.time.LocalDate;
import java.util.ArrayList;

public class HotelDatabase {
    public static ArrayList<Guest> guests = new ArrayList<>();
    public static ArrayList<Staff> staffs = new ArrayList<>();
    public static ArrayList<Reservation> reservations = new ArrayList<>();
    public static ArrayList<Invoice> invoices = new ArrayList<>();
    public static ArrayList<Room> rooms = new ArrayList<>();
    public static ArrayList<RoomType> roomTypes =  new ArrayList<>();
    public static ArrayList<Amenity> amenitiesDatabase = new ArrayList<>();


    public static void addGuest(Guest guest){
        guests.add(guest);
    }
    public static void addStaff(Staff staff){
        staffs.add(staff);
    }
    public static void addReservation(Reservation reservation){
        reservations.add(reservation);
    }
    public static void addRoom(Room room){
        rooms.add(room);
    }
    public static void addAmenity(Amenity amenity){
        amenitiesDatabase.add(amenity);
    }
    public static void addInvoice(Invoice invoice){
        invoices.add(invoice);
    }
    public static void addRoomType(RoomType roomType){
        roomTypes.add(roomType);
    }


    public static void createDummyData(){
        RoomType single = new RoomType(1, "Single",1, 250.00);
        RoomType dbl = new RoomType(2, "Double",2, 450.00);
        RoomType suite = new RoomType(3, "Suite",4, 900.00);
        addRoomType(single);
        addRoomType(dbl);
        addRoomType(suite);

        Amenity wifi = new Amenity(1, "WiFi",0.00);
        Amenity tv = new Amenity(2, "TV",50.00);
        Amenity minibar = new Amenity(3, "Mini-Bar",100.00);
        Amenity jacuzzi = new Amenity(4, "Jacuzzi",200.00);
        addAmenity(wifi);
        addAmenity(tv);
        addAmenity(minibar);
        addAmenity(jacuzzi);

        Room r101 = new Room(101, 101, 1, single);
        r101.addAmenity(wifi);
        r101.addAmenity(tv);

        Room r102 = new Room(102, 102, 1, single);
        r102.addAmenity(wifi);

        Room r201 = new Room(201, 201, 2, dbl);
        r201.addAmenity(wifi);
        r201.addAmenity(tv);
        r201.addAmenity(minibar);

        Room r202 = new Room(202, 202, 2, dbl);
        r202.addAmenity(wifi);
        r202.addAmenity(tv);

        Room r301 = new Room(301, 301, 3, suite);
        r301.addAmenity(wifi);
        r301.addAmenity(tv);
        r301.addAmenity(minibar);
        r301.addAmenity(jacuzzi);

        addRoom(r101);
        addRoom(r102);
        addRoom(r201);
        addRoom(r202);
        addRoom(r301);

        Guest ahmed = new Guest("ahmed",   "123456", LocalDate.of(2003, 3, 14), 5000.00, "12 Nile St, Cairo",   Gender.MALE, "Double, high floor");
        Guest mohamed = new Guest("mohamed",     "secret67", LocalDate.of(2006, 7,  4), 8000.00, "7 Tahrir Sq, Cairo",  Gender.MALE,   "Suite, quiet room");
        Guest mahmoud = new Guest("mahmoud",   "mahmoud456", LocalDate.of(2007, 11, 22), 1500.00, "88 Corniche, Alex",    Gender.MALE, "Single, low floor");
        addGuest(ahmed);
        addGuest(mohamed);
        addGuest(mahmoud);

        Schedule morningShift   = new Schedule(8,  16);
        Schedule eveningShift   = new Schedule(16, 22);

        Admin admin = new Admin("admin",  "adminPass1", LocalDate.of(1980, 1, 1), morningShift);
        Receptionist rec = new Receptionist("receptionist1", "recPass1", LocalDate.of(1992, 5, 20), eveningShift);
        addStaff(admin);
        addStaff(rec);

        Reservation reservation = new Reservation(mohamed, r201, LocalDate.now().plusDays(1), LocalDate.now().plusDays(4), ReservationStatus.PENDING, 1);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        r201.setStatus(Room.RoomStatus.OCCUPIED);
        mohamed.getMyReservations().add(reservation);
        addReservation(reservation);
    }
}
