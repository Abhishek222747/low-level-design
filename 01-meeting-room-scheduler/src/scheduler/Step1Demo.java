package scheduler;

/**
 * Run this after compiling (output goes to out/, not next to .java files):
 *   javac -d out src/scheduler/*.java
 *   java -cp out scheduler.Step1Demo
 *
 * We use small numbers instead of real clock times so the overlap math is obvious.
 * Think of them as "hour 10", "hour 11", etc.
 */
public class Step1Demo {
    public static void main(String[] args) {
        TimeRange tenToEleven = new TimeRange(10, 11);
        TimeRange elevenToTwelve = new TimeRange(11, 12);
        TimeRange tenThirtyToElevenThirty = new TimeRange(10, 12); // 10–12 occupies 10 and 11

        System.out.println("Back-to-back 10-11 and 11-12 overlap? "
                + tenToEleven.overlaps(elevenToTwelve)); // false — this is what we want

        System.out.println("10-11 and 10-12 overlap? "
                + tenToEleven.overlaps(tenThirtyToElevenThirty)); // true

        try {
            new TimeRange(11, 11);
        } catch (IllegalArgumentException e) {
            System.out.println("Zero-length meeting rejected: " + e.getMessage());
        }
    }
}
