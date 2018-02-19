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

public class ContactAlter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Contact contact;

        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        try {
            contact = ServletUtil.extractContactFromRequest(req, "id");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }


        req.setAttribute("contact", contact);

        try {
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

        selectionOfAlterMod(req, resp);
    }

    //Выбирает и вызывает нужный метод для модификации контакта
    private void selectionOfAlterMod(HttpServletRequest req, HttpServletResponse resp) {
        Contact contact;

        try {
            contact = ServletUtil.extractContactFromRequest(req, "contactId");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        String target = req.getParameter("target");

        if ("alter".equals(target)) {
            alterContact(req, contact);
        } else if ("add".equals(target)) {
            addNewPhone(req, resp, contact);
        } else if ("delete".equals(target)) {
            deletePhone(req, contact);
        }
    }

    //Запускается если требуется изменить поля контакта
    private void alterContact(HttpServletRequest req, Contact contact) {

        String name = req.getParameter("name");
        String address = req.getParameter("address");

        if (!contact.getName().equals(name))
            contact.setName(name);

        if (!contact.getAddress().equals(address))
            contact.setAddress(address);

        Set<Integer> phoneNumbersId = ServletUtil.parseIdSet(req.getParameter("idSet"));

        List<PhoneNumber> numberList = contact.getNumbers();

        for (PhoneNumber ph : numberList) {
            int id = ph.getId();
            if (phoneNumbersId.contains(id)) {
                String description = req.getParameter("description" + id);
                if (!ph.getDescription().equals(description))
                    ph.setDescription(description);
                String number = req.getParameter("number" + id);
                if (!ph.getNumber().equals(number))
                    ph.setNumber(number);
            }
        }

        try {
            int defId = ServletUtil.parseParameterToInt(req, "default");
            if (contact.getDefaultNumber() != null && contact.getDefaultNumber().getId() != defId) {
                contact.setDefoultNumber(defId);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    //Добавляет новый номер к контакту и отсулает клиенту id нового номера
    private void addNewPhone(HttpServletRequest req, HttpServletResponse resp, Contact contact) {

        String description = req.getParameter("description");
        String name = req.getParameter("number");

        try {
            PhoneNumber ph = contact.addNumber(name, description);

            String phId = "" + ph.getId();

            OutputStream os = resp.getOutputStream();
            os.write(phId.getBytes("utf-8"));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Удаляет номер из контакта
    private void deletePhone(HttpServletRequest req, Contact contact) {

        Set<Integer> setId = ServletUtil.parseIdSet(req.getParameter("idSet"));

        for (int id : setId) {
            contact.removeNumber(id);
        }
    }
}
