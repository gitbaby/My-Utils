<%@ tag isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="videos" required="true" type="java.util.List"%>

<table>
	<c:forEach var="v" items="${videos}" varStatus="loopStatus">
		<tr><td><a href="/video/${v.id}/${v.titleUrl}">${v.title}</a></td></tr>
	</c:forEach>
</table>