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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactListSender extends HttpServlet{

    private static final int CONTACTS_PER_PAGE = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        String filter = request.getParameter("filter");

        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();

        List<Contact> contacts;
        if(filter == null || filter.equals("")) {
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
            request.getRequestDispatcher("/contactlist.jsp").forward(request, response);

        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String target = req.getParameter("target");

        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();

        if("remove".equals(target)) {
            Set<Integer> setId = parseIdSet(req.getParameter("idSet"));

            for(int id : setId) {
                phoneBook.remove(id);
            }
        }
    }

    private List<Contact> prepareContacts(List<Contact> contactList, int column) {
        int start = (column - 1) * CONTACTS_PER_PAGE;
        int totalSize = contactList.size();

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

    private Set<Integer> parseIdSet(String idSet) {
        Set<Integer> setId = new HashSet<>();
        if(idSet == null || idSet.length() == 0)
            return setId;

        String[] phoneNumbersIdStr = new String[0];
        phoneNumbersIdStr = idSet.substring(1).split("#");
        for(String id : phoneNumbersIdStr) {
            setId.add(Integer.valueOf(id));
        }
        return setId;
    }
}
