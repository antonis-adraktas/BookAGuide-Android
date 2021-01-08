# BookAGuide-Android

## About
BookAGuide is an android app developed for a student project that involves a booking service for guides in a costrained area (Athens,Greece).
User has the ability to select custom routes by placing markers on the map to the desired locations. 
When a reservation is complete the user can access the routes reserved on a map interface and alerts will appear when the user is approaching each marker.

## How to use this
Clone the repository or download the .zip file of the project and open it in android studio (or any other IDE suitable for android development).
Under app folder is the android application and under wear is a wearable application with limited functionality.  

The application is using firebase as backend database and also using the Google Maps API.
For Firebase a new project needs to be created and the necessary process to configure the connection of the app to your firebase realtime db needs to be completed
(download .json file from firebase and place in your folder etc).

For the Google Map interface of the app to be functional you need to create your API key for Google Map following the intructions [here](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
and place it in hte local.properties file of the application with parameter name MAPS_API_KEY.
