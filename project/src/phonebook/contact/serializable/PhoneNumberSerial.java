package src.phonebook.contact.serializable;

import src.phonebook.contact.AbstractPhoneNumber;

import java.io.Serializable;

public class PhoneNumberSerial extends AbstractPhoneNumber implements  Serializable {

    private final int id;

    private String number;

    private String description;

    public PhoneNumberSerial(int id, String number, String description) {
        this.id = id;
        this.number = number;
        this.description = description;
    }

    public PhoneNumberSerial(int id, String number) {
        this(id, number, "");
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
