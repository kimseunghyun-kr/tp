package seedu.address.model.person;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import lombok.Builder;
import lombok.Data;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.anniversary.Birthday;
import seedu.address.model.anniversary.WorkAnniversary;
import seedu.address.model.tag.Tag;



/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
@Data
@Builder
public class Person {

    // Identity fields
    private final EmployeeId employeeId;
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final JobPosition jobPosition;
    private final Set<Tag> tags;

    // Anniversary
    private final List<Anniversary> anniversaries;

    /**
     * Every field must be present and not null.
     */
    public Person(EmployeeId employeeId, Name name, Phone phone, Email email, JobPosition jobPosition, Set<Tag> tags,
                  List<Anniversary> anniversaries) {
        this.employeeId = employeeId;
        requireAllNonNull(name, phone, email, jobPosition, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.jobPosition = jobPosition;
        this.tags = new HashSet<>(tags);
        this.tags.addAll(tags);
        this.anniversaries = new ArrayList<>(anniversaries);
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same employee id.
     * This defines a clear notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == null) {
            return false;
        }
        return otherPerson.employeeId.equals(this.employeeId);
    }

    /**
     * Returns true if both persons have the same user details.
     */
    public boolean hasSameDetails(Person otherPerson) {
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && jobPosition.equals(otherPerson.jobPosition)
                && tags.equals(otherPerson.tags);
    }

    //TODO: Remove this method after finalising the feature
    /**
     * Returns the next upcoming important date (birthday or work anniversary) for this person.
     *
     * @return The next upcoming date as a {@code LocalDate} object, or {@code null} if none exists.
     */
    public LocalDate getNextUpcomingDate() {
        return anniversaries.stream()
                .map(Anniversary::getDate) // Get the anniversary dates
                .filter(date -> date != null) // Filter out null values
                .map(date -> {
                    LocalDate today = LocalDate.now();
                    LocalDate nextDate = date.withYear(today.getYear());

                    // If the date has already passed this year, set it to next year
                    if (nextDate.isBefore(today)) {
                        nextDate = nextDate.plusYears(1);
                    }
                    return nextDate;
                })
                .min(LocalDate::compareTo) // Find the earliest upcoming date
                .orElse(null); // Return null if no valid dates are found
    }

    /**
     * Calculates the next upcoming date for a given anniversary date.
     *
     * @param date The original anniversary date.
     * @return The next upcoming anniversary date adjusted to the current or next year.
     */
    private LocalDate calculateNextUpcomingDate(LocalDate date) {
        LocalDate today = LocalDate.now();
        LocalDate nextDate = date.withYear(today.getYear());
        return nextDate.isBefore(today) ? nextDate.plusYears(1) : nextDate;
    }

    /**
     * Returns the next upcoming date for the given anniversary type.
     *
     * @param anniversaryClass The class representing the anniversary type (e.g., Birthday.class).
     * @return The next upcoming date, or {@code null} if no matching anniversary is found.
     */
    private LocalDate getNextUpcomingDateByType(Class<? extends AnniversaryType> anniversaryClass) {
        return anniversaries.stream()
                .filter(a -> anniversaryClass.isInstance(a.getType()))
                .map(Anniversary::getDate)
                .filter(Objects::nonNull)
                .map(this::calculateNextUpcomingDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    /**
     * Returns the next upcoming birthday date for this person.
     *
     * @return The upcoming birthday as a {@code LocalDate}, or {@code null} if none found.
     */
    public LocalDate getNextUpcomingBirthdayDate() {
        return getNextUpcomingDateByType(Birthday.class);
    }

    /**
     * Returns the next upcoming work anniversary date for this person.
     *
     * @return The upcoming work anniversary as a {@code LocalDate}, or {@code null} if none found.
     */
    public LocalDate getNextUpcomingWorkAnniversaryDate() {
        return getNextUpcomingDateByType(WorkAnniversary.class);
    }

    public String getEmployeeIdAsString() {
        return employeeId.toString();
    }

    //TODO: Remove this method after finalising the feature
    /**
     * Checks if the next upcoming important date (birthday or work anniversary)
     * for this person is within the specified number of days from today.
     *
     * @param days The number of days from today to check for an upcoming date.
     * @return {@code true} if the next upcoming date is within the specified number of days,
     *         {@code false} otherwise or if no upcoming date is available.
     */
    public boolean isUpcomingWithinDays(int days) {
        LocalDate nextDate = getNextUpcomingDate();
        if (nextDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !nextDate.isBefore(today) && !nextDate.isAfter(today.plusDays(days));
    }

    /**
     * Checks if the person's next birthday is within the specified number of days from today.
     *
     * @param days The number of days to check.
     * @return True if the birthday is within the range, false otherwise.
     */
    public boolean isBirthdayUpcomingWithinDays(int days) {
        LocalDate upcoming = getNextUpcomingBirthdayDate();
        return isWithinDays(upcoming, days);
    }

    /**
     * Checks if the person's next work anniversary is within the specified number of days from today.
     *
     * @param days The number of days to check.
     * @return True if the work anniversary is within the range, false otherwise.
     */
    public boolean isWorkAnniversaryUpcomingWithinDays(int days) {
        LocalDate upcoming = getNextUpcomingWorkAnniversaryDate();
        return isWithinDays(upcoming, days);
    }

    /**
     * Utility method to check if a given date is within the next N days from today.
     *
     * @param date The date to check.
     * @param days The number of days to consider.
     * @return True if date is not null and within the range, false otherwise.
     */
    private boolean isWithinDays(LocalDate date, int days) {
        if (date == null) return false;
        LocalDate today = LocalDate.now();
        return !date.isBefore(today) && !date.isAfter(today.plusDays(days));
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return employeeId.equals(otherPerson.employeeId)
                && name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && jobPosition.equals(otherPerson.jobPosition)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(employeeId, name, phone, email, jobPosition, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeId", employeeId)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("job", jobPosition)
                .add("tags", tags)
                .toString();
    }
}
