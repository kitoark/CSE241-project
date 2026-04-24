public class RoomType implements Manageable{

    private int typeId;
    private String typeName;
    private int maxOccupancy;
    private double basePricePerNight;

    public RoomType(int typeId, String typeName, int maxOccupancy, double basePricePerNight) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.maxOccupancy = maxOccupancy;
        this.basePricePerNight = basePricePerNight;
    }

    // Getters
    public int getTypeId() {
        return typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public double getBasePricePerNight() {
        return basePricePerNight;
    }

    // Setters
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        if (maxOccupancy < 1) {
            System.out.println("Error: max occupancy must be at least 1.");
        } else {
            this.maxOccupancy = maxOccupancy;
        }
    }

    public void setBasePricePerNight(double basePricePerNight) {
        if (basePricePerNight <= 0) {
            System.out.println("Error: base price must be greater than 0.");
        } else {
            this.basePricePerNight = basePricePerNight;
        }
    }

    public void printInfo() {
        System.out.println("Type ID: " + typeId);
        System.out.println("Type Name: " + typeName);
        System.out.println("Max Occupancy: " + maxOccupancy);
        System.out.println("Base Price Per Night: " + basePricePerNight + " EGP");
    }
}