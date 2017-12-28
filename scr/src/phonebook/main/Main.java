package phonebook.main;

import phonebook.command_line.Menu;
import phonebook.contact.ContactList;
import phonebook.contact.serializable_list_imp.ContactListSerializableImp;

import static javafx.scene.input.KeyCode.M;

public class Main {
    public static void main(String[] args) {
        Menu nemu = new Menu();
        nemu.start();
    }
}
