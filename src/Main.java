import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        HotelDatabase.createDummyData();

        System.out.println("------------------------------------------------");
        System.out.println("|       Desktop Hotel Reservation System       |");
        System.out.println("------------------------------------------------");

        boolean running = true;
        while (running) {
            System.out.println("\n----------- MAIN MENU -----------");
            System.out.println(" 1. Guest Login");
            System.out.println(" 2. Guest Register");
            System.out.println(" 3. Staff Login");
            System.out.println(" 0. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    guestLoginMenu();
                    break;
                case "2":
                    guestRegisterFlow();
                    break;
                case "3":
                    staffLoginMenu();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        System.out.println("Thank you for using Desktop Hotel Reservation System.");
        scanner.close();
    }

    //----------------------

    private static void guestRegisterFlow() {
        System.out.println("\n--- Guest Registration ---");
        String username = inputString("Username: ");
        String password = inputPassword();
        LocalDate dob = inputDate("Date of Birth (dd/MM/yyyy): ");
        String address = inputString("Address: ");
        Gender gender = inputGender();
        String prefs = inputString("Room Preferences (e.g. Single, Double): "); //useless???????
        System.out.print("Starting balance (EGP): ");
        double balance = inputDouble();

        try {
            Guest guest = new Guest(username, password, dob, balance, address, gender, prefs);
            if (guest.register()) {
                System.out.println("Registration successful! You can now log in.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Registration error: " + e.getMessage());
        }
    }

    private static void guestLoginMenu() {
        System.out.println("\n--- Guest Login ---");
        String username = inputString("Username: ");
        String password = inputString("Password: ");
        Guest guest = Guest.login(username, password);
        if (guest != null) guestMenu(guest);
    }

    private static void staffLoginMenu() {
        System.out.println("\n--- Staff Login ---");
        String username = inputString("Username: ");
        String password = inputString("Password: ");
        Staff staff = Staff.login(username, password);
        if (staff == null) {
            return;
        }
        if (staff.getRole() == Role.ADMIN) {
            adminMenu((Admin) staff);
        }
        else if (staff.getRole() == Role.RECEPTIONIST){
            receptionistMenu((Receptionist) staff);
        }
    }



    private static void guestMenu(Guest guest) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\n----- GUEST MENU [" + guest.getUsername() + "] -----");
            System.out.println(" 1. View My Profile");
            System.out.println(" 2. View Available Rooms");
            System.out.println(" 3. Make a Reservation");
            System.out.println(" 4. View My Reservations");
            System.out.println(" 5. Cancel a Reservation");
            System.out.println(" 6. Checkout & Pay");
            System.out.println(" 0. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    guest.printProfile();
                    break;
                case "2":
                    guest.viewAvailableRooms();
                    break;
                case "3":
                    makeReservationFlow(guest);
                    break;
                case "4":
                    guest.viewReservations();
                    break;
                case "5":
                    cancelReservationFlow(guest);
                    break;
                case "6":
                    checkoutFlow(guest);
                    break;
                case "0":
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void makeReservationFlow(Guest guest) {
        System.out.println("\n--- Available Rooms ---");
        boolean anyAvailable = false;
        for (Room room : HotelDatabase.rooms) {
            if (room.isAvailable()) {
                room.printInfo(); anyAvailable = true;
            }
        }
        if (!anyAvailable) { System.out.println("No rooms available right now.");
            return; }

        System.out.print("Enter Room Number to reserve: ");
        int roomNumber;
        try {
            roomNumber = Integer.parseInt(scanner.nextLine().trim());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid room number.");
            return;
        }

        Room chosen = findRoomByNumber(roomNumber);
        if (chosen == null){
            System.out.println("Room not found.");
            return;
        }
        if (!chosen.isAvailable()) {
            System.out.println("That room is not available.");
            return;
        }

        LocalDate checkIn = inputDate("Check-In Date  (dd/MM/yyyy): ");
        LocalDate checkOut = inputDate("Check-Out Date (dd/MM/yyyy): ");

        try {
            guest.makeReservation(chosen, checkIn, checkOut);
        } catch (RoomNotAvailableException | IllegalArgumentException e) {
            System.out.println("Reservation failed: " + e.getMessage());
        }
    }

    private static void cancelReservationFlow(Guest guest) {
        if (guest.getMyReservations().isEmpty()) {
            System.out.println("You have no reservations to cancel.");
            return;
        }
        guest.viewReservations();
        System.out.print("Enter Reservation ID to cancel: ");
        int id;
        try { id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid ID.");
            return;
        }

        Reservation target = findGuestReservationById(id, guest);
        if (target == null) {
            System.out.println("Reservation #" + id + " not found.");
            return;
        }
        guest.cancelReservation(target);
    }

    private static void checkoutFlow(Guest guest) {
        if (guest.getMyReservations().isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }
        guest.viewReservations();
        System.out.print("Enter Reservation ID to checkout: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
            return;
        }

        Reservation target = findGuestReservationById(id, guest);
        if (target == null) {
            System.out.println("Reservation #" + id + " not found.");
            return;
        }

        PaymentMethod method = inputPaymentMethod();
        try {
            guest.checkout(target, method);
        } catch (InvalidPaymentException e) {
            System.out.println("Checkout failed: " + e.getMessage());
        }
    }


    private static void adminMenu(Admin admin) {
        boolean active = true;
        while (active) {
            System.out.println("\n----- ADMIN MENU [" + admin.getUsername() + "] -----");
            System.out.println(" 1.  View All Guests");
            System.out.println(" 2.  View All Rooms");
            System.out.println(" 3.  View All Reservations");
            System.out.println(" -- Room Management -------------");
            System.out.println(" 4.  Add Room");
            System.out.println(" 5.  View Room");
            System.out.println(" 6.  Update Room");
            System.out.println(" 7.  Delete Room");
            System.out.println(" -- Room Type Management -------------");
            System.out.println(" 8.  Add Room Type");
            System.out.println(" 9.  View Room Type");
            System.out.println(" 10. Update Room Type");
            System.out.println(" 11. Delete Room Type");
            System.out.println(" -- Amenity Management -------------");
            System.out.println(" 12. Add Amenity");
            System.out.println(" 13. View Amenity");
            System.out.println(" 14. Update Amenity");
            System.out.println(" 15. Delete Amenity");
            System.out.println(" 0.  Logout");
            System.out.print("Choose: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    admin.viewAllGuests();
                    break;
                case "2":
                    admin.viewAllRooms();
                    break;
                case "3":
                    admin.viewAllReservations();
                    break;
                case "4":
                    adminAddRoomMenu(admin);
                    break;
                case "5":
                    adminViewRoomMenu(admin);
                    break;
                case "6":
                    adminUpdateRoomMenu(admin);
                    break;
                case "7":
                    adminDeleteRoomMenu(admin);
                    break;
                case "8":
                    adminAddRoomTypeMenu(admin);
                    break;
                case "9":
                    adminViewRoomTypeMenu(admin);
                    break;
                case "10":
                    adminUpdateRoomTypeMenu(admin);
                    break;
                case "11":
                    adminDeleteRoomTypeMenu(admin);
                    break;
                case "12":
                    adminAddAmenityMenu(admin);
                    break;
                case "13":
                    adminViewAmenityMenu(admin);
                    break;
                case "14":
                    adminUpdateAmenityMenu(admin);
                    break;
                case "15":
                    adminDeleteAmenityMenu(admin);
                    break;
                case "0":
                    active = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }


    private static void adminAddRoomMenu(Admin admin) {
        System.out.println("\n--- Add Room ---");
        System.out.print("Room ID: ");
        int id = (int) inputDouble();
        System.out.print("Room Number: ");
        int number = (int) inputDouble();
        System.out.print("Floor Number: ");
        int floor = (int) inputDouble();

        listRoomTypes();
        System.out.print("Room Type ID: "); int typeId = (int) inputDouble();
        RoomType roomType = findRoomTypeById(typeId);
        if (roomType == null) { System.out.println("Room type not found.");
            return;
        }

        try {
            Room newRoom = new Room(id, number, floor, roomType);
            System.out.print("Add amenities now? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                addAmenitiesToRoom(newRoom);
            }
            admin.createRoom(newRoom);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void adminViewRoomMenu(Admin admin) {
        System.out.print("Room Number: ");
        try {
            admin.readRoom(Integer.parseInt(scanner.nextLine().trim())); }
        catch (NumberFormatException e) { System.out.println("Invalid number."); }
    }

    private static void adminUpdateRoomMenu(Admin admin) {
        System.out.println("\n--- Update Room ---");
        System.out.print("Room Number to update: ");
        int oldNumber;
        try {
            oldNumber = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid number.");
            return; }

        Room existing = findRoomByNumber(oldNumber);
        if (existing == null) { System.out.println("Room not found.");
            return; }
        existing.printInfo();

        System.out.print("New Floor (Enter to keep " + existing.getFloorNumber() + "): ");
        String floorIn = scanner.nextLine().trim();
        int newFloor = floorIn.isEmpty() ? existing.getFloorNumber() : Integer.parseInt(floorIn);

        listRoomTypes();
        System.out.print("New Room Type ID (Enter to keep current): ");
        String typeIn = scanner.nextLine().trim();
        RoomType rt = typeIn.isEmpty() ? existing.getRoomType() : findRoomTypeById(Integer.parseInt(typeIn));
        if (rt == null) { System.out.println("Room type not found.");
            return; }

        Room updated = new Room(existing.getRoomId(), oldNumber, newFloor, rt);
        updated.getAmenities().addAll(existing.getAmenities());
        admin.updateRoom(oldNumber, updated);
    }

    private static void adminDeleteRoomMenu(Admin admin) {
        System.out.print("Room Number to delete: ");
        try {
            admin.deleteRoom(Integer.parseInt(scanner.nextLine().trim())); }
        catch (NumberFormatException e) { System.out.println("Invalid number."); }
    }


    private static void adminAddRoomTypeMenu(Admin admin) {
        System.out.println("\n--- Add Room Type ---");
        System.out.print("Type ID: ");
        int id = (int) inputDouble();
        String name = inputString("Type Name: ");
        System.out.print("Max Occupancy: ");
        int occ = (int) inputDouble();
        System.out.print("Base Price (EGP): ");
        double price = inputDouble();
        try {
            admin.createRoomType(new RoomType(id, name, occ, price)); }
        catch (IllegalArgumentException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void adminViewRoomTypeMenu(Admin admin) {
        admin.readRoomType(inputString("Room Type Name: "));
    }

    private static void adminUpdateRoomTypeMenu(Admin admin) {
        System.out.println("\n--- Update Room Type ---");
        String oldName = inputString("Current Room Type Name: ");
        RoomType existing = findRoomTypeByName(oldName);
        if (existing == null) {
            System.out.println("Room type not found.");
            return; }

        System.out.print("New Name (Enter to keep '" + existing.getTypeName() + "'): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty()) newName = existing.getTypeName();

        System.out.print("New Max Occupancy (Enter to keep " + existing.getMaxOccupancy() + "): ");
        String occIn = scanner.nextLine().trim();
        int occ = occIn.isEmpty() ? existing.getMaxOccupancy() : Integer.parseInt(occIn);

        System.out.print("New Price (Enter to keep " + existing.getBasePricePerNight() + "): ");
        String priceIn = scanner.nextLine().trim();
        double price = priceIn.isEmpty() ? existing.getBasePricePerNight() : Double.parseDouble(priceIn);

        try {
            admin.updateRoomType(oldName, new RoomType(existing.getTypeId(), newName, occ, price)); }
        catch (IllegalArgumentException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void adminDeleteRoomTypeMenu(Admin admin) {
        admin.deleteRoomType(inputString("Room Type Name to delete: "));
    }


    private static void adminAddAmenityMenu(Admin admin) {
        System.out.println("\n--- Add Amenity ---");
        System.out.print("Amenity ID: ");
        int id = (int) inputDouble();
        String name = inputString("Name: ");
        System.out.print("Extra Cost/Night (EGP): ");
        double cost = inputDouble();
        try { admin.createAmenity(new Amenity(id, name, cost)); }
        catch (IllegalArgumentException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void adminViewAmenityMenu(Admin admin) {
        admin.readAmenity(inputString("Amenity Name: "));
    }

    private static void adminUpdateAmenityMenu(Admin admin) {
        System.out.println("\n--- Update Amenity ---");
        String oldName = inputString("Current Amenity Name: ");
        Amenity existing = findAmenityByName(oldName);
        if (existing == null)
        { System.out.println("Amenity not found."); return; }

        System.out.print("New Name (Enter to keep '" + existing.getName() + "'): ");
        String newName = scanner.nextLine().trim();
        if (newName.isEmpty())
            newName = existing.getName();

        System.out.print("New Cost (Enter to keep " + existing.getExtraCostPerNight() + "): ");
        String costIn = scanner.nextLine().trim();
        double cost = costIn.isEmpty() ? existing.getExtraCostPerNight() : Double.parseDouble(costIn);

        try {
            admin.updateAmenity(oldName, new Amenity(existing.getAmenityId(), newName, cost)); }
        catch (IllegalArgumentException e) { System.out.println("Error: " + e.getMessage()); }
    }

    private static void adminDeleteAmenityMenu(Admin admin) {
        admin.deleteAmenity(inputString("Amenity Name to delete: "));
    }



    private static void receptionistMenu(Receptionist rec) {
        boolean active = true;
        while (active) {
            System.out.println("\n===== RECEPTIONIST MENU [" + rec.getUsername() + "] =====");
            System.out.println(" 1. View All Guests");
            System.out.println(" 2. View All Rooms");
            System.out.println(" 3. View All Reservations");
            System.out.println(" 4. Check-In Guest");
            System.out.println(" 5. Check-Out Guest");
            System.out.println(" 0. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": rec.viewAllGuests();
                    break;
                case "2": rec.viewAllRooms();
                    break;
                case "3": rec.viewAllReservations();
                    break;
                case "4": checkInMenu(rec);
                    break;
                case "5": checkOutMenu(rec);
                    break;
                case "0": active = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void checkInMenu(Receptionist receptionist) {
        System.out.println("\n--- Check-In ---");
        receptionist.viewAllReservations();
        System.out.print("Reservation ID: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim()); }
        catch (NumberFormatException e)
        { System.out.println("Invalid ID.");
            return; }

        Reservation res = findReservationById(id);
        if (res == null) {
            System.out.println("Reservation not found."); return; }
        receptionist.manageCheckIn(res);
    }

    private static void checkOutMenu(Receptionist receptionist) {
        System.out.println("\n--- Check-Out ---");
        receptionist.viewAllReservations();
        System.out.print("Reservation ID: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid ID."); return;
        }

        Reservation reservation = findReservationById(id);
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }
        receptionist.manageCheckOut(reservation, inputPaymentMethod());
    }



    private static String inputString(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    private static String inputPassword() {
        while (true) {
            System.out.print("Password (min 6 chars): ");
            String p = scanner.nextLine().trim();
            if (p.length() >= 6)
                return p;
            System.out.println("Too short. Try again.");
        }
    }

    private static double inputDouble() {
        while (true) {
            try {
                return scanner.nextDouble();
            }
            catch (InputMismatchException e) {
                System.out.print("Enter a valid number: "); }
        }
    }

    private static LocalDate inputDate(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return LocalDate.parse(scanner.nextLine().trim(), DATE_TIME_FORMATTER);
            }
            catch (DateTimeParseException e) {
                System.out.println("Use dd/MM/yyyy format."); }
        }
    }

    private static Gender inputGender() {
        while (true) {
            System.out.print("Gender (M/F): ");
            String in = scanner.nextLine().trim().toUpperCase();
            if (in.equals("M") || in.equalsIgnoreCase("MALE")){
                return Gender.MALE;
            }
            if (in.equals("F") || in.equalsIgnoreCase("FEMALE")) {
                return Gender.FEMALE;
            }
            System.out.println("Enter M or F.");
        }
    }

    private static PaymentMethod inputPaymentMethod() {
        while (true) {
            System.out.println("Payment: 1=CASH  2=CREDIT_CARD  3=ONLINE");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1":
                    return PaymentMethod.CASH;
                case "2":
                    return PaymentMethod.CREDIT_CARD;
                case "3":
                    return PaymentMethod.ONLINE;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void listRoomTypes() {
        System.out.println("Room Types:");
        for (RoomType roomType : HotelDatabase.roomTypes)
            System.out.println("  ID " + roomType.getTypeId() + " – " + roomType);
    }

    private static void addAmenitiesToRoom(Room room) {
        System.out.println("Amenities:");
        for (Amenity a : HotelDatabase.amenitiesDatabase)
            System.out.println("  ID " + a.getAmenityId() + " – " + a.getName());
        System.out.println("Enter amenity IDs one by one. Blank line to finish.");
        while (true) {
            System.out.print("Amenity ID: ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()){
                break;
            }
            try {
                Amenity found = findAmenityById(Integer.parseInt(line));
                if (found != null) room.addAmenity(found);
                else System.out.println("Not found.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID."); }
        }
    }



    private static Room findRoomByNumber(int roomNumber) {
        for (Room room : HotelDatabase.rooms)
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        return null;
    }

    private static Reservation findReservationById(int id) {
        for (Reservation reservation : HotelDatabase.reservations)
            if (reservation.getReservationId() == id) {
                return reservation;}
        return null;
    }

    private static Reservation findGuestReservationById(int id, Guest guest) {
        for (Reservation reservation : guest.getMyReservations())
            if (reservation.getReservationId() == id) {
                return reservation;}
        return null;
    }

    private static RoomType findRoomTypeById(int id) {
        for (RoomType roomType : HotelDatabase.roomTypes)
            if (roomType.getTypeId() == id) {
                return roomType;
            }
        return null;
    }

    private static RoomType findRoomTypeByName(String name) {
        for (RoomType roomType : HotelDatabase.roomTypes)
            if (roomType.getTypeName().equalsIgnoreCase(name)) {
                return roomType;
            }
        return null;
    }

    private static Amenity findAmenityByName(String name) {
        for (Amenity amenity : HotelDatabase.amenitiesDatabase)
            if (amenity.getName().equalsIgnoreCase(name)){
                return amenity;
            }
        return null;
    }

    private static Amenity findAmenityById(int id) {
        for (Amenity amenity : HotelDatabase.amenitiesDatabase)
            if (amenity.getAmenityId() == id) {
                return amenity;
            }
        return null;
    }
}
