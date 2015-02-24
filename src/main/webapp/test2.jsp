<%--
  Created by IntelliJ IDEA.
  User: DOTIN SCHOOL 3
  Date: 2/24/2015
  Time: 11:36 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>jsp</title>
</head>
<body>
<table style="border: 2px " >
    <tr>
        <form action="/ModifyServletClass" method="get">

            <td>
                <input type="text" value="salam" name="greating">
            </td>
            <td>
                <input type="text" value="row1" name="companyName">
            </td>

            <td>
                <input type="submit" value="بگرد">
            </td>

        </form>

    </tr>
    <p>

    </p>
    <tr>
        <form action="/ModifyServletClass" method="get">

            <td>
                <input type="text" value="salam" name="greating">
            </td>
            <td>
                <input type="text" value="row2" name="companyName">
            </td>

            <td>
                <input type="submit" value="click">
            </td>

        </form>

    </tr>
</table>
</body>
</html>
