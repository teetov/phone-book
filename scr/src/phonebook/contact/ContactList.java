package src.phonebook.contact;

import src.phonebook.contact.Contact;

import java.util.*;

public interface ContactList {
    Contact getContact(int id);

    List<Contact> getContactsByNumber(String number);
    List<Contact> getContactsByName(String name);

    List<Contact> getContactList();

    boolean remove(int id);

    Contact createNewContact(String name);

    void saveChanges();
}
