<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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

<h3 class="text-center">
	Enroll For Cab Service <span class="label label-default"></span>
</h3>
</head>

<body>


	<form class="form-horizontal" role="form">
		<div class="form-group container-fluid">
			<label for="EmployeeId" class="col-sm-2 control-label">Employee
				Id</label>
			<div class="col-sm-10">
				<input type="text" class="form-control container-fluid"
					id="EmployeeId" value="${userdetails.employeeID}">
			</div>
		</div>
		<div class="form-group container-fluid">
			<label for="name" class="col-sm-2 control-label">Name</label>
			<div class="col-sm-10">
				<input type="text" class="form-control container-fluid" id="name"
					value="${userdetails.name}">
			</div>
		</div>
		<div class="form-group container-fluid">
			<label for="inputEmail3" class="col-sm-2 control-label">Email</label>
			<div class="input-group container-fluid">

				<input class="form-control " type="email"
					value="${userdetails.getEmailId()}">
				<div class="input-group-addon container-fluid">@IDeaS.com</div>
			</div>
		</div>
		
		<div class="form-group container-fluid">
			<label for="mobile" class="col-sm-2 control-label">Mobile</label>
			<div class="col-sm-10">
				<input type="text" class="form-control container-fluid" id="mobile"
					placeholder="Enter mobile number">
			</div>
		</div>
		<div class="form-group" style="text-align: center">
			<div class="col-sm-offset-1 col-sm-10">
				<button type="submit" class="btn btn-success btn-large">Enroll</button>				
				<button type="submit" class="btn btn-danger btn-large">Cancel</button>
			</div>
		</div>
	</form>

</body>
</html>