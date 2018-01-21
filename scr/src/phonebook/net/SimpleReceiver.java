package phonebook.net;

import phonebook.contact.Contact;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class SimpleReceiver {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(21786);

        Socket socket = serverSocket.accept();

        InputStream inputStream = socket.getInputStream();
        DataInputStream dis = new DataInputStream(inputStream);


        try {
            byte[] buffer;
            int i;
            while ((i = dis.readInt()) != -1) {
                buffer = new byte[i];
                dis.read(buffer);
                System.out.println(ContactByteArrayConverter.byteArrayToContact(buffer));

            }
        } catch(EOFException exc) {
            exc.printStackTrace();
        }


        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}
