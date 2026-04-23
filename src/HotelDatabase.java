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
        //addGuest(new Guest());
        //addRoom(new Room(1000,1,1,new RoomType(1000,"Single Room",1,250.99)));
        //addInvoice(new Invoice());
        //addReservation(new Reservation());
    }
}
