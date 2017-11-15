/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var currentLatitude = null; 
var currentLongitude = null;
var map = null;
$(document).ready(function () {

    if (navigator.geolocation) {
        // Use method getCurrentPosition to get coordinates
        navigator.geolocation.getCurrentPosition(function (position) {
            // Access them accordingly
            currentLatitude =  position.coords.latitude;
            currentLongitude = position.coords.longitude;
            var latlng = new google.maps.LatLng(currentLatitude, currentLongitude);
            map = new google.maps.Map(document.getElementById('map'), {
                center: { lat: currentLatitude, lng: currentLongitude },
                zoom: 8
            });
            var marker = new google.maps.Marker({
                position: latlng,
                map: map,
                title: 'Drag this marker to the machine location',
                draggable: true
            });
            
            google.maps.event.addListener(marker, 'dragend', function (a) {
                // bingo!
                // a.latLng contains the co-ordinates where the marker was dropped
                //change inout text 
                $("#location").val(a.latLng);
            });
        });
    }
    
    $("#save-button").on('click', function(){
        $("#new-machine-form").submit(); 
    });
});