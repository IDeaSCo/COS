<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.List" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 	<%@ page isELIgnored="false"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-theme.min.css">
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">


<title>Enroll For Cab Service</title>
</head>

<body>

<form id= "employeeDetailsForm" name="employeeDetailsForm" method="post">
					<p>Email:</p>
					<input type="text" name="email" id="email"  />
					<p>Name:</p>
					<input type="text" name="name" id="name"  />
					<p>Mobile:</p>
					<input type="text" name="mobile" id="mobile"  />
					<p>Password:</p>
					<input type="password" name="password" id="password"  />
</form>


</body>
</html>