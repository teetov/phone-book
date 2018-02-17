//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.15 at 05:04:24 PM MSK 
//


package src.phonebook.contact.xml.generated;

import src.phonebook.contact.PhoneNumber;
import src.phonebook.contact.xml.XMLSaveLoader;

import java.util.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contact"
})
@XmlRootElement(name = "contactList")
public class PhoneBook implements src.phonebook.contact.PhoneBook {

    protected List<Contact> contact;

    public PhoneBook() {
        contact = new ArrayList<>();
    }

    private static class IDGen {
        static boolean hasBeanInitialized = false;

        static SortedSet<Integer> idList = new TreeSet<>();

        public static int newId(src.phonebook.contact.xml.generated.PhoneBook cl) {
            if(!hasBeanInitialized) {
                initialize(cl);
            }
            int result;
            if(idList.size() > 0) {
                result = idList.last();
                result++;
            } else {
                result = 1;
            }

            idList.add(result);
            return result;
        }

        public static void removeId(src.phonebook.contact.xml.generated.PhoneBook cl, int id) {
            if(!hasBeanInitialized) {
                initialize(cl);
            }
            idList.remove(id);
        }
        private static void initialize(src.phonebook.contact.xml.generated.PhoneBook cl) {
            if(hasBeanInitialized)
                return;
            for(Contact cont : cl.contact) {
                idList.add(cont.getContactID());
            }
            hasBeanInitialized = true;
        }
    };

    List<Contact> getContact() {
        if (contact == null) {
            contact = new ArrayList<Contact>();
        }
        return this.contact;
    }

    @Override
    public src.phonebook.contact.Contact getContact(int id) {
        for(Contact cont : contact) {
            if(cont.getId() == id)
                return cont;
        }
        return null;
    }

    @Override
    public List<src.phonebook.contact.Contact> findContacts(String partOfAttribute) {
        String matcher = ".*" +partOfAttribute + ".*";
        List<src.phonebook.contact.Contact> resultList = new ArrayList<>();
        for(src.phonebook.contact.Contact cont : contact) {
            if(cont.getName().matches(partOfAttribute))
                resultList.add(cont);
            else {
                newContact: for(PhoneNumber numb : cont.getNumbers()) {
                    if (numb.getNumber().matches(partOfAttribute)) {
                        resultList.add(cont);
                        break newContact;
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public List<src.phonebook.contact.Contact> getContactList() {
        List<src.phonebook.contact.Contact> result = new ArrayList<>(contact);
        return result;
    }

    @Override
    public boolean remove(int id) {
        Iterator<Contact> iter = contact.iterator();
        while(iter.hasNext()) {
            if (iter.next().getId() == id) {
                iter.remove();
                IDGen.removeId(this, id);
                return true;
            }
        }

        saveChanges();

        return false;
    }

    @Override
    public src.phonebook.contact.Contact createNewContact(String name) {
        return createNewContact( name, "");
    }

    @Override
    public src.phonebook.contact.Contact createNewContact(String name, String address) {
        Contact cont = new Contact(IDGen.newId(this), name, address);
        contact.add(cont);

        saveChanges();

        return cont;
    }

    private void saveChanges() {
        XMLSaveLoader.savePhoneBook(this);
    }
}
