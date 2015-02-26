package classes;

import DatabaseManager.DBManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.log4j.*;

/**
 * Created by DOTIN SCHOOL 3 on 2/21/2015.
 *
 * @author SamiraRezaei
 */
public class SearchServletClass extends HttpServlet {
    static Logger logger = Logger.getLogger(SearchServletClass.class);

    public boolean checkFields(HttpServletRequest request) {
        String type = request.getParameter("type");
        boolean validation = true;
        if (type.equalsIgnoreCase("real")) {
            if ((request.getParameter("first_name")).length() == 0 && request.getParameter("last_name").length() == 0
                    && request.getParameter("customer_id").length() == 0 && request.getParameter("national_code").length() == 0) {
                logger.info("Insufficient parameter for search...real customer");
                validation = false;
            }

        } else if (type.equalsIgnoreCase("legal")) {
            if ((request.getParameter("company_name")).length() == 0 && request.getParameter("economic_code").length() == 0
                    && request.getParameter("customer_ID").length() == 0) {
                logger.info("Insufficient parameter for search...legal customer");
                validation = false;
            }
        }
        return validation;
    }

    public String makeSelectQueryForRealCustomer(HttpServletRequest request) {
        String query = "SELECT nationalCode,fk_customerID,firstName,lastName,fatherName,birthDate FROM real_customer WHERE ";
        int count = 0;

        if (request.getParameter("customer_id").length() > 0) {
            query = query + " fk_customerID=" + Integer.parseInt(request.getParameter("customer_id"));
            count++;
        }
        if (request.getParameter("national_code").length() > 0) {
            if (count == 0) {
                query = query + " nationalCode='" + request.getParameter("national_code") + "'";

            } else {
                query = query + " AND nationalCode='" + request.getParameter("national_code") + "'";
            }
            count++;
        }
        if (request.getParameter("first_name").length() > 0) {
            if (count == 0) {
                query = query + " firstName='" + request.getParameter("first_name") + "'";

            } else {
                query = query + " AND firstName='" + request.getParameter("first_name") + "'";
            }
            count++;
        }
        if (request.getParameter("last_name").length() > 0) {
            if (count == 0) {
                query = query + " lastName='" + request.getParameter("last_name") + "'";

            } else {
                query = query + " AND lastName='" + request.getParameter("last_name") + "'";
            }
        }
        return query;
    }

    public String makeSelectQueryForLegalCustomer(HttpServletRequest request) {
        String query = "SELECT economicCode,fk_customerID,companyName,registrationDate FROM legal_customer WHERE ";
        int count = 0;

        if (request.getParameter("customer_ID").length() > 0) {
            query = query + " fk_customerID='" + request.getParameter("customer_ID") + "'";
            count++;
        }
        if (request.getParameter("economic_code").length() > 0) {
            if (count == 0) {
                query = query + " economicCode='" + request.getParameter("economic_code") + "'";

            } else {
                query = query + " AND economicCode='" + request.getParameter("economic_code") + "'";
            }
            count++;
        }
        if (request.getParameter("company_name").length() > 0) {
            if (count == 0) {
                query = query + " companyName='" + request.getParameter("company_name") + "'";
            } else {
                query = query + " AND companyName='" + request.getParameter("company_name") + "'";
            }

        }

        return query;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request != null) {
            request.setCharacterEncoding("UTF-8");
            ///first check that user write sth for search///
            String type = request.getParameter("type");
            if (checkFields(request)) {
                ///start search process and send query to server...///
                if (type.equalsIgnoreCase("real")) {
                    String query = makeSelectQueryForRealCustomer(request);
                    logger.info("Make query: " + query);
                    logger.info("Search query in DataBase...");
                    ArrayList<RealCustomer> realCustomerArrayList = DBManager.searchRealCustomer(query);
                    response.setContentType("text/html; charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(makeRealCustomerTable(realCustomerArrayList));
                    logger.info("Render search result...");
                }
                ///search for legal customer...///
                else if (type.equalsIgnoreCase("legal")) {
                    String query = makeSelectQueryForLegalCustomer(request);
                    logger.info("Make query: " + query);
                    logger.info("Search query in DataBase...");
                    ArrayList<LegalCustomer> legalCustomerArrayList = DBManager.searchLegalCustomer(query);
                    response.setContentType("text/html; charset=UTF-8");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter printWriter = response.getWriter();
                    printWriter.println(makeLegalCustomerTable(legalCustomerArrayList));
                    logger.info("Render search result...");
                }
            }
            ///should tell user :Fill in at least one field....///
            else {
                if (type.equalsIgnoreCase("real")) {
                    response.sendRedirect("new-real-customer.html");
                } else {
                    response.sendRedirect("legal-customer.html");
                }
            }
        }


    }

    public String makeRealCustomerTable(ArrayList<RealCustomer> realCustomers) {
        String tableRows = "";
        int count = realCustomers.size();
        String deleteButton = "<input type=\"submit\" name=\"delete\"  value=\"delete\">\n";
        String modifyButton = "<input type=\"submit\" value=\"modify\" name=\"modify\">\n";

        String tableHeader = "<tr>\n" +
                "    <th>شماره حساب</th>\n" +
                "    <th>نام</th>\t\t\n" +
                "    <th>نام خانوادگی</th>\n" +
                "    <th>نام پدر</th>\n" +
                "    <th>کد ملی</th>\t\t\n" +
                "    <th>تاریخ تولد</th>\n" +
                "     <th>حذف</th>\n" +
                "     <th>بروزرسانی</th>\n" +
                "     </tr>\n";
        for (RealCustomer realCustomer : realCustomers) {
            String firstName = fillTableRow(realCustomer.getFirstName());
            String lastName = fillTableRow(realCustomer.getLastName());
            String fatherName = fillTableRow(realCustomer.getFatherName());
            String birthDate = fillTableRow(realCustomer.getBirthDate());
            String nationalCode = fillTableRow(realCustomer.getNationalCode());
            String accountNumber = fillTableRow(realCustomer.getCustomerID());
            tableRows = tableRows +
                    "<tr> " +
                    "<form action= \"/ModifyServletClass\" method=\"post\">\n" +
                    "<input type=\"hidden\" name=\"type\" value=\"real\">" +
                    "<td>" + accountNumber + "</td>\n" + "<input type=\"hidden\" name=\"customer_id\" value=" + accountNumber + ">" +
                    "<td> <input type=\"text\" name=\"first_name\" value=\"" + firstName + "\">" + "</td>\n" +
                    "<td> <input type=\"text\" name=\"last_name\" value=\"" + lastName + "\">" + "</td>\n" +
                    "<td><input type=\"text\" name=\"father_name\" value=\"" + fatherName + "\">" + "</td>\n" +
                    "<td><input type=\"text\" name=\"national_code\" value=\"" + nationalCode + "\">" + "</td>\n" +
                    "<td><input type=\"text\" name=\"birth_date\" value=\"" + birthDate + "\">" + "</td>\n" +
                    "<td>" + deleteButton + "</td>\n" +
                    "<td>" + modifyButton + "</td>\n" +
                    " </form>"
                    + "</tr>\n";
        }
        String htmlPart1 = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "<title> نتایج جستجو</title>" +
                "<style>\n" +
                "body,table,form {\n" +
                "background-color: #eeffee;\n" +
                "align-content: center;\n" +
                "margin-left: auto;\n" +
                " margin-right: auto;\n" +
                "        }" +
                " div {\n" +
                " margin-top: 10px;\n" +
                "margin-right: 70px;\n" +
                " margin-left: 70px;\n" +
                "text-align: center;\n" +
                "}" +
                "table, th, td {\n" +
                "    border: 1px solid black;\n" +
                "    border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n" +
                "    padding: 5px;\n" +
                "}\n" +
                "</style>" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                "</head>\n" +
                "\n" +
                "<body>\n";

        String tableTag = "<table>\n" + tableHeader + tableRows + "</table>";
        String htmlPart2 = "<div>\n" +
                "  <p>\n" +
                "  #of search result:" + count + "\n" +
                "  </p>\n" +
                "  <p>\n" +
                "   <a href=\"new-real-customer.html\">\n" +
                "            بازگشت به صفحه ی قبل\n" +
                "  </a>\n" +
                "  </p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>";

        return htmlPart1 + tableTag + htmlPart2;

    }


    public String makeLegalCustomerTable(ArrayList<LegalCustomer> legalCustomers) {
        String tableRows = "";
        int count = legalCustomers.size();
        String deleteButton = "<input type=\"submit\" name=\"delete\"  value=\"delete\">\n";
        String modifyButton = "<input type=\"submit\" value=\"modify\" name=\"modify\">\n";

        String tableHeader = "<tr>\n" +
                "    <th> شماره حساب</th>\n" +
                "    <th>نام شرکت</th>\t\t\n" +
                "    <th>تاریخ ثبت</th>\n" +
                "    <th>کد اقتصادی</th>\n" +
                "     <th>حذف</th>\n" +
                "     <th>بروزرسانی</th>\n" +
                "     </tr>\n";
        for (LegalCustomer legalCustomer : legalCustomers) {
            String companyName = fillTableRow(legalCustomer.getName());
            String registrationDate = fillTableRow(legalCustomer.getRegistrationDate());
            String economicCode = fillTableRow(legalCustomer.getEconomicCode());
            String accountNumber = fillTableRow(legalCustomer.getCustomerID());

            tableRows = tableRows +
                    "<tr> " +
                    "<form action= \"/ModifyServletClass\" method=\"post\">\n" +
                    "<input type=\"hidden\" name=\"type\" value=\"legal\">" +
                    "<td>" + accountNumber + "</td>\n" + "<input type=\"hidden\" name=\"customer_id\" value=" + accountNumber + ">" +
                    "<td>  <input type=\"text\" name=\"company_name\" value=\"" + companyName + "\">" + "</td>\n" +
                    "<td> <input type=\"text\" name=\"registration_date\" value=\"" + registrationDate + "\">" + "</td>\n" +
                    "<td> <input type=\"text\" name=\"economic_code\" value=\"" + economicCode + "\">" + "</td>\n" +
                    "<td> " + deleteButton + "</td>\n" +
                    "<td>" + modifyButton + "</td>\n" +
                    " </form>"
                    + "</tr>\n";

        }
        String htmlPart1 = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "\n" +
                "<head>\n" +
                "<title> نتایج جستجو</title>" +
                "<style>\n" +
                "body,table,form {\n" +
                "background-color: #eeffee;\n" +
                "align-content: center;\n" +
                "margin-left: auto;\n" +
                " margin-right: auto;\n" +
                "}" +
                " div {\n" +
                " margin-top: 10px;\n" +
                "margin-right: 70px;\n" +
                " margin-left: 70px;\n" +
                "text-align: center;\n" +
                "}" +
                "table, th, td {\n" +
                "    border: 1px solid black;\n" +
                "    border-collapse: collapse;\n" +
                "}\n" +
                "th, td {\n" +
                "    padding: 5px;\n" +
                "}\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">" +
                "</style>" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #eeffee\">\n";

        String tableTag = "<table>\n" + tableHeader + tableRows + "</table>";
        String htmlPart2 = "<div>\n" +
                "    <p>\n" +
                " #of search result:" + count + "\n" +
                "    </p>\n" +
                "    <p>\n" +
                "        <a href=\"legal-customer.html\">\n" +
                "            بازگشت به صفحه ی قبل\n" +
                "        </a>\n" +
                "    </p>\n" +
                "</div>" +
                "</body>\n" +
                "</html>";

        return htmlPart1 + tableTag + htmlPart2;

    }

    ///set "-not set-" for null value in DB...
    public String fillTableRow(String rowValue) {
        AtomicReference<String> finalRowValue = new AtomicReference<String>(" ");
        if (rowValue == null) {
            finalRowValue.set("-Not Set-");
            return finalRowValue.get();
        } else
            return rowValue;
    }
}
