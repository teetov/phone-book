package src.phonebook.servlet;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneBookFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ContactSender extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Contact contact;

        try {
            contact = ServletUtil.extractContactFromRequest(req, "id");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        req.setAttribute("contact", contact);

        try{
            req.getRequestDispatcher("/contact.jsp").forward(req, resp);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
