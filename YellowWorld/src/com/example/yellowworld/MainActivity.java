/* Get my speed; 
 * Calculate my distance from beginning
 * Get best provider */

package com.example.yellowworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	  private TextView latituteField;
	  private TextView longitudeField;
	  private TextView printProv;
	  private TextView startLatituteField;
	  private TextView startLongitudeField;
	  
	  private LocationManager locationManager;
	  private Location location;
	  private String provider;
	  private float lat;
	  private float lng;
	  private float startLatitute;
	  private float startLongitude;
	  
	  static final String SAVED_LATITUTE = "saved lat";
	  static final String SAVED_LONGITUDE = "saved long";
      
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        latituteField = (TextView) findViewById(R.id.TextView02);
        longitudeField = (TextView) findViewById(R.id.TextView04);
        printProv = (TextView) findViewById(R.id.Print2);
        startLatituteField = (TextView) findViewById(R.id.TextView06);
        startLongitudeField = (TextView) findViewById(R.id.TextView08);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
          System.out.println("Provider " + provider + " has been selected.");
          onLocationChanged(location);
        } else {
          latituteField.setText("Location not available");
          longitudeField.setText("Location not available");
        }
        
        // Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            // Restore value of members from saved state
            startLatitute = savedInstanceState.getFloat(SAVED_LATITUTE);
            startLongitude = savedInstanceState.getFloat(SAVED_LONGITUDE);

	        startLatituteField.setText(String.valueOf(startLatitute));
	        startLongitudeField.setText(String.valueOf(startLongitude));
        } else { 
        	// Prints defaults
        }
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
      locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
      lat = (float) (location.getLatitude());
      lng = (float) (location.getLongitude());
      
      latituteField.setText(String.valueOf(lat));
      longitudeField.setText(String.valueOf(lng));
      printProv.setText(provider);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
      // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Enabled new provider " + provider,
          Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "Disabled provider " + provider,
          Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void addStart(View view) {
    	// Save start location
    	
    	if (location != null){
	    	startLatitute = lat;
	        startLongitude = lng;
	        startLatituteField.setText(String.valueOf(startLatitute));
	        startLongitudeField.setText(String.valueOf(startLongitude));
    	} else {
	        startLatituteField.setText("Location not available");
	        startLongitudeField.setText("Location not available");
    	}
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current start state
        savedInstanceState.putFloat(SAVED_LATITUTE, startLatitute);
        savedInstanceState.putFloat(SAVED_LONGITUDE, startLongitude);
        
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }
    
}
