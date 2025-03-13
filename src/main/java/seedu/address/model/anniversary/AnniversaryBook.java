package seedu.address.model.anniversary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lombok.Data;

/**
 * Stores Anniversaries separately from Person objects.
 * Maps each person's UUID to the list of Anniversaries belonging to that person.
 */
@Data
public class AnniversaryBook {

    // For each personId, store a list of the Anniversaries that belong to them.
    private final Map<UUID, List<Anniversary>> personToAnniversaries;

    /**
     * Creates an empty AnniversaryBook.
     */
    public AnniversaryBook() {
        this.personToAnniversaries = new HashMap<>();
    }

    /**
     * Creates an AnniversaryBook from existing data.
     */
    public AnniversaryBook(Map<UUID, List<Anniversary>> personToAnniversaries) {
        this.personToAnniversaries = personToAnniversaries;
    }

    /**
     * Overwrites the current data with the given {@code newData}.
     */
    public void resetData(AnniversaryBook newData) {
        this.personToAnniversaries.clear();
        this.personToAnniversaries.putAll(newData.personToAnniversaries);
    }

    /**
     * Adds an Anniversary to the given person’s list, if not duplicate.
     */
    public void addAnniversary(UUID personId, Anniversary anniversary) {
        personToAnniversaries.putIfAbsent(personId, new ArrayList<>());
        personToAnniversaries.get(personId).add(anniversary);
    }

    /**
     * Checks if the exact same Anniversary (date, name, description, types) already exists.
     */
    public boolean isDuplicateAnniversary(UUID personId, Anniversary ann) {
        List<Anniversary> existing = personToAnniversaries.getOrDefault(personId, new ArrayList<>());
        return existing.stream().anyMatch(x ->
                x.getDate().equals(ann.getDate())
                        && x.getName().equals(ann.getName())
                        && x.getDescription().equals(ann.getDescription())
                        && x.getType().equals(ann.getType())
        );
    }

    /**
     * Retrieves all Anniversaries for the given Person.
     */
    public List<Anniversary> getAnniversariesOf(UUID personId) {
        return personToAnniversaries.getOrDefault(personId, new ArrayList<>());
    }

    // (You can add remove/edit methods similarly.)
}
