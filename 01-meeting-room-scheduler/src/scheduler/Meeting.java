package scheduler;

/**
 * One booking: who, why, when, and which room.
 *
 * Composition: a Meeting *has a* TimeRange. We do not copy start/end fields
 * onto Meeting — overlap already lives on TimeRange.
 *
 * We store roomId, not a Room object. Reasons to say in the interview:
 * 1. A meeting does not own the room; the office does.
 * 2. If Meeting held Room and Room held List<Meeting>, you get a cycle
 *    that is painful to print, persist, and reason about.
 * 3. Conflict is "same roomId + overlapping TimeRange", which needs the id.
 */
public final class Meeting {
    private final String id;
    private final String roomId;
    private final String title;
    private final String organizer;
    private final int attendeeCount;
    private final TimeRange timeRange;

    public Meeting(
            String id,
            String roomId,
            String title,
            String organizer,
            int attendeeCount,
            TimeRange timeRange) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("meeting id is required");
        }
        if (roomId == null || roomId.isBlank()) {
            throw new IllegalArgumentException("roomId is required");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (organizer == null || organizer.isBlank()) {
            throw new IllegalArgumentException("organizer is required");
        }
        if (attendeeCount <= 0) {
            throw new IllegalArgumentException("attendeeCount must be positive");
        }
        if (timeRange == null) {
            throw new IllegalArgumentException("timeRange is required");
        }
        this.id = id;
        this.roomId = roomId;
        this.title = title;
        this.organizer = organizer;
        this.attendeeCount = attendeeCount;
        this.timeRange = timeRange;
    }

    public String getId() {
        return id;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganizer() {
        return organizer;
    }

    public int getAttendeeCount() {
        return attendeeCount;
    }

    public TimeRange getTimeRange() {
        return timeRange;
    }

    /**
     * Same-room clash only. Two meetings in different rooms never conflict,
     * even if the clocks are identical. Say this out loud if asked.
     */
    public boolean conflictsWith(Meeting other) {
        if (!this.roomId.equals(other.roomId)) {
            return false;
        }
        return this.timeRange.overlaps(other.timeRange);
    }

    @Override
    public String toString() {
        return title + " in room " + roomId + " at " + timeRange;
    }
}
