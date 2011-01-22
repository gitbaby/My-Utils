<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<my:header title="${title}" description="${description}"
	keywords="${keywords}" h1="${h1}" user="${user}" />
<a href="/app/editcompany">New Company</a>
<br />
<br />
<table>
	<thead>
		<tr>
			<th>Company Id</th>
			<th>Company Name</th>
			<th>Address</th>
			<th>Restaurants</th>
			<th>Errors</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${records}" varStatus="loopStatus">
			<tr ${loopStatus.index % 2 == 1 ? ' class="even"' : ""}>
				<td><a href="/app/editcompany?id=${r.id}">${r.id}</a></td>
				<td><a href="/app/editcompany?id=${r.id}">${r.name}</a></td>
				<td><a href="/app/editcompany?id=${r.id}">${r.addressLine1}${r.addressLine2
				== "" ? "" : ", "}${r.addressLine2}, ${r.city}<br />
				${r.state}${r.state == "" ? "" : ", "}${r.postCode}<br />
				${r.country}</a></td>
				<td>${r.restaurantNum}</td>
				<td>${r.errors}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<my:footer />