package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditCommand.EditEmployeeDescriptor;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditEmployeeDescriptor objects.
 */
public class EditEmployeeDescriptorBuilder {

    private EditEmployeeDescriptor descriptor;

    public EditEmployeeDescriptorBuilder() {
        descriptor = new EditEmployeeDescriptor();
    }

    public EditEmployeeDescriptorBuilder(EditEmployeeDescriptor descriptor) {
        this.descriptor = new EditEmployeeDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditEmployeeDescriptor} with fields containing {@code employee}'s details
     */
    public EditEmployeeDescriptorBuilder(Employee employee) {
        descriptor = new EditEmployeeDescriptor();
        descriptor.setEmployeeId(employee.getEmployeeId());
        descriptor.setName(employee.getName());
        descriptor.setPhone(employee.getPhone());
        descriptor.setEmail(employee.getEmail());
        descriptor.setJobPosition(employee.getJobPosition());
        descriptor.setTags(employee.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditEmployeeDescriptor} that we are building.
     */
    public EditEmployeeDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditEmployeeDescriptor} that we are building.
     */
    public EditEmployeeDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditEmployeeDescriptor} that we are building.
     */
    public EditEmployeeDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code JobPosition} of the {@code EditEmployeeDescriptor} that we are building.
     */
    public EditEmployeeDescriptorBuilder withJobPosition(String jobposition) {
        descriptor.setJobPosition(new JobPosition(jobposition));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditEmployeeDescriptor}
     * that we are building.
     */
    public EditEmployeeDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditEmployeeDescriptor build() {
        return descriptor;
    }

    /**
     * Parses the {@code employeeId} into a {@code EmployeeId} and set it to the {@code EditEmployeeDescriptor}
     * that we are building.
     */
    public EditEmployeeDescriptorBuilder withEmployeeId(String validEmployeeIdAmy) {
        descriptor.setEmployeeId(EmployeeId.fromString(validEmployeeIdAmy));
        return this;
    }
}