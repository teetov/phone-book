package src.phonebook.contact.sqldb;

import src.phonebook.contact.Contact;
import src.phonebook.contact.PhoneBook;
import src.phonebook.contact.PhoneNumber;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBPhoneBook implements PhoneBook {
    @Override
    public Contact getContact(int id) {
        Contact result = null;
        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement prep = connection.prepareStatement("SELECT * FROM notes WHERE id = ?;");
            prep.setInt(1, id);

            ResultSet rs =  prep.executeQuery();

            int contacId = -1;
            String contactName = null;
            String address = null;
            Calendar uploadDate = null;
            int defaultPhoneId = -1;

            if(rs.next()) {

                contacId = rs.getInt("id");
                contactName = rs.getString("name");
                address = rs.getString("address");
                Timestamp timestamp = rs.getTimestamp("date_of_creation");
                uploadDate = Calendar.getInstance();
                uploadDate.setTime(new Date(timestamp.getTime()));
                defaultPhoneId = rs.getInt("default_phone_id");

            } else {
                prep.close();
                rs.close();
                return null;
            }

            rs.close();
            prep.close();

            //Поиск и добавление связаных с контактом номеров
            prep = connection.prepareStatement("SELECT * FROM \"phoneNumbers\" WHERE \"noteId\" = ?;");
            prep.setInt(1, contacId);

            rs = prep.executeQuery();

            List<PhoneNumber> phList = new ArrayList<>();
            PhoneNumber defaultId = null;

            while(rs.next()) {
                PhoneNumber ph = new DBPhoneNumber(rs.getInt("id"), rs.getString("number"),
                        rs.getString("description"));
                if (ph.getId() == defaultPhoneId) {
                    defaultId = ph;
                }
                phList.add(ph);
            }

            rs.close();
            prep.close();

            result = new DBContact(id, contactName, address, uploadDate, defaultId, phList);


            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    //Метод ищет в базе данных совпадения указанного атрибута с именем контакта или одним из его номеров.
    // Поиск не затрагивает описание номера или адрес. Поиск по имени проводится без учёта регистра.
    public List<Contact> findContacts(String filter) {
        List<Contact> resultList = new ArrayList<>();

        String likePattern = "%" + filter + "%";

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT n.id id, n.\"name\" \"name\", n.address address, n.\"date_of_creation\"\n " +
                            "\"date_of_creation\", n.\"default_phone_id\" \"default_phone_id\",\n " +
                            "\"ph\".id \"phoneid\", ph.\"number\" \"number\", ph.description description\n " +
                            "FROM \"notes\" \"n\"\n " +
                            "LEFT JOIN \"phoneNumbers\" ph ON n.id = ph.\"noteId\"\n" +
                            "WHERE LOWER(\"name\") LIKE LOWER(?) OR \"number\" LIKE ? \n " +
                            "ORDER BY n.id, ph.id;");

            stmt.setString(1, likePattern);
            stmt.setString(2, likePattern);

            ResultSet rsContact  = stmt.executeQuery();

            resultList.addAll(createList(rsContact));

            rsContact.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    @Override
    public List<Contact> getContactList() {
        List<Contact> resultList = new ArrayList<>();

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            Statement stmt = connection.createStatement();

            ResultSet rsContact = stmt.executeQuery(
                    "SELECT n.id id, n.\"name\" \"name\", n.address address, n.\"date_of_creation\" " +
                            "\"date_of_creation\", n.default_phone_id default_phone_id, " +
                            "ph.id phoneid, ph.number number, ph.description description " +
                            "FROM \"notes\" \"n\" " +
                            "LEFT JOIN \"phoneNumbers\" ph ON n.id = ph.\"noteId\" " +
                            "ORDER BY n.id, ph.id;");

            resultList.addAll(createList(rsContact));

            rsContact.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    //Создаёт List контактов из базы данных на основе полученного resultSet. Перед пердачи в метод с resultSet
    // не рекомендуется проводить никаких манипуляций. Входные данные должны быть отсортированы на notes.id.
    //@param resultSet ResultSet должен содержать таблицу со следующими колонками:
    //      id(int), name (String), address (String), date_of_creation (Date), default_phone_id (int),
    //      phoneid (int), number (String), description (String)
    private List<Contact> createList(ResultSet resultSet) {

        List<Contact> resultList = new ArrayList<>();

        try {
            int contacId = -1;
            String contactName = null;
            String address = null;
            Calendar uploadDate = null;
            int defaultPhoneId = -1;

            List<PhoneNumber> phList = new ArrayList<>();
            PhoneNumber defaultPh = null;

            while(resultSet.next()) {
                //если значение отличается от предидущей строки
                if(contacId != resultSet.getInt("id"))
                {
                    //создать контакт из значение, полученных в предидущем цикле
                    if(!resultSet.isFirst())
                    {
                        Contact contact = new DBContact(contacId, contactName, address, uploadDate, defaultPh, phList);
                        resultList.add(contact);
                        defaultPh = null;
                        phList.clear();
                    }

                    contacId = resultSet.getInt("id");
                    contactName = resultSet.getString("name");
                    address = resultSet.getString("address");
                    uploadDate = Calendar.getInstance();
                    uploadDate.setTime(resultSet.getDate("date_of_creation"));
                    defaultPhoneId = resultSet.getInt("default_phone_id");
                }



                //если в данной строке присутствует информаия о номере, он будет добавлен в список текущего контакта
                if(resultSet.getInt("phoneid") > 0)
                {
                    PhoneNumber ph = new DBPhoneNumber(resultSet.getInt("phoneid"),
                            resultSet.getString("number"),
                            resultSet.getString("description"));

                    if (ph.getId() == defaultPhoneId) {
                        defaultPh = ph;
                    }
                    phList.add(ph);
                }

                //создаёт новый контакт если это последняя запись в ResultSet
                if(resultSet.isLast()) {
                    Contact contact = new DBContact(contacId, contactName, address, uploadDate, defaultPh, phList);
                    resultList.add(contact);
                    defaultPh = null;
                    phList.clear();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public boolean remove(int id) {
        boolean result = false;

        try(Connection connection = DBConnectionBuilder.getConnection()) {
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM notes WHERE id = ?");
            stmt.setInt(1, id);

            result = (stmt.executeUpdate() > 0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Contact createNewContact(String name, String address) {
        Contact result = null;

        name = Validator.validate(name, DBContact.NAME_SIZE);
        address = Validator.validate(address, DBContact.ADDRESS_SIZE);
       try(Connection connection = DBConnectionBuilder.getConnection()) {
           PreparedStatement stmt = connection.prepareStatement("INSERT INTO \"notes\" (\"name\", address, default_phone_id) VALUES (?, ?, ?)",
                   new String[] {"id", "date_of_creation"});

           stmt.setString(1, name);
           stmt.setString(2, address);
           stmt.setObject(3, null);

           stmt.execute();
           ResultSet resultSet = stmt.getGeneratedKeys();

           int id = 0;
           Calendar uploadDate = null;
           if(resultSet.next()) {
               id = resultSet.getInt("id");
               Timestamp timestamp = resultSet.getTimestamp("date_of_creation");
               uploadDate = Calendar.getInstance();
               uploadDate.setTime(new Date(timestamp.getTime()));
           }

           result = new DBContact(id, name, "", uploadDate);

       } catch (SQLException e) {
           e.printStackTrace();
       }

        return result;
    }
    @Override
    public Contact createNewContact(String name) {
        return createNewContact(name, "");
    }

}
