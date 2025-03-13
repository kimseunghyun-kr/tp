package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.anniversary.AnniversaryBook;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book + anniversary book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final AnniversaryBook anniversaryBook; // NEW FIELD
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook, anniversaryBook, and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, AnniversaryBook anniversaryBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);
        // anniversaryBook can be null if you want a default; handle it gracefully.

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.anniversaryBook = (anniversaryBook != null) ? anniversaryBook : new AnniversaryBook();
        this.userPrefs = new UserPrefs(userPrefs);

        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    /**
     * Overloaded constructor for convenience, if you just have an AddressBook + UserPrefs, etc.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        this(addressBook, new AnniversaryBook(), userPrefs);
    }

    /**
     * Default constructor, starts with empty addressBook + anniversaryBook + default userPrefs.
     */
    public ModelManager() {
        this(new AddressBook(), new AnniversaryBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public boolean hasDuplicatePersonDetails(Person person) {
        requireNonNull(person);
        return addressBook.hasDuplicatePersonDetails(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
        // Potentially also remove that Person's Anniversaries from anniversaryBook if desired
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);
        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    //=========== AnniversaryBook ============================================================================

    @Override
    public AnniversaryBook getAnniversaryBook() {
        return anniversaryBook;
    }

    @Override
    public void setAnniversaryBook(AnniversaryBook anniversaryBook) {
        requireNonNull(anniversaryBook);
        // Overwrite existing data with the new data
        this.anniversaryBook.resetData(anniversaryBook);
    }

    //=========== Equality ===================================================================================

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons)
                && anniversaryBook.equals(otherModelManager.anniversaryBook);
    }
}
