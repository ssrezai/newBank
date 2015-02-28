package classes;

import DatabaseManager.DBManager;
import logic.*;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;


/**
 * Created by DOTIN SCHOOL 3 on 2/19/2015.
 *
 * @author Samira Rezaei
 */
public class InsertServletClass extends HttpServlet {
    private static final long serialVersionUID = 1L;
    static final Logger logger = Logger.getLogger(DBManager.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertServletClass() {
        super();

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public String checkParameter(HttpServletRequest request) throws IOException {
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


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request != null) {
            BasicConfigurator.configure();
            request.setCharacterEncoding("UTF-8");

            String url = RealCustomerManager.checkParameter(request);
            if (url.length() != 0) {
                logger.warn("Insufficient parameter for submit new customer...");
                response.sendRedirect(url);

            } else {
                Connection connection = DBManager.makeConnection();
                String type = request.getParameter("type");
                if (type.equalsIgnoreCase("real")) {
                    RealCustomer realCustomer = new RealCustomer();
                    realCustomer.setFirstName(request.getParameter("first_name"));
                    realCustomer.setLastName(request.getParameter("last_name"));
                    realCustomer.setFatherName(request.getParameter("father_name"));
                    String birthDate = request.getParameter("year") + "/" + request.getParameter("month") + "/" + request.getParameter("day");
                    realCustomer.setBirthDate(birthDate);
                    realCustomer.setNationalCode(request.getParameter("national_code"));

                    try {
                        int result = RealCustomerManager.insertRealCustomer(realCustomer);
                        if (result != -1) {
                            response.sendRedirect("successful-real-insertion.html");
                            logger.info("Redirect: successful-real-insertion.html...");
                        }

                    } catch (logic.DuplicateCustomerException e) {
                        response.sendRedirect("duplicate-real-customer.html");
                        logger.info("Redirect: duplicate-real-customer.html...");
                    }


                } else if (type.equalsIgnoreCase("legal")) {
                    LegalCustomer legalCustomer = new LegalCustomer();
                    legalCustomer.setEconomicCode(request.getParameter("economic_code"));
                    legalCustomer.setName(request.getParameter("company_name"));
                    String registrationDate = request.getParameter("year") + "/" + request.getParameter("month") + "/" + request.getParameter("day");
                    legalCustomer.setRegistrationDate(registrationDate);

                    try {
                        int result = LegalCustomerManager.insertLegalCustomer(legalCustomer);
                        if (result != -1) {
                            response.sendRedirect("successful-legal-insertion.html");
                            logger.info("Redirect: successful-legal-insertion.html...");
                        }

                    } catch (logic.DuplicateCustomerException e) {
                        response.sendRedirect("duplicate-legal-customer.html");
                        logger.info("Redirect: duplicate-legal-customer.html...");
                    }

                }
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}

