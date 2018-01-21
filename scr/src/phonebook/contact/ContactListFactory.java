package src.phonebook.contact;

import src.phonebook.contact.xml.XMLSaveLoader;

public class  ContactListFactory {
    private static ContactList contactList;

 /*   public static ContactList getContactList() {
        if(contactList == null)
            contactList = new ContactListSerializableImp();
        return contactList;
    }*/

    public static ContactList getContactList() {
        if(contactList == null)
            contactList = XMLSaveLoader.loadContactList();
        return contactList;
    }
}
