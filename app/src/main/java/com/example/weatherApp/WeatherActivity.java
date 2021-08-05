//Class is mainly used to set text views, image views, etc for the activity.

package com.example.weatherApp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class WeatherActivity extends AppCompatActivity {

    //Objects
    WeatherInfo w = new WeatherInfo();
    DecimalFormat df = new DecimalFormat("#");
    DecimalFormat df1 = new DecimalFormat("#.#");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather2);

        //removing restriction to allow network operations on thread.
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //back button.
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //functions to set output.
        setWeatherInfo();
        setBackground();


    }

    //function to set the weather info onto the activity.
    public void setWeatherInfo(){
        TextView text;
        double f, f1;
        String date;
        
        //getting weather info for given input.
        getWeatherInfo();

        //setting the appropriate text fields.
        text = findViewById(R.id.tempText);
        f = Double.parseDouble(w.tempCel);
        text.setText(df.format(f) + "°C");

        text = findViewById(R.id.tempText2);
        f = Double.parseDouble(w.tempFah);
        text.setText(df.format(f) + "°F");

        text = findViewById(R.id.weatherText);
        text.setText(w.weatherValue);

        text = findViewById(R.id.updateText);
        date = UTCToLocalDate(w.lastUpdate);
        text.setText("Last Updated: " + date);

        text = findViewById(R.id.sunriseText);
        date = UTCToLocalDate(w.citySunrise);
        text.setText("Sunrise: " + date);

        text = findViewById(R.id.sunsetText);
        date = UTCToLocalDate(w.citySunset);
        text.setText("Sunset:  " + date);

        //showing current city and country, using the Locale api to get the full country name from country code.
        //also making sure first initial of the city is upper-case, despite user input.
        text = findViewById(R.id.locationText);
        Locale loc = new Locale("", MainActivity.country);
        String str = MainActivity.city;
        text.setText(str.substring(0, 1).toUpperCase() + str.substring(1) + ", " + loc.getDisplayCountry());

        //using simpledateformat to set appropriate format.
        text = findViewById(R.id.timezoneText);
        SimpleDateFormat formatter= new SimpleDateFormat("EEEE, MMMM d yyyy 'at' h:mm a z");
        Date d = new Date(System.currentTimeMillis());
        text.setText(formatter.format(d));

        text = findViewById(R.id.precipitationText2);
        if (w.precipitationMode.compareTo("no") == 0) {
            text.setText("Currently no precipitation.");
        }
        else {
            text.setText(w.precipitation + "mm of " + w.precipitationMode);
        }

        text = findViewById(R.id.windText2);
        f = Double.parseDouble(w.wind);
        text.setText(df1.format(f) + " km/h");

        text = findViewById(R.id.windtypeText2);
        text.setText(w.windCode + " " + w.windName);

        text = findViewById(R.id.humidityText2);
        text.setText(w.humidity + "%");

        text = findViewById(R.id.pressureText2);
        f = Double.parseDouble(w.pressure);
        text.setText(df1.format(f) + " kPa");

        text = findViewById(R.id.feelslikeText2);
        f = Double.parseDouble(w.tempCelFeelsLike);
        f1 = Double.parseDouble(w.tempFahFeelsLike);
        text.setText(df1.format(f) + "°C or " + df1.format(f1) + "°F");

        text = findViewById(R.id.visibiltyText2);
        f = Double.parseDouble(w.visibility);
        text.setText(df1.format(f) + "km");

    }

    //function to grab the info from the "openweathermap" api for the given input
    public void getWeatherInfo() {
        //creating a link to access the weather at the ID.
        String myAPIKey = "1e847aceca810720c7b857299939f9f0";
        String urlString = "https://api.openweathermap.org/data/2.5/weather?id=" + MainActivity.id + "&appid=" + myAPIKey + "&units=metric" + "&mode=xml";
        String xmlString = "";

        //fetching the data.
        try {
            StringBuilder result = new StringBuilder();

            // url object for API
            URL url = new URL(urlString);

            // open url connection to api for reading
            URLConnection conn = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;

            //creating the xml string of data.
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();

            //this prints the entire xml string.
            //System.out.println(result);

            //converting the StringBuilder to a string.
            xmlString = result.toString();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //converting xml string to a xml document.
        Document doc = stringToXMLDoc(xmlString);

        //getting necessary weather data.
        w.getCity(doc);
        w.getTemperature(doc);
        w.getFeelsLike(doc);
        w.getHumidity(doc);
        w.getPressure(doc);
        w.getWind(doc);
        w.getClouds(doc);
        w.getVisibility(doc);
        w.getPrecipitation(doc);
        w.getWeather(doc);
        w.getLastUpdate(doc);

        //getting current weathers icon (.png) from URL then setting in the activity.
        URL url = null;
        try {
            //creating url from location data to create bitmap of image.
            url = new URL("http://openweathermap.org/img/wn/" + w.weatherIcon + "@4x.png");
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            // finding view and setting it to weather icon from API
            ImageView im = (ImageView)findViewById(R.id.weatherIcon);
            im.setImageBitmap(bmp);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //setting the correct background for each weather condition.
    public void setBackground() {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activityBackground);

        String iconID = w.weatherIcon;
        switch (iconID) {
            //sunny
            case "01d":
            case "02d":
                layout.setBackgroundResource(R.drawable.sunny_james_day_unsplash);
                break;
            //cloudy
            case "03d":
            case "03n":
            case "04d":
            case "04n":
                layout.setBackgroundResource(R.drawable.darkclouds_daoudi_aissa_unsplash);
                break;
            //rain
            case "09d":
            case "09n":
            case "10d":
                layout.setBackgroundResource(R.drawable.rain_janfillem_unsplash);
                break;
            //thunder
            case "11d":
            case "11n":
                layout.setBackgroundResource(R.drawable.thunder_melody_unsplash);
                break;
            //snow
            case "13d":
            case "13n":
                layout.setBackgroundResource(R.drawable.snow_jack_church_unsplash);
                break;
            //windy
            case "50d":
            case "50n":
                layout.setBackgroundResource(R.drawable.windy_anthony_ievlev_unsplash);
                break;
            //night
            case "01n":
            case "02n":
            case "10n":
                layout.setBackgroundResource(R.drawable.night_guille_pozzi_unsplash);
                break;
        }

    }

    //function to convert xml string to xml document
    private static Document stringToXMLDoc(String xmlString) {
        //parser for producing the doc object trees from the XML.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //Document instance
        DocumentBuilder builder = null;

        try {
            //creating the DocumentBuilder
            builder = factory.newDocumentBuilder();
            //parsing content to Document object.
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    //for implementing the back button.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            //ends activity
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }


    //takes a ISO-8601 string date, coverts it to users local date time, fixes it for specific format.
    public String UTCToLocalDate(String date) {
        Date inputDate = new Date();
        String s;

        //ISO-8601 to local time
        if (date != null && !date.isEmpty()) {
            //simpledateformat is deprecated, therefore @SuppressLint is necessary to suppress the warnings of it being not optimal.
            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                inputDate = simpleDateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //formatting it for app
        s = inputDate.toString();
        s = s.substring(11, 19);

        //changing from military to standard time
        Date date1;
        String output = null;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");

        try {
            date1 = df.parse(s);
            output = outputFormat.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return output;

    }
}
