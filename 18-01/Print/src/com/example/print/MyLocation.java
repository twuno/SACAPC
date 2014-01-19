package com.example.print;

import java.util.List;

import com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


public class MyLocation {
	Location loca = new Location(LocationManager.GPS_PROVIDER);
	Context c;
	
	public MyLocation(Context c) {
		super();
		this.c = c;
	}

	public void Posicion(Context c)
	{
		LocationManager lm = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
		Location loc= new Location(LocationManager.GPS_PROVIDER);//lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		//mostrarPosicion(loc);
		LocationListener locListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				mostrarPosicion(location);
			}
			
			
		};
		
		  lm.requestLocationUpdates(
                   LocationManager.GPS_PROVIDER, 30000, 0, locListener);
	}
	private void mostrarPosicion(Location loc)
	{
		if (loc != null){
			loca=loc;
		}else
		{
			loca.setLatitude(0.0);
			loca.setLongitude(0.0);
		}
	}
}
	    

