# Background Information
This weather application was a project I worked on in between my second and third year of my degree. 

The entire project was built using Android Studio. For the frontend it was built using XML and the IDEs built in user-interface maker. The backend was built using JAVA and calls for the weather data was obtained by calling a Weather API (https://openweathermap.org/api) where data manipulation was required for displaying the JSON data that was received.

To my knowledge the only way to run this project currently is through Android Studio due to their built in android emulator support, I assume this can be replicated in other softwares such as IntelliJ IDEA, Visual Studio, Eclipse, etc. It was tested on multiple android emulators but the main one used during development and testing was the Pixel 3a API28 as well as other Pixel variations.

Most of the files at the root were automatically generated / android studio files, the main files worked on were in "app/src/main", more specifically "app/src/main/java" for the JAVA and "app/src/main/res/layout" for the XML.


# Summary: Main Pipeline
## 1. Home Page
<img src="screenshots/Screenshot_1627761685.png" width=300 align=left>
<figcaption>Fig.1 - Home Page (activity_main.xml)</figcaption>

Users are greeted with the home page in the following figure, and are expected to enter an input in the correct format shown at the top of the screen. This is due to how the JSON in the API is set up, so entries must be "City, Country" where the country is a 2-letter abbreviation. All the countries are pre-defined in a JSON list given to us by the weather API, so when the user enters an input we perform validation checks on it to make sure it's in the list before we fetch the data for the entered input from the API. These checks mostly consist of making sure that the string is in the correct format explained above, if good procceed but if it's bad let the user know whats wrong. For correct inputs in the main button click event we can now scan the list to find the location ID for an exact match of the input. If the location id is found we can proceed by rendering the next activity aka the result page.

I have different backgrounds for each weather type, for most background images I kept the author and site in the file name under the drawable folder, if curious.

Note: The search function is limited to the way it is because it uses the city name and country instead of city name and state/province because some places don't have them in their JNON file and by using the country instead it gets rid of countries that may have a city with the same name as others.


## 2. Result Page


