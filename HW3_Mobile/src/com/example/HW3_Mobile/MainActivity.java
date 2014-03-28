package com.example.HW3_Mobile;

import com.example.accel.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

/**
 * Implementing an image downloader app. The app will consist of two fragments:
 * one for the image URL and the other for displaying the image
 * 
 * @author Carl Barbee
 * @assignment Homework 3
 */
public class MainActivity extends FragmentActivity {

	protected ImageFragment imagFrag;
	protected URLFragment urlFrag;
	boolean sensorChanged = false;

	/**
	 * Creates the layouts for the fragments.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageValidator imageValidator = new ImageValidator();
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}