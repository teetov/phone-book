package src.phonebook.servlet;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneBookFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

public class ServletUtil {
    //Возвращает список чисел полученных из строки, где перечисленны числа разделенные #;
    static Set<Integer> parseIdSet(String idSet) {
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

    //Достает из запроса нужный параметр и преобразыет его в число
    static int parseParameterToInt(HttpServletRequest req, String param) throws NumberFormatException {
        String contactIdStr = req.getParameter(param);
        int contactId;

        try {
            contactId = Integer.valueOf(contactIdStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Wrong format id: " + contactIdStr);
        }

        return contactId;
    }

    //Возвращает контакт по id, указанному в запросе
    static Contact extractContactFromRequest(HttpServletRequest req, String idFormat) throws NumberFormatException {
        int id = parseParameterToInt(req, idFormat);

        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();

        return phoneBook.getContact(id);
    }
}
