public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException(String message) {
        super("Room is not available");
    }
}
