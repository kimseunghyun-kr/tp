package seedu.address.testutil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.anniversary.AnniversaryType;
import seedu.address.model.anniversary.Birthday;
import seedu.address.model.anniversary.WorkAnniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Employee objects.
 */
@Builder
public class EmployeeBuilder {

    public static final String DEFAULT_EMPLOYEE_ID = "00000000-0000-0000-0000-000000000001";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_JOBPOSITION = "Hiring Manager";
    public static final Anniversary DEFAULT_BIRTHDAY = new Anniversary(LocalDate.of(2000, 1, 1),
            new Birthday(), "Birthday", "Amy");
    public static final Anniversary DEFAULT_WORK_ANNIVERSARY = new Anniversary(LocalDate.of(2000, 1, 1),
            new WorkAnniversary(), "Work Anniversary", "Amy");

    private EmployeeId employeeId;
    private Name name;
    private Phone phone;
    private Email email;
    private JobPosition jobPosition;
    private Set<Tag> tags;
    private List<Anniversary> anniversaries;

    /**
     * Creates a {@code EmployeeBuilder} with the default details.
     */
    public EmployeeBuilder() {
        employeeId = EmployeeId.fromString(DEFAULT_EMPLOYEE_ID);
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        jobPosition = new JobPosition(DEFAULT_JOBPOSITION);
        tags = new HashSet<>();
        anniversaries = new ArrayList<>();
    }
    /**
     * Initializes the EmployeeBuilder with the data of {@code employeeToCopy}.
     */
    public EmployeeBuilder(Employee employeeToCopy) {
        employeeId = employeeToCopy.getEmployeeId();
        name = employeeToCopy.getName();
        phone = employeeToCopy.getPhone();
        email = employeeToCopy.getEmail();
        jobPosition = employeeToCopy.getJobPosition();
        tags = new HashSet<>(employeeToCopy.getTags());
        anniversaries = new ArrayList<>(employeeToCopy.getAnniversaries());
    }

    /**
     * Initializes the EmployeeBuilder with the data of {@code personToCopy}.
     * @return
     */
    public static Employee defaultEmployee() {
        Employee employee = new EmployeeBuilder()
                .withEmployeeId(DEFAULT_EMPLOYEE_ID)
                .withName(DEFAULT_NAME)
                .withEmail(DEFAULT_EMAIL)
                .withPhone(DEFAULT_PHONE)
                .withJobPosition(DEFAULT_JOBPOSITION)
                .build();
        employee.getAnniversaries().add(DEFAULT_BIRTHDAY);
        employee.getAnniversaries().add(DEFAULT_WORK_ANNIVERSARY);
        employee.getTags();
        return employee;
    }

    /**
     * Sets the {@code Name} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Employee} that we are building.
     */
    public EmployeeBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withJobPosition(String jobposition) {
        this.jobPosition = new JobPosition(jobposition);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code EmployeeID} of the {@code Employee} that we are building.
     */
    public EmployeeBuilder withEmployeeId(String employeeId) {
        this.employeeId = EmployeeId.fromString(employeeId);
        return this;
    }

    /**
     * Sets both the Birthday and Work Anniversary for the {@code Employee} that we are building.
     */
    public EmployeeBuilder withBirthdayAndWorkAnniversary(LocalDate localBDate, LocalDate localWaDate) {
        List<Anniversary> anni = new ArrayList<>();
        anni.add(new Anniversary(localBDate, new Birthday(),
                "Birthday", name.fullName));
        anni.add(new Anniversary(localWaDate, new WorkAnniversary(),
                "Work Anniversary", name.fullName));
        this.anniversaries = anni;
        return this;
    }
    /**
     * Adds an anniversary to the employee's anniversary list.
     */
    public EmployeeBuilder withAnniversary(String date, String type, String name, String description) {
        this.anniversaries.add(new Anniversary(
                LocalDate.parse(date),
                new AnniversaryType(type, ""),
                name, description));
        return this;
    }

    public Employee build() {
        return new Employee(employeeId, name, phone, email, jobPosition, tags, anniversaries);
    }
}
