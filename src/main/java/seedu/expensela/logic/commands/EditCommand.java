package seedu.expensela.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.expensela.logic.parser.CliSyntax.*;
import static seedu.expensela.model.Model.PREDICATE_SHOW_ALL_TRANSACTIONS;

import java.util.List;
import java.util.Optional;

import seedu.expensela.commons.core.Messages;
import seedu.expensela.commons.core.index.Index;
import seedu.expensela.commons.util.CollectionUtil;
import seedu.expensela.logic.commands.exceptions.CommandException;
import seedu.expensela.model.Model;
import seedu.expensela.model.transaction.*;

/**
 * Edits the details of an existing transaction in the transaction list.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the transaction identified "
            + "by the index number used in the displayed transaction list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_AMOUNT + "AMOUNT] "
            + "[" + PREFIX_DATE + "DATE] "
            + "[" + PREFIX_REMARK + "REMARK] "
            + "[" + PREFIX_CATEGORY + "CATEGORY]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_AMOUNT + "24 "
            + PREFIX_DATE + "2020-02-02";

    public static final String MESSAGE_EDIT_TRANSACTION_SUCCESS = "Edited Transaction: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_TRANSACTION = "This transaction already exists in the address book.";

    private final Index index;
    private final editTransaction editTransaction;

    /**
     * @param index of the transaction in the filtered transaction list to edit
     * @param editTransaction details to edit the transaction with
     */
    public EditCommand(Index index, editTransaction editTransaction) {
        requireNonNull(index);
        requireNonNull(editTransaction);

        this.index = index;
        this.editTransaction = new editTransaction(editTransaction);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Transaction> lastShownList = model.getFilteredTransactionList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
        }

        Transaction transactionToEdit = lastShownList.get(index.getZeroBased());
        Transaction editedTransaction = createEditedTransaction(transactionToEdit, editTransaction);

        if (!transactionToEdit.isSameTransaction(editedTransaction) && model.hasTransaction(editedTransaction)) {
            throw new CommandException(MESSAGE_DUPLICATE_TRANSACTION);
        }

        model.setTransaction(transactionToEdit, editedTransaction);
        model.updateFilteredTransactionList(PREDICATE_SHOW_ALL_TRANSACTIONS);
        return new CommandResult(String.format(MESSAGE_EDIT_TRANSACTION_SUCCESS, editedTransaction));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code transactionToEdit}
     * edited with {@code editTransaction}.
     */
    private static Transaction createEditedTransaction(Transaction transactionToEdit,
                                                  editTransaction editTransaction) {
        assert transactionToEdit != null;

        Name updatedName = editTransaction.getName().orElse(transactionToEdit.getName());
        Amount updatedAmount = editTransaction.getAmount().orElse(transactionToEdit.getAmount());
        Date updatedDate = editTransaction.getDate().orElse(transactionToEdit.getDate());
        Remark updatedRemark = editTransaction.getRemark().orElse(transactionToEdit.getRemark());
        Category updatedCategory = editTransaction.getCategory().orElse(transactionToEdit.getCategory());

        return new Transaction(updatedName, updatedAmount, updatedDate, updatedRemark, updatedCategory);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editTransaction.equals(e.editTransaction);
    }

    /**
     * Stores the details to edit the transaction with. Each non-empty field value will replace the
     * corresponding field value of the transaction.
     */
    public static class editTransaction {
        private Name name;
        private Amount amount;
        private Date date;
        private Remark remark;
        private Category category;

        public editTransaction() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public editTransaction(editTransaction toCopy) {
            setName(toCopy.name);
            setAmount(toCopy.amount);
            setDate(toCopy.date);
            setRemark(toCopy.remark);
            setCategory(toCopy.category);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, amount, date);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setAmount(Amount amount) {
            this.amount = amount;
        }

        public Optional<Amount> getAmount() {
            return Optional.ofNullable(amount);
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Optional<Date> getDate() {
            return Optional.ofNullable(date);
        }

        public void setRemark(Remark remark) {
            this.remark = remark;
        }

        public Optional<Remark> getRemark() {
            return Optional.ofNullable(remark);
        }

        public void setCategory(Category category) { this.category = category; }

        public Optional<Category> getCategory() {
            return Optional.ofNullable(category);
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof editTransaction)) {
                return false;
            }

            // state check
            editTransaction e = (editTransaction) other;

            return getName().equals(e.getName())
                    && getAmount().equals(e.getAmount())
                    && getDate().equals(e.getDate())
                    && getRemark().equals(e.getRemark())
                    && getCategory().equals(e.getCategory());
        }

    }
}
