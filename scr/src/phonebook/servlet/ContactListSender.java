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
            Set<Integer> setId = ServletUtil.parseIdSet(req.getParameter("idSet"));

            for(int id : setId) {
                phoneBook.remove(id);
            }
        }
    }

    //Подготавливает список нужных контактов для конкретного номера страницы страницы.
    private List<Contact> prepareContacts(List<Contact> contactList, int index) {
        int start = (index - 1) * CONTACTS_PER_PAGE;
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

    //Возвращаяет номер страниы в из запроса. Если он не указан возвращает 1.
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
