package scheduler;

/**
 * javac -d out src/scheduler/*.java
 * java -cp out scheduler.Step2Demo
 *
 * Hours as small numbers again: 10 means "hour 10".
 */
public class Step2Demo {
    public static void main(String[] args) {
        Room alpha = new Room("r1", "Alpha", 8, 3);
        Room beta = new Room("r2", "Beta", 4, 3);

        Meeting standup = new Meeting(
                "m1", alpha.getId(), "Standup", "Abhishek", 5, new TimeRange(10, 11));
        Meeting designReview = new Meeting(
                "m2", alpha.getId(), "Design review", "Abhishek", 6, new TimeRange(10, 12));
        Meeting hiring = new Meeting(
                "m3", beta.getId(), "Hiring loop", "Priya", 3, new TimeRange(10, 12));

        System.out.println("Alpha can fit 5 people? " + alpha.canFit(standup.getAttendeeCount()));
        System.out.println("Beta can fit 8 people? " + beta.canFit(8));

        System.out.println("Standup vs design review (same room, overlap)? "
                + standup.conflictsWith(designReview));
        System.out.println("Standup vs hiring (different rooms, same time)? "
                + standup.conflictsWith(hiring));
    }
}
