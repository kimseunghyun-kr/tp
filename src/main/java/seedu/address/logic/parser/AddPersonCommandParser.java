package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.AnniversaryParserUtils.multiAddAnniversary;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import seedu.address.logic.commands.AddEmployeeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmployeeId;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Name;
import seedu.address.model.person.Employee;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddPersonCommandParser implements Parser<AddEmployeeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddEmployeeCommand
     * and returns an AddEmployeeCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEmployeeCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMPLOYEEID, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_JOBPOSITION, PREFIX_TAG, PREFIX_BIRTHDAY, PREFIX_WORK_ANNIVERSARY);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_JOBPOSITION, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEmployeeCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EMPLOYEEID, PREFIX_NAME,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_JOBPOSITION);

        Optional<String> employeeIdString = argMultimap.getValue(PREFIX_EMPLOYEEID);
        EmployeeId employeeId;
        if (employeeIdString.isPresent()) {
            employeeId = ParserUtil.parseEmployeeId(employeeIdString.get());
        } else {
            employeeId = EmployeeId.generateNewEmployeeId();
        }
        try {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
            JobPosition jobPosition = ParserUtil.parseJobPosition(argMultimap.getValue(PREFIX_JOBPOSITION).get());
            Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));
            List<Anniversary> anniversaryList = multiAddAnniversary(argMultimap);
            Employee employee = new Employee(employeeId, name, phone, email, jobPosition, tagList, anniversaryList);

            return new AddEmployeeCommand(employee);
        } catch (NoSuchElementException nsee) {
            throw new ParseException(AddEmployeeCommand.MESSAGE_USAGE);
        }
    }
}
