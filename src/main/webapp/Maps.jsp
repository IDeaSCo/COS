<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.security.Principal"%>
<%@page import="waffle.windows.auth.WindowsAccount"%>
<%@page import="waffle.servlet.WindowsPrincipal"%>
<%@page import="com.sun.jna.platform.win32.Secur32"%>
<%@page import="com.sun.jna.platform.win32.Secur32Util"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<title>Draggable directions</title>

<%String usernamexyz =request.getRemoteUser();  %>

<link rel="stylesheet" href="map.css">
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script
	src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&dummy=.js"></script>
<script type="text/javascript">
    var rendererOptions = {
        draggable: true,
    suppressMarkers:true
   };
    var map;
    var geocoder = new google.maps.Geocoder();;
    var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);
    var directionsService = new google.maps.DirectionsService();
    var Ideas = new google.maps.LatLng(18.566697, 73.769627);
    var marker_end = new google.maps.Marker;
    var infowindow = new google.maps.InfoWindow();
   
    function codeLatLng(latlng){
    	   geocoder.geocode({'latLng': latlng}, function(results, status) {
			    if (status == google.maps.GeocoderStatus.OK) {
			      if (results[1]) {
			        map.setZoom(11);
			    var contentString = '<div id="content">'+
			    '<p>Click Submit or refine your search</p>'+
			    '<form action="authenticate">'+' <input type="hidden" name="username"'+
						'value="<%=usernamexyz%>" />'
										+ '<input type="submit" value="Submit" />'
										+ '</form>' + '</div>';
								infowindow.setContent(contentString
										+ results[1].formatted_address);
								infowindow.open(map, marker_end);
							} else {
								alert('No results found');
							}
						} else {
							alert('Geocoder failed due to: ' + status);
						}
					});
    }
    
  	function initialize() {

		var mapOptions = {
			zoom : 12,
			center : Ideas
		};
		map = new google.maps.Map(document.getElementById('map-canvas'),
				mapOptions);

		marker_start = new google.maps.Marker({
			position : Ideas,
			map : map,
			title : 'Ideas Revenue Solutions'
		});

		// Create the search box and link it to the UI element.
		var input = /** @type {HTMLInputElement} */
		(document.getElementById('pac-input'));
		map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);
       
		var searchBox = new google.maps.places.SearchBox(input);
		google.maps.event.addListener(searchBox, 'places_changed', function() {
			debugger;
		    var places = searchBox.getPlaces();
		    if (places.length == 0) {
		      return;
		    }
		   var place; place = places[0];
		    	 var image = {
		      url: place.icon,
		      size: new google.maps.Size(71, 71),
		      origin: new google.maps.Point(0, 0),
		      anchor: new google.maps.Point(17, 34),
		      scaledSize: new google.maps.Size(35, 35)
		    };
		    
		    	  marker_end.setMap(map);
		    	  marker_end.setDraggable(true);
		    marker_end.setPosition(places[0].geometry.location);
		    marker_end.setVisible(true);
		    codeLatLng(marker_end.position);
		    directionsDisplay.setMap(map);
			directionsDisplay.setPanel(document
					.getElementById('directionsPanel'));
		    calcRoute(marker_end.position);
			
		
		});

			google.maps.event.addListener(marker_end, 'dragend', function() {
				calcRoute(marker_end.position);
				codeLatLng(marker_end.position);
				infowindow.open(map, marker_end);
				
			});
	
  	}
		

	function calcRoute(places) {

		debugger;
		var request = {
			origin : Ideas,
			destination : places,
			travelMode : google.maps.TravelMode.DRIVING
		};
		directionsService.route(request, function(response, status) {
			if (status == google.maps.DirectionsStatus.OK) {
				directionsDisplay.setDirections(response);
				computeTotalDistanceAndTime(response);
			}
		});
	}

	function computeTotalDistanceAndTime(result) {
		debugger;
		var totalDistance = 0;
		var totalTime = 0;
		var myroute = result.routes[0];
		for (var i = 0; i < myroute.legs.length; i++) {
			totalDistance += myroute.legs[i].distance.value;
			totalTime += myroute.legs[i].duration.value;
		}
		totalDistance = (totalDistance) / 1000.0 ;
		totalTime = totalTime/60;
		document.getElementById('distance').innerHTML ='Distance  '+ Math.round((totalDistance) * 100) / 100 + ' km';
		document.getElementById('duration').innerHTML = 'Estimated time  '+ Math.round((totalTime+15) * 100) / 100 + ' mins';
	}

	function computeTimeAndDistance(){
		var directions = new GDirections(map);
		
	}
	google.maps.event.addDomListener(window, 'load', initialize);
</script>

</head>

<body>


	<input id="pac-input" class="controls" type="text"
		placeholder="Search Box">
	<div id="map-canvas"></div>
	<div id="distance"></div>
	<div id="duration"></div>
</body>
</html>