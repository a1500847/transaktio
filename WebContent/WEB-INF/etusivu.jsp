<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Lainausten selaus</title>
</head>
<body>
<h2>Lainausten selaus</h2>
<form action="LainausServlet" method="get">
<table>
<tr>
  <td>
  Valitse lainausnumero: <select name="lainausnumero">
  <c:forEach items="${lainausnumerot}" var="numero">
  <option><c:out value="${numero}"/></option>
  </c:forEach>
</select>
   </td>
   <td>&nbsp; &nbsp;  </td>
  <td>
 	<input type="submit" name="action" value="Hae lainaus" />
  </td>
  <td> &nbsp;&nbsp;&nbsp;</td>
  <td> 
 	<input type="submit" name="action" value="Hae kaikki lainaukset" />
  </td>
  <td> &nbsp;&nbsp;&nbsp;</td>
  <td> 
 	<input type="submit" name="action" value="Tee lainaus" />
  </td>
 </tr>
 </table></form>
</body>
</html>