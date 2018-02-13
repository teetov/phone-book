package src.phonebook.contact.xml;

import src.phonebook.contact.PhoneBook;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

public class XMLSaveLoader {
    private static String xmlPath = "K:/java/ContactListDb.xml";
    private static String contextPath = "src.phonebook.contact.xml.generated";

    public static PhoneBook loadPhoneBook() {
        File file = new File(xmlPath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new src.phonebook.contact.xml.generated.PhoneBook();
        }

        try(InputStream is = new FileInputStream(file)) {

            JAXBContext context = JAXBContext.newInstance(contextPath);

            Unmarshaller unmarshaller = context.createUnmarshaller();

            PhoneBook result = (PhoneBook) unmarshaller.unmarshal(is);

            return  result;

        } catch (Exception e) {
            e.printStackTrace();
            return new src.phonebook.contact.xml.generated.PhoneBook();
        }
    }

    public static void savePhoneBook(PhoneBook phoneBook) {
        try (OutputStream os = new FileOutputStream(xmlPath)){
            JAXBContext context = JAXBContext.newInstance(contextPath);

            Marshaller marshaller = context.createMarshaller();

            marshaller.marshal(phoneBook, os);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
