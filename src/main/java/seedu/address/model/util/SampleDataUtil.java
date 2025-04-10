package seedu.address.model.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Employee[] getSamplePersons() {
        return new Employee[] {
            new Employee(EmployeeId.generateNewEmployeeId(), new Name("Alex Yeoh"), new Phone("87438807"),
                    new Email("alexyeoh@example.com"),
                    //new Address("Blk 30 Geylang Street 29, #06-40"),
                    new JobPosition("Hiring General"),
                    getTagSet("friends"), new ArrayList<>()),
            new Employee(EmployeeId.generateNewEmployeeId(), new Name("Bernice Yu"), new Phone("99272758"),
                    new Email("berniceyu@example.com"),
                    //new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                    new JobPosition("Vetting Queen"),
                    getTagSet("colleagues", "friends"), new ArrayList<>()),
            new Employee(EmployeeId.generateNewEmployeeId(), new Name("Charlotte Oliveiro"),
                    new Phone("93210283"), new Email("charlotte@example.com"),
                    //new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                    new JobPosition("The one above all"),
                    getTagSet("neighbours"), new ArrayList<>()),
            new Employee(EmployeeId.generateNewEmployeeId(), new Name("David Li"),
                    new Phone("91031282"), new Email("lidavid@example.com"),
                    //new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                    new JobPosition("Coding Manager"),
                    getTagSet("family"), new ArrayList<>()),
            new Employee(EmployeeId.generateNewEmployeeId(), new Name("Irfan Ibrahim"),
                    new Phone("92492021"), new Email("irfan@example.com"),
                    //new Address("Blk 47 Tampines Street 20, #17-35"),
                    new JobPosition("Human Resource"),
                    getTagSet("classmates"), new ArrayList<>()),
            new Employee(EmployeeId.generateNewEmployeeId(), new Name("Roy Balakrishnan"),
                    new Phone("92624417"), new Email("royb@example.com"),
                    //new Address("Blk 45 Aljunied Street 85, #11-31"),
                    new JobPosition("Boss"),
                    getTagSet("colleagues"), new ArrayList<>())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Employee sampleEmployee : getSamplePersons()) {
            sampleAb.addPerson(sampleEmployee);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
