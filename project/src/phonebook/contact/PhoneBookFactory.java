package src.phonebook.contact;

import com.sun.deploy.panel.PropertyTreeModel;
import src.phonebook.contact.sqldb.DBPhoneBook;
import src.phonebook.contact.xml.XMLSaveLoader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PhoneBookFactory {
    private static PhoneBook phoneBook;

    //Возможные значения PHONE_BOOK_VERSION:  SQLDB, SERIAL;
    private static String PHONE_BOOK_VERSION;

    static {
        Properties prop = new Properties();

        try(InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("src/phonebook/prop.properties")) {
            prop.load(is);
            PHONE_BOOK_VERSION = prop.getProperty("PHONE_BOOK_VERSION");

        } catch (IOException e) {
            PHONE_BOOK_VERSION = "SQLDB";
            e.printStackTrace();
        }

        System.out.println(PHONE_BOOK_VERSION);
        switch(PHONE_BOOK_VERSION) {
            case ("SQLDB"):
                phoneBook = new DBPhoneBook();
                break;
            case ("XML"):
                phoneBook = new XMLSaveLoader().loadPhoneBook();
                break;
            default:
                System.out.println("in default");
                phoneBook = new DBPhoneBook();
                break;
        }
    }


     public static synchronized PhoneBook getPhoneBook() {
        return phoneBook;
    }
}
