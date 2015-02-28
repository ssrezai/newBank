package classes;

import DatabaseManager.DBManager;
import org.apache.log4j.Logger;
import Exception.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by DOTIN SCHOOL 3 on 2/23/2015.
 *
 * @author samira rezaei
 *         this class will be handle request from client for update rocords...
 */
public class ModifyServletClass extends HttpServlet {
    final static Logger logger = Logger.getLogger(ModifyServletClass.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("delete") != null) {
            if (request.getParameter("type").equals("real")) {
                String query = "DELETE FROM real_customer WHERE fk_customerID= ?";
                DBManager.deleteRecord(query, request.getParameter("customer_id"));
                logger.info("Redirect: successful-remove.html");
                response.sendRedirect("successful-remove.html");
            } else if (request.getParameter("type").equals("legal")) {
                String query = "DELETE FROM legal_customer WHERE fk_customerID= ?";
                DBManager.deleteRecord(query, request.getParameter("customer_id"));
                logger.info("Redirect: successful-remove.html...");
                response.sendRedirect("successful-remove.html");
            }

        } else if (request.getParameter("modify") != null) {
            if (request.getParameter("type").equals("real")) {
                RealCustomer realCustomer = new RealCustomer();
                realCustomer.setFirstName(request.getParameter("first_name"));
                realCustomer.setLastName(request.getParameter("last_name"));
                realCustomer.setFatherName(request.getParameter("father_name"));
                realCustomer.setBirthDate(request.getParameter("birth_date"));
                realCustomer.setNationalCode(request.getParameter("national_code"));
                realCustomer.setCustomerID(request.getParameter("customer_id"));
                try {
                    DBManager.updateRecord(realCustomer);
                    logger.info("Redirect: successful-real-update.html...");
                    response.sendRedirect("successful-real-update.html");
                } catch (DuplicateCustomerException e) {
                    e.printStackTrace();
                    logger.warn("Duplicate National Code...");
                    response.sendRedirect("duplicate-real-customer.html");
                }

            } else if (request.getParameter("type").equals("legal")) {
                LegalCustomer legalCustomer = new LegalCustomer();
                legalCustomer.setEconomicCode(request.getParameter("economic_code"));
                legalCustomer.setRegistrationDate(request.getParameter("registration_date"));
                legalCustomer.setName(request.getParameter("company_name"));
                legalCustomer.setCustomerID(request.getParameter("customer_id"));

                try {
                    DBManager.updateRecord(legalCustomer);
                    logger.info("Redirect: successful-legal-update.html...");
                    response.sendRedirect("successful-legal-update.html");
                } catch (DuplicateCustomerException e) {
                    logger.warn("Duplicate Economic code...");
                    response.sendRedirect("duplicate-legal-customer.html");
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
