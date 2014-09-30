<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Distance and time Calculator Demo</title>
	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=false" type="text/javascript"></script> 
</head>
<body onunload="GUnload()" style="font-family: Arial; font-size: 12px;">
   <div id="map" style="width: 1000px; height: 800px"></div> 
   <div id="duration">Duration: </div> 
   <div id="distance">Distance: </div> 

   <script type="text/javascript"> 
	   var map = new GMap2(document.getElementById("map"));
	   var directions = new GDirections(map);
	   directions.load("from: IDeaS Revenue Solutions, Baner Road, Ram Nagar, Pune, Maharashtra, India to: Pradeep Marg, Lokmanya Nagar, Pune, Maharashtra, India");
	   GEvent.addListener(directions, "load", function() {
	       // Display the distance from the GDirections.getDistance() method:
	       document.getElementById('distance').innerHTML += 
	           directions.getDistance().meters / 1000 + " kms";
	       // Display the duration from the GDirections.getDuration() method:
	       document.getElementById('duration').innerHTML += 
	           directions.getDuration().seconds / 3600 + " hours";
	   });
   </script> 
</body>
</html>