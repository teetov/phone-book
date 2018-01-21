package phonebook.net;

import phonebook.contact.Contact;
import phonebook.contact.ContactList;
import phonebook.contact.ContactListFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SimpleSender {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.1.1", 21786);

        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream oos = new DataOutputStream(outputStream);


        ContactList contactList = ContactListFactory.getContactList();
        for(Contact contact : contactList.getContactList()) {

            System.out.println(contact);
            byte[] bytes = ContactByteArrayConverter.contactToByteArray(contact);


            try {
                oos.writeInt(bytes.length);
                outputStream.write(bytes);
            } catch(IOException exc) {
                exc.printStackTrace();
            }
        }

        oos.writeInt(-1);
        outputStream.close();
        socket.close();
    }
}
