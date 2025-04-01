package seedu.address.model.reminder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.person.Person;

/**
 * Represents a reminder for an upcoming anniversary related to a specific {@link Person}.
 * This could be a birthday, work anniversary, or any other custom anniversary.
 *
 * A {@code Reminder} contains the type and description of the anniversary,
 * the upcoming date of occurrence, and the associated person.
 */
public class Reminder implements Comparable<Reminder> {

    private final Person person;
    private final LocalDate date;
    private final AnniversaryType type;
    private final String description;

    /**
     * Constructs a {@code Reminder}.
     *
     * @param person      The person to whom the anniversary belongs.
     * @param date        The upcoming date of the anniversary.
     * @param type        The type of the anniversary (e.g., Birthday, Work Anniversary).
     * @param description A short description for the anniversary.
     */
    public Reminder(Person person, LocalDate date, AnniversaryType type, String description) {
        this.person = person;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    /**
     * Returns the {@link Person} associated with this reminder.
     */
    public Person getPerson() {
        return person;
    }

    /**
     * Returns the upcoming {@link LocalDate} of this anniversary.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the {@link AnniversaryType} of this reminder.
     */
    public AnniversaryType getType() {
        return type;
    }

    /**
     * Returns the description of this reminder's anniversary.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Calculates how many days are left from today until this reminder's anniversary.
     *
     * @return Number of days remaining.
     */
    public long getDaysLeft() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    /**
     * Compares this reminder with another by their upcoming anniversary dates.
     *
     * @param other The other reminder to compare with.
     * @return A negative integer, zero, or a positive integer as this reminder's date
     *         is before, equal to, or after the other reminder's date.
     */
    @Override
    public int compareTo(Reminder other) {
        return this.date.compareTo(other.date);
    }
}
