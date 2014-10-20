<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.json.JSONObject"%>
<%@page import="java.security.Principal"%>
<%@page import="waffle.windows.auth.WindowsAccount"%>
<%@page import="waffle.servlet.WindowsPrincipal"%>
<%@page import="com.sun.jna.platform.win32.Secur32"%>
<%@page import="com.sun.jna.platform.win32.Secur32Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<link rel="stylesheet" href="calendar/bootstrap-combined.min.css">
	<script src="calendar/bootstrap.min.js"></script>
	<link rel='stylesheet' href='calendar/fullcalendar.css' />
	<script src='calendar/fullcalendar.js'></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Dashboard</title>
	<script>
		var eventDate = new Date();
		$(document).ready(function() {
			var date = new Date();
			var d = date.getDate();
			var m = date.getMonth();
			var y = date.getFullYear();
			var calendar = $('#calendar').fullCalendar({
				header : {
					left : 'prev,next today',
					center : 'title',
				},
				selectable : true,
				aspectRatio: 1.7,
				selectHelper : true,
				eventClick : function(event, element) {
					event.title = "CLICKED!";
					$('#calendar').fullCalendar('removeEvents', event._id);
				},
				select: function(start, end, allDay) {
					eventDate = start;
					$("#markHoliday").modal("show");
					calendar.fullCalendar('unselect');
				},
				editable : true,
			});
		});
		
		function markHoliday(){
			var holidayReason = document.getElementById('reason').value;
			$('#calendar').fullCalendar('removeEvents', function(event) {
				return true;
			});
			$('#calendar').fullCalendar('renderEvent',
					{
						title : holidayReason,
						start : eventDate,
						allDay : true
					}, true 
			);
			jQuery.post("/COS/admin",
					{
						title: holidayReason,
						start: eventDate
					}
			);
		}
	</script>
	<style type="text/css">
		body {
			margin-top: 30px;
			text-align: center;
			font-size: 14px;
			font-family: "Lucida Grande", Helvetica, Arial, Verdana, sans-serif;
		}
		#calendar {
			width: 1200px;
			margin: 0 auto;
		}
		.fc-sat, .fc-sun {
			background: #F0F0F0;
		}
	</style>
</head>
<body>
	<div id='calendar'></div>
	<div id="markHoliday" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body">
					<div class="form-group">
				  		<b>Reason: </b> <input type="text" class="form-control" id="reason" name="reason">
				  </div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="markHoliday()">Mark Company Holiday</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>