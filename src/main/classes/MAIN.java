package classes;

import DatabaseManager.DBManager;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by DOTIN SCHOOL 3 on 2/22/2015.
 */
public class MAIN {

    public static void main(String args[]) {
        Connection connection = null;
        boolean successful;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("MySQL JDBC Driver Registered!");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_db?useUnicode=true&characterEncoding=UTF-8", "root", "12345");
            if (connection != null) {
                System.out.println("You made it, take control your database now!");
            } else {
                System.out.println("Failed to make connection!");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        System.out.println("سلام");
//        DBManager. insertCustomer(connection, 1);
//        DBManager. insertCustomer(connection,3);

//       int id=DBManager.returnNewCustomerID(connection);
//        DBManager. insertCustomer(connection,id);
//        LegalCustomer legalCustomer= new LegalCustomer();
//        legalCustomer.setName("fanap2");
//        legalCustomer.setEconomicCode("1223");
//        legalCustomer.setCustomerID(id);
//        legalCustomer.setRegistrationDate("1352");
//        DBManager.insertLegalCustomer(connection,legalCustomer);
//        RealCustomer realCustomer = new RealCustomer();
//        int count = 0;
//
//        String url="";
//        if(url.length()==0)
//            System.out.println(url.length());
//
//
////        realCustomer.setCustomerID("20");
////
////        String query="SELECT nationalCode,fk_customerID,firstName,lastName,fatherName,birthDate FROM real_customer WHERE ";
////        if (realCustomer.getFirstName() != null) {
////            if (count == 0) {
////                query = query + " firstName='" + realCustomer.getFirstName()+"'";
////                count++;
////            } else {
////                query = query + " & firstName='" + realCustomer.getFirstName()+"'";
////            }
////        }
////        if (realCustomer.getLastName() != null) {
////            if (count == 0) {
////                query = query + " lastName='" + realCustomer.getLastName()+"'";
////                count++;
////            } else {
////                query = query + " & lastName='" + realCustomer.getLastName()+"'";
////            }
////        }
////        if (realCustomer.getNationalCode() != null) {
////            if (count == 0) {
////                query = query + " nationalCode='" + realCustomer.getNationalCode()+"'";
////                count++;
////            } else {
////                query = query + " & nationalCode='" + realCustomer.getNationalCode()+"'";
////            }
////        }
////        if (realCustomer.getCustomerID() != null) {
////            if (count == 0) {
////                query = query + " fk_customerID=" + realCustomer.getCustomerID();
////                count++;
////            } else {
////                query = query + " & fk_customerID='" + realCustomer.getCustomerID()+"'";
////            }
////        }
////       ArrayList<RealCustomer>realCustomers= DBManager.searchRealCustomer(query);
////        for(int i=0;i<realCustomers.size();i++)
////        {
////            System.out.println(realCustomers.get(i).getFatherName());
////        }
//
//        //System.out.println(realCustomer.getFatherName());
//
//
//        ArrayList<RealCustomer> realCustomers= new ArrayList<RealCustomer>();
//        RealCustomer r1= new RealCustomer();
//        RealCustomer r2= new RealCustomer();
//        LegalCustomer legalCustomer=new LegalCustomer();
//        legalCustomer.setName("LG");
//        legalCustomer.setCustomerID("27");
//        legalCustomer.setEconomicCode("225566");
//        legalCustomer.setRegistrationDate("1300");
//        DBManager.updateRecord(legalCustomer);
//
////        String query = "UPDATE legal_customer SET economicCode=?, companyName=? ,registrationDate=?  WHERE fk_customerID=? ";
////        try {
////            PreparedStatement preparedStatement = connection.prepareStatement(query);
////            preparedStatement.setString(1, "1122");
////            preparedStatement.setString(2, legalCustomer.getName());
////            preparedStatement.setString(3, "1369");
////            preparedStatement.setInt(4,Integer.parseInt(legalCustomer.getCustomerID()));
////            preparedStatement.executeUpdate();
////
////            preparedStatement.executeUpdate();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//
////        r1.setFirstName("ali");
////        r1.setLastName("mahmoodi");
////        r2.setFirstName("ahmad");
////        r2.setLastName("marani");
////        String query="UPDATE real_customer SET firstName=? WHERE fk_customerID=?";
////        try {
////            PreparedStatement preparedStatement=connection.prepareStatement(query);
////            preparedStatement.setString(1,"Samira");
////            preparedStatement.setInt(2,19);
////            preparedStatement.executeUpdate();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
////        realCustomers.add(r1);
////        realCustomers.add(r2);
////        SearchServletClass servletClass= new SearchServletClass();
////        System.out.println(servletClass.makeRealCustomerTable(realCustomers));
////        System.out.println("action= \"/hi\"" );



    }
}
