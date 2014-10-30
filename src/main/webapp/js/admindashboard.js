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
			}
		}
		
		function addTiming(){
 			document.getElementById('addShift').disabled = true;
 			document.getElementById('newShiftTimings').style.display = 'block';
 			$('input[name="newInTime"]').ptTimeSelect();
 			$('input[name="newOutTime"]').ptTimeSelect();
		}
		
		function cancel(){
			document.getElementById('addShift').disabled = false;
			document.getElementById('newShiftTimings').style.display = 'none';
		}
		
		function save(){
			var inTime = document.getElementById('newInTime').value;
			var outTime = document.getElementById('newOutTime').value;
			jQuery.post("/COS/admin",
					{
						action: "addShift",
						start: inTime,
						end: outTime
					}
			);
			window.location.reload(true);
		}