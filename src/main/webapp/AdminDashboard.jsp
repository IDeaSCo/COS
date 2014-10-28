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
<% ArrayList<?> holidayList = (ArrayList<?>) request.getAttribute("holidays");
   ArrayList<?> shiftTimings = (ArrayList<?>) request.getAttribute("shiftTimings");
%>
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
			$("#shiftDetails").hide();
			var calendar = $('#calendar').fullCalendar({
				header : {
					left : 'prev,next today',
					center : 'title',
				},
				selectable : true,
				aspectRatio: 2,
				selectHelper : true,
				select: function(start, end, allDay) {
					eventDate = start;
					var calendarEvents = $('#calendar').fullCalendar('clientEvents');
					var flag = false;
					for (var i = 0; i < calendarEvents.length; i++) {
 						if(calendarEvents[i].start.toString() == eventDate.toString()){
							document.getElementById('reason').value = calendarEvents[i].title;
							flag = true;
							break;							
						}
					}
					$("#markHolidayModal").modal("show");
  					if(flag)
						manageButtons("add", "remove");
					else
						manageButtons("remove", "add");
					calendar.fullCalendar('unselect');
				},
				editable : true,
				events: <%=holidayList%>,
			});
		});
		
		function manageButtons(hide_id, show_id){
/* 			document.getElementById(hide_id).style.display = 'none';
			document.getElementById(show_id).style.display = 'block';
 */
 			document.getElementById(hide_id).disabled = true;
 			document.getElementById(show_id).disabled = false;
 		}
		
		function markHoliday(){
			var holidayReason = document.getElementById('reason').value;
			$('#calendar').fullCalendar('renderEvent',
					{
						title : holidayReason,
						start : eventDate,
						allDay : true
					}, true 
			);
			jQuery.post("/COS/admin",
					{
						action: "add",
						title: holidayReason,
						start: eventDate.getTime()
					}
			);
			window.location.reload();
		}
		
		function removeHoliday(){
			jQuery.post("/COS/admin",
					{
						action: "remove",
						start: eventDate.getTime()
					}
			);
			window.location.reload();
		}
		
		function showShifts(){
			var caption = document.getElementById("shiftManager");
			var div = document.getElementById("shiftDetails");
			if(caption.firstChild.data == "View Shift Details"){
				caption.firstChild.data = "Hide Shift Details";
				div.style.display = "block";
			}
			else{
				caption.firstChild.data = "View Shift Details";
				div.style.display = "none";
				window.location.reload();
			}
		}
	</script>
	<style type="text/css">
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
	<div id='shiftDetailsContainer'>
		<button type="button" class="btn btn-primary" id="shiftManager" onclick="showShifts()">View Shift Details</button>
		<div id="shiftDetails">
			<% for(int i = 0; i < shiftTimings.size(); i++) { %>
			<p><%=shiftTimings.get(i) %></p>
			<% } %>
			<a data-toggle="modal" data-backdrop="static" href="#addShift" class="btn btn-success">Add new shift</a>
		</div>
	</div><br>
	<div id='calendar'></div>
	<div id="markHolidayModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body">
					<div class="form-group">
						<!-- <label>Reason</label> -->
				  		Reason <input type="text" class="form-control" id="reason" name="reason">
				  </div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="add" onclick="markHoliday()">Mark Holiday</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="remove" onclick="removeHoliday()">Remove Holiday</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<div id="addShift" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body">
					<form id="itemForm" method="post">
						<div class="form-group">
							<label>Shift Timing</label>
							<input type="text" class="form-control" id="shift" name="shift">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="addItem" type="submit" class="btn btn-primary">Save shift</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>