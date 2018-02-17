package src.phonebook.contact.sqldb;

import src.phonebook.contact.AbstractPhoneNumber;

import java.sql.*;

public class DBPhoneNumber extends AbstractPhoneNumber{

    protected static int DESCRIPTION_SIZE;
    protected static int PHONE_SIZE;

    static {
        try (Connection connection = DBConnectionBuilder.getConnection()) {

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT \"description\", \"number\" FROM \"phoneNumbers\";");

            DESCRIPTION_SIZE = resultSet.getMetaData().getColumnDisplaySize(1);
            PHONE_SIZE = resultSet.getMetaData().getColumnDisplaySize(2);

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (DESCRIPTION_SIZE == 0)
                DESCRIPTION_SIZE = 255;
            if (PHONE_SIZE == 0)
                PHONE_SIZE = 255;
        }
    }

    private int id;

    private String number;
    private String description;

    public DBPhoneNumber(int id, String number, String description) {
        this.id = id;
        number = Validator.validate(number, PHONE_SIZE);
        description = Validator.validate(description, DESCRIPTION_SIZE);
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
        number = Validator.validate(number, PHONE_SIZE);
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
        description = Validator.validate(description, DESCRIPTION_SIZE);
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
