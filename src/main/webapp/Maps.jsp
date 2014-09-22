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
<script src="http://maps.googleapis.com/maps/api/js?sensor=false&libraries=places&dummy=.js"></script>
<script type="text/javascript">
    var rendererOptions = {
        draggable: true,
    suppressMarkers:true
   };
    var directionsDisplay = new google.maps.DirectionsRenderer(rendererOptions);;
    var directionsService = new google.maps.DirectionsService();
    var geocoder = new google.maps.Geocoder();
    var map;
    var c=0;
    var Ideas = new google.maps.LatLng(18.567054, 73.769757);
    var marker_end = new google.maps.Marker;
    var infowindow = new google.maps.InfoWindow();
   
    function codeLatLng() {
    var input = marker_end.position;
    var lat = input.lat();
    var lng = input.lng();
    var latlng = new google.maps.LatLng(lat, lng);
      geocoder.geocode({'latLng': latlng}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
      if (results[1]) {
        map.setZoom(11);
    var contentString = '<div id="content">'+
    '<p>Click Submit or refine your search</p>'+
    '<form action="authenticate">'+' <input type="hidden" name="username"'+
			'value="<%=usernamexyz%>" />'+ '<input type="submit" value="Submit" />'+'</form>'+
      '</div>';
        infowindow.setContent(contentString + results[1].formatted_address);
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
            zoom: 12,
            center: Ideas
        };
        map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
     
    marker_start = new google.maps.Marker({
      position: Ideas,
      map: map,
      title: 'Ideas Revenue Solutions'
  });


        // Create the search box and link it to the UI element.
        var input = /** @type {HTMLInputElement} */
        (
        document.getElementById('pac-input'));
        map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

        var searchBox = new google.maps.places.SearchBox(
        /** @type {HTMLInputElement} */ (input));

        // [START region_getplaces]
        
        google.maps.event.addListener(searchBox, 'places_changed', function() {
    
            var places = searchBox.getPlaces();

            if (places.length == 0) {
                return;
            }
   
    directionsDisplay.setMap(map);
        directionsDisplay.setPanel(document.getElementById('directionsPanel'));

        google.maps.event.addListener(directionsDisplay, 'directions_changed', function() {
            computeTotalDistance(directionsDisplay.getDirections());
    
        });
          
       
       var bounds = new google.maps.LatLngBounds();
    
   for (var i = 0, place; place = places[i]; i++) 
    {
    var image = {
                    url: place.icon,
                    size: new google.maps.Size(71, 71),
                    origin: new google.maps.Point(0, 0),
                    anchor: new google.maps.Point(17, 34),
                    scaledSize: new google.maps.Size(25, 25)
                };
       marker_end.setMap(map);
       marker_end.setDraggable(true);
       marker_end.setTitle(place.geometry.name);
       marker_end.setPosition(place.geometry.location);

    codeLatLng();
    infowindow.open(map,marker_end);
     }
    
   calcRoute(marker_end.position);  
        
    google.maps.event.addListener(marker_end, 'dragend', function() 
    {
        calcRoute(marker_end.position);
        codeLatLng(); 
        infowindow.open(map,marker_end);
    
     });
         });
    

     google.maps.event.addListener(map, 'bounds_changed', function() {
            var bounds = map.getBounds();
            searchBox.setBounds(bounds);
        });
    }

    function calcRoute(places) {
    
        var request = {
            origin: Ideas,
            destination: places,
            travelMode: google.maps.TravelMode.DRIVING
        };
        directionsService.route(request, function(response, status) {
            if (status == google.maps.DirectionsStatus.OK) {
                directionsDisplay.setDirections(response);
            }
        });
    }

   function computeTotalDistance(result) {
        var total = 0;
        var myroute = result.routes[0];
        for (var i = 0; i < myroute.legs.length; i++) {
            total += myroute.legs[i].distance.value;
        }
        total = total / 1000.0;
        document.getElementById('total').innerHTML = total + ' km';
    }

    google.maps.event.addDomListener(window, 'load', initialize);
    
</script>

</head>

<body>


    <input id="pac-input" class="controls" type="text" placeholder="Search Box">
    <div id="map-canvas"></div>
</body>
</html>