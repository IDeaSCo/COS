<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="org.json.JSONObject" %>
<%@page import="java.security.Principal"%>
<%@page import="waffle.windows.auth.WindowsAccount"%>
<%@page import="waffle.servlet.WindowsPrincipal"%>
<%@page import="com.sun.jna.platform.win32.Secur32"%>
<%@page import="com.sun.jna.platform.win32.Secur32Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
	String employeeUsername = request.getRemoteUser();
	ArrayList<?> eventScheduleArray = (ArrayList<?>) request.getAttribute("eventScheduleArray");
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
	for(i=0;i<eventsFromCalendar.length;i++){
		var JSONObj = { "title":eventsFromCalendar[i].title, "start":new Date(eventsFromCalendar[i].start.getTime()+330*60000)};		
		events.push(JSON.stringify(JSONObj));		
	}
	
	console.log(events);
	

	//var fsfds= JSON.stringify(temp);
	
	//var stringEventsFromCalendar = JSON.stringify(temp); 
	//console.log(stringEventsFromCalendar); 
	document.getElementById('eventInput').value=events;
	$.ajax({
    	type: "POST",
    	dataType: "json",
		url: "http://didnsorina6:8080/COS/dashboard",
		data:  $('#eventsFromCalendarForm').serialize()
      	/* success: function(msg){
     		 if(msg === true) {
                 $("#resultContainer").html("<div class='alert alert-success'>Request sent successfully</div>");
    		 } else {
    			 $("#resultContainer").html("<div class='alert alert-danger'>Requested quantity unavailable</div>");
    		 }
         },
		error: function(){
			  $("#resultContainer").html("<div class='alert alert-danger'>Server Error. Request could not be placed, please try again later</div>");
    	} */
    });

}
	function saveChange() {
		$('#calendar').fullCalendar('removeEvents', function(event) {
			endSelect.setHours(23, 59, 59, 999);
			//alert(event.start.toDateString());
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
							start : new Date(date.getFullYear(), date
									.getMonth(), date.getDate(),
									outTimeArray[0], outTimeArray[1]),
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
				//var title = prompt('Event Input Title:');
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
</head>
<body>

<button type="button" class="btn btn-success" data-dismiss="modal" onclick="updateChanges()">Save</button>
	<div id='calendar'></div>
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
									<% Date dt = new Date(66600000);
									
									for(int i= 0; i <96;i++ ){
										%>
										<option><%=dt.getHours() %>:<%=dt.getMinutes() %></option>
									<%
									dt = new Date(dt.getTime() + 15 * 60 * 1000);
									}
									
									
									%>
								</select>

							</div>
						</div>
						<div class="form-group">
							<label for="outTimeLabel" class="col-sm-2 control-label">Out-Time:</label>
							<div class="col-sm-10">
								<select class="form-control" id="outTime">
									<option>SKIP</option>
									<option>00:00</option>
									<option>00:15</option>
									<option>00:30</option>
									<option>00:45</option>
									<option>01:00</option>
									<option>01:15</option>
									<option>01:30</option>
									<option>01:45</option>
									<option>02:00</option>
									<option>02:15</option>
									<option>02:30</option>
									<option>02:45</option>
									<option>03:00</option>
									<option>03:15</option>
									<option>03:30</option>
									<option>03:45</option>
									<option>04:00</option>
									<option>04:15</option>
									<option>04:30</option>
									<option>04:45</option>
									<option>05:00</option>
									<option>05:15</option>
									<option>05:30</option>
									<option>05:45</option>
									<option>06:00</option>
									<option>06:15</option>
									<option>06:30</option>
									<option>06:45</option>
									<option>07:00</option>
									<option>07:15</option>
									<option>07:30</option>
									<option>07:45</option>
									<option>08:00</option>
									<option>08:15</option>
									<option>08:30</option>
									<option>08:45</option>
									<option>09:00</option>
									<option>09:15</option>
									<option>09:30</option>
									<option>09:45</option>
									<option>10:00</option>
									<option>10:15</option>
									<option>10:30</option>
									<option>10:45</option>
									<option>11:00</option>
									<option>11:15</option>
									<option>11:30</option>
									<option>11:45</option>
									<option>12:00</option>
									<option>12:15</option>
									<option>12:30</option>
									<option>12:45</option>
									<option>13:00</option>
									<option>13:15</option>
									<option>13:30</option>
									<option>13:45</option>
									<option>14:00</option>
									<option>14:15</option>
									<option>14:30</option>
									<option>14:45</option>
									<option>15:00</option>
									<option>15:15</option>
									<option>15:30</option>
									<option>15:45</option>
									<option>16:00</option>
									<option>16:15</option>
									<option>16:30</option>
									<option>16:45</option>
									<option>17:00</option>
									<option>17:15</option>
									<option>17:30</option>
									<option>17:45</option>
									<option>18:00</option>
									<option>18:15</option>
									<option>18:30</option>
									<option>18:45</option>
									<option>19:00</option>
									<option>19:15</option>
									<option>19:30</option>
									<option>19:45</option>
									<option>20:00</option>
									<option>20:15</option>
									<option>20:30</option>
									<option>20:45</option>
									<option>21:00</option>
									<option>21:15</option>
									<option>21:30</option>
									<option>21:45</option>
									<option>22:00</option>
									<option>22:15</option>
									<option>22:30</option>
									<option>22:45</option>
									<option>23:00</option>
									<option>23:15</option>
									<option>23:30</option>
									<option>23:45</option>
									<option>00:00</option>
									<option>00:15</option>
									<option>00:30</option>
									<option>00:45</option>
									<option>01:00</option>
									<option>01:15</option>
									<option>01:30</option>
									<option>01:45</option>
									<option>02:00</option>
									<option>02:15</option>
									<option>02:30</option>
									<option>02:45</option>
									<option>03:00</option>
									<option>03:15</option>
									<option>03:30</option>
									<option>03:45</option>
									<option>04:00</option>
									<option>04:15</option>
									<option>04:30</option>
									<option>04:45</option>
									<option>05:00</option>
									<option>05:15</option>
									<option>05:30</option>
									<option>05:45</option>
									<option>06:00</option>
									<option>06:15</option>
									<option>06:30</option>
									<option>06:45</option>
									<option>07:00</option>
									<option>07:15</option>
									<option>07:30</option>
									<option>07:45</option>
									<option>08:00</option>
									<option>08:15</option>
									<option>08:30</option>
									<option>08:45</option>
									<option>09:00</option>
									<option>09:15</option>
									<option>09:30</option>
									<option>09:45</option>
									<option>10:00</option>
									<option>10:15</option>
									<option>10:30</option>
									<option>10:45</option>
									<option>11:00</option>
									<option>11:15</option>
									<option>11:30</option>
									<option>11:45</option>
									<option>12:00</option>
									<option>12:15</option>
									<option>12:30</option>
									<option>12:45</option>
									<option>13:00</option>
									<option>13:15</option>
									<option>13:30</option>
									<option>13:45</option>
									<option>14:00</option>
									<option>14:15</option>
									<option>14:30</option>
									<option>14:45</option>
									<option>15:00</option>
									<option>15:15</option>
									<option>15:30</option>
									<option>15:45</option>
									<option>16:00</option>
									<option>16:15</option>
									<option>16:30</option>
									<option>16:45</option>
									<option>17:00</option>
									<option>17:15</option>
									<option>17:30</option>
									<option>17:45</option>
									<option>18:00</option>
									<option>18:15</option>
									<option>18:30</option>
									<option>18:45</option>
									<option>19:00</option>
									<option>19:15</option>
									<option>19:30</option>
									<option>19:45</option>
									<option>20:00</option>
									<option>20:15</option>
									<option>20:30</option>
									<option>20:45</option>
									<option>21:00</option>
									<option>21:15</option>
									<option>21:30</option>
									<option>21:45</option>
									<option>22:00</option>
									<option>22:15</option>
									<option>22:30</option>
									<option>22:45</option>
									<option>23:00</option>
									<option>23:15</option>
									<option>23:30</option>
									<option>23:45</option>
								</select>
							</div>
						</div>

					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" data-dismiss="modal"
						onclick="saveChange()">Save Changes</button>
					<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
				</div>
			</div>
		</div>
	</div>
	<form method="post" id="eventsFromCalendarForm">
		<input type="hidden" name="events" id="eventInput">
		<input type="hidden" name="username" value="<%=employeeUsername%>">
	</form>
</body>
</html>