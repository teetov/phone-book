package src.phonebook.contact.serializable_list;

import src.phonebook.contact.AbstractContact;
import src.phonebook.contact.IdGenerator;
import src.phonebook.contact.PhoneNumber;

import java.io.Serializable;
import java.util.*;

public class ContactImpl extends AbstractContact implements Serializable{

    private final int id;

    private String name;

    private final IdGenerator numberIdGen = new IdGeneratorImpl();

    private String address;

    private Calendar dateOfCreation;
    private List<PhoneNumber> numbers = new ArrayList<>();

    private PhoneNumber defPhoneNumber;

    ContactImpl(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
        dateOfCreation = Calendar.getInstance();
    }

    ContactImpl(int id, String name) {
        this(id, name, "");
    }


    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<PhoneNumber> getNumbers() {
        return numbers;
    }

    @Override
    public int lengthOfNumbersList() {
        return numbers.size();
    }

    @Override
    public void addNumber(String number, String description) {
        PhoneNumber num = new PhoneNumberImpl(numberIdGen.newId(), number, description);
        if(numbers.size() == 0)
            defPhoneNumber = num;
        numbers.add(num);
    }

    @Override
    public void addNumber(String number) {
        addNumber(number, "");
    }

    //реализовать
    @Override
    public PhoneNumber getNumber(String number) {
        for(PhoneNumber numb : numbers) {
            if(numb.getNumber().equals(number))
                return numb;
        }
        return null;
    }

    //реализовать
    @Override
    public PhoneNumber detNumberByDescription(String description) {
        for(PhoneNumber numb : numbers) {
            if(numb.getDescription().matches(description))
                return numb;
        }
        return null;
    }

    @Override
    public PhoneNumber getNumber(int id) {
        return numbers.get(id);
    }


    @Override
    public Calendar getUploadDate() {
        return dateOfCreation;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public PhoneNumber getDefoultNumber() {
        if(defPhoneNumber == null) {
            if(numbers.size() == 0)
                return null;
            defPhoneNumber = numbers.get(0);
        }

        return defPhoneNumber;
    }

    @Override
    public void setDefoultNumber(int id) {
        defPhoneNumber = getNumber(id);
    }

    @Override
    public void setDefoultNumber(PhoneNumber phoneNumber) {
        defPhoneNumber = phoneNumber;
    }

    @Override
    public void removeNumber(int id) {
        if(numbers.get(id) == getDefoultNumber()) {
            defPhoneNumber = null;
        }
        numberIdGen.removeId(id);
        numbers.remove(id);
    }

    @Override
    public void removeNumber(PhoneNumber phoneNumber) {
        if(phoneNumber == getDefoultNumber()) {
            defPhoneNumber = null;
        }
        numberIdGen.removeId(phoneNumber.getId());
        numbers.remove(phoneNumber);
    }

}
