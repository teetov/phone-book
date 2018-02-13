package src.phonebook.contact;

import src.phonebook.contact.Contact;

import java.util.*;

public interface PhoneBook {
    Contact getContact(int id);

    List<Contact> findContacts(String partOfAttribute);

    List<Contact> getContactList();

    boolean remove(int id);

    Contact createNewContact(String name);


}
