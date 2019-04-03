package com.example.android.earthquakeapp;
import android.graphics.drawable.GradientDrawable;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class detailAdapter extends ArrayAdapter<details> {
@NonNull
@Override
public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
{ View listItemView = convertView;
    if(listItemView == null) {
        listItemView = LayoutInflater.from(getContext()).inflate(
                R.layout.list_layout, parent, false);
    }
  details current=getItem(position);

    double MagnitudeInput = current.getmMag(); // A double that stores the magnitude of the earthquake
    String output = formatMagnitude(MagnitudeInput); // A helper method to format the decimal value
    TextView mag= listItemView.findViewById(R.id.magnitude);

    mag.setText(output); // set the text in the required format

    // Set the proper background color on the magnitude circle.
    // Fetch the background from the TextView, which is a GradientDrawable.
    GradientDrawable magnitudeCircle = (GradientDrawable) mag.getBackground();

    // Get the appropriate background color based on the current earthquake magnitude
    int magnitudeColor = getMagnitudeColor(current.getmMag());

    // Set the color on the magnitude circle
    magnitudeCircle.setColor(magnitudeColor);



    String locationInput=current.getmLocation(); // The current location data from JSON

    String LocationOffset ; // To Store Location Offset
    String PrimaryLocation ; //To Store Primary Location
    if(locationInput.contains(" of "))  // If the location contains the string " of " it means it nee dto split from this point
    {
        String[] parts = locationInput.split(" of ");  //The function split returns an array of strings split from given string " of "
        LocationOffset = parts[0] + " of"; // ADD " of" because split function would have removed it
        PrimaryLocation = parts[1]; //The part after " of " is the primary location
    }
    else // if " of " is absent ,the input location itself is the primary location
    {
        LocationOffset = "Near the "; // Use this string as the location offset
        PrimaryLocation=locationInput;
    }
    // Find the TextView with view ID PrimaryLocation
    TextView Primarylocation=(TextView)listItemView.findViewById(R.id.PrimaryLocation);
    // Set the required text
    Primarylocation.setText(PrimaryLocation);

    TextView Locationoffset=(TextView)listItemView.findViewById(R.id.locationOffset);
    Locationoffset.setText(LocationOffset);




    Date dateObject = new Date(current.getmDate());

    // Find the TextView with view ID date
    TextView dateView = (TextView) listItemView.findViewById(R.id.date);
    // Format the date string (i.e. "Mar 3, 1984")
    String formattedDate = formatDate(dateObject);
    // Display the date of the current earthquake in that TextView
    dateView.setText(formattedDate);




    // Find the TextView with view ID time
    TextView timeView = (TextView) listItemView.findViewById(R.id.time);
    // Format the time string (i.e. "4:30PM")
    String formattedTime = formatTime(dateObject);
    // Display the time of the current earthquake in that TextView
    timeView.setText(formattedTime);



    return listItemView;
}
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private String formatMagnitude(double magObject) {
        DecimalFormat formatter =new DecimalFormat("0.0"); //  an object of the decimal formatter where we specify the pattern
        return formatter.format(magObject); //return the formatted string
    }

    private int getMagnitudeColor(double magObject)
    {
        int output;
        switch ((int)magObject)
        {
            case 0:
            case 1: {
                output= R.color.magnitude1;
                break;
            }
            case 2: {
                output= R.color.magnitude2;
                break;
            }
            case 3: {
                output= R.color.magnitude3;
                break;
            }
            case 4: {
                output= R.color.magnitude4;
                break;
            }
            case 5: {
                output= R.color.magnitude5;
                break;
            }
            case 6: {
                output= R.color.magnitude6;
                break;
            }
            case 7: {
                output= R.color.magnitude7;
                break;
            }
            case 8: {
                output= R.color.magnitude8;
                break;
            }
            case 9: {
                output= R.color.magnitude9;
                break;
            }
            default: {
                output= R.color.magnitude10plus;
                break;
            }
        }
       return ContextCompat.getColor(getContext(), output);

    }
public detailAdapter(Activity context, ArrayList<details> earthquakes) {
    super(context, 0,earthquakes);
}
}
