package seedu.expensela.logic.commands;

import static seedu.expensela.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.expensela.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.expensela.testutil.TypicalTransactions.getTypicalExpenseLa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.expensela.model.Model;
import seedu.expensela.model.ModelManager;
import seedu.expensela.model.UserPrefs;
import seedu.expensela.model.transaction.Transaction;
import seedu.expensela.testutil.TransactionBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalExpenseLa(), new UserPrefs());
    }

    @Test
    public void execute_newTransaction_success() {
        Transaction validTransaction = new TransactionBuilder().build();

        Model expectedModel = new ModelManager(model.getExpenseLa(), new UserPrefs());
        expectedModel.addTransaction(validTransaction);

        assertCommandSuccess(new AddCommand(validTransaction), model,
                String.format(AddCommand.MESSAGE_SUCCESS, validTransaction), expectedModel);
    }

    @Test
    public void execute_duplicateTransaction_throwsCommandException() {
        Transaction transactionInList = model.getExpenseLa().getTransactionList().get(0);
        assertCommandFailure(new AddCommand(transactionInList), model, AddCommand.MESSAGE_DUPLICATE_TRANSACTION);
    }

}
