<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<my:header title="${title}" description="${description}"
	keywords="${keywords}" h1="${h1}" user="${user}" />
<table>
	<thead>
		<tr>
			<th>User</th>
			<th>Contact</th>
			<th>Field</th>
			<th>Value</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="r" items="${records}">
			<tr>
				<td colspan="4">${r.user}</td>
			</tr>
			<c:forEach var="f" items="${r.fields}">
				<tr>
					<td></td>
					<td>${f.name}</td>
					<td>${f.field}</td>
					<td>${f.value}</td>
				</tr>
			</c:forEach>
		</c:forEach>
	</tbody>
</table>
<my:footer />