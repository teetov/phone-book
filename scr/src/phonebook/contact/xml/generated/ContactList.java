//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.01.15 at 05:04:24 PM MSK 
//


package src.phonebook.contact.xml.generated;

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
public class ContactList implements src.phonebook.contact.ContactList {

    protected List<Contact> contact;

    public ContactList() {
        contact = new ArrayList<>();
    }

    private static class IDGen {
        static boolean hasBeanInitialized = false;

        static SortedSet<Integer> idList = new TreeSet<>();

        public static int newId(ContactList cl) {
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

        public static void removeId(ContactList cl, int id) {
            if(!hasBeanInitialized) {
                initialize(cl);
            }
            idList.remove(id);
        }
        private static void initialize(ContactList cl) {
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
    public List<src.phonebook.contact.Contact> getContactsByNumber(String number) {
        List<src.phonebook.contact.Contact> result = new ArrayList<>();
        for(Contact cont : contact) {
            for(PhoneNumber ph : cont.getPhoneNumber())
                if(number.matches(ph.getNumber())){
                    result.add(cont);
                    break;
                }
        }
        return result;
    }

    @Override
    public List<src.phonebook.contact.Contact> getContactsByName(String name) {
        List<src.phonebook.contact.Contact> result = new ArrayList<>();
        for(Contact cont : contact) {
            if(name.matches(cont.getName()))
                result.add(cont);
        }
        return result;
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
        return false;
    }

    @Override
    public src.phonebook.contact.Contact createNewContact(String name) {
        Contact cont = new Contact(IDGen.newId(this), name);
        contact.add(cont);
        return cont;
    }

    @Override
    public void saveChanges() {
        XMLSaveLoader.saveContactList(this);
    }
}
