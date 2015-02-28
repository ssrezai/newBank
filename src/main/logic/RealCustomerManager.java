package logic;

import DatabaseManager.DBManager;
import classes.RealCustomer;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by DOTIN SCHOOL 3 on 2/28/2015.
 * @author Samira Rezaei
 */
public class RealCustomerManager {
    static final Logger logger = Logger.getLogger(DBManager.class);

    public static String checkParameter(HttpServletRequest request) throws IOException {
        String type = request.getParameter("type");

        String url = "";
        if (type.equalsIgnoreCase("real")) {

            if ((request.getParameter("first_name")).length() == 0 || request.getParameter("last_name").length() == 0
                    || request.getParameter("father_name").length() == 0 || request.getParameter("national_code").length() == 0) {
                url = "real-incomplete.html";
            }


        } else if (type.equalsIgnoreCase("legal")) {
            if ((request.getParameter("company_name")).length() == 0 || request.getParameter("economic_code").length() == 0) {
                url = "legal-incomplete.html";
            }
        }
        return url;
    }

    public static int insertRealCustomer(RealCustomer realCustomer) throws DuplicateCustomerException {
        int result = -1;
        try {

            Boolean find = hasRecordInRealCustomerTable(realCustomer.getNationalCode());
            if (find) {
                logger.warn("DUPLICATE NATIONAL CODE...");
                result = -1;
                throw new DuplicateCustomerException("");

            } else {
                Connection connection = DBManager.makeConnection();
                result = DBManager.insertRealCustomer(connection, realCustomer);
                connection.close();
                  logger.info("SUCCESSFULLY ADD NEW REAL CUSTOMER...");
            }

              logger.info("...CLOSED CONNECTION TO DB...");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    public static void deleteRealCustomer(String customerId) {
        String query = "DELETE FROM real_customer WHERE fk_customerID= ?";
        DBManager.deleteRecord(query, customerId);
    }

    public static boolean checkNationalCode(RealCustomer realCustomer) throws DuplicateCustomerException {
        boolean result = false;
        Connection connection = DBManager.makeConnection();
        try {
            boolean find = hasRecordInRealCustomerTable(realCustomer.getNationalCode());
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
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static boolean checkRealCustomerModify(RealCustomer realCustomer) throws DuplicateCustomerException {
        if(checkNationalCode(realCustomer))
        {
           DBManager.updateRecord(realCustomer);
            return true;
        }
        else
        {
            throw new DuplicateCustomerException("");
        }
    }

    public static boolean hasRecordInRealCustomerTable(String nationalCode) throws SQLException {
        Connection connection = DBManager.makeConnection();
        String query = "Select 1 from real_customer where nationalCode = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, nationalCode);
        ResultSet rs = preparedStatement.executeQuery();

        boolean has= rs.next();
        connection.close();
        return has;
    }
}
