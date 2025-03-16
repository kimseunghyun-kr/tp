package seedu.address.model.person;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.tag.Tag;



/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
@Data
@Builder
public class Person {

    // Identity fields
    private final UUID employeeId;
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags;

    // Anniversary
    private final List<Anniversary> anniversaries;

    /**
     * Every field must be present and not null.
     */
    public Person(UUID employeeId, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  List<Anniversary> anniversaries) {
        this.employeeId = employeeId;
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
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
     * Returns true if both persons have the same uuid.
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
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);

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
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(employeeId, name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeId", employeeId)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }

}
