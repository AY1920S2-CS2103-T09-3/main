package seedu.expensela.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.expensela.commons.exceptions.DataConversionException;
import seedu.expensela.model.ReadOnlyExpenseLa;
import seedu.expensela.model.ReadOnlyUserPrefs;
import seedu.expensela.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends ExpenseLaStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyExpenseLa> readExpenseLa() throws DataConversionException, IOException;

    @Override
    void saveExpenseLa(ReadOnlyExpenseLa expenseLa) throws IOException;

}
