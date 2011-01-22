<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<my:header />
<my:videosnavigation />
<form action="${uploadUrl}" method="post" enctype="multipart/form-data">
<table>
	<tr><td>Video:</td><td><input type="file" name="video" /></td></tr>
	<tr><td>Document:</td><td><input type="file" name="attachment" /></td></tr>
	<tr><td>Title:</td><td><input type="text" name="title" /></td></tr>
	<tr><td>Description:</td><td><textarea name="description"></textarea></td></tr>
	<tr><td>Keywords:</td><td><input type="text" name="keywords" /></td></tr>
	<tr><td></td><td><input type="submit" value="Submit" /></td></tr>
</table>
</form>
<my:footer />