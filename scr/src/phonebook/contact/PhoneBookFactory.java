package src.phonebook.contact;

import src.phonebook.contact.sqldb.DBPhoneBook;
import src.phonebook.contact.xml.XMLSaveLoader;

public class PhoneBookFactory {
    private static PhoneBook phoneBook;

 /*  public static synchronized PhoneBook getPhoneBook() {
        if(phoneBook == null)
            phoneBook = new PhoneBookSerial();
        return phoneBook;
    }*/

    /*public static synchronized PhoneBook getPhoneBook() {
        if(phoneBook == null)
            phoneBook = XMLSaveLoader.loadPhoneBook();
        return phoneBook;
    }*/

     public static synchronized PhoneBook getPhoneBook() {
        if (phoneBook == null)
            phoneBook = new DBPhoneBook();
        return phoneBook;
    }
}
