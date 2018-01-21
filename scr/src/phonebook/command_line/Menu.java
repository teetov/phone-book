package phonebook.command_line;

import phonebook.contact.Contact;
import phonebook.contact.ContactList;
import phonebook.contact.ContactListFactory;
import phonebook.contact.PhoneNumber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

public class Menu {

    private String startMessege = "Телефонная книга";

    private String mainMenu = "Выберите действие:" +
            "\r\n1 список всех контактов" +
            "\r\n2 найти контакт" +
            "\r\n3 добавить контакт" +
            "\r\n4 закрыть приложение";

    private String contactListMenu = "Выберите действие со списком контактов:" +
            "\r\n1 выбрать контакт" +
            "\r\n2 удалить контакт" +
            "\r\n3 выход";

    private String lineBreaker = "________**************________\r\n";

    private String contactMenu = "Выберите действие с данным контактом" +
            "\r\n1 добавить номер" +
            "\r\n2 выбрать номер" +
            "\r\n3 удалить номер" +
            "\r\n4 изменить имя контакта" +
            "\r\n5 изменить адрес" +
            "\r\n6 изменить номер по умолчанию" +
            "\r\n7 посмотреь дату создания контакта" +
            "\r\n8 выход";

    private String phoneNumberMenu = "Выберите действие с данным номером" +
            "\r\n1 изменить номер" +
            "\r\n2 изменить описание" +
            "\r\n3 выход";

    ContactList contactList;

    BufferedReader inputReader;
    public Menu() {
        contactList = ContactListFactory.getContactList();

        inputReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() {
        boolean flag = true;

        System.out.println(startMessege);
        try {
            while(flag) {
                System.out.println(mainMenu);

                int command = readIntCommand(4);

                switch(command) {
                    case 1:
                        manageContactList(contactList.getContactList());
                        break;

                    case 2:
                        String filter = readString("Введите часть имени или номера");

                        List<Contact> contacts = findContacts(contactList.getContactList(), filter);
                        manageContactList(contacts);
                        break;

                    case 3:
                        Contact contact = addContactSimple();
                        manageContact(contact);
                        break;

                    case 4:
                        flag = false;
                        break;
                }
            }
        } finally {
            contactList.saveChanges();
        }
    }

    private void manageContactList(List<Contact> list) {
        boolean flag = true;

        while (flag) {
            if(list.size() == 0) {
                System.out.println("Отсутствуют доступные элементы\r\n");
                return;
            }
            sortContactsByName(list);
            showContactList(list);


            System.out.println();
            System.out.println(contactListMenu);

            int command = readIntCommand(3);

            switch(command) {
                case 1:
                    System.out.println("Выберете номер интересующего вас контакта:");
                    int index = readIntCommand(list.size());
                    manageContact(list.get(index - 1));

                    break;
                case 2:
                    System.out.println("Выберете номер контакта для удаления:");
                    int numberToDelet = readIntCommand(list.size());
                    contactList.remove(list.get(numberToDelet - 1).getId());
                    list.remove(numberToDelet - 1);

                    contactList.saveChanges();

                    break;
                case 3:
                    flag = false;
                    break;
                default: break;
            }
        }
    }

    private List<Contact> findContacts(List<Contact> contacts, String str) {
        Set<Contact> result = new HashSet<>();

        Pattern pattern = Pattern.compile(str.toLowerCase());
        for(Contact contact : contacts) {
            if(pattern.matcher(contact.getName().toLowerCase()).find())
                result.add(contact);
            else {
                for(PhoneNumber phn : contact.getNumbers()) {
                    if(pattern.matcher(phn.getNumber()).find()) {
                        result.add(contact);
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(result);
    }

    private void showContactList(List<Contact> list) {
        System.out.println(lineBreaker);

        for(int i = 0; i < list.size(); i++) {
            Contact contact = list.get(i);
            System.out.println("Контакт № " + (i + 1));
            System.out.println(contact.getName());
            for(PhoneNumber number : contact.getNumbers()) {
                System.out.print("\t");
                System.out.println(number.getNumber());
            }
            System.out.println();
        }
    }

    private void sortContactsByName(List<Contact> list) {

        list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

    }

    private int readIntCommand(int maxValue) {
        System.out.println("(Введите число в диапазоне между 1 и " + maxValue + ")");
        int result = -1;
        while(result < 1 || result > maxValue) {
            try {
                String command = inputReader.readLine();
                if(command.matches("\\d+"))
                    result = Integer.valueOf(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        return result;
    }

    private String readString(String message) {
        System.out.println(message);
        String result = "";
        try {
            result = inputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
        return result;
    }

    private  void manageContact(Contact contact) {
        boolean flag = true;

        while(flag) {
            System.out.println(contact);
            System.out.println();
            System.out.println(contactMenu);

            int command = readIntCommand(8);

            switch (command) {
                case 1:
                    String number, description;

                    number = readString("Введите номер: ");
                    description = readString("Введите описание: ");

                    contact.addNumber(number, description);

                    contactList.saveChanges();

                    break;

                case 2:
                    List<PhoneNumber> phList = contact.getNumbers();
                    showPhoneNumberList(contact);
                    System.out.println();
                    System.out.println("Выберете номер записи:");

                    int i = readIntCommand(phList.size());
                    managePhoneNumber(phList.get(i - 1));
                    break;

                case 3:
                    phList = contact.getNumbers();
                    showPhoneNumberList(contact);

                    System.out.println("Выберете номер записи:");

                    i = readIntCommand(phList.size());
                    contact.removeNumber(phList.get(i - 1));

                    contactList.saveChanges();

                    break;

                case 4:
                    String newName = readString("Введите новое имя: ");
                    contact.setName(newName);

                    contactList.saveChanges();

                    break;

                case 5:
                    String newAddress = readString("Введите новый адрес: ");

                    contact.setAddress(newAddress);

                    contactList.saveChanges();

                    break;

                case 6:
                    phList = contact.getNumbers();
                    showPhoneNumberList(contact);
                    System.out.println();
                    System.out.println("Выберете новый номер по умолчанию:");

                    i = readIntCommand(phList.size());
                    contact.setDefoultNumber(phList.get(i - 1));

                    contactList.saveChanges();

                    break;

                case 7:
                    String date = new Formatter().format("%1$tY-%1$tm-%1$tR"
                            , contact.getUploadDate()).toString();
                    showMessage(date);
                    break;

                case 8:
                    flag = false;

                default:
                    break;
            }
        }
    }

    private void managePhoneNumber(PhoneNumber phNumber) {
        boolean flag = true;
        while(flag) {

            System.out.println(phNumber);
            System.out.println();

            System.out.println(phoneNumberMenu);
            System.out.println();

            int command = readIntCommand(3);

            switch(command) {
                case 1:
                    phNumber.setNumber(readString("Введите новый номер:"));

                    contactList.saveChanges();

                    break;

                case 2:
                    phNumber.setDescription(readString("Введите новое описание:"));

                    contactList.saveChanges();

                    break;
                case 3:
                    flag = false;
                    break;
            }
        }
    }

    private void showPhoneNumberList(Contact contact) {
        List<PhoneNumber> phList = contact.getNumbers();

        for(int i = 0; i < phList.size(); i++) {
            if(phList.get(i) == contact.getDefoultNumber())
                System.out.println("Номер по умолчанию: ");
            System.out.println("№ " + (i + 1));
            System.out.println("\t" + phList.get(i).toString());
        }

        System.out.println();
    }

    private void showMessage(String message) {
        System.out.println(message);
        System.out.println();
        System.out.println("Нажмите Enter для продолжения...");
        try {
            inputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private Contact addContactSimple() {
        String name = readString("Введите имя нового контакта");

        String phoneNumber = readString("Введите номер телефона");

        Contact contact = contactList.createNewContact(name);
        contact.addNumber(phoneNumber);;
        return contact;
    }

}
