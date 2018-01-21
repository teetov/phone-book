package phonebook.net;

import phonebook.contact.Contact;
import phonebook.contact.ContactList;
import phonebook.contact.ContactListFactory;
import phonebook.contact.PhoneNumber;

import java.io.*;
import java.util.List;

public class ContactByteArrayConverter {

    public static byte[] contactToByteArray(Contact contact) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)){

            //Запись имени контакта
            byte[] name = contact.getName().getBytes();
            oos.writeInt(name.length);
            oos.write(name);

            //Запись адреса
            byte[] address = contact.getAddress().getBytes();
            oos.writeInt(address.length);
            oos.write(address);

            List<PhoneNumber> numbers = contact.getNumbers();
            oos.writeInt(numbers.size());

            //id номера по умолчанию
            PhoneNumber def = contact.getDefoultNumber();
            oos.writeInt(def == null ? -2 : def.getId());
            //Запись номеров
            for(PhoneNumber phn : numbers) {
                oos.writeInt(phn.getId());

                byte[] num = phn.getNumber().getBytes();
                oos.writeInt(num.length);
                oos.write(num);

                byte[] descr = phn.getDescription().getBytes();
                oos.writeInt(descr.length);
                oos.write(descr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }

    public static Contact byteArrayToContact(byte[] array) {
        Contact result = null;

        ByteArrayInputStream bais = new ByteArrayInputStream(array);
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            //чтение имени
            int nameSize = ois.readInt();
            byte[] buffer = new byte[nameSize];
            ois.read(buffer);
            String name = new String(buffer);

            //чтение адреса
            int addressSize = ois.readInt();
            buffer = new byte[addressSize];
            ois.read(buffer);
            String address = new String(buffer);

            ContactList contactList = ContactListFactory.getContactList();

            result = contactList.createNewContact(name);

            result.setAddress(address);

            //чтение номеров
            int numCount = ois.readInt();

            int defId = ois.readInt();
            System.out.println("incoming default id: " + defId);
            for(int i = 0; i < numCount; i++) {
                int id = ois.readInt();
                boolean isDef = id == defId;

                System.out.println("incoming id: " + id);

                int phNumSize = ois.readInt();
                buffer = new byte[phNumSize];
                ois.read(buffer);
                String phn = new String(buffer);

                int descrSize = ois.readInt();
                buffer = new byte[descrSize];
                ois.read(buffer);
                String descr = new String(buffer);

                result.addNumber(phn, descr);

                if(isDef) {
                    System.out.println("detected id coincidence " + defId + " " + id);
                    result.setDefoultNumber(result.getNumber(phn));
                }
            }

            contactList.saveChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
