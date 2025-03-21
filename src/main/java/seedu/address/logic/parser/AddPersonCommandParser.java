package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BIRTHDAY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOBPOSITION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPLOYEEID;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_WORK_ANNIVERSARY;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import seedu.address.logic.commands.AddPersonCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.anniversary.Anniversary;
import seedu.address.model.person.JobPosition;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddPersonCommandParser implements Parser<AddPersonCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPersonCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_EMPLOYEEID, PREFIX_NAME, PREFIX_PHONE,
                        PREFIX_EMAIL, PREFIX_JOBPOSITION, PREFIX_TAG, PREFIX_BIRTHDAY, PREFIX_WORK_ANNIVERSARY);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_JOBPOSITION, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPersonCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EMPLOYEEID, PREFIX_NAME,
                PREFIX_PHONE, PREFIX_EMAIL, PREFIX_JOBPOSITION);
        UUID employeeId = ParserUtil.parseEmployeeId(argMultimap.getValue(PREFIX_EMPLOYEEID)
                .orElse(UUID.randomUUID().toString()));
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        //Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        JobPosition jobPosition = ParserUtil.parseJobPosition(argMultimap.getValue(PREFIX_JOBPOSITION).get());
        System.out.println("Parsed job position: " + jobPosition);
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Anniversary birthday = ParserUtil.parseAnniversary(name, argMultimap.getValue(PREFIX_BIRTHDAY).get(),
                "Birthday");
        Anniversary workAnniversary = ParserUtil.parseAnniversary(name,
                argMultimap.getValue(PREFIX_WORK_ANNIVERSARY).get(), "Work Anniversary");

        List<Anniversary> anniversaryList = new ArrayList<>();
        anniversaryList.add(birthday);
        anniversaryList.add(workAnniversary);
        Person person = new Person(employeeId, name, phone, email, jobPosition, tagList, anniversaryList);

        return new AddPersonCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
