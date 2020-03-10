package seedu.expensela.testutil;

import seedu.expensela.model.AddressBook;
import seedu.expensela.model.transaction.Transaction;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Person} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withPerson(Transaction transaction) {
        addressBook.addPerson(transaction);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}