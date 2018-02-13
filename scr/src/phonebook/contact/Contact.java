package src.phonebook.contact;

import java.util.Calendar;
import java.util.List;

public interface Contact {
    int getId();

    List<PhoneNumber> getNumbers();

    int lengthOfNumbersList();

    void addNumber(String phoneNumber, String description);
    void addNumber(String phoneNumber);

    PhoneNumber getNumber(String number);
    PhoneNumber detNumberByDescription(String description);
    PhoneNumber getNumber(int id);

    Calendar getUploadDate();

    void setName(String name);
    String getName();

    String getAddress();
    void setAddress(String address);

    PhoneNumber getDefaultNumber();
    void setDefoultNumber(int id);
    void setDefaultNumber(PhoneNumber phoneNumber);

    void removeNumber(int id);
    void removeNumber(PhoneNumber phoneNumber);

}
