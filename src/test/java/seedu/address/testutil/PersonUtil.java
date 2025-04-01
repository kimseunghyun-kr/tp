package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;

import java.util.Set;

import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Employee;
import seedu.address.model.tag.Tag;

/**
 * A utility class for Employee.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code employee}.
     */
    public static String getAddCommand(Employee employee) {
        return AddPersonCommand.COMMAND_WORD + " " + getPersonDetails(employee);
    }

    /**
     * Returns the part of command string for the given {@code employee}'s details.
     */
    public static String getPersonDetails(Employee employee) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + employee.getName().fullName + " ");
        sb.append(PREFIX_PHONE + employee.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + employee.getEmail().value + " ");
        sb.append(PREFIX_JOBPOSITION + employee.getJobPosition().value + " ");
        employee.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        sb.append(PREFIX_BIRTHDAY + employee.getAnniversaries().get(0).getDate().toString() + " ");
        sb.append(PREFIX_WORK_ANNIVERSARY + employee.getAnniversaries().get(1).getDate().toString() + " ");
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditEmployeeDescriptor}'s details.
     */
    public static String getEditPersonDescriptorDetails(EditPersonDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getEmployeeId().ifPresent(
                employeeId -> sb.append(PREFIX_EMPLOYEEID).append(employeeId.value).append(" ")
        );
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getjobPosition().ifPresent(
                jobPosition -> sb.append(PREFIX_JOBPOSITION).append(jobPosition.value).append(" ")
        );
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
