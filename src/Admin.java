import java.time.LocalDate;

public class Admin extends Staff{
   public Admin(String username, String password, LocalDate dateOfBirth, Role role, Schedule workingHours) {
        super(username, password, dateOfBirth, role, workingHours);
    }
    public void createRoom(Room room){
        HotelDatabase.rooms.add(room);
        System.out.println("Room"+getRoomNumber()+"is added to the system");
    }
    public void readRoom(String roomNumber){
        for(Room room : HotelDatabase.rooms){
          if(room.getRoomNumber().equals(roomNumber)){
            System.out.println("Room"+room.getRoomNumber()+ " Type:" +room.getRoomType().getTypeName() + "Available
            :"+room.isAvailable());
            return;
          }  
        }
        System.out.println("Room"+roomNumber+"not found");
    }
    public void updateRoom(String oldRoomNumber,Room newRoomData){
       for(int i = 0; i < HotelsDatabase.Rooms.size();i++){ 
       if(HotelDatabase.rooms.get(i).getRoomNumber().equals(oldRoomNumber)){
        HotelDatabase.rooms.set(i, newRoomData);
    System.out.println("Room"+oldRoomNumber+"updated successfully");
       }
    } 
    System.out.println("Room"+oldRoomNumber+"no updates");
    }

    public void deleteRoom(String roomNumber){
    HotelDatabase.rooms.remove(room);
    System.out.println("room"+getRoomNumber()+"is removed");
}
 public void createAmenity(Amenity amenity){
        HotelDatabase.amenities.add(ammenity);
        System.out.println("amenity"+ameniy.getName()+"is added to the system");
    }
      public void readAmenity(String amenityName){
        for(Amenity a : HotelDatabase.amenities){
          if(a.getName().equalsIgnorecase(amenityName)){
            System.out.println("amenity Found"+a.getName());
            return;
          } 
}
System.out.println("Amenity not found");
      }
      public void updateAmenity(String oldName,Amenity newAmenity){
       for(int i = 0; i < HotelsDatabase.amenities.size();i++){ 
       if(HotelDatabase.amenities.get(i).getName().equalsIgnorecase(oldName)){
        HotelDatabase.amenities.set(i, newAmenity);
    System.out.println("Amenity is"+newAmenity.getName()+"updated successfully");
     return; 
}
    }
System.out.println("Amenity not found for update");
    }
       public void deleteAmenity(String amenityName){
    HotelDatabase.amenities.remove(Amenity);
    System.out.println("amenity"+a.getName()+"is removed");
}
}