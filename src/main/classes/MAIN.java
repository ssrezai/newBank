package classes;

import java.sql.*;

/**
 * Created by DOTIN SCHOOL 3 on 2/22/2015.
 *
 */
public class MAIN {

    public static void main(String args[]) {
        Connection connection;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Registered!");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db?useUnicode=true&characterEncoding=UTF-8", "root", "12345");
            if (connection != null) {
                System.out.println("You made it, take control your database now!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("سلام");


    }
}
