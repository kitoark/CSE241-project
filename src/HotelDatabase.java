import java.util.ArrayList;

public class HotelDatabase {
    public static ArrayList<Guest> guests;
    public static ArrayList<Reservation> reservations;
    public static ArrayList<Invoice> invoices;
    public static ArrayList<Room> rooms;
    public static ArrayList<Amenity> amenitiesDatabase;


    public static void addGuest(Guest guest){
        guests.add(guest);
    }

    public static void addReservation(Reservation reservation){
        reservations.add(reservation);
    }
    
    public static void addRoom(Room room){
        rooms.add(room);
    }

    public static void addInvoice(Invoice invoice){
        invoices.add(invoice);
    }

    public static void createDummyData(){
        addGuest(new Guest());
        addRoom(new Room(1000,1,1,new RoomType(1000,"Single Room",1,250.99)));
        //addInvoice(new Invoice());
        //addReservation(new Reservation());
    }
}
