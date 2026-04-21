import java.util.ArrayList;

public class HotelDatabase {
    public static ArrayList<Guest> guests;
    public static ArrayList<Reservation> reservations;
    public static ArrayList<Amenity> amenities;
    public static ArrayList<Invoice> invoices;
    public static ArrayList<Room> rooms;



    public void addGuest(Guest guest){
        guests.add(guest);
    }

    public void addReservation(Reservation reservation){
        reservations.add(reservation);
    }

    public void addRoom(Room room){
        rooms.add(room);
    }

    public void addInvoice(Invoice invoice){
        invoices.add(invoice);
    }

    public void addAmenity(Amenity amenity){
        amenities.add(amenity);
    }
}
