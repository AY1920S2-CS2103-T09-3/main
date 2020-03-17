package seedu.expensela.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.expensela.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensela.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.expensela.testutil.Assert.assertThrows;
import static seedu.expensela.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.expensela.logic.commands.AddCommand;
import seedu.expensela.logic.commands.ClearCommand;
import seedu.expensela.logic.commands.DeleteCommand;
import seedu.expensela.logic.commands.EditCommand;
import seedu.expensela.logic.commands.ExitCommand;
import seedu.expensela.logic.commands.FindCommand;
import seedu.expensela.logic.commands.HelpCommand;
import seedu.expensela.logic.commands.ListCommand;
import seedu.expensela.logic.parser.exceptions.ParseException;
import seedu.expensela.model.transaction.NameContainsKeywordsPredicate;
import seedu.expensela.model.transaction.Transaction;
import seedu.expensela.testutil.EditPersonDescriptorBuilder;
import seedu.expensela.testutil.PersonBuilder;
import seedu.expensela.testutil.PersonUtil;

public class DateBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Transaction transaction = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(transaction));
        assertEquals(new AddCommand(transaction), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Transaction transaction = new PersonBuilder().build();
        EditCommand.editTransaction descriptor = new EditPersonDescriptorBuilder(transaction).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
