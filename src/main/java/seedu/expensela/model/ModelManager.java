package seedu.expensela.model;

import static java.util.Objects.requireNonNull;
import static seedu.expensela.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.expensela.commons.core.GuiSettings;
import seedu.expensela.commons.core.LogsCenter;
import seedu.expensela.model.monthlydata.Expense;
import seedu.expensela.model.monthlydata.Income;
import seedu.expensela.model.monthlydata.MonthlyData;
import seedu.expensela.model.transaction.Transaction;

/**
 * Represents the in-memory model of the expensela data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final ExpenseLa expenseLa;
    private final UserPrefs userPrefs;
    private final FilteredList<Transaction> unfilteredTransactions;
    private final MonthlyData monthlyData;
    private final Filter filter;

    /**
     * Initializes a ModelManager with the given expenseLa and userPrefs.
     */
    public ModelManager(ReadOnlyExpenseLa expenseLa, ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(expenseLa, userPrefs);

        logger.fine("Initializing with expenseLa: " + expenseLa + " and user prefs " + userPrefs);

        this.expenseLa = new ExpenseLa(expenseLa);
        this.userPrefs = new UserPrefs(userPrefs);
        unfilteredTransactions = new FilteredList<>(this.expenseLa.getTransactionList());
        monthlyData = this.expenseLa.getMonthlyData();
        filter = this.expenseLa.getFilter();
    }

    public ModelManager() {
        this(new ExpenseLa(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getExpenseLaFilePath() {
        return userPrefs.getExpenseLaFilePath();
    }

    @Override
    public void setExpenseLaFilePath(Path expenseLaFilePath) {
        requireNonNull(expenseLaFilePath);
        userPrefs.setExpenseLaFilePath(expenseLaFilePath);
    }

    //=========== ExpenseLa ================================================================================

    @Override
    public void setExpenseLa(ReadOnlyExpenseLa expenseLa) {
        this.expenseLa.resetData(expenseLa);
    }

    @Override
    public ReadOnlyExpenseLa getExpenseLa() {
        return expenseLa;
    }

    @Override
    public boolean hasTransaction(Transaction transaction) {
        requireNonNull(transaction);
        return expenseLa.hasTransaction(transaction);
    }

    @Override
    public void deleteTransaction(Transaction target) {
        expenseLa.removeTransaction(target);
        boolean positive = target.getAmount().positive;
        double amount = target.getAmount().transactionAmount;
        updateMonthlyData(positive, amount*(-1));
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        expenseLa.addTransaction(transaction);
        boolean positive = transaction.getAmount().positive;
        double amount = transaction.getAmount().transactionAmount;
        updateMonthlyData(positive, amount);
        updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
    }

    public void updateMonthlyData(boolean positive, double amount) {
        if (positive) {
            monthlyData.setIncome(new Income(Double.toString(monthlyData.getIncome().incomeAmount + amount)));
            userPrefs.setTotalBalance(userPrefs.getTotalBalance() + amount);
        } else {
            monthlyData.setExpense(new Expense(Double.toString(monthlyData.getExpense().expenseAmount + amount)));
            userPrefs.setTotalBalance(userPrefs.getTotalBalance() - amount);
        }
    }

    @Override
    public void setTransaction(Transaction target, Transaction editedTransaction) {
        requireAllNonNull(target, editedTransaction);

        expenseLa.setTransaction(target, editedTransaction);
    }

    //=========== Filtered Transaction List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Transaction} backed by the internal list of
     * {@code versionedExpenseLa}
     */

    @Override
    public ObservableList<Transaction> getFilteredTransactionList() {
        return unfilteredTransactions;
    }

    @Override
    public void updateFilteredTransactionList(Predicate<Transaction> predicate) {
        requireNonNull(predicate);
        unfilteredTransactions.setPredicate(predicate);
    }



    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return expenseLa.equals(other.expenseLa)
                && userPrefs.equals(other.userPrefs)
                && unfilteredTransactions.equals(other.unfilteredTransactions);
    }

    //=========== Monthly Data Accessors =============================================================
    /**
     * Returns monthly data object
     */
    @Override
    public MonthlyData getMonthlyData() {
        return monthlyData;
    }

    /**
     * Update monthly data object
     */
    @Override
    public void updateMonthlyData(MonthlyData monthlyData) {
        expenseLa.setMonthlyData(monthlyData);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    @Override
    public Double getTotalBalance() {
        return userPrefs.getTotalBalance();
    }

    @Override
    public void updateTotalBalance(Double balance) {
        userPrefs.setTotalBalance(balance);
        System.out.println(userPrefs.getTotalBalance());
    }
}
