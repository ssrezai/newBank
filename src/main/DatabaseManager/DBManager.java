package DatabaseManager;

import C2.Customer;
import C2.LegalCustomer;
import C2.RealCustomer;
import Exception.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/21/2015.
 *
 */
public class DBManager {
//    public static DBManager dbManager = new DBManager();
//
//    public static void setDbManager(DBManager dbManager) {
//        DBManager.dbManager = dbManager;
//    }
//
//    private DBManager() {
//    }
//
//    public static DBManager getDbManager() {
//        return dbManager;
//    }

    public static Connection makeConnection()  {
        Connection connection = null;
        boolean successful;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Registered!");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db?useUnicode=true&characterEncoding=UTF-8", "root", "12345");
            if (connection != null) {
                System.out.println("You made it, take control your database now!");
                return connection;

            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;

    }

    public static void insertToDataBase(Connection connection, Customer customer) throws DuplicateCustomerException {
        boolean successful;
        String customerType = customer.getClass().getName();
        if (customerType.contains("RealCustomer")) {
            RealCustomer realCustomer = (RealCustomer) customer;
            try {
                Boolean find = hasRecordInRealCustomerTable(connection, realCustomer.getNationalCode());
                if (find) {
                    successful = false;
                    throw new DuplicateCustomerException("");

                } else {
                    insertRealCustomer(connection, realCustomer);
                    successful = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (customerType.contains("LegalCustomer")) {
            LegalCustomer legalCustomer = (LegalCustomer) customer;
            try {
                Boolean find = hasRecordInLegalCustomerTable(connection, legalCustomer.getEconomicCode());
                if (find) {
                    successful = false;
                    throw new DuplicateCustomerException("");

                } else {
                    insertLegalCustomer(connection, legalCustomer);
                    successful = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void insertCustomer(Connection connection, int customerID) {
        String query = "INSERT INTO customer" + "(customerID) VALUES " +
                "(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
            System.out.println("Record is inserted into customer table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRealCustomer(Connection connection, RealCustomer realCustomer) {
        Boolean result = true;
        String firstName = realCustomer.getFirstName();
        String lastName = realCustomer.getLastName();
        String fatherName = realCustomer.getFatherName();
        String birthDate = realCustomer.getBirthDate();
        String nationalCode = realCustomer.getNationalCode();
        int customerID = returnNewCustomerID(connection);
        realCustomer.setCustomerID(String.valueOf(customerID));
        ///first add a new customer in customer table///
        insertCustomer(connection, customerID);

        ///make query..///
        String query = "INSERT INTO real_customer" + "(nationalCode,fk_customerID,firstName,lastName,fatherName,birthDate) VALUES " +
                "(?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nationalCode);
            preparedStatement.setInt(2, Integer.parseInt(realCustomer.getCustomerID()));
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, fatherName);
            preparedStatement.setString(6, birthDate);
            preparedStatement.executeUpdate();
            System.out.println("Record is inserted into real_customer table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    ///find a new Customer ID///
    public static int returnNewCustomerID(Connection connection) {
        int count = 0;
        int customerID = 1;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM customer");
            while (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            customerID = count + 1;
            System.out.println("new customer ID:" + customerID);
            return customerID;


        } catch (SQLException s) {
            System.out.println("SQL statement is not executed!");
        } finally {
            return customerID;
        }

    }

    public static void insertLegalCustomer(Connection connection, LegalCustomer legalCustomer) {
        Boolean result = true;
        String companyName = legalCustomer.getName();
        String registrationDate = legalCustomer.getRegistrationDate();
        String economicCode = legalCustomer.getEconomicCode();
        ///make a new Customer ID///
        int customerID = returnNewCustomerID(connection);
        insertCustomer(connection, customerID);
        legalCustomer.setCustomerID(String.valueOf(customerID));

        ///make query..///
        String query = "INSERT INTO legal_customer" + "(economicCode,fk_customerID,companyName,registrationDate) VALUES " +
                "(?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, economicCode);
            preparedStatement.setInt(2, Integer.parseInt(legalCustomer.customerID));
            preparedStatement.setString(3, companyName);
            preparedStatement.setString(4, registrationDate);
            preparedStatement.executeUpdate();
            System.out.println("Record is inserted into legal_customer table!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static boolean hasRecordInRealCustomerTable(Connection connection, String nationalCode) throws SQLException {
        String query = "Select 1 from real_customer where nationalCode = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nationalCode);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    private static boolean hasRecordInLegalCustomerTable(Connection connection, String economicCode) throws SQLException {
        String query = "Select 1 from legal_customer where economicCode = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, economicCode);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    public static ArrayList<RealCustomer> searchRealCustomer(String query) {
        ArrayList<RealCustomer> realCustomersResult = new ArrayList<RealCustomer>();
        Connection connection=makeConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RealCustomer realCustomer= new RealCustomer();
                realCustomer.setNationalCode(resultSet.getString("nationalCode"));
                realCustomer.setCustomerID(resultSet.getString("fk_customerID"));
                realCustomer.setFirstName(resultSet.getString("firstName"));
                realCustomer.setLastName(resultSet.getString("lastName"));
                realCustomer.setFatherName(resultSet.getString("fatherName"));
                realCustomer.setBirthDate(resultSet.getString("birthDate"));
                realCustomersResult.add(realCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return realCustomersResult;
    }

    public static ArrayList<LegalCustomer> searchLegalCustomer(String query) {
        ArrayList<LegalCustomer> legalCustomersResult = new ArrayList<LegalCustomer>();
        Connection connection=makeConnection();
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LegalCustomer legalCustomer= new LegalCustomer();
                legalCustomer.setRegistrationDate(resultSet.getString("registrationDate"));
                legalCustomer.setName(resultSet.getString("companyName"));
                legalCustomer.setCustomerID(resultSet.getString("fk_customerID"));
                legalCustomer.setEconomicCode(resultSet.getString("economicCode"));
                legalCustomersResult.add(legalCustomer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return legalCustomersResult;
    }
}