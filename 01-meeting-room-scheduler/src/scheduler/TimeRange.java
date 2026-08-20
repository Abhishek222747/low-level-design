package scheduler;

/**
 * A meeting occupies [startMillis, endMillis).
 *
 * This is the smallest honest unit of the whole LLD.
 * Booking, availability, and cancellation all ask this object one question:
 * "do you collide with that other range?"
 */
public final class TimeRange {
    private final long startMillis;
    private final long endMillis;

    public TimeRange(long startMillis, long endMillis) {
        if (endMillis <= startMillis) {
            throw new IllegalArgumentException(
                    "end must be after start (got start=" + startMillis + ", end=" + endMillis + ")");
        }
        this.startMillis = startMillis;
        this.endMillis = endMillis;
    }

    public long getStartMillis() {
        return startMillis;
    }

    public long getEndMillis() {
        return endMillis;
    }

    /**
     * True if the two half-open intervals share any instant.
     *
     * Example: [10, 11) and [11, 12) → false (back-to-back is allowed).
     * Example: [10, 12) and [11, 13) → true.
     */
    public boolean overlaps(TimeRange other) {
        return this.startMillis < other.endMillis
                && other.startMillis < this.endMillis;
    }

    @Override
    public String toString() {
        return "[" + startMillis + ", " + endMillis + ")";
    }
}
