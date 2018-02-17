package src.phonebook.contact.sqldb;

import src.phonebook.contact.AbstractContact;
import src.phonebook.contact.PhoneNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBContact extends AbstractContact{

    protected static int NAME_SIZE;
    protected static int ADDRESS_SIZE;

    static {
        try (Connection connection = DBConnectionBuilder.getConnection()) {

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT \"name\", \"address\" FROM \"notes\";");

            NAME_SIZE = resultSet.getMetaData().getColumnDisplaySize(1);
            ADDRESS_SIZE = resultSet.getMetaData().getColumnDisplaySize(2);

            resultSet.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (NAME_SIZE == 0)
                NAME_SIZE = 255;
            if (ADDRESS_SIZE == 0)
                ADDRESS_SIZE = 255;
        }
    }

    private int id;

    private String name;
    private String address;
    private Calendar uploadDate;

    private PhoneNumber defaultNumber;

    List<PhoneNumber> numberList = new ArrayList<>();

    DBContact(int id, String name, String address, Calendar uploadDate) {
        this.id = id;
        name = Validator.validate(name, NAME_SIZE);
        address = Validator.validate(address, ADDRESS_SIZE);
        this.name = name;
        this.address = address;
        this.uploadDate = uploadDate;
    }

    DBContact(int id, String name, String address, Calendar uploadDate,
              PhoneNumber defaultNumber, List<PhoneNumber> numberList) {
        this(id, name, address, uploadDate);
        this.defaultNumber = defaultNumber;
        this.numberList.addAll(numberList);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public List<PhoneNumber> getNumbers() {
        return numberList;
    }


    @Override
    public PhoneNumber addNumber(String phoneNumber, String description) {
        phoneNumber = Validator.validate(phoneNumber, DBPhoneNumber.PHONE_SIZE);
        description = Validator.validate(description, DBPhoneNumber.DESCRIPTION_SIZE);

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(
                    "INSERT INTO \"phoneNumbers\" (\"noteId\", number, description) " +
                    "VALUES (?, ?, ?);",
                    new String[] {"id"});

            prep.setInt(1, getId());
            prep.setString(2, phoneNumber);
            prep.setString(3, description);

            prep.executeUpdate();

            ResultSet resultSet = prep.getGeneratedKeys();

            int phNumbId = -1;
            if(resultSet.next()) {
                phNumbId = resultSet.getInt(1);
            }

            PhoneNumber ph = new DBPhoneNumber(phNumbId, phoneNumber, description);

            numberList.add(ph);

            if(defaultNumber == null)
                setDefaultNumber(ph);

            resultSet.close();
            prep.close();

            return ph;

        } catch(SQLException exc) {
            exc.printStackTrace();
        }
        return null;
    }

    @Override
    public PhoneNumber getNumber(String number) {
        String match = ".*" + number + ".*";
        for(PhoneNumber ph : numberList) {
            if(ph.getNumber().matches(match))
                return ph;
        }
        return null;
    }

    @Override
    public PhoneNumber detNumberByDescription(String description) {
        String match = ".*" + description + ".*";
        for(PhoneNumber ph : numberList) {
            if(ph.getDescription().matches(match))
                return ph;
        }
        return null;
    }

    @Override
    public PhoneNumber getNumber(int id) {
        for(PhoneNumber ph : numberList) {
            if(ph.getId() == id)
                return ph;
        }
        return null;
    }

    @Override
    public Calendar getUploadDate() {
        return uploadDate;
    }

    @Override
    public void setName(String name) {
        name = Validator.validate(name, NAME_SIZE);
        this.name = name;
        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(
                    "UPDATE \"notes\" SET \"name\" = ? WHERE id = ? ");

            prep.setString(1, name);
            prep.setInt(2, getId());

            prep.executeUpdate();

            prep.close();

        } catch(SQLException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public void setAddress(String address) {
        address = Validator.validate(address, ADDRESS_SIZE);
        this.address = address;

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement(
                    "UPDATE \"notes\" SET \"address\" = ? WHERE id = ?");

            prep.setString(1, address);
            prep.setInt(2, getId());

            prep.executeUpdate();

            prep.close();

        } catch(SQLException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public PhoneNumber getDefaultNumber() {
        if(defaultNumber == null && numberList.size() > 0) {
            setDefaultNumber(numberList.get(0));
        }
        return defaultNumber;
    }


    @Override
    public void setDefaultNumber(PhoneNumber phoneNumber) {
        if(!numberList.contains(phoneNumber))
            return;

        defaultNumber = phoneNumber;

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement("UPDATE \"notes\" SET \"default_phone_id\" = ? " +
                    "WHERE id = ?;");

            prep.setInt(1, phoneNumber.getId());
            prep.setInt(2, getId());

            prep.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void removeNumber(PhoneNumber phoneNumber) {
        numberList.remove(phoneNumber);

        if(phoneNumber == defaultNumber) {
            if(numberList.size() > 0)
                setDefaultNumber(numberList.get(0));
            else
                defaultNumber = null;
        }

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement("DELETE FROM \"phoneNumbers\" WHERE id = ?;");

            prep.setInt(1, phoneNumber.getId());

            prep.executeUpdate();
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
    }
}
