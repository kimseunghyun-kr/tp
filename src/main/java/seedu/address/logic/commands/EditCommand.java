package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EMPLOYEES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.Data;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing employee in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the employee identified "
            + "by a prefix of their Employee ID. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: EMPLOYEE_ID_PREFIX (there must be only one employee that matches this prefix) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_JOBPOSITION + "JOBPOSITION] "
            + "[" + PREFIX_TAG + "TAG] "
            + "[" + PREFIX_EMPLOYEEID + "EMPLOYEE ID]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_EMPLOYEE_SUCCESS = "Edited Employee: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_EMPLOYEE_ID_CONFLICT = "The new employee ID is either a prefix of another "
            + "existing employee ID or another existing employee ID is a prefix of this one";

    private final EmployeeId employeeIdPrefix;
    private final EditEmployeeDescriptor editEmployeeDescriptor;

    /**
     * @param employeeIdPrefix of the employee in the filtered employee list to edit
     * @param editEmployeeDescriptor details to edit the employee with
     */
    public EditCommand(EmployeeId employeeIdPrefix, EditEmployeeDescriptor editEmployeeDescriptor) {
        requireNonNull(employeeIdPrefix);
        requireNonNull(editEmployeeDescriptor);

        this.employeeIdPrefix = employeeIdPrefix;
        this.editEmployeeDescriptor = new EditEmployeeDescriptor(editEmployeeDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Employee> matchedEmployees = model.getFullFilteredByEmployeeIdPrefixListFromData(employeeIdPrefix);

        if (matchedEmployees.size() > 1) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_MULTIPLE_EMPLOYEES_FOUND_WITH_PREFIX,
                    employeeIdPrefix
            ));
        }

        if (matchedEmployees.isEmpty()) {
            throw new CommandException(String.format(
                    Messages.MESSAGE_EMPLOYEE_PREFIX_NOT_FOUND,
                    employeeIdPrefix
            ));
        }

        Employee employeeToEdit = matchedEmployees.get(0);

        // Save the state before any potential changes
        model.commitChanges();

        Employee editedEmployee = createEditedEmployee(employeeToEdit, editEmployeeDescriptor);

        if (model.hasEmployeeIdPrefixConflictIgnoringSpecific(editedEmployee.getEmployeeId(),
                employeeToEdit.getEmployeeId())) {
            throw new CommandException(MESSAGE_EMPLOYEE_ID_CONFLICT);
        }

        model.setEmployee(employeeToEdit, editedEmployee);
        model.updateFilteredEmployeeList(PREDICATE_SHOW_ALL_EMPLOYEES);
        return new CommandResult(String.format(MESSAGE_EDIT_EMPLOYEE_SUCCESS, Messages.format(editedEmployee)));
    }

    /**
     * Creates and returns a {@code Employee} with the details of {@code employeeToEdit}
     * edited with {@code editEmployeeDescriptor}.
     */
    private static Employee createEditedEmployee(Employee employeeToEdit,
                                                 EditEmployeeDescriptor editEmployeeDescriptor) {
        assert employeeToEdit != null;
        /*
         * this is purposefully kept as employeeToEdit.getEmployeeId(), currently changing EmployeeID is not supported,
         * under Roman to change as he suggests.
         */
        EmployeeId employeeId = editEmployeeDescriptor.getEmployeeId().orElse(employeeToEdit.getEmployeeId());
        Name updatedName = editEmployeeDescriptor.getName().orElse(employeeToEdit.getName());
        Phone updatedPhone = editEmployeeDescriptor.getPhone().orElse(employeeToEdit.getPhone());
        Email updatedEmail = editEmployeeDescriptor.getEmail().orElse(employeeToEdit.getEmail());
        JobPosition updatedjobPosition = editEmployeeDescriptor.getJobPosition()
                                            .orElse(employeeToEdit.getJobPosition());
        Set<Tag> updatedTags = editEmployeeDescriptor.getTags().orElse(employeeToEdit.getTags());
        List<Anniversary> anniversaryList = employeeToEdit.getAnniversaries();
        return new Employee(employeeId, updatedName, updatedPhone,
                updatedEmail, updatedjobPosition, updatedTags, anniversaryList);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand otherEditCommand)) {
            return false;
        }

        return this.employeeIdPrefix.equals(otherEditCommand.employeeIdPrefix)
                && editEmployeeDescriptor.equals(otherEditCommand.editEmployeeDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("employeeIdPrefix", employeeIdPrefix)
                .add("editEmployeeDescriptor", editEmployeeDescriptor)
                .toString();
    }

    /**
     * checks if the editCommandDescriptor has the same details, excluding EmployeeId
     * @param command the editCommand to compare with
     * @return true if they have the same details
     */
    public boolean hasSameDetails(EditCommand command) {
        requireNonNull(command);
        return editEmployeeDescriptor.hasSameDetails(command.editEmployeeDescriptor);
    }

    /**
     * Stores the details to edit the employee with. Each non-empty field value will replace the
     * corresponding field value of the employee.
     */
    @Data
    public static class EditEmployeeDescriptor {
        private EmployeeId employeeId;
        private Name name;
        private Phone phone;
        private Email email;
        private Set<Tag> tags;
        private JobPosition jobPosition;

        public EditEmployeeDescriptor() {}
        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditEmployeeDescriptor(EditEmployeeDescriptor toCopy) {
            setEmployeeId(toCopy.employeeId);
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setJobPosition(toCopy.jobPosition);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(employeeId, name, phone, email, jobPosition, tags);
        }

        public Optional<EmployeeId> getEmployeeId() {
            return Optional.ofNullable(employeeId);
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public Optional<JobPosition> getJobPosition() {
            return Optional.ofNullable(jobPosition);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("employeeId", employeeId)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("jobposition", jobPosition)
                    .add("tags", tags)
                    .toString();
        }

        /**
         * checks if the two EditPersonDescriptors have the same details, a weaker equality.
         * @param editEmployeeDescriptor the other descriptor
         * @return true if the same
         */
        public boolean hasSameDetails(EditEmployeeDescriptor editEmployeeDescriptor) {
            return editEmployeeDescriptor.employeeId.equals(this.employeeId)
                    && editEmployeeDescriptor.name.equals(this.name)
                    && editEmployeeDescriptor.phone.equals(this.phone)
                    && editEmployeeDescriptor.email.equals(this.email)
                    && editEmployeeDescriptor.jobPosition.equals(this.jobPosition)
                    && editEmployeeDescriptor.tags.equals(this.tags);
        }
    }
}
