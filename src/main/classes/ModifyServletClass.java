package classes;

import DatabaseManager.DBManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by DOTIN SCHOOL 3 on 2/23/2015.
 *
 */
public class ModifyServletClass extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (request.getParameter("delete") != null) {
            if(request.getParameter("type").equals("real")) {

              //  String query = "DELETE FROM real_customer WHERE fk_customerID="+request.getParameter("customer_id");
                String query = "DELETE FROM real_customer WHERE fk_customerID= ?";
                System.out.println(query);
                DBManager.deleteRecord(query,request.getParameter("customer_id"));
                response.sendRedirect("successful-remove.html");
            }
            else if(request.getParameter("type").equals("legal"))
            {
                String query = "DELETE FROM legal_customer WHERE fk_customerID= ?";
                System.out.println(query);
                DBManager.deleteRecord(query,request.getParameter("customer_id"));

                System.out.println(request.getParameter("company_name"));
                System.out.println(request.getParameter("registration_date"));
                System.out.println(request.getParameter("economic_code"));
                System.out.println(request.getParameter("customer_id"));

                response.sendRedirect("successful-remove.html");



            }

        } else if(request.getParameter("modify")!=null) {
            if(request.getParameter("type").equals("real"))
            {
                RealCustomer realCustomer= new RealCustomer();
                realCustomer.setFirstName(request.getParameter("first_name"));
                realCustomer.setLastName(request.getParameter("last_name"));
                realCustomer.setFatherName(request.getParameter("father_name"));
                realCustomer.setBirthDate(request.getParameter("birth_date"));
                realCustomer.setNationalCode(request.getParameter("national_code"));
                realCustomer.setCustomerID(request.getParameter("customer_id"));
                System.out.println("ModifyServlet.."+realCustomer.getFirstName()+" "+realCustomer.getLastName()+" "+realCustomer.getCustomerID()+" "+realCustomer.getNationalCode());
                DBManager.updateRecord(realCustomer);
                response.sendRedirect("successful-real-update.html");
            }
            else if(request.getParameter("type").equals("legal"))
            {
                LegalCustomer legalCustomer= new LegalCustomer();
                legalCustomer.setEconomicCode(request.getParameter("economic_code"));
                legalCustomer.setRegistrationDate(request.getParameter("registration_date"));
                legalCustomer.setName(request.getParameter("company_name"));
                legalCustomer.setCustomerID(request.getParameter("customer_id"));
                System.out.println("ModifyServlet.."+legalCustomer.getName()+" "+legalCustomer.getEconomicCode()+" "+legalCustomer.getCustomerID()+" "+legalCustomer.getRegistrationDate());

                DBManager.updateRecord(legalCustomer);
                response.sendRedirect("successful-legal-update.html");
            }

        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        System.out.println(request.getParameter("companyName"));

    }
}
