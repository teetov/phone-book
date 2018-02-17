package src.phonebook.servlet;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneBookFactory;
import src.phonebook.contact.PhoneNumber;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContactAlter extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();
        Contact contact;

        //если запрашивают страницу для редактирования контакта
        String contactId;
        if((contactId = req.getParameter("id")) != null) {
            int contId;
            try {
                contId = Integer.valueOf(contactId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }

            contact = phoneBook.getContact(contId);

            req.setAttribute("contact", contact);

            try{
                req.getRequestDispatcher("/alterContact.jsp").forward(req, resp);
            } catch (ServletException e) {
                System.out.println("ServletException");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException");
                e.printStackTrace();
            }
            return;
        }

    }


//    Parameters:
//    target=[alter, remove, add] alter - изменение контакта, add - добавление нового номера, remove - удаление номера
//    contactId;
//    если target - alter:
//        name
//        address
//        idSet: перечисление id номеров через #
//        description[idPhone] - например description14
//        number[idPhone]
//    если target - add:
//        number
//        description
//        в response следует записать id добавленного контакта
//    если target - remove:
//        idSet: перечисление id номеров через #
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String target = req.getParameter("target");
        System.out.println(target);

        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();
        Contact contact;

        String contactIdStr = req.getParameter("contactId");
        int contactId;
        System.out.println(contactIdStr);
        try {
            contactId = Integer.valueOf(contactIdStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        contact = phoneBook.getContact(contactId);
        if(contact == null)
            return;

        if("alter".equals(target)) {
            String name = req.getParameter("name");
            String address = req.getParameter("address");

            if(!contact.getName().equals(name))
                contact.setName(name);

            if(!contact.getAddress().equals(address))
                contact.setName(address);

            Set<Integer> phoneNumbersId = parseIdSet(req.getParameter("idSet"));

            List<PhoneNumber> numberList = contact.getNumbers();

            for(PhoneNumber ph : numberList) {
                int id = ph.getId();
                if(phoneNumbersId.contains(id)) {
                    String description = req.getParameter("description" + id);
                    if(!ph.getDescription().equals(description))
                        ph.setDescription(description);
                    String number = req.getParameter("number" + id);
                    if(!ph.getNumber().equals(number))
                        ph.setNumber(number);
                }
            }

            String defIdStr = req.getParameter("default");
            int defId;
            try {
                defId = Integer.valueOf(defIdStr);
                if(contact.getDefaultNumber().getId() != defId) {
                    contact.setDefoultNumber(defId);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        } else if("add".equals(target)) {
            String description = req.getParameter("description");
            String name = req.getParameter("number");

            PhoneNumber ph = contact.addNumber(name, description);

            String phId = "" + ph.getId();

            OutputStream os = resp.getOutputStream();
            os.write(phId.getBytes("utf-8"));
            os.flush();
            os.close();
        } else if("delete".equals(target)) {
            Set<Integer> setId = parseIdSet(req.getParameter("idSet"));

            for(int id : setId) {
                contact.removeNumber(id);
            }
        }

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
