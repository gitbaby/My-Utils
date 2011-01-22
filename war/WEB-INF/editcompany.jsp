<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<my:header title="${title}" description="${description}"
	keywords="${keywords}" h1="${h1}" user="${user}" />
<form method="post" action="/app/editcompany"><input
	type="hidden" name="id" value="${id}" />
<table>
	<tr colspan="2">
		<td>Company Name</td>
	</tr>
	<tr colspan="2">
		<td><input type="text" name="name" value="${name}" /></td>
	</tr>
	<tr colspan="2">
		<td>&nbsp;</td>
	</tr>
	<tr colspan="2">
		<td>Address</td>
	</tr>
	<tr colspan="2">
		<td><input type="text" name="addressline1"
			value="${addressLine1}" /></td>
	</tr>
	<tr colspan="2">
		<td><input type="text" name="addressline2"
			value="${addressLine2}" /></td>
	</tr>
	<tr colspan="2">
		<td>&nbsp;</td>
	</tr>
	<tr colspan="2">
		<td>City</td>
	</tr>
	<tr colspan="2">
		<td><input type="text" name="city" value="${city}" /></td>
	</tr>
	<tr colspan="2">
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>Country</td>
		<td>State</td>
	</tr>
	<tr>
		<td><my:country selected="${country}" /></td>
		<td><my:state selected="${state}" /></td>
	</tr>
	<tr colspan="2">
		<td>&nbsp;</td>
	</tr>
	<tr colspan="2">
		<td>Zip/Postcode</td>
	</tr>
	<tr colspan="2">
		<td><input type="text" name="postcode" value="${postCode}" /></td>
	</tr>
	<tr colspan="2">
		<td>&nbsp;</td>
	</tr>
	<tr colspan="2">
		<td><input type="submit" value="Submit" /> <input type="button"
			value="Cancel" onclick="document.location='/app/main'" /></td>
	</tr>
</table>
</form>
<my:footer />