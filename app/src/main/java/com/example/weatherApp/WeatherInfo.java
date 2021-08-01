//class to grab and hold weather data.

package com.example.weatherApp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WeatherInfo {

    //globals
    static public String cityName = "";
    static public String cityCountry = "";
    static public String cityTimezone = "";
    static public String citySunrise = "";
    static public String citySunset = "";
    static public String tempCel = "";
    static public String tempFah = "";
    static public String tempCelFeelsLike = "";
    static public String tempFahFeelsLike = "";
    static public String humidity = "";
    static public String pressure = "";
    static public String wind = "";
    static public String windName = "";
    static public String windCode = "";
    static public String cloudiness = "";
    static public String cloudName = "";
    static public String visibility = "";
    static public String precipitation = "";
    static public String precipitationMode = "";
    static public String weatherNumber = "";
    static public String weatherValue = "";
    static public String weatherIcon = "";
    static public String lastUpdate = "";


    //getting city name, country, timezone, sunrise, and sunset.
    public static void getCity(Document doc) {
        NodeList list = doc.getElementsByTagName("city");
        Node n = list.item(0);
        Element e = (Element) n;

        cityName = e.getAttribute("name");
        cityCountry = e.getAttribute("country");
        cityTimezone = e.getAttribute("timezone");

        list = e.getElementsByTagName("sun");
        n = list.item(0);
        e = (Element) n;

        citySunrise = e.getAttribute("rise");
        citySunset = e.getAttribute("set");
    }


    //getting temp in celsius and fahrenheit.
    public static void getTemperature(Document doc) {
        NodeList list = doc.getElementsByTagName("temperature");
        Node n = list.item(0);
        Element e = (Element) n;

        tempCel = e.getAttribute("value");
        double fah = (Double.parseDouble(tempCel) * 9 / 5) + 32;
        tempFah = Double.toString(fah);
    }

    //getting feels_like temp in celsius and fahrenheit.
    public static void getFeelsLike(Document doc) {
        NodeList list = doc.getElementsByTagName("feels_like");
        Node n = list.item(0);
        Element e = (Element) n;

        tempCelFeelsLike = e.getAttribute("value");
        double fah = (Double.parseDouble(tempCelFeelsLike) * 9 / 5) + 32;
        tempFahFeelsLike = Double.toString(fah);
    }

    //getting the humidity (unit: %)
    public static void getHumidity(Document doc) {
        NodeList list = doc.getElementsByTagName("humidity");
        Node n = list.item(0);
        Element e = (Element) n;

        humidity = e.getAttribute("value");
    }

    //getting the pressure (unit: kPa)
    public static void getPressure(Document doc) {
        NodeList list = doc.getElementsByTagName("pressure");
        Node n = list.item(0);
        Element e = (Element) n;

        pressure = e.getAttribute("value");
        //pressure is in hPa so converting to kPa
        pressure = Double.toString(Double.parseDouble(pressure) * 0.1);
    }

    //getting the wind (unit: km/h)
    public static void getWind(Document doc) {
        NodeList list = doc.getElementsByTagName("wind");
        Node n = list.item(0);
        Element e = (Element) n;

        list = e.getElementsByTagName("speed");
        n = list.item(0);
        e = (Element) n;

        wind = e.getAttribute("value");
        //wind is in m/s so converting to km/h
        wind = Double.toString(Double.parseDouble(wind) * 3.6);
        windName = e.getAttribute("name");

        list = doc.getElementsByTagName("wind");
        n = list.item(0);
        e = (Element) n;

        list = e.getElementsByTagName("direction");
        n = list.item(0);
        e = (Element) n;

        windCode = e.getAttribute("code");
    }

    //getting the cloudiness and name of cloudiness (units: %)
    public static void getClouds(Document doc) {
        NodeList list = doc.getElementsByTagName("clouds");
        Node n = list.item(0);
        Element e = (Element) n;

        cloudiness = e.getAttribute("value");
        cloudName = e.getAttribute("name");
    }

    //getting the visibility (units: meters)
    public static void getVisibility(Document doc) {
        NodeList list = doc.getElementsByTagName("visibility");
        Node n = list.item(0);
        Element e = (Element) n;

        visibility = e.getAttribute("value");
        //converting from m to km.
        visibility = Double.toString(Double.parseDouble(visibility) * 0.001);
        System.out.println(visibility);
    }

    //getting the precipitation (units: millimeters)
    public static void getPrecipitation(Document doc) {
        NodeList list = doc.getElementsByTagName("precipitation");
        Node n = list.item(0);
        Element e = (Element) n;

        precipitation = e.getAttribute("value");
        precipitationMode = e.getAttribute("mode");
    }

    //getting the weather data
    public static void getWeather(Document doc) {
        NodeList list = doc.getElementsByTagName("weather");
        Node n = list.item(0);
        Element e = (Element) n;

        weatherNumber = e.getAttribute("number");
        weatherValue = e.getAttribute("value");
        weatherIcon = e.getAttribute("icon");
    }

    //getting the last time the data was updated.
    public static void getLastUpdate(Document doc) {
        NodeList list = doc.getElementsByTagName("lastupdate");
        Node n = list.item(0);
        Element e = (Element) n;

        lastUpdate = e.getAttribute("value");
    }
}
