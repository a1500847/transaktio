<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:f="http://java.sun.com/jsf/core" xmlns:h="http://java.sun.com/jsf/html">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Lainauksen varmistus</title>
</head>
<body>
<h1>LAINAUS&nbsp;&nbsp;<c:out value="${lainaus.lainausPvm}" />
</h1>
<form action="NiteenLainausOhjelma" method="get" >
lainaaja: <c:out value="${lainaus.lainaaja.numero}"/>&nbsp;&nbsp;
<c:out value="${lainaus.lainaaja.etunimi}"/>&nbsp;&nbsp;
<c:out value="${lainaus.lainaaja.sukunimi}"/><br />
<c:out value="${lainaus.lainaaja.osoite}"/><br />
<c:out value="${lainaus.lainaaja.posti.postinro}"/>&nbsp;&nbsp;
<c:out value="${lainaus.lainaaja.posti.postitmp}"/>&nbsp;&nbsp;

<p>Lainautut kirjat:<br />

<c:forEach items="${lainaus.lista}" var ="niteenlainaus" >
<c:out value="${niteenlainaus.nide.kirja.isbn}" /> &nbsp;
<c:out value="${niteenlainaus.nide.nidenro}" /> &nbsp;
<c:out value="${niteenlainaus.nide.kirja.nimi}" /> &nbsp;
<c:out value="${niteenlainaus.nide.kirja.kirjoittaja}" /> &nbsp;
<br />
</c:forEach>
</p>
<p>
<input type="submit" name="action" value="talleta lainaus" />
<input type="submit" name="action" value="peruuta" />
</p>
</form>
</body>
</html>