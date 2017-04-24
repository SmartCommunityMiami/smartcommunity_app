package edu.miami.c11173414.smartcommunitydrawer;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

class DecodeLocation extends AsyncTask<Location,Void,String> {
    //-----------------------------------------------------------------------------
    private Context theContext;
    private Activity theActivity;

    //-----------------------------------------------------------------------------
    public DecodeLocation(Context context, Activity activity) {

        theContext = context;
        theActivity = activity;
    }

    //-----------------------------------------------------------------------------
    protected String doInBackground(Location... location) {

        return (geodecode(location[0]));
    }

    //-----------------------------------------------------------------------------
    protected void onPostExecute(String result) {
        try {
            ((TextView) theActivity.findViewById(R.id.current_location)).setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //-----------------------------------------------------------------------------
    private String geodecode(Location thisLocation) {

        String locationName = "";

        // locationName = "Android says\n" + androidGeodecode(thisLocation) + "\n\n";
        // locationName += "Google says\n" + googleGeodecode(thisLocation,"https://maps.googleapis.com/maps/api/geocode/xml?sensor=true&latlng=%f,%f");
        locationName += googleGeodecode(thisLocation,"https://maps.googleapis.com/maps/api/geocode/xml?sensor=true&latlng=%f,%f");

//----Aaaargh, can't get this to work
//theActivity.getString(R.string.google_location_url));

        return (locationName);
    }

    //-----------------------------------------------------------------------------
    private String androidGeodecode(Location thisLocation) {

        Geocoder androidGeocoder;
        List<Address> addresses;
        Address firstAddress;
        String addressLine;
        String locationName;
        int index;

        if (Geocoder.isPresent()) {
            androidGeocoder = new Geocoder(theContext);
            try {
                addresses = androidGeocoder.getFromLocation(
                        thisLocation.getLatitude(), thisLocation.getLongitude(), 1);
                if (addresses.isEmpty()) {
                    return ("ERROR: Unkown location");
                } else {
                    firstAddress = addresses.get(0);
                    locationName = "";
                    index = 0;
                    while ((addressLine = firstAddress.getAddressLine(index)) != null) {
                        locationName += addressLine + ", ";
                        index++;
                    }
                    return (locationName);
                }
            } catch (Exception e) {
                return ("ERROR: " + e.getMessage());
            }
        } else {
            return ("ERROR: No Geocoder available");
        }
    }

    //-----------------------------------------------------------------------------
    private String googleGeodecode(Location thisLocation,
                                   String urlFormat) {

        String urlString;
        URL url;
        URLConnection urlConnection;
        HttpURLConnection httpConnection;
        InputStream xmlStream;
        DocumentBuilder xmlBuilder;
        Document xmlDocument;
        Element documentElement;
        NodeList nameNodes;
        Node nameNode;
        String theLocationName;

        urlString = String.format(urlFormat, thisLocation.getLatitude(),
                thisLocation.getLongitude());
        try {
            url = new URL(urlString);
            urlConnection = url.openConnection();
            if (!(urlConnection instanceof HttpURLConnection)) {
                return ("ERROR: That's not an HTTP connection");
            }
            httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(true);
            httpConnection.setReadTimeout(10000);
            try {
                httpConnection.connect();
            } catch (IOException e) {
                return ("ERROR: Could not connect");
            }

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                xmlStream = httpConnection.getInputStream();
                xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                xmlDocument = xmlBuilder.parse(xmlStream);
                xmlStream.close();
                httpConnection.disconnect();
                if ((documentElement = xmlDocument.getDocumentElement()) == null) {
                    return ("ERROR: Missing element");
                }
                nameNodes = documentElement.getElementsByTagName("formatted_address");
                if (nameNodes.getLength() < 1) {
                    return ("ERROR: No name for this location");
                }
                nameNode = nameNodes.item(0).getFirstChild();
                if (nameNode == null) {
                    return ("ERROR: No known name");
                }
                theLocationName = nameNode.getNodeValue();
                return (theLocationName);
            } else {
                return ("ERROR: Bad connection");
            }
        } catch (Exception e) {
            return ("ERROR: Error while geodecoding");
        }
    }
}