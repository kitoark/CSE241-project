public class Schedule {
    private int startHour;  // e.g., 8  for 08:00
    private int endHour;    // e.g., 16 for 16:00

    public Schedule() {}
    public Schedule(int startHour, int endHour) {
        if (startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23) {
            throw new IllegalArgumentException("Hours must be between 0 and 23.");
        }
        if (startHour >= endHour) {
            throw new IllegalArgumentException("Start hour must be before end hour.");
        }
        this.startHour = startHour;
        this.endHour   = endHour;
    }

    public int getStartHour() {
        return startHour;
    }
    public int getEndHour(){
        return endHour;
    }
    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }
    public void setEndHour(int endHour){
        this.endHour   = endHour;
    }

    @Override
    public String toString() {
        return String.format("%02d:00 - %02d:00", startHour, endHour);
    }
}
