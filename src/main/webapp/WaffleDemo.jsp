<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@page import="java.security.Principal"%>
<%@page import="waffle.windows.auth.WindowsAccount"%>
<%@page import="waffle.servlet.WindowsPrincipal"%>
<%@page import="com.sun.jna.platform.win32.Secur32"%>
<%@page import="com.sun.jna.platform.win32.Secur32Util"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<title>Insert title here</title>

</head>
<body>
	Your user id is <b><%=request.getRemoteUser()%></b>.
	<form action="authenticate" method="post">
		<input type='hidden' name='username' value="<%=request.getRemoteUser()%>" />
		<input type="submit" value="Submit" />
	</form>
</body>
</html>