package com.example.hw3;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;

@SuppressLint("NewApi")
public class MapScreen extends FragmentActivity {
	ExifInterface exif;
	private GoogleMap mMap; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_screen);

		//Setup the GoogleMap Fragment object
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		
		/* Locations for PDF
		mMap.addMarker(new MarkerOptions().position(new LatLng(44.5673404, -123.2785331)).title("image1"));
		mMap.addMarker(new MarkerOptions().position(new LatLng(42.9393043, -122.1486882)).title("image2"));
		mMap.addMarker(new MarkerOptions().position(new LatLng(45.6321752, -121.2008741)).title("image3"));
		*/
		
		//Read throught the contents of the file where the images are saved, 
		ArrayList<File> files =  new ArrayList<File>();
		String pathAppend = "/Images/GeoApp/";
		File dir = new File(Environment.getExternalStorageDirectory(), pathAppend);
		
		//Save to array list
		for(File child: dir.listFiles()){
			files.add(child);
		}
		
		//Loop through each file and extract Geotag data
		for(int i=0; i < files.size(); i++){
			try {
				exif = new ExifInterface(files.get(i).getAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			String lat = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
			String lon = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
			
			//convert back to decimal degrees
			double latDec = convertHourToDecimal(lat);
			double lonDec = convertHourToDecimal(lon);
			
			//add the marker to map
			mMap.addMarker(new MarkerOptions().position(new LatLng(latDec, lonDec)).title("image"));
		}
		
	}
	
	/*
	 * Google Maps API requires decimal degree format to add a marker to the map.
	 * This function parses the string given by the GPS_LATITUDE and GPS_LONGITUDE tags
	 */
	public double convertHourToDecimal(String latOrLon) { 
		
		String hours;
		String minutes;
		String seconds;
		
		if(latOrLon.length() <= 20){
			hours = latOrLon.substring(0, 2);
			minutes = latOrLon.substring(5,7);
		    seconds = latOrLon.substring(10,13) + "." + latOrLon.substring(13,15);
		}else if(latOrLon.substring(0,1).equals("-")){
			hours = latOrLon.substring(0, 4);
			minutes = latOrLon.substring(7,9);
		    seconds = latOrLon.substring(12,15) + "." + latOrLon.substring(15,17);
		}else{
			hours = latOrLon.substring(0, 3);
			minutes = latOrLon.substring(6,8);
		    seconds = latOrLon.substring(11,14) + "." + latOrLon.substring(14,16);
		}
		
	    
	    double hrs = Double.parseDouble(hours);
	    double mins = Double.parseDouble(minutes);
	    double secs = Double.parseDouble(seconds);
	    
	    double dd = (secs/36000) + (mins/60) + hrs;
	    
	    System.out.println(dd);
	   
	    return dd;
	}

}
