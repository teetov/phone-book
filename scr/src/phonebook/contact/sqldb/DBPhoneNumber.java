package src.phonebook.contact.sqldb;

import src.phonebook.contact.AbstractPhoneNumber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBPhoneNumber extends AbstractPhoneNumber{

    private int id;

    private String number;
    private String description;

    public DBPhoneNumber(int id, String number, String description) {
        this.id = id;
        this.number = number;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(
                    "UPDATE \"phoneNumbers\" SET \"number\" = ? WHERE id = ?");

            prep.setString(1, number);
            prep.setInt(2, getId());

            prep.executeUpdate();

            prep.close();

        } catch(SQLException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(
                    "UPDATE \"phoneNumbers\" SET \"description\" = ? WHERE id = ?");

            prep.setString(1, description);
            prep.setInt(2, getId());

            prep.executeUpdate();

            prep.close();

        } catch(SQLException exc) {
            exc.printStackTrace();
        }
    }
}
