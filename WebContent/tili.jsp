<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="TiliTuliTiliMeni" method="get">
<h1>Tilin haku</h1>
<div>
Anna tilinumero: <input type="text" name="tilinro" />
</div>
<br />
<c:if test="${tilinroVIRHE==true}" >
<p  style="color: red">
<c:out value="Tiliä " /> <c:out value="${tilinro}"/> <c:out value= " ei löytynyt"/>
</c:if>
<c:if test="${tilinroPUUTTUU==true}" >
<p  style="color: red">
<c:out value="Et antanut tilinumeroa! " /> 
</c:if>
<input type ="submit" name="action" value="hae tili"/>

<div>
<c:if test="${tili!= null}">
<br />
Tilinumero: <c:out value="${tili.tilinro}" /> <br />
saldo: <c:out value="${tili.saldo}"/> <br /> <br />
Tilin omistaja: <c:out value="${tili.omistaja.etunimi}" /> 
&nbsp; <c:out value="${tili.omistaja.sukunimi}" /><br />
<c:out value="${tili.omistaja.osoite}" /> <br />
<c:out value="${tili.omistaja.posti.postinro}" />&nbsp; <c:out value="${tili.omistaja.posti.postitmp}" />&nbsp;
</c:if>
</div>
</form>
</body>
</html>