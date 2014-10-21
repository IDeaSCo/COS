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
<%
	ArrayList<?> eventScheduleArray = (ArrayList<?>) request.getAttribute("eventScheduleArray");
	ArrayList<?> shiftTimings = (ArrayList<?>) request.getAttribute("shiftTimings");
%>
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
		var startSelect = new Date();
		var endSelect = new Date();
		var eventsFromCalendar;
		var events=[];
		function updateChanges(){
			eventsFromCalendar = $('#calendar').fullCalendar('clientEvents');
			for(i = 0; i < eventsFromCalendar.length; i++){
				var JSONObj = { "title":eventsFromCalendar[i].title, "start":new Date(eventsFromCalendar[i].start.getTime()+330*60000)};
				events.push(JSON.stringify(JSONObj));
			}
			document.getElementById('eventInput').value=events;
			$.ajax({
		    	type: "POST",
		    	dataType: "json",
				url: "/COS/dashboard",
				data:  $('#eventsFromCalendarForm').serialize(),
		      	success: function(msg){
		     		 if(msg === true) {
		                 $("#resultContainer").html("<p style='color:green' class='alert alert-success'>Schedule Updated</p>");
		    		 } else {
		    			 $("#resultContainer").html("<div class='alert alert-danger'>Please try again</div>");
		    		 }
		         },
				error: function(){
					  $("#resultContainer").html("<div class='alert alert-danger'>Server Error. Request could not be placed, please try again later</div>");
		    	}
		    });
		}
		function saveChange() {
			$('#calendar').fullCalendar('removeEvents', function(event) {
				endSelect.setHours(23, 59, 59, 999);
				if ((event.start >= startSelect) && (event.start <= endSelect))
					return true;
				return false;
			});
			var date = startSelect;
	
			while (date <= endSelect) {
				var e = document.getElementById("inTime");
				var inTime = e.options[e.selectedIndex].text;
				if (inTime != 'SKIP') {
					var inTimeArray = inTime.split(":");
					$('#calendar').fullCalendar(
							'renderEvent',
							{
								title : 'In-Time',
								start : new Date(date.getFullYear(), date
										.getMonth(), date.getDate(),
										inTimeArray[0], inTimeArray[1]),
								allDay : false
							}, true // make the event "stick"
					);
				}
				e = document.getElementById("outTime");
				var outTime = e.options[e.selectedIndex].text;
				if (outTime != 'SKIP') {
					var outTimeArray = outTime.split(":");
					$('#calendar').fullCalendar(
							'renderEvent',
							{
								title : 'Out-Time',
								start : new Date(date.getFullYear(), date.getMonth(), date.getDate(), outTimeArray[0], outTimeArray[1]),
								allDay : false
							}, true // make the event "stick"
					);
				}
				date.setDate(date.getDate() + 1);
			}
	
		}
	
		$(document).ready(function() {
			/*
				date store today date.
				d store today date.
				m store current month.
				y store current year.
			 */
			var date = new Date();
			var d = date.getDate();
			var m = date.getMonth();
			var y = date.getFullYear();
	
			/*
				Initialize fullCalendar and store into variable.
				Why in variable?
				Because doing so we can use it inside other function.
				In order to modify its option later.
			 */
	
			var calendar = $('#calendar').fullCalendar({
				/*
					header option will define our calendar header.
					left define what will be at left position in calendar
					center define what will be at center position in calendar
					right define what will be at right position in calendar
				 */
				header : {
					left : 'prev,next today',
					center : 'title',
	
				},
				/*
					defaultView option used to define which view to show by default,
					for example we have used agendaWeek.
				 */
	
				/*
					selectable:true will enable user to select datetime slot
					selectHelper will add helpers for selectable.
				 */
				selectable : true,
				aspectRatio: 1.7,
				selectHelper : true,
				eventClick : function(event, element) {
				event.title = "CLICKED!";
				$('#calendar').fullCalendar('removeEvents', event._id);
	
				},
				/*
					when user select timeslot this option code will execute.
					It has three arguments. Start,end and allDay.
					Start means starting time of event.
					End means ending time of event.
					allDay means if events is for entire day or not.
				 */
				select : function(start, end, allDay) {
					startSelect = start;
					endSelect.setTime(end.getTime());
	
					/*
						after selection user will be promted for enter title for event.
					 */
					$("#createEventModal").modal("show");
	
					/*
						if title is enterd calendar will add title and event into fullCalendar.
					 */
	
					/* if (title) {
						//alert(start);
						//alert(end);
						calendar.fullCalendar('renderEvent', {
							title : title,
							start : start,
							end : end,
							allDay : allDay
						}, true // make the event "stick"
						);
					} */
					calendar.fullCalendar('unselect');
				},
				/*
					editable: true allow user to edit events.
				 */
				editable : true,
				/*
					events is the main option for calendar.
					for demo we have added predefined events in json object.
				 */
				events : <%=eventScheduleArray%> 
			});
	
		});
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
	</style>
</head>
<body>
	<div id='calendar'></div>
	<br>
	<button type="button" class="btn btn-success" data-dismiss="modal"
		onclick="updateChanges()" style="font-size: large">Save My
		Schedule</button>
	<br>
	<br>
	<p id="resultContainer"></p>
	<div id="createEventModal" class="modal fade">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<div id="resultContainer"></div>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
						<div class="form-group">
							<label for="inTimeLabel" class="col-sm-2 control-label">In-Time:</label>
							<div class="col-sm-10">
								<select class="form-control" id="inTime">
									<option>SKIP</option>
									<% for(int i = 0; i < shiftTimings.size(); i++) { %>
									<option><%=shiftTimings.get(i) %></option>
									<% } %>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="outTimeLabel" class="col-sm-2 control-label">Out-Time:</label>
							<div class="col-sm-10">
								<select class="form-control" id="outTime">
									<option>SKIP</option>
									<% for(int i = 0; i < shiftTimings.size(); i++) { %>
									<option><%=shiftTimings.get(i) %></option>
									<% } %>
								</select>
							</div>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-primary" data-dismiss="modal" onclick="saveChange()">Done</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<form method="post" id="eventsFromCalendarForm">
		<input type="hidden" name="events" id="eventInput">
	</form>
</body>
</html>