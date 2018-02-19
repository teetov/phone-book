package src.phonebook.contact.serializable;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneNumber;

import java.io.*;
import java.util.*;

public class PhoneBookSerial implements PhoneBook, Serializable {
    private HashMap<Integer, Contact> contactList = new HashMap<>();
    private String fileName = "SerializableContactsStore.serial";

    private IdGenerator contactIdGen = new IdGenerator();

    public PhoneBookSerial() {
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
            contactIdGen = new IdGenerator();
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
            contactIdGen = new IdGenerator();
            e.printStackTrace();
        }
    }

    @Override
    public Contact getContact(int id) {
        return contactList.get(id);
    }

    @Override
    public List<Contact> findContacts(String filter) {
        String matcher = ".*" + filter + ".*";
        List<Contact> resultList = new ArrayList<>();
        for(Contact cont : contactList.values()) {
            if(cont.getName().matches(filter))
                resultList.add(cont);
            else {
                newContact: for(PhoneNumber numb : cont.getNumbers()) {
                    if (numb.getNumber().matches(filter)) {
                        resultList.add(cont);
                        break newContact;
                    }
                }
            }
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

        saveChanges();

        return true;
    }

    @Override
    public Contact createNewContact(String name, String address) {
        Contact result = new ContactSerial(contactIdGen.newId(), name, address);
        contactList.put(result.getId(), result);

        saveChanges();

        return result;
    }

    @Override
    public Contact createNewContact(String name) {
        return createNewContact(name, "");
    }

    private void saveChanges() {
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