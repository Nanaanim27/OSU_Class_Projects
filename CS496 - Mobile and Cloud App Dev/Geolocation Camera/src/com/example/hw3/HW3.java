package com.example.hw3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class HW3 extends Activity {

	private Context HW3Context;
	
	MediaScannerConnection conn;
	Button takePic;
	Button openGal;
	Button openMap;
	public static final int IMAGE = 1;
	File topFile;
	String globalTemp;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hw3);

		takePic = (Button) findViewById(R.id.openCam);
		openGal = (Button) findViewById(R.id.openGal);
		openMap = (Button) findViewById(R.id.mapButton);
	
		topFile = new File(Environment.getExternalStorageDirectory()
				+ "/Images/GeoApp");
		if (!topFile.exists()) {
			if(topFile.mkdirs()){
				Toast.makeText(getApplicationContext(), "Directory Created at "+topFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
			}
			
		}
		//Take picture button
		takePic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startCameraIntent();
			}
		});
		//Open map Button
		openMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HW3.this, MapScreen.class);
	            startActivity(intent);
			}
		
		});
		//Open Photo Gallery Button
		openGal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//This code simply opens the default photo gallery app. 
				Intent i=new Intent(Intent.ACTION_PICK);
	            i.setType("image/*");
	            startActivity(i);
			}
		
		});
		
	
	}
	/*
	 * Intent to start built in camera called when the take picture 
	 * button is clicked by user. 
	 */
	private void startCameraIntent() {
		//TODO: make a more clever uniquifier.
		
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Random random = new Random();
		int x = random.nextInt(10000);
		String ext = String.valueOf(x);
		
		globalTemp = ext;
		
		String pathAppend = "/Images/GeoApp/Image_" + ext + ".jpg";
		File fileUri = new File(Environment.getExternalStorageDirectory(), pathAppend);
		//Store the image
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileUri));
		startActivityForResult(cameraIntent, IMAGE);
		
	}
	/*
	 * If the user saves the image the file path is preserved and the latitude
	 * and longitude are fetched, and passed to a geotagging function
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {

			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location location = (Location) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			double longitude = location.getLongitude();
			double latitude = location.getLatitude();
			
			String pathAppend = "/Images/GeoApp/Image_" + globalTemp + ".jpg";
			
			File file = new File(Environment.getExternalStorageDirectory(), pathAppend);
			
			geoTag(file.getAbsolutePath(), latitude, longitude);
			
		}
	}
	/*
	 * Function that adds a GeoTag to each image using Exchangable Image
	 * Format tags. ExifInterface expects DMS encoding so this function 
	 * converts degrees to DMS and sets the attributes
	 */
	public void geoTag(String filePath, double lat, double lon) {
		ExifInterface exif;
		
		try {
			exif = new ExifInterface(filePath);
			
			int num1Lat = (int) Math.floor(lat);
			int num2Lat = (int) Math.floor((lat - num1Lat) * 60);
			double num3Lat = (lat - ((double) num1Lat + ((double) num2Lat / 60))) * 3600000;

			int num1Lon = (int) Math.floor(lon);
			int num2Lon = (int) Math.floor((lon - num1Lon) * 60);
			double num3Lon = (lon - ((double) num1Lon + ((double) num2Lon / 60))) * 3600000;
			
			exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, num1Lat + "/1,"
					+ num2Lat + "/1," + num3Lat + "/1000");
			exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, num1Lon + "/1,"
					+ num2Lon + "/1," + num3Lon + "/1000");
		
			
			exif.saveAttributes();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	
}
