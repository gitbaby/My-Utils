<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<my:header />
<my:videosnavigation />
<c:if test="${!empty error}">
	<p><b>${error}</b></p>
</c:if>
<my:videos videos="${videos}"></my:videos>
<my:footer />