package C2;

import Exception.*;
import DatabaseManager.DBManager;

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

    //private String message;

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
                    || request.getParameter("father_name").length() == 0 || request.getParameter("birth_date").length() == 0
                    || request.getParameter("national_code").length() == 0) {
                url = "real-incomplete.html";
                System.out.println("repeat...");
            }


        } else if (type.equalsIgnoreCase("legal")) {
            if ((request.getParameter("company_name")).length() == 0 || request.getParameter("economic_code").length() == 0
                    || request.getParameter("registration_date").length() == 0) {
                url = "legal-incomplete.html";
                System.out.println("repeat legal...");
            }

        }
        System.out.println(url);
        return url;
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //boolean successful;
        if (request != null) {
            String url = checkParameter(request);

            System.out.println(url);
            if (url.length() != 0) {
               // message = "اطلاعات را به طور کامل پر نمایید.";
                // System.out.println(message);
                response.sendRedirect(url);

            } else {
                Connection connection = DBManager.makeConnection();
                String type = request.getParameter("type");
                if (type.equalsIgnoreCase("real")) {
                    RealCustomer realCustomer = new RealCustomer();
                    realCustomer.setFirstName(request.getParameter("first_name"));
                    realCustomer.setLastName(request.getParameter("last_name"));
                    realCustomer.setFatherName(request.getParameter("father_name"));
                    realCustomer.setBirthDate(request.getParameter("birth_date"));
                    realCustomer.setNationalCode(request.getParameter("national_code"));
                    System.out.println(realCustomer.getFirstName() + " " + realCustomer.getLastName() + " is a " + type + " customer");
                    System.out.println(request.getParameter("type"));
                    try {
                        DBManager.insertToDataBase(connection, realCustomer);
                        response.sendRedirect("successful-real-insertion.html");
                    } catch (DuplicateCustomerException e) {
                      //  successful = false;
                     //   message = "شماره شناسنامه تکراری است.";
                        response.sendRedirect("duplicate-real-customer.html");
                        System.out.println("Duplicate real user");
                    }

                } else if (type.equalsIgnoreCase("legal")) {
                    System.out.println(request.getParameter("type"));
                    LegalCustomer legalCustomer = new LegalCustomer();
                    legalCustomer.setEconomicCode(request.getParameter("economic_code"));
                    legalCustomer.setName(request.getParameter("company_name"));
                    legalCustomer.setRegistrationDate(request.getParameter("registration_date"));
                    System.out.println(legalCustomer.getName() + " is a " + type + " customer");
                    try {
                        DBManager.insertToDataBase(connection, legalCustomer);
                        response.sendRedirect("successful-real-insertion.html");
                    } catch (DuplicateCustomerException e) {
                       // successful = false;
                       // message = "کد اقتصادی تکراری است.";
                        System.out.println("Duplicate legal user");
                        response.sendRedirect("duplicate-legal-customer.html");
                    }
                }
            }



        }

        // Set response content type
        // response.sendRedirect("real-customer.html");
        //  request.getRequestDispatcher("real-customer.html").forward(request,response);
//        response.setContentType("text/html");
//
//        PrintWriter out = response.getWriter();
//        String title = "Using GET Method to Read Form Data";
//        String docType =
//                "<!doctype html public \"-//w3c//dtd html 4.0 " +
//                        "transitional//en\">\n";
//        out.println(docType +
//                "<html>\n" +
//                "<head><title>" + title + "</title></head>\n" +
//                "<body bgcolor=\"#f0f0f0\">\n" +
//                "<h1 align=\"center\">" + title + "</h1>\n" +
//                "<ul>\n" +
//                "  <li><b>First Name</b>: "
//                + request.getParameter("first_name") + "\n" +
//                "  <li><b>Last Name</b>: "
//                + request.getParameter("last_name") + "\n" +
//                "</ul>\n" +
//                "</body></html>");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        System.out.println("سلام");
//        System.out.println("455");

    }
}

