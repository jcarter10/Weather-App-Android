package com.example.weatherApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.app.AlertDialog.THEME_HOLO_DARK;

public class MainActivity extends AppCompatActivity {

    // globals
    static public int matchFound;
    static public String city;
    static public String country;
    static public String id;
    public boolean errorFound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //background doesn't fit correctly when setting it in the xml file for this activity, setting it programmatically.
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activityBackground);
        layout.setBackgroundResource(R.drawable.homescreen_kenrick_mills_unsplash);

        //removing restriction to allow network operations on thread.
        if (android.os.Build.VERSION.SDK_INT > 9)
         {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

    //Called when the user taps the Enter button
    public void clickedButton(View view) {

        TextView text = (TextView) findViewById(R.id.locationText);
        String input = text.getText().toString();

        //calling function to get locationID.
        //function will also determine if location is real or not.
        getLocationID(input);

        //if there was an error found in the input, jump out of function.
        if (errorFound == true) {
            return;
        }

        //if the location is correct, go to the next activity.
        if (matchFound == 1) {
            startActivity(new Intent(MainActivity.this, WeatherActivity.class));

        }
    }


    //getting user input, and finding the corresponding city id (have to add a "context" in parameters because this is a java class and not an activity).
    public void getLocationID(String input) {

        //Getting input from user.
        String theInput = input;
        //removing whitespace (if any) from user input.
        theInput = theInput.trim();

        //checking for input errors.
        checkForInputErrors(theInput);

        //if there was an error found in the input, jump out of function.
        if (errorFound == true) {
            return;
        }

        //seperates the city and country into their respective strings.
        city = theInput.substring(0, theInput.indexOf(","));
        country = theInput.substring(theInput.indexOf(",") + 1, theInput.length());

        //removing whitespace (if any).
        city = city.trim();
        country = country.trim();

        //searching the list of cities for a match.
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getAssets().open("cityList.txt")));

            //variables
            String str;
            String tempStr;
            String curCity;
            String curCountry;
            String curID = "";

            //reading each line of the file.
            while ((str = br.readLine()) != null) {

                //trimming the lines whitespace.
                str = str.trim();

                //if it's some kind of JNON notation (brackets), skip it.
                if (str.length() > 2) {

                    //removing quotations from "name"
                    tempStr = str.substring(1, str.indexOf(58));
                    tempStr = tempStr.substring(0, tempStr.length() - 1);

                    //for checking the name row.
                    if (tempStr.equals("name") == true) {

                        //grabbing city name from current row.
                        curCity = str.substring(9, str.length() - 2);

                        //checking if the city name matches input given.
                        if (curCity.equalsIgnoreCase(city) == true) {

                            //parsing the lines to look at country.
                            str = br.readLine();
                            str = br.readLine();

                            //checking the if the country matches now.
                            curCountry = str.substring(16, str.length() - 2);

                            //checking if the country name matches input given.
                            if (curCountry.equalsIgnoreCase(country) == true) {
                                //setting the ID because country and city matches input.
                                id = curID;
                                matchFound = 1;
                                break;
                            }
                            //when no match found.
                            else {
                                matchFound = 0;
                            }
                        }
                        //when no match found.
                        else {
                            matchFound = 0;
                        }
                    }
                    //gets the current location id.
                    else if (tempStr.equals("id") == true) {
                        curID = str.substring(6, str.length() - 1);
                    }
                }
            }

            //if location was not in JNON, sends a message to try again.
            if (matchFound == 0) {
                sendTryAgainMessage();
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    //checks if the user input string is wrong.
    public void checkForInputErrors(String input) {
        boolean comma = false;
        String afterComma;
        char c;

        //check if string is empty
        if (input.isEmpty() == true) {
            sendErrorMessage();
            return;
        }

        //checking for a comma, since string is not empty.
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            if (c == ',') {
                comma = true;
            }
        }

        //no comma, so send error.
        if (comma == false) {
            sendErrorMessage();
            return;
        }

        //since comma is found, checking for the country code after comma.
        afterComma = input.substring(input.lastIndexOf(',') + 1);
        afterComma = afterComma.trim();
        if (afterComma.isEmpty() == true || afterComma.length() == 1 || afterComma.length() > 2) {
            sendErrorMessage();
            return;
        }

    }

    //sends an error message to the activity and cancels the button click event.
    public void sendErrorMessage() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this, THEME_HOLO_DARK);
        builder1.setMessage("Error!\nPlease enter a valid input!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        restartActivity();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        errorFound = true;
        return;
    }


    //sends a try again message, because location is not in JNON file.
    public void sendTryAgainMessage() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this, THEME_HOLO_DARK);
        builder1.setMessage("Sorry, the location you are trying to find is not in the database...\nPlease try again!");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        restartActivity();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

        errorFound = true;
        return;
    }

    //restarts activity to it's base format.
    public void restartActivity() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }


}