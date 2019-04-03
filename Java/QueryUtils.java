package com.example.android.earthquakeapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {

    }


    /*This is a static method.We donot need any object to call this method.The method is associated with the class itself.
    * The method will return an ArrayList of earthquakes*/
    public static ArrayList<details> extractEarthquakes(String requestUrl) {

        URL url;
        url = createUrl(requestUrl);
        String json= null;
        try{
                json =makeHttpRequest(url);
        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Error in closing input stream", e);
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<details> earthquakes = extractFeatureFromJson(json);


        return earthquakes;
    }

    private static URL createUrl(String stringurl) // private will help in not allowing any other class to call this function and static will allow this method to be called without instance
    {
        URL url=null;
        try
        {
            url = new URL(stringurl); //Convert string into url using the constructor of the URL class
        }
        catch (MalformedURLException e)
        {
            Log.e(LOG_TAG,"Error in creating url",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException
    {
      String jsonResponse = "";
      if(url==null)
          return jsonResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
   private static ArrayList<details> extractFeatureFromJson(String jsonResponse)
   {
       if(jsonResponse=="")
           return null;

       ArrayList<details> earthquakes = new ArrayList<details>();
       try {
           JSONObject root =new JSONObject(jsonResponse); //this is the object of the base json received via the api
           JSONArray earthquakeArray =root.getJSONArray("features"); //Now we go to the array named features.
           // This array contains list of all earthquakes
           for(int i=0;i<earthquakeArray.length();i++) //We loop through each element of the features array
           {
               JSONObject currentEarthquake = earthquakeArray.getJSONObject(i); //ith index of features array gets converted into an object
               JSONObject properties=currentEarthquake.getJSONObject("properties"); //Each index has an object called properties
               double magnitude=properties.getDouble("mag");/*Extract the properties
                                                                                 one by one */
               long time=properties.getLong("time");
               String place = properties.getString("place");
               String url = properties.getString("url");
               details earthquake=new details(magnitude,place,time,url);// create a new details object and add these details
               earthquakes.add(earthquake); // this object is added to earthquakes ArrayList
           }

return earthquakes;
       } catch (JSONException e) {
           // If an error is thrown when executing any of the above statements in the "try" block,
           // catch the exception here, so the app doesn't crash. Print a log message
           // with the message from the exception.
           Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
       }
     return null;
   }
}
