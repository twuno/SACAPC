package com.example.print;

import java.security.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidGPSTrackingActivity extends Activity {

	Button btnShowLocation;
	TextView lat;
	TextView lon;
	private LocationManager locationManager;
	  private String provider;
    // GPSTracker class
    GPSTracker gps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_gpstracking);
		
		btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		lat = (TextView) findViewById(R.id.txtlatitud);
		lon = (TextView) findViewById(R.id.txtlongitud);

		
				btnShowLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				gps = new GPSTracker(AndroidGPSTrackingActivity.this);
				
			    if(gps.canGetLocation()){
	                   
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                   
                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Tu Posicion es - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
                   
			}
		});
	}
}
