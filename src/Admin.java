import java.time.LocalDate;

public class Admin extends Staff {
    public Admin(String username, String password, LocalDate dateOfBirth,  Schedule workingHours) {
        super(username, password, dateOfBirth, Role.ADMIN, workingHours);
    }

    public void createRoom(Room room) {
        HotelDatabase.rooms.add(room);
        System.out.println("Room " + room.getRoomNumber() + " is added to the system");
    }

    public void readRoom(int roomNumber) {
        Room room = findRoomByNumber(roomNumber);
        if (room != null) {
            room.printInfo();
        }
        else {
            System.out.println("Room " + roomNumber + " not found.");
        }
    }

    public void updateRoom(int oldRoomNumber, Room newRoomData) {
        for (int i = 0; i < HotelDatabase.rooms.size(); i++) {
            if (HotelDatabase.rooms.get(i).getRoomNumber() == oldRoomNumber) {
                HotelDatabase.rooms.set(i, newRoomData);
                System.out.println("Room " + oldRoomNumber + " updated successfully");
                return;
            }
        }
        System.out.println("room " + oldRoomNumber + " could not be found");
    }

    public void deleteRoom(int roomNumber) {
        for (int i = 0; i < HotelDatabase.rooms.size(); i++) {
            if (HotelDatabase.rooms.get(i).getRoomNumber() == roomNumber) {
                HotelDatabase.rooms.remove(i);
                System.out.println("Room " + roomNumber + " removed.");
                return;
            }
        }
        System.out.println("Room " + roomNumber + " not found.");
    }

    public void createAmenity(Amenity amenity) {
        HotelDatabase.amenitiesDatabase.add(amenity);
        System.out.println("amenity '" + amenity.getName() + "' is added to the system");
    }

    public void readAmenity(String amenityName) {
        for (Amenity amenity : HotelDatabase.amenitiesDatabase) {
            if (amenity.getName().equalsIgnoreCase(amenityName)) {
                amenity.printInfo();
                return;
            }
        }
        System.out.println("Amenity '" + amenityName + "' not found.");
    }

    public void updateAmenity(String amenityName, Amenity newAmenity) {
        for (int i = 0; i < HotelDatabase.amenitiesDatabase.size(); i++) {
            if (HotelDatabase.amenitiesDatabase.get(i).getName().equalsIgnoreCase(amenityName)) {
                HotelDatabase.amenitiesDatabase.set(i, newAmenity);
                System.out.println("Amenity '" + newAmenity.getName() + "' updated successfully");
                return;
            }
        }
        System.out.println("Amenity '" + amenityName + "' could not be found");
    }

    public void deleteAmenity(String name) {
        for (int i = 0; i < HotelDatabase.amenitiesDatabase.size(); i++) {
            if (HotelDatabase.amenitiesDatabase.get(i).getName().equalsIgnoreCase(name)) {
                HotelDatabase.amenitiesDatabase.remove(i);
                System.out.println("Amenity '" + name + "' removed.");
                return;
            }
        }
        System.out.println("Amenity '" + name + "' not found.");
    }

    private Room findRoomByNumber(int roomNumber) {
        for (Room room : HotelDatabase.rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
}