# Background Information
This weather application was a project I worked on in between my second and third year of my degree. 

The entire project was built using Android Studio. For the frontend it was built using XML and the IDEs built in user-interface maker. The backend was built using JAVA and calls for the weather data was obtained by calling a Weather API (https://openweathermap.org/api) where data manipulation was required for displaying the JSON data that was received.

To my knowledge the only way to run this project currently is through Android Studio due to their built in android emulator support, I assume this can be replicated in other softwares such as IntelliJ IDEA, Visual Studio, Eclipse, and etc. It was tested on multiple android emulators but the main one used during development and testing was the Pixel 3a API28 as well as other Pixel variations.


# Main Pipeline
![Home Page](/images/logo.png)
Format: ![Alt Text](url)


Users are greeted with 

I have different backgrounds for each weather type, for most background images I kept the author and site in the file name under the drawable folder, if curious.

The search function uses the city name and country instead of city name and state/province because some places don't have them in their JNON file and by using the country instead it gets rid of countries that may have a city with the same name as others.



