<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>TILINSiirto</title>
</head>
<body>
<h1>TILINSIIRTO</h1>
<div>
<br />
<form action="TiliTuliTiliMeni" method="get" >

	<c:forEach items="${tililista}"  var="tili" >
	
		<c:out value="Tilinumero: "/><c:out value="${tili.tilinro}" /> <br/>
		<c:out value="saldo: "/><c:out value="${tili.saldo}"/> eur <br />
		<c:out value="Tilin omistaja: "/><c:out value="${tili.omistaja.etunimi}" /> 
		&nbsp; <c:out value="${tili.omistaja.sukunimi}" /> <br />
		
		<br/> <br />
	</c:forEach>
</div>
<c:if test="${SiirtoOK==true}" >
<p  style="color: red">
<c:out value="Tilin siirto onnistui" />
</c:if>
<c:if test="${SiirtoOK==false}" >
<p  style="color: red">
<c:out value="Tilin siirto epäonnistui, odota hetki ja tee tilinsiirto uudestaan!" />
</c:if>
	<p>
Kirjoita tilinumero, jolta siirretään rahaa:
<input type="Text" name="tili1" value="${tili1}" />
<c:if test="${tili1VIRHE==true}" >
<p style="color: red">
<c:out value="Tiliä ei löydy" />
</c:if>
</p>
<p>
Kirjoita tilinumero, johon siirretään rahaa:
<input type="Text" name="tili2" value="${tili2}"/>
<c:if test="${tili2VIRHE==true}" >
<p  style="color: red">
<c:out value="Tiliä ei löydy" />
</c:if>
</p>
<p>
Kirjoita rahasumma, joka siirretään tililtä toiselle:
<input type="Text" name="maara" value="${maara}"/>
<c:if test="${maaraVIRHE==true}" >
<p  style="color: red">
<c:out value="Antamasi rahamäärä on virheellinen!" />
</c:if>
<c:if test="${EIKATETTA==true}" >
<p  style="color: red">
<c:out value="Tilillä "/> <c:out value="${tili1}" /> <c:out value=" ei ole katetta!" />
</c:if>
</p>

<input type="submit" name="action" value="tallenna tilinsiirto"/>
</form>

</body>
</html>