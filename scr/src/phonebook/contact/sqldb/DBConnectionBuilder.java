package src.phonebook.contact.sqldb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 */
public class DBConnectionBuilder {

    private static DataSource dataSource;

    static {
        try {
            Context initContext = new InitialContext();

            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/postgres");

        } catch(NamingException exc) {
            System.out.println("DataSource is not found. New Connection will be created");
            System.out.println();
            exc.printStackTrace();
        }

    }

    public static Connection getConnection() {
        if(dataSource != null)
            try {
                return dataSource.getConnection();
            } catch (SQLException exc) {
                System.out.println("DataSource is not found. New Connection will be created");
                System.out.println();
                exc.printStackTrace();
            }
        return createNewConnection();
    }

    private static Connection createNewConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/phonebook",
                    "phonebook_user", "phonebook");
            return connection;
        } catch (SQLException exc) {
            exc.printStackTrace();
        }
        return null;
    }
}
