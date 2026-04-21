
public class Amenity {

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
        this.name = name;
    }

    public void setExtraCostPerNight(double extraCostPerNight) {
        if (extraCostPerNight < 0) {
            System.out.println("Error: cost cannot be negative.");
        } else {
            this.extraCostPerNight = extraCostPerNight;
        }
    }

    public void printInfo() {
        System.out.println("Room.Amenity ID: " + amenityId);
        System.out.println("Name: " + name);
        System.out.println("Extra Cost Per Night: " + extraCostPerNight + " EGP");
    }
}