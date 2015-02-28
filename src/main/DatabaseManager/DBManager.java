package DatabaseManager;

import classes.Customer;
import classes.LegalCustomer;
import classes.RealCustomer;
import Exception.*;
import org.apache.log4j.*;


import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Samira Rezaei
 *         Created by DOTIN SCHOOL 3 on 2/21/2015.
 */
public class DBManager {
    static final Logger logger = Logger.getLogger(DBManager.class);

    public static Connection makeConnection() {

        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            logger.info("MySQL JDBC Driver Registered!");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db?characterEncoding=UTF-8", "root", "12345");
            if (connection != null) {
                logger.info("...SUCCESSFULLY CONNECTED to DB...");
                return connection;

            } else {
                logger.error("Failed to make connection!");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void insertToDataBase(Connection connection, Customer customer) throws DuplicateCustomerException {
        String customerType = customer.getClass().getName();
        if (customerType.contains("RealCustomer")) {
            RealCustomer realCustomer = (RealCustomer) customer;
            try {
                Boolean find = hasRecordInRealCustomerTable(connection, realCustomer.getNationalCode());
                if (find) {
                    logger.warn("DUPLICATE NATIONAL CODE...");
                    throw new DuplicateCustomerException("");

                } else {
                    insertRealCustomer(connection, realCustomer);
                    logger.info("SUCCESSFULLY ADD NEW REAL CUSTOMER...");
                }
                connection.close();
                logger.info("...CLOSED CONNECTION TO DB...");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (customerType.contains("LegalCustomer")) {
            LegalCustomer legalCustomer = (LegalCustomer) customer;
            try {
                Boolean find = hasRecordInLegalCustomerTable(connection, legalCustomer.getEconomicCode());
                if (find) {
                    logger.warn("DUPLICATE ECONOMIC CODE...");
                    throw new DuplicateCustomerException("");

                } else {
                    insertLegalCustomer(connection, legalCustomer);
                    logger.info("SUCCESSFULLY ADD NEW LEGAL CUSTOMER...");
                }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void insertCustomer(Connection connection, int customerID) {
        String query = "INSERT INTO customer (customerID) VALUES (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, customerID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertRealCustomer(Connection connection, RealCustomer realCustomer) {
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
            connection.close();
            logger.info("CONNECTION WAS CLOSED...");
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
            return customerID;
        } catch (SQLException s) {
            logger.error("SQL statement is not executed!");
        }
        return customerID;
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
            connection.close();
            logger.info("CONNECTION WAS CLOSED...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean hasRecordInRealCustomerTable(Connection connection, String nationalCode) throws SQLException {
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
        Connection connection = makeConnection();
        //String query=makeSelectQueryForRealCustomer(requestedRealCustomer);
        //System.out.println(query);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RealCustomer realCustomer = new RealCustomer();
                realCustomer.setNationalCode(resultSet.getString("nationalCode"));
                realCustomer.setCustomerID(resultSet.getString("fk_customerID"));
                realCustomer.setFirstName(resultSet.getString("firstName"));
                realCustomer.setLastName(resultSet.getString("lastName"));
                realCustomer.setFatherName(resultSet.getString("fatherName"));
                realCustomer.setBirthDate(resultSet.getString("birthDate"));
                realCustomersResult.add(realCustomer);
            }
            connection.close();
            logger.info("...CONNECTION WAS CLOSED...SEARCH COMPLETE...");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return realCustomersResult;
    }

    public static ArrayList<LegalCustomer> searchLegalCustomer(String query) {
        ArrayList<LegalCustomer> legalCustomersResult = new ArrayList<LegalCustomer>();
        Connection connection = makeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LegalCustomer legalCustomer = new LegalCustomer();
                legalCustomer.setRegistrationDate(resultSet.getString("registrationDate"));
                legalCustomer.setName(resultSet.getString("companyName"));
                legalCustomer.setCustomerID(resultSet.getString("fk_customerID"));
                legalCustomer.setEconomicCode(resultSet.getString("economicCode"));
                legalCustomersResult.add(legalCustomer);
            }
            connection.close();
            logger.info("...CONNECTION WAS CLOSED...SEARCH COMPLETE...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return legalCustomersResult;
    }

    public static void deleteRecord(String query, String customer_id) {
        Connection connection = makeConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(customer_id));
            preparedStatement.executeUpdate();
            String customerQuery = "DELETE FROM customer WHERE customerID = " + customer_id;
            Statement statement = connection.createStatement();
            statement.executeUpdate(customerQuery);
            connection.close();
            logger.info("...CONNECTION WAS CLOSED...DELETE 1 RECORD...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkNationalCodeUpdating(Connection connection, RealCustomer realCustomer) throws DuplicateCustomerException {
        boolean result = true;

        try {
            boolean find = hasRecordInRealCustomerTable(connection, realCustomer.getNationalCode());
            if (find) {
                String checkNCodeQuery = "SELECT fk_customerID FROM real_customer WHERE nationalCode=" + new BigInteger(realCustomer.getNationalCode());
                PreparedStatement statement = connection.prepareStatement(checkNCodeQuery);
                ResultSet resultSet = statement.executeQuery(checkNCodeQuery);
                while (resultSet.next()) {
                    if (realCustomer.getCustomerID().equals(resultSet.getString(1))) {
                        result = true;
                    } else {
                        result = false;
                        throw new DuplicateCustomerException("...Duplicate National Code Exception...");
                    }
                }
            } else {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static boolean checkEconomicCodeUpdating(Connection connection, LegalCustomer legalCustomer) throws DuplicateCustomerException {
        boolean result = true;

        try {
            boolean find = hasRecordInLegalCustomerTable(connection, legalCustomer.getEconomicCode());
            if (find) {
                String query = "SELECT fk_customerID FROM legal_customer WHERE economicCode='" + legalCustomer.getEconomicCode() + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery(query);
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1));

                    if (legalCustomer.getCustomerID().equals(resultSet.getString(1))) {
                        result = true;
                    } else {
                        result = false;
                        throw new DuplicateCustomerException("...Duplicate Economic Code Exception...");
                    }
                }
            } else {
                result = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static void updateRecord(Customer customer) throws DuplicateCustomerException {
        Connection connection = makeConnection();
        String customerType = customer.getClass().getName();
        if (customerType.contains("RealCustomer")) {
            RealCustomer realCustomer = (RealCustomer) customer;
            if (checkNationalCodeUpdating(connection, realCustomer)) {
                String query = "UPDATE real_customer SET nationalCode=?, firstName=? ,lastName=? , fatherName=?, birthDate=? WHERE fk_customerID = ?";
                try {

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, realCustomer.getNationalCode());
                    preparedStatement.setString(2, realCustomer.getFirstName());
                    preparedStatement.setString(3, realCustomer.getLastName());
                    preparedStatement.setString(4, realCustomer.getFatherName());
                    preparedStatement.setString(5, realCustomer.getBirthDate());
                    preparedStatement.setInt(6, Integer.parseInt(realCustomer.getCustomerID()));
                    preparedStatement.executeUpdate();
                    connection.close();
                    logger.info("...CONNECTION WAS CLOSED...UPDATE COMPLETE...");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        } else if (customerType.contains("LegalCustomer")) {
            LegalCustomer legalCustomer = (LegalCustomer) customer;
            if (checkEconomicCodeUpdating(connection, legalCustomer)) {
                String query = "UPDATE legal_customer SET economicCode=?, companyName=? ,registrationDate=?  WHERE fk_customerID=? ";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, legalCustomer.getEconomicCode());
                    preparedStatement.setString(2, legalCustomer.getName());
                    preparedStatement.setString(3, legalCustomer.getRegistrationDate());
                    preparedStatement.setInt(4, Integer.parseInt(legalCustomer.getCustomerID()));
                    preparedStatement.executeUpdate();
                    connection.close();
                    logger.info("...CONNECTION WAS CLOSED...UPDATE COMPLETE...");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static String makeSelectQueryForRealCustomer(RealCustomer realCustomer) {
//        String part1 = "SELECT ";
//        String part2 = " FROM real_customer WHERE ";
//
//        int count = 0;
//
//        if (realCustomer.getNationalCode().length() > 0) {
//            part1 = part1 + " nationalCode" ;
//            part2=part2+ " ?";
//            count++;
//        }
//        if (realCustomer.getCustomerID().length() > 0) {
//            if (count == 0) {
//                part1 = part1 + "fk_customerID" ;
//                part2=part2+ " ? ";
//
//            } else {
//                part1 = part1 + ",fk_customerID" ;
//                part2=part2+ ", ?";
//            }
//            count++;
//        }
//        if (realCustomer.getFirstName().length() > 0) {
//            if (count == 0) {
//                part1 = part1 + "firstName" ;
//                part2=part2+ " ? ";
//
//            } else {
//                part1 = part1 + ",firstName" ;
//                part2=part2+ ", ?";            }
//            count++;
//        }
//        if (realCustomer.getLastName().length() > 0) {
//            if (count == 0) {
//                part1 = part1 + "lastName" ;
//                part2=part2+ " ? ";
//            } else {
//                part1 = part1 + ",lastName" ;
//                part2=part2+ ", ?";            }
//        }
//        return part1+part2;
//    }


//    public static ArrayList<LegalCustomer> searchLegalCustomer2(RealCustomer realC) {
//        ArrayList<LegalCustomer> legalCustomersResult = new ArrayList<LegalCustomer>();
//        Connection connection = makeConnection();
//        String query=makeSelectQueryForRealCustomer(realC);
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement(query);
//            if(realC.getNationalCode().length()>0)
//            {
//                preparedStatement.setInt(1,Integer.parseInt(realC.getNationalCode()));
//            }
//            if(realC.get().length()>0)
//            {
//                preparedStatement.setInt(1,Integer.parseInt(realC.getNationalCode()));
//            }
//            if(realC.getNationalCode().length()>0)
//            {
//                preparedStatement.setInt(1,Integer.parseInt(realC.getNationalCode()));
//            }
//            if(realC.getNationalCode().length()>0)
//            {
//                preparedStatement.setInt(1,Integer.parseInt(realC.getNationalCode()));
//            }
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                LegalCustomer legalCustomer = new LegalCustomer();
//                legalCustomer.setRegistrationDate(resultSet.getString("registrationDate"));
//                legalCustomer.setName(resultSet.getString("companyName"));
//                legalCustomer.setCustomerID(resultSet.getString("fk_customerID"));
//                legalCustomer.setEconomicCode(resultSet.getString("economicCode"));
//                legalCustomersResult.add(legalCustomer);
//            }
//            connection.close();
//            logger.info("...CONNECTION WAS CLOSED...SEARCH COMPLETE...");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return legalCustomersResult;
//    }

}
