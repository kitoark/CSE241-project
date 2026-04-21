import java.time.LocalDate;

public class Admin extends Staff {
    public Admin(String username, String password, LocalDate dateOfBirth, Role role, Schedule workingHours) {
        super(username, password, dateOfBirth, role, workingHours);
    }

    public void createRoom(Room room) {
        HotelDatabase.rooms.add(room);
        System.out.println("Room " + room.getRoomNumber() + " is added to the system");
    }

    public void readRoom(int roomNumber) {
        for (Room room : HotelDatabase.rooms) {
            if (room.getRoomNumber() == roomNumber) {
                System.out.println("Room " + room.getRoomNumber() + " Type:" + room.getRoomType().getTypeName() + "Available: "
                        + room.isAvailable());
                return;
            }
        }
        System.out.println("room with room number " + roomNumber + " could not be found");
    }

    public void updateRoom(int oldRoomNumber, Room newRoomData) {
        for (int i = 0; i < HotelDatabase.rooms.size(); i++) {
            if (HotelDatabase.rooms.get(i).getRoomNumber() == oldRoomNumber) {
                HotelDatabase.rooms.set(i, newRoomData);
                System.out.println("Room " + oldRoomNumber + " updated successfully");
                return;
            }
        }
        System.out.println("room with room number " + oldRoomNumber + " could not be found");
    }

    public void deleteRoom(int roomNumber) {
        for (Room room : HotelDatabase.rooms) {
            if (room.getRoomNumber() == roomNumber) {
                HotelDatabase.rooms.remove(room);
                System.out.println("room " + roomNumber + " is removed");
            }
        }
        System.out.println("room with room number " + roomNumber + " could not be found");
    }

    public void createAmenity(Amenity amenity) {
        HotelDatabase.amenitiesDatabase.add(amenity);
        System.out.println("amenity " + amenity.getName() + " is added to the system");
    }

    public void readAmenity(String amenityName) {
        for (Amenity amenity : HotelDatabase.amenitiesDatabase) {
            if (amenity.getName().equalsIgnoreCase(amenityName)) {
                System.out.println("amenity Found" + amenity.getName());
                return;
            }
        }
        System.out.println("Amenity not found");
    }

    public void updateAmenity(String amenityName, Amenity newAmenity) {
        for (int i = 0; i < HotelDatabase.amenitiesDatabase.size(); i++) {
            if (HotelDatabase.amenitiesDatabase.get(i).getName().equalsIgnoreCase(amenityName)) {
                HotelDatabase.amenitiesDatabase.set(i, newAmenity);
                System.out.println("Amenity is  " + newAmenity.getName() + " updated successfully");
                return;
            }
        }
        System.out.println("Amenity: " + amenityName + "could not be found");
    }

    public void deleteAmenity(String amenityName) {
        for (Amenity amenity : HotelDatabase.amenitiesDatabase) {
            if (amenity.getName().equalsIgnoreCase(amenityName)) {
                HotelDatabase.amenitiesDatabase.remove(amenity);
                System.out.println("amenity: " + amenity.getName() + " is removed");
            }
        }
    }
}