package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * The service. Rooms describe space; this class owns calendars.
 *
 * Storage (say this if they ask "what is the data structure?"):
 *   rooms:     id -> Room
 *   calendars: roomId -> TreeMap keyed by meeting START
 *   byId:      meetingId -> Meeting  (so cancel does not scan)
 *
 * Why not one List for the whole office? Conflict is per room.
 * Why TreeMap, not List? A valid calendar never overlaps, so a new
 * [start, end) can only clash with the meeting just before start
 * and the meeting just after start. TreeMap finds those in O(log n).
 */
public final class MeetingScheduler {
    private final Map<String, Room> rooms = new HashMap<>();
    private final Map<String, TreeMap<Long, Meeting>> calendars = new HashMap<>();
    private final Map<String, Meeting> byId = new HashMap<>();
    private int nextMeetingNumber = 1;

    public void addRoom(Room room) {
        if (rooms.containsKey(room.getId())) {
            throw new BookingException("room already exists: " + room.getId());
        }
        rooms.put(room.getId(), room);
        calendars.put(room.getId(), new TreeMap<>());
    }

    public Meeting book(
            String roomId,
            String title,
            String organizer,
            int attendeeCount,
            TimeRange timeRange) {
        Room room = rooms.get(roomId);
        if (room == null) {
            throw new BookingException("unknown room: " + roomId);
        }
        if (!room.canFit(attendeeCount)) {
            throw new BookingException(
                    room.getName() + " holds " + room.getCapacity()
                            + ", requested " + attendeeCount);
        }
        TreeMap<Long, Meeting> calendar = calendars.get(roomId);
        if (conflicts(calendar, timeRange)) {
            throw new BookingException(
                    "room " + roomId + " is busy for " + timeRange);
        }

        String id = "m-" + nextMeetingNumber++;
        Meeting meeting = new Meeting(id, roomId, title, organizer, attendeeCount, timeRange);
        calendar.put(timeRange.getStartMillis(), meeting);
        byId.put(id, meeting);
        return meeting;
    }

    public void cancel(String meetingId) {
        Meeting meeting = byId.remove(meetingId);
        if (meeting == null) {
            throw new BookingException("unknown meeting: " + meetingId);
        }
        calendars.get(meeting.getRoomId()).remove(meeting.getTimeRange().getStartMillis());
    }

    public List<Room> findAvailable(TimeRange timeRange, int attendeeCount) {
        List<Room> available = new ArrayList<>();
        for (Room room : rooms.values()) {
            if (room.canFit(attendeeCount)
                    && !conflicts(calendars.get(room.getId()), timeRange)) {
                available.add(room);
            }
        }
        return available;
    }

    public List<Meeting> meetingsFor(String roomId) {
        TreeMap<Long, Meeting> calendar = calendars.get(roomId);
        if (calendar == null) {
            throw new BookingException("unknown room: " + roomId);
        }
        return Collections.unmodifiableList(new ArrayList<>(calendar.values()));
    }

    /**
     * In a clash-free TreeMap, only two neighbors of `start` can overlap
     * a new interval: the meeting that starts at-or-before, and the one
     * that starts at-or-after.
     */
    private static boolean conflicts(TreeMap<Long, Meeting> calendar, TimeRange incoming) {
        Map.Entry<Long, Meeting> previous = calendar.floorEntry(incoming.getStartMillis());
        if (previous != null && previous.getValue().getTimeRange().overlaps(incoming)) {
            return true;
        }
        Map.Entry<Long, Meeting> next = calendar.ceilingEntry(incoming.getStartMillis());
        return next != null && next.getValue().getTimeRange().overlaps(incoming);
    }
}
