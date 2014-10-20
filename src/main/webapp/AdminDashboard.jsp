<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
	<link rel="stylesheet" href="calendar/bootstrap-combined.min.css">
	<script src="calendar/bootstrap.min.js"></script>
	<link rel='stylesheet' href='calendar/fullcalendar.css' />
	<script src='calendar/fullcalendar.js'></script>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>DashBoard</title>
	<script type="text/javascript">
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
				select : function(start, end, allDay) {
					startSelect = start;
					endSelect.setTime(end.getTime());
					$("#createEventModal").modal("show");
					calendar.fullCalendar('unselect');
				},
				editable : true,
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
</body>
</html>