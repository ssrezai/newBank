package C2;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by DOTIN SCHOOL 3 on 2/19/2015.
 */
public class ServletClass extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static final String HTML_START="<html><body>";
    public static final String HTML_END="</body></html>";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletClass() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException
    {
        if(request!=null) {
            RealCustomer customer= new RealCustomer();
           customer.setFirstName( request.getParameter("first_name"));
            customer.setLastName(request.getParameter("last_name"));
            customer.setFatherName(request.getParameter("father_name"));
            customer.setBirthDate(request.getParameter("birth_date"));
            customer.setNationalCode(request.getParameter("national_code"));
            System.out.println(customer.getFirstName()+" "+customer.getLastName());
            request.setAttribute("Done",new MessageFormat("done"));

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
        // TODO Auto-generated method stub
    }
}
