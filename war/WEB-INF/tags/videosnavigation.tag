<%@ tag isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
function search(str) {
	if (str.replace(/^\s+/,'').length) location = "/search/" + encodeURIComponent(str);
	return false;
}
</script>
<p>
<a href="/">Videos</a> |
<a href="/member/upload">Upload Video</a> |
<input type="text" id="search"/> <input type="button" value="Search" onclick="search(document.getElementById('search').value)" />
</p>