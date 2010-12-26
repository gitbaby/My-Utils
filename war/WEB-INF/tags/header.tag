<%@ tag isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="title"%>
<%@ attribute name="description"%>
<%@ attribute name="keywords"%>
<%@ attribute name="h1"%>
<%@ attribute name="user"%>

<!doctype html>
<html>
<head>
<c:if test="${!empty title}">
	<title>${title}</title>
</c:if>
<c:if test="${!empty description}">
	<meta name="description" content="${description}" />
</c:if>
<c:if test="${!empty keywords}">
	<meta name="keywords" content="${keywords}" />
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript" src="/utils.js" charset="utf-8"></script>
</head>
<body>
<c:if test="${!empty user}">
	<div>Welcome ${user} <a href="${logout}">Sign Out</a></div>
</c:if>
<c:if test="${!empty h1}">
	<h1>${h1}</h1>
</c:if>