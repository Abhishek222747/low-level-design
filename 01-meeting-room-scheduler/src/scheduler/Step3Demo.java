package scheduler;

/**
 * javac -d out src/scheduler/*.java
 * java -cp out scheduler.Step3Demo
 */
public class Step3Demo {
    public static void main(String[] args) {
        MeetingScheduler scheduler = new MeetingScheduler();
        scheduler.addRoom(new Room("r1", "Alpha", 8, 3));
        scheduler.addRoom(new Room("r2", "Beta", 4, 3));

        Meeting standup = scheduler.book("r1", "Standup", "Abhishek", 5, new TimeRange(10, 11));
        System.out.println("Booked: " + standup);

        try {
            scheduler.book("r1", "Design review", "Abhishek", 6, new TimeRange(10, 12));
        } catch (BookingException e) {
            System.out.println("Alpha overlap rejected: " + e.getMessage());
        }

        Meeting hiring = scheduler.book("r2", "Hiring loop", "Priya", 3, new TimeRange(10, 12));
        System.out.println("Same hours, other room: " + hiring);

        Meeting retro = scheduler.book("r1", "Retro", "Abhishek", 8, new TimeRange(11, 12));
        System.out.println("Back-to-back on Alpha: " + retro);

        try {
            scheduler.book("r2", "All hands", "Priya", 8, new TimeRange(13, 14));
        } catch (BookingException e) {
            System.out.println("Beta too small: " + e.getMessage());
        }

        System.out.println("Free for 6 people at [10, 11): " + scheduler.findAvailable(new TimeRange(10, 11), 6));
        System.out.println("Free for 3 people at [14, 15): " + scheduler.findAvailable(new TimeRange(14, 15), 3));
        System.out.println("Alpha calendar: " + scheduler.meetingsFor("r1"));

        scheduler.cancel(standup.getId());
        System.out.println("Cancelled standup. [10, 12) still hits retro [11, 12).");
        try {
            scheduler.book("r1", "Design review", "Abhishek", 6, new TimeRange(10, 12));
        } catch (BookingException e) {
            System.out.println("Still rejected: " + e.getMessage());
        }
        Meeting review = scheduler.book("r1", "Design review", "Abhishek", 6, new TimeRange(10, 11));
        System.out.println("Freed slot [10, 11) works: " + review);
        System.out.println("Alpha calendar: " + scheduler.meetingsFor("r1"));
    }
}
