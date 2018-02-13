package src.phonebook.servlet;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneBookFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ContactListSender extends HttpServlet{

    private static final int CONTACTS_PER_PAGE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String filter = request.getParameter("filter");

        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();

        List<Contact> contacts;
        if(filter == null || filter.equals("")) {
            System.out.println(filter);
            contacts = phoneBook.getContactList();
        } else {
            contacts = phoneBook.findContacts(filter);
        }

        Collections.sort(contacts,
                (Contact c1, Contact c2) -> c1.getName().compareTo(c2.getName()));

        request.setAttribute("phoneBook", contacts);

        int index = columnIndex(request);
        List<Contact> contactForPrint = prepareContacts(contacts, index);
        request.setAttribute("printBook", contactForPrint);

        request.setAttribute("index", index);
        request.setAttribute("maxIndex",
                (contacts.size() + CONTACTS_PER_PAGE - 1) / CONTACTS_PER_PAGE);

        try {
            //System.out.println(contacts);
            request.getRequestDispatcher("/contactlist.jsp").forward(request, response);

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Contact> prepareContacts(List<Contact> contactList, int column) {
        int start = (column - 1) * CONTACTS_PER_PAGE;
        int totalSize = contactList.size();

        System.out.println("start: " + start + "sotatl size " + totalSize);
        if(start > totalSize) {
            if(totalSize < CONTACTS_PER_PAGE) {
                return contactList.subList(0, totalSize);
            }
            return contactList.subList(totalSize - CONTACTS_PER_PAGE, totalSize);
        }

        int end = totalSize < (start + CONTACTS_PER_PAGE) ? totalSize : start + CONTACTS_PER_PAGE;

        return contactList.subList(start, end);
    }

    private int columnIndex(HttpServletRequest req) {
        String index = req.getParameter("index");
        if(index == null || index.equals(""))
            return 1;

        int i;
        try {
            i = Integer.valueOf(index);
        } catch(NumberFormatException exc) {
            return 1;
        }
        return i;
    }
}
