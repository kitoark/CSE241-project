public class Amenity implements Manageable {

    private int amenityId;
    private String name;
    private double extraCostPerNight;

    // Constructor
    public Amenity(int amenityId, String name, double extraCostPerNight) {
        this.amenityId = amenityId;
        this.name = name;
        this.extraCostPerNight = extraCostPerNight;
    }

    // Getters
    public int getAmenityId() {
        return amenityId;
    }

    public String getName() {
        return name;
    }

    public double getExtraCostPerNight() {
        return extraCostPerNight;
    }

    // Setters
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Amenity name cannot be empty.");
        }
        this.name = name.trim();
    }

    public void setExtraCostPerNight(double extraCostPerNight) {
        if (extraCostPerNight < 0) {
            throw new IllegalArgumentException("Extra cost cannot be negative.");
        }
        this.extraCostPerNight = extraCostPerNight;
    }

    @Override
    public void printInfo() {
        System.out.println("Room.Amenity ID: " + amenityId);
        System.out.println("Name: " + name);
        System.out.println("Extra Cost Per Night: " + extraCostPerNight + " EGP");
    }

    @Override
    public String toString() {
        return name + " (+" + extraCostPerNight + " EGP/night)";
    }
}