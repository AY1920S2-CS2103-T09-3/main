package seedu.expensela.logic.parser;

import static seedu.expensela.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensela.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensela.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.expensela.logic.commands.FilterCommand;
import seedu.expensela.model.transaction.CategoryEqualsKeywordPredicate;
import seedu.expensela.model.transaction.DateEqualsKeywordPredicate;





class FilterCommandParserTest {
    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        // no leading and trailing whitespaces
        FilterCommand expectedFilterCommand =
                new FilterCommand(new CategoryEqualsKeywordPredicate(Arrays.asList("FOOD")),
                        new DateEqualsKeywordPredicate(Arrays.asList("2020-01")));
        assertParseSuccess(parser, "FOOD", expectedFilterCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFilterCommand);
    }
}