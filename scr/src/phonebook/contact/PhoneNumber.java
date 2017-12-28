package phonebook.contact;

public interface PhoneNumber {

    int getId();

    String getDescription();

    String getNumber();
    void setNumber(String number);

    void setDescription(String description);
}
