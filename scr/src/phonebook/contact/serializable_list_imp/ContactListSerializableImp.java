package phonebook.contact.serializable_list_imp;

import phonebook.contact.Contact;
import phonebook.contact.ContactList;
import phonebook.contact.PhoneNumber;

import java.io.*;
import java.util.*;

public class ContactListSerializableImp implements ContactList, Serializable {
    private HashMap<Integer, Contact> contactList = new HashMap<>();
    private String fileName = "SerializableContactsStore.serial";

    private  IdGenerator contactIdGen = new IdGeneratorImpl();

    public ContactListSerializableImp() {
        File file = new File("SerializableContactsStore.serial");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Список контактов не обнаружен." +
                    "\r\nСоздан новый список контактов.\r\n");
            contactList = new HashMap<>();
            contactIdGen = new IdGeneratorImpl();
            return;
        }
        try(ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fileName))) {

            contactList =  (HashMap<Integer, Contact>) ois.readObject();
            contactIdGen = (IdGenerator) ois.readObject();
        } catch (Exception e) {
            System.out.println("Список контактов не обнаружен." +
                    "\r\nСоздан новый список контактов.\r\n");
            contactList = new HashMap<>();
            contactIdGen = new IdGeneratorImpl();
            e.printStackTrace();
        }
    }

    @Override
    public Contact getContact(int id) {
        return contactList.get(id);
    }

    @Override
    public List<Contact> getContactsByNumber(String number) {
        List<Contact> resultList = new ArrayList<>();
        for(Contact cont : contactList.values()) {
            newContact: for(PhoneNumber numb : cont.getNumbers()) {
                if(number != null && number.equals(numb.getNumber())) {
                    resultList.add(cont);
                    break newContact;
                }
            }
        }
        return resultList;
    }

    @Override
    public List<Contact> getContactsByName(String name) {
        List<Contact> resultList = new ArrayList<>();
        for(Contact cont : contactList.values()) {
            if(name != null && name.matches(cont.getName()))
                resultList.add(cont);
        }
        return resultList;
    }

    @Override
    public List<Contact> getContactList() {
        return new ArrayList<Contact>(contactList.values());
    }

    @Override
    public boolean remove(int id) {
        if(!contactList.containsKey(id))
            return false;
        contactList.remove(id);
        contactIdGen.removeId(id);
        return true;
    }

    @Override
    public Contact createNewContact(String name) {
        Contact result = new ContactImpl(contactIdGen.newId(), name);
        contactList.put(result.getId(), result);
        return result;
    }

    @Override
    public void close() {
        try (ObjectOutputStream ous = new ObjectOutputStream(
                new FileOutputStream(fileName)
        )) {
            ous.writeObject(contactList);
            ous.writeObject(contactIdGen);
            System.out.println("Load contact list in file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
