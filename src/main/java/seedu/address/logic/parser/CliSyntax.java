package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_EMPLOYEEID = new Prefix("eid/");
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_JOBPOSITION = new Prefix("jp/");

    /* Prefix definitions for birthdays and work anniversaries */
    public static final Prefix PREFIX_BIRTHDAY = new Prefix("bd/");
    public static final Prefix PREFIX_WORK_ANNIVERSARY = new Prefix("wa/");

    /* Prefix definitions for custom anniversaries */
    public static final Prefix PREFIX_ANNIVERSARY_DATE = new Prefix("d/");
    public static final Prefix PREFIX_ANNIVERSARY_DESC = new Prefix("ad/");
    public static final Prefix PREFIX_ANNIVERSARY_TYPE = new Prefix("at/");
    public static final Prefix PREFIX_ANNIVERSARY_INDEX = new Prefix("ai/");

}
