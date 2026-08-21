package scheduler;

/**
 * A physical space. It exists even when nobody has booked it.
 *
 * Capacity is a fact about the room, not about a meeting.
 * We do not store meetings here yet — that is Step 3.
 * If Room both "is a room" and "is a calendar", one class does two jobs
 * and the interviewer cannot tell which methods belong where.
 */
public final class Room {
    private final String id;
    private final String name;
    private final int capacity;
    private final int floor;

    public Room(String id, String name, int capacity, int floor) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("room id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("room name is required");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.floor = floor;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getFloor() {
        return floor;
    }

    public boolean canFit(int attendeeCount) {
        return attendeeCount <= capacity;
    }

    @Override
    public String toString() {
        return name + " (id=" + id + ", floor=" + floor + ", cap=" + capacity + ")";
    }
}
