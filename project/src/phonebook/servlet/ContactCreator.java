package src.phonebook.servlet;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneBookFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactCreator extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.getRequestDispatcher("createContact.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        String description = req.getParameter("description");
        String number = req.getParameter("number");

        PhoneBook phoneBook = PhoneBookFactory.getPhoneBook();
        Contact contact = phoneBook.createNewContact(name, address);

        if((number != null || !"".equals(number)) || (description != null || !"".equals(description)))
        contact.addNumber(number, description);

        resp.sendRedirect("/contact?id=" + contact.getId());
    }
}
