package logic;

import database.DBManager;
import org.apache.log4j.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by DOTIN SCHOOL 3 on 2/28/2015.
 *
 * @author samira rezaei
 */
public class LegalCustomerDAO {

    static final Logger logger = Logger.getLogger(DBManager.class);

    public static int insertLegalCustomer(LegalCustomer legalCustomer) throws DuplicateCustomerException {
        int result = -1;
        try {

            Boolean find = hasRecordInLegalCustomerTable( legalCustomer.getEconomicCode());
            if (find) {
                 logger.warn("DUPLICATE NATIONAL CODE...");
                result = -1;
                throw new DuplicateCustomerException("");

            } else {
                Connection connection = DBManager.makeConnection();
                result = DBManager.insertLegalCustomer(connection, legalCustomer);
                connection.close();
                 logger.info("SUCCESSFULLY ADD NEW REAL CUSTOMER...");
            }

              logger.info("...CLOSED CONNECTION TO DB...");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void deleteLegalCustomer(String customerId) {
        String query = "DELETE FROM legal_customer WHERE fk_customerID= ?";
        DBManager.deleteRecord(query, customerId);
    }

    public static boolean checkLegalCustomerModify(LegalCustomer legalCustomer) throws DuplicateCustomerException {
        if (checkEconomicCodeUpdating(legalCustomer)) {
            DBManager.updateRecord(legalCustomer);
            return true;
        } else {
            throw new DuplicateCustomerException("");
        }
    }

    public static boolean checkEconomicCodeUpdating(LegalCustomer legalCustomer) throws DuplicateCustomerException {
        boolean result = false;
        Connection connection = DBManager.makeConnection();
        try {
            boolean find = hasRecordInLegalCustomerTable(legalCustomer.getEconomicCode());
            if (find) {
                String query = "SELECT fk_customerID FROM legal_customer WHERE economicCode='" + legalCustomer.getEconomicCode() + "'";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery(query);
                while (resultSet.next()) {
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
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static boolean hasRecordInLegalCustomerTable(String economicCode) throws SQLException {
        Connection connection = DBManager.makeConnection();
        String query = "Select 1 from legal_customer where economicCode = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, economicCode);
        ResultSet rs = preparedStatement.executeQuery();
        boolean has = rs.next();
        connection.close();
        return has;
    }
}
