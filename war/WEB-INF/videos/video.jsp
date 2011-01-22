<%@ page isELIgnored="false"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<my:header />
<my:videosnavigation />
<object id="player" name="player" width="640" height="380"> 
	<param name="movie" value="/player.swf" />
	<param name="allowfullscreen" value="true" /> 
	<param name="flashvars" value="config=/playerconfig.xml&amp;type=video&amp;file=/serve/${video.videoBlobId}" /> 
</object>
<p>Uploaded by: ${member.nick}</p>
<p>${video.description}</p>
<p>Document: <a href="/serve/${video.attachmentBlobId}">Download</a></p>
<p>Keywords: ${video.userKeywords}</p>
<my:footer />