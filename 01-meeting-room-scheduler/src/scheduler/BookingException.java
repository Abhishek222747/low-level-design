package scheduler;

/** Thrown when book/cancel cannot proceed (unknown room, too small, clash, unknown meeting). */
public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
}
