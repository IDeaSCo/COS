<%@page import="com.mysql.fabric.xmlrpc.base.Array"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.sql.Time"%>
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
   ArrayList<Time> inTime = (ArrayList<Time>) request.getAttribute("inTime");
   ArrayList<Time> outTime = (ArrayList<Time>) request.getAttribute("outTime");
%>
<head>
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.22/themes/redmond/jquery-ui.css" />
	<link rel="stylesheet" href="calendar/bootstrap-combined.min.css">
	<link rel="stylesheet" type="text/css" href="calendar/jquery.ptTimeSelect.css" />
	<script src="calendar/bootstrap.min.js"></script>
	<script type="text/javascript" src="calendar/jquery.ptTimeSelect.js"></script>
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
				//window.location.reload();
			}
		}
		function addTiming(){
 			document.getElementById('addShift').disabled = true;
 			document.getElementById('newShiftTimings').style.display = 'block';
 			$('input[name="newInTime"]').ptTimeSelect();
 			$('input[name="newOutTime"]').ptTimeSelect();
		}
		function resetAll(){
			document.getElementById('addShift').disabled = false;
			document.getElementById('newShiftTimings').style.display = 'none';
		}
		function saveNewTimings(){
			var inTime = document.getElementById('newInTime').value;
			var outTime = document.getElementById('newOutTime').value;
			jQuery.post("/COS/admin",
					{
						action: "addShift",
						start: inTime,
						end: outTime
					}
			);
			//window.location.reload();
			showShifts();
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
<!-- 	<input name="time" value="" />
	<script type="text/javascript">
	    $(document).ready(function(){
	        $('input[name="time"]').ptTimeSelect();
	    });
	</script> -->
	<div id='shiftDetailsContainer'>
		<button type="button" class="btn btn-primary" id="shiftManager" onclick="showShifts()">View Shift Details</button>
		<button type="button" class="btn btn-primary" id="addShift" onclick="addTiming()">Add new timings</button>
		<div id="newShiftTimings" style="display:none">
			<form id="newTime">
				In Time: <input name="newInTime" id="newInTime"/>
				Out Time: <input name="newOutTime" id="newOutTime"/>
				<button type="button" class="btn btn-success btn-xs" id="saveTime" onclick="saveNewTimings()">Save</button>
				<button type="button" class="btn btn-danger btn-xs" onclick="resetAll()">Cancel</button>
			</form>
		</div>
		<div id="shiftDetails">
			<table width="100%">
				<td width="50%">
					<table class="table table-striped table-bordered table-condensed" id="inTimeTable">
						<tr><th>In Time</th></tr>
						<c:forEach var="element" items="${inTime}" varStatus="status">
							<tr><td><c:out value="${element}" /></td></tr>
						</c:forEach>
					</table>
				</td>
				<td width="50%">
					<table class="table table-striped table-bordered table-condensed">
						<tr><th>Out Time</th></tr>
						<c:forEach var="element" items="${outTime}" varStatus="status">
							<tr><td><c:out value="${element}" /></td></tr>
						</c:forEach>
					</table>
				</td>
			</table>
		</div>
	</div><br>
	<div id='calendar'></div>
	<div id="markHolidayModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header"></div>
				<div class="modal-body">
					<div class="form-group">
				  		Reason: <input type="text" class="form-control" id="reason" name="reason">
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
</body>
</html>