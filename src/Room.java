import java.util.ArrayList;

public class Room {
    // Status options for a room
    public enum RoomStatus {
        AVAILABLE,
        RESERVED,
        OCCUPIED,
        UNDER_MAINTENANCE
    }

    private int roomId;
    private int roomNumber;
    private int floorNumber;
    private RoomType roomType;
    private RoomStatus status;
    private ArrayList<Amenity> amenities;

    // Constructor
    public Room(int roomId, int roomNumber, int floorNumber, RoomType roomType) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.roomType = roomType;
        this.status = RoomStatus.AVAILABLE;  // default status
        this.amenities = new ArrayList<Amenity>();
    }
   //  Getters & Setters 

    public int getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public ArrayList<Amenity> getAmenities() {
        return amenities;
    }

    //  Room.Amenity Methods

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
        System.out.println(amenity.getName() + " added to room " + roomNumber);
    }

    public void removeAmenity(int amenityId) {
        for (int i = 0; i < amenities.size(); i++) {
            if (amenities.get(i).getAmenityId() == amenityId) {
                System.out.println(amenities.get(i).getName() + " removed from room " + roomNumber);
                amenities.remove(i);
                return;
            }
        }
        System.out.println("Room.Amenity not found in this room.");
    }

    public void printAmenities() {
        if (amenities.size() == 0) {
            System.out.println("No amenities for this room.");
        } else {
            System.out.println("Amenities in Room.Room " + roomNumber + ":");
            for (int i = 0; i < amenities.size(); i++) {
                System.out.println("  - " + amenities.get(i).getName()
                        + " (+" + amenities.get(i).getExtraCostPerNight() + " EGP/night)");
            }
        }
    }

    //  Pricing 

    public double getTotalPricePerNight() {
        double total = roomType.getBasePricePerNight();
        for (int i = 0; i < amenities.size(); i++) {
            total += amenities.get(i).getExtraCostPerNight();
        }
        return total;
    }

    public double getTotalCostForStay(int nights) {
        return getTotalPricePerNight() * nights;
    }

    // Availability Methods

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    public void reserve() {
        if (status == RoomStatus.AVAILABLE) {
            status = RoomStatus.RESERVED;
            System.out.println("Room " + roomNumber + " is now RESERVED.");
        } else {
            System.out.println("Room " + roomNumber + " is not available. Current status: " + status);
        }
    }

    public void checkIn() {
        if (status == RoomStatus.RESERVED) {
            status = RoomStatus.OCCUPIED;
            System.out.println("Guest checked in to room " + roomNumber);
        } else {
            System.out.println("Cannot check in. Room.Room status is: " + status);
        }
    }

    public void checkOut() {
        if (status == RoomStatus.OCCUPIED) {
            status = RoomStatus.AVAILABLE;
            System.out.println("Guest checked out from room " + roomNumber);
        } else {
            System.out.println("Cannot check out. Room.Room status is: " + status);
        }
    }

    public void cancelReservation() {
        if (status == RoomStatus.RESERVED) {
            status = RoomStatus.AVAILABLE;
            System.out.println("Reservation for room " + roomNumber + " has been cancelled.");
        } else {
            System.out.println("No active reservation to cancel. Room.Room status is: " + status);
        }
    }

 
    //  Print Info 

    public void printInfo() {
        System.out.println("-----------------------------");
        System.out.println("Room.Room ID     : " + roomId);
        System.out.println("Room.Room Number : " + roomNumber);
        System.out.println("Floor       : " + floorNumber);
        System.out.println("Type        : " + roomType.getTypeName());
        System.out.println("Status      : " + status);
        System.out.println("Price/Night : " + getTotalPricePerNight() + " EGP");
        printAmenities();
        System.out.println("-----------------------------");
    }
}