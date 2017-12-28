package phonebook.contact;

import phonebook.contact.serializable_list_imp.ContactListSerializableImp;

public class  ContactListFactory {
    public static ContactList getContactList() {
        return new ContactListSerializableImp();
    }
}
