package phonebook.contact.xml;

import phonebook.contact.ContactList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class XMLSaveLoader {
    private static String xmlPath = "ContactListDb.xml";
    private static String contextPath = "phonebook.contact.xml.generated";

    public static ContactList loadContactList() {
        File file = new File(xmlPath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new phonebook.contact.xml.generated.ContactList();
        }

        try(InputStream is = new FileInputStream(file)) {

            JAXBContext context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            ContactList result = (ContactList) unmarshaller.unmarshal(is);

            return  result;

        } catch (Exception e) {
            e.printStackTrace();
            return new phonebook.contact.xml.generated.ContactList();
        }
    }

    public static void saveContactList(ContactList contactList) {
        try (OutputStream os = new FileOutputStream(xmlPath)){
            JAXBContext context = JAXBContext.newInstance(contextPath);

            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(contactList, os);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
