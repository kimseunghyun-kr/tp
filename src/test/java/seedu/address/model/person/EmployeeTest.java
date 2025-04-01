package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMPLOYEE_ID_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOBPOSITION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.ALICE;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.BOB;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class EmployeeTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Employee employee = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> employee.getTags().remove(0));
    }

    @Test
    public void isSameEmployee() {
        // same object -> returns true
        assertTrue(ALICE.isSameEmployee(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameEmployee(null));

        // same name, all other attributes different -> returns true
        Employee editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withJobPosition(VALID_JOBPOSITION_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameEmployee(editedAlice));

        // different employee id, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmployeeId(VALID_EMPLOYEE_ID_BOB).build();
        assertFalse(ALICE.isSameEmployee(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Employee aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different employee -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Employee editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withJobPosition(VALID_JOBPOSITION_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Employee.class.getCanonicalName() + "{employeeId=" + ALICE.getEmployeeId()
                + ", name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", job=" + ALICE.getJobPosition()
                + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void getNextUpcomingDate_noAnniversaries_returnsNull() {
        Employee employee = new PersonBuilder().withBirthdayAndWorkAnniversary(null, null).build();
        assertNull(employee.getNextUpcomingDate());
    }

    @Test
    public void getNextUpcomingDate_singleFutureBirthday_returnsDate() {
        LocalDate futureBirthday = LocalDate.now().plusDays(10);
        Employee employee = new PersonBuilder()
                .withBirthdayAndWorkAnniversary(futureBirthday, null)
                .build();

        assertEquals(futureBirthday, employee.getNextUpcomingDate());
    }

    @Test
    public void getNextUpcomingDate_pastBirthday_rollsToNextYear() {
        LocalDate pastBirthday = LocalDate.now().minusDays(10);
        Employee employee = new PersonBuilder()
                .withBirthdayAndWorkAnniversary(pastBirthday, null)
                .build();

        LocalDate expectedDate = pastBirthday.plusYears(1);
        assertEquals(expectedDate, employee.getNextUpcomingDate());
    }
}
