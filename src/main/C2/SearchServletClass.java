package C2;

import DatabaseManager.DBManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/21/2015.
 */
public class SearchServletClass extends HttpServlet {

    public boolean checkFields(HttpServletRequest request) {
        String type = request.getParameter("type");
        boolean validation = true;
        if (type.equalsIgnoreCase("real")) {

            if ((request.getParameter("first_name")).length() == 0
                    || request.getParameter("last_name").length() == 0
                    || request.getParameter("customer_id").length() == 0
                    || request.getParameter("national_code").length() == 0) ;
            System.out.println("insufficient parameter for search...");
            validation = false;

        } else if (type.equalsIgnoreCase("legal")) {
            if ((request.getParameter("company_name")).length() == 0 || request.getParameter("economic_code").length() == 0
                    || request.getParameter("customer_id").length() == 0) ;
            System.out.println("insufficient parameter for search...");
            validation = false;
        }
        return validation;
    }

    public String makeQueryForRealCustomer(HttpServletRequest request) {
        String query = "SELECT nationalCode,fk_customerID,firstName,lastName,fatherName,birthDate FROM real_customer WHERE ";
        RealCustomer realCustomer = new RealCustomer();
        Customer customer = new Customer();

        if (request.getParameter("customer_id").length() > 0) {
            customer.setCustomerID((request.getParameter("customer_id")));
            realCustomer.setCustomerID(customer.getCustomerID());
        }
        if (request.getParameter("first_name").length() > 0) {
            realCustomer.setFirstName(request.getParameter("first_name"));
        }
        if (request.getParameter("last_name").length() > 0) {
            realCustomer.setLastName(request.getParameter("last_name"));
        }
        if (request.getParameter("national_code").length() > 0) {
            realCustomer.setNationalCode(request.getParameter("national_code"));
        }
        ///until now we have a real customer with some file
        // Connection connection = DBManager.makeConnection();
        int count = 0;

        if (realCustomer.getFirstName() != null) {
            if (count == 0) {
                query = query + " firstName='" + realCustomer.getFirstName() + "'";
                count++;
            } else {
                query = query + " & firstName='" + realCustomer.getFirstName() + "'";
            }
        }
        if (realCustomer.getLastName() != null) {
            if (count == 0) {
                query = query + " lastName='" + realCustomer.getLastName() + "'";
                count++;
            } else {
                query = query + " & lastName='" + realCustomer.getLastName() + "'";
            }
        }
        if (realCustomer.getNationalCode() != null) {
            if (count == 0) {
                query = query + " nationalCode='" + realCustomer.getNationalCode() + "'";
                count++;
            } else {
                query = query + " & nationalCode='" + realCustomer.getNationalCode() + "'";
            }
        }
        if (realCustomer.getCustomerID() != null) {
            if (count == 0) {
                query = query + " fk_customerID=" + Integer.parseInt(realCustomer.getCustomerID());
                count++;
            } else {
                query = query + " & fk_customerID='" + realCustomer.getCustomerID() + "'";
            }
        }
        return query;
    }

    public String makeQueryForLegalCustomer(HttpServletRequest request) {
        Customer customer = new Customer();
        LegalCustomer legalCustomer = new LegalCustomer();
        String query = "SELECT nationalCode,fk_customerID,firstName,lastName,fatherName,birthDate FROM real_customer WHERE ";

        if (request.getParameter("customer_ID").length() > 0) {
            customer.setCustomerID((request.getParameter("customer_ID")));
            legalCustomer.setCustomerID(customer.getCustomerID());
        }
        if (request.getParameter("economic_code").length() > 0) {
            legalCustomer.setEconomicCode(request.getParameter("economic_code"));
        }
        if (request.getParameter("company_name").length() > 0) {
            legalCustomer.setName(request.getParameter("company_name"));
        }


        int count = 0;

        if (legalCustomer.getName() != null) {
            if (count == 0) {
                query = query + " companyName='" + legalCustomer.getName() + "'";
                count++;
            } else {
                query = query + " & companyName='" + legalCustomer.getName() + "'";
            }
        }
        if (legalCustomer.getEconomicCode() != null) {
            if (count == 0) {
                query = query + " economicCode='" + legalCustomer.getEconomicCode() + "'";
                count++;
            } else {
                query = query + " & economicCode='" + legalCustomer.getEconomicCode() + "'";
            }
        }
        if (legalCustomer.getCustomerID() != null) {
            if (count == 0) {
                query = query + " fk_customerID='" + legalCustomer.getCustomerID() + "'";
                count++;
            } else {
                query = query + " & fk_customerID='" + legalCustomer.getCustomerID() + "'";
            }
        }

        return query;
    }

    public String makeResponse(ArrayList<String> arrayList)
    {
        String html="</head>\n" +
                "<body>\n" +
                "\n" +
                arrayList.get(1)+
                "</body>\n" +
                "</html>";

        return html;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ///first check that user write sth for search
        if (checkFields(request)) {
            ///start search process and send query to server...
            String type = request.getParameter("type");
            Customer customer = new Customer();
            if (type.equalsIgnoreCase("real")) {
                String query = makeQueryForRealCustomer(request);
                ArrayList<RealCustomer> realCustomerArrayList = DBManager.searchRealCustomer(query);
                response.setContentType("text/html");
                PrintWriter out = response.getWriter();
                 String docType ="<!DOCTYPE html>";
                ArrayList<String> st=new ArrayList<String>();
                st.add("a");
                st.add("b");
                st.add("c");
                out.println(docType+makeResponse(st));

            }
            ///search for legal customer...///
            else if (type.equalsIgnoreCase("legal")) {
                String query=makeQueryForLegalCustomer(request);
                ArrayList<LegalCustomer> legalCustomerArrayList= DBManager.searchLegalCustomer(query);

            }
        }
        ///should tell user :Fill in at least one field....
        else {
            response.sendRedirect("legal-customer.html");
        }

    }
}
