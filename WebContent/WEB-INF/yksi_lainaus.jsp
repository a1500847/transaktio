<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Tilaus</title>
</head>
<body>
<form action="NiteenLainausOhjelma" method="get">
<h1> LAINAUS</h1>
<p>LAINAUSNUMERO: <c:out value="${lainaus.numero}"/>&nbsp;&nbsp;&nbsp;
<c:out value="${lainaus.lainausPvm}"/>
</p>
<div>
Lainaaja: <c:out value="${lainaus.lainaaja.numero}"/> 
<c:out value="${lainaus.lainaaja.etunimi}"/>&nbsp;
<c:out value="${lainaus.lainaaja.sukunimi}"/> <br />
<c:out  value="${lainaus.lainaaja.osoite}"/><br />
<c:out value="${lainaus.lainaaja.posti.postinro}"/>&nbsp;
<c:out value="${lainaus.lainaaja.posti.postitmp}"/>
</div>
<p> </p>
<table>
<tr>
<td>isbn</td>
<td>nidenro</td>
<td>nimi</td>
<td>kirjoittaja</td>
<td>painos</td>
<td>kustantaja</td>
<td>palautuspvm </td>
</tr>

<c:forEach items="${lainaus.lista}" var="niteenlainaus">
	<tr>
 		<td><c:out value="${niteenlainaus.nide.kirja.isbn}"/>
		 </td>
 		<td>
 			&nbsp&nbsp&nbsp<c:out value="${niteenlainaus.nide.nidenro}"/>
 		</td>
 		<td><c:out value="${niteenlainaus.nide.kirja.nimi}"/>
		 </td>
		 <td><c:out value="${niteenlainaus.nide.kirja.kirjoittaja}"/>
		 </td>
		 <td>&nbsp&nbsp&nbsp<c:out value="${niteenlainaus.nide.kirja.painos}"/>
		 </td>
		 <td><c:out value="${niteenlainaus.nide.kirja.kustantaja}"/>
		 </td>
 		<td> 			<c:if test="${niteenlainaus.palautusPvm!=null}">
			 	<c:out value="${niteenlainaus.palautusPvm}"/>
			 </c:if>
		</td>
	</tr>
</c:forEach>
</table>
</form>
</body>
</html>