import java.util.ArrayList;

public class Room implements Manageable {

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

    public Room(int roomId, int roomNumber, int floorNumber, RoomType roomType) {
        if (roomNumber <= 0) throw new IllegalArgumentException("Room number must be positive.");
        if (floorNumber < 0) throw new IllegalArgumentException("Floor number cannot be negative.");
        if (roomType == null)  throw new IllegalArgumentException("RoomType cannot be null.");
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.roomType = roomType;
        this.status = RoomStatus.AVAILABLE;
        this.amenities = new ArrayList<>();
    }

    // Getters
    public int           getRoomId()     { return roomId; }
    public int           getRoomNumber() { return roomNumber; }
    public int           getFloorNumber(){ return floorNumber; }
    public RoomType      getRoomType()   { return roomType; }
    public RoomStatus    getStatus()     { return status; }
    public ArrayList<Amenity> getAmenities() { return amenities; }

    // Setters
    public void setRoomNumber(int roomNumber)   { this.roomNumber  = roomNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }
    public void setRoomType(RoomType roomType)  { this.roomType    = roomType; }
    public void setStatus(RoomStatus status)    { this.status      = status; }

    public void setAvailable(boolean available) {
        this.status = available ? RoomStatus.AVAILABLE : RoomStatus.OCCUPIED;
    }

    public void addAmenity(Amenity amenity) {
        amenities.add(amenity);
        System.out.println(amenity.getName() + " added to room " + roomNumber + ".");
    }

    public void removeAmenity(int amenityId) {
        for (int i = 0; i < amenities.size(); i++) {
            if (amenities.get(i).getAmenityId() == amenityId) {
                System.out.println(amenities.get(i).getName() + " removed from room " + roomNumber + ".");
                amenities.remove(i);
                return;
            }
        }
        System.out.println("Amenity with ID " + amenityId + " not found in room " + roomNumber + ".");
    }

    public void printAmenities() {
        if (amenities.isEmpty()) {
            System.out.println("  No amenities for this room.");
        } else {
            System.out.println("  Amenities:");
            for (Amenity a : amenities) {
                System.out.println("    - " + a);
            }
        }
    }


    public double getTotalPricePerNight() {
        double total = roomType.getBasePricePerNight();
        for (Amenity a : amenities) total += a.getExtraCostPerNight();
        return total;
    }

    public double getTotalCostForStay(int nights) {
        return getTotalPricePerNight() * nights;
    }

    public boolean isAvailable() { return status == RoomStatus.AVAILABLE; }

    public void reserve() {
        if (status == RoomStatus.AVAILABLE) {
            status = RoomStatus.RESERVED;
            System.out.println("Room " + roomNumber + " is now RESERVED.");
        }
        else {
            throw new RoomNotAvailableException("Room " + roomNumber + " is not available. Current status: " + status);
        }
    }

    public void checkIn() {
        if (status == RoomStatus.RESERVED) {
            status = RoomStatus.OCCUPIED;
            System.out.println("Guest checked in to room " + roomNumber + ".");
        }
        else {
            System.out.println("Cannot check in. Room status is: " + status);
        }
    }

    public void checkOut() {
        if (status == RoomStatus.OCCUPIED) {
            status = RoomStatus.AVAILABLE;
            System.out.println("Guest checked out from room " + roomNumber + ".");
        }
        else {
            System.out.println("Cannot check out. Room status is: " + status);
        }
    }

    public void cancelReservation() {
        if (status == RoomStatus.RESERVED) {
            status = RoomStatus.AVAILABLE;
            System.out.println("Reservation for room " + roomNumber + " has been cancelled.");
        }
        else {
            System.out.println("No active reservation to cancel. Room status is: " + status);
        }
    }

    @Override
    public void printInfo() {
        System.out.println("-----------------------------");
        System.out.println("  Room ID     : " + roomId);
        System.out.println("  Room Number : " + roomNumber);
        System.out.println("  Floor       : " + floorNumber);
        System.out.println("  Type        : " + roomType.getTypeName());
        System.out.println("  Status      : " + status);
        System.out.println("  Price/Night : " + getTotalPricePerNight() + " EGP");
        printAmenities();
        System.out.println("-----------------------------");
    }
}
