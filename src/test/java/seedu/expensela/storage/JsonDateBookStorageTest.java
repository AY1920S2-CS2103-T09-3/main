package seedu.expensela.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.expensela.testutil.Assert.assertThrows;
import static seedu.expensela.testutil.TypicalTransactions.ALICE;
import static seedu.expensela.testutil.TypicalTransactions.HOON;
import static seedu.expensela.testutil.TypicalTransactions.IDA;
import static seedu.expensela.testutil.TypicalTransactions.getTypicalExpenseLa;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.expensela.commons.exceptions.DataConversionException;
import seedu.expensela.model.ExpenseLa;
import seedu.expensela.model.ReadOnlyExpenseLa;

public class JsonDateBookStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonExpenseLaStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readExpenseLa_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readExpenseLa(null));
    }

    private java.util.Optional<ReadOnlyExpenseLa> readExpenseLa(String filePath) throws Exception {
        return new JsonExpenseLaStorage(Paths.get(filePath)).readExpenseLa(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readExpenseLa("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readExpenseLa("notJsonFormatExpenseLa.json"));
    }

    @Test
    public void readExpenseLa_invalidTransactionExpenseLa_throwDataConversionException() {
        assertThrows(DataConversionException.class, (
        ) -> readExpenseLa("invalidTransactionExpenseLa.json"));
    }

    @Test
    public void readExpenseLa_invalidAndValidTransactionExpenseLa_throwDataConversionException() {
        assertThrows(DataConversionException.class, (
        ) -> readExpenseLa("invalidAndValidTransactionExpenseLa.json"));
    }

    @Test
    public void readAndSaveExpenseLa_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempExpenseLa.json");
        ExpenseLa original = getTypicalExpenseLa();
        JsonExpenseLaStorage jsonExpenseLaStorage = new JsonExpenseLaStorage(filePath);

        // Save in new file and read back
        jsonExpenseLaStorage.saveExpenseLa(original, filePath);
        ReadOnlyExpenseLa readBack = jsonExpenseLaStorage.readExpenseLa(filePath).get();
        assertEquals(original, new ExpenseLa(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addTransaction(HOON);
        original.removeTransaction(ALICE);
        jsonExpenseLaStorage.saveExpenseLa(original, filePath);
        readBack = jsonExpenseLaStorage.readExpenseLa(filePath).get();
        assertEquals(original, new ExpenseLa(readBack));

        // Save and read without specifying file path
        original.addTransaction(IDA);
        jsonExpenseLaStorage.saveExpenseLa(original); // file path not specified
        readBack = jsonExpenseLaStorage.readExpenseLa().get(); // file path not specified
        assertEquals(original, new ExpenseLa(readBack));

    }

    @Test
    public void saveExpenseLa_nullExpenseLa_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveExpenseLa(null, "SomeFile.json"));
    }

    /**
     * Saves {@code ExpenseLa} at the specified {@code filePath}.
     */
    private void saveExpenseLa(ReadOnlyExpenseLa ExpenseLa, String filePath) {
        try {
            new JsonExpenseLaStorage(Paths.get(filePath))
                    .saveExpenseLa(ExpenseLa, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveExpenseLa_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveExpenseLa(new ExpenseLa(), null));
    }
}
