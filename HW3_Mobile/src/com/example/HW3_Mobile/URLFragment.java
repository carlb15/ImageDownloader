package com.example.HW3_Mobile;

import java.net.MalformedURLException;
import java.net.URL;
import com.example.accel.R;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * The URL fragment for displaying the accelerometer values and reading the
 * accelerometer. Sends the JPG file in to an Async Thread for uploading the
 * image to the Image Fragment.
 * 
 * @author Carl Barbee
 * @assignment Homework 3
 */
public class URLFragment extends Fragment implements SensorEventListener {

	private float x, y, z;
	/** Minimum movement force to consider. */
	private static final int MIN_FORCE = 10;
	/**
	 * Minimum times in a shake gesture that the direction of movement needs to
	 * change.
	 */
	private static final int MIN_DIRECTION_CHANGE = 3;

	/** Maximum pause between movements. */
	private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 300;

	/** Maximum allowed time for shake gesture. */
	private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;

	/** Time when the gesture started. */
	private long mFirstDirectionChangeTime = 0;

	/** Time when the last movement started. */
	private long mLastDirectionChangeTime;

	/** How many movements are considered so far. */
	private int mDirectionChangeCount = 0;

	/** The last x position. */
	private float lastX = 0;

	/** The last y position. */
	private float lastY = 0;

	/** The last z position. */
	private float lastZ = 0;
	/** Alpha value for the accelerometer */
	private final float ALPHA = 0.2f;
	/** Gravity sensor values for low pass filter. */
	protected float[] gravSensorVals;
	/** Textviews for each acclerometer */
	private TextView tX, tY, tZ;
	/** Sensor manager for accelerometer */
	private SensorManager sManager;
	/** Sensor for accelerometer */
	private Sensor accel;
	/** EditText for the image URL. */
	private EditText imageURLText;
	/** The URL string value. */
	private String urlString;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_url, container, false);

		// Accelerometer values.
		x = 0;
		y = 0;
		z = 0;

		// Set layouts for fragment
		tX = (TextView) rootView.findViewById(R.id.textView1);
		tY = (TextView) rootView.findViewById(R.id.textView2);
		tZ = (TextView) rootView.findViewById(R.id.textView3);
		imageURLText = (EditText) rootView.findViewById(R.id.editText1);
		// Start Accelerometer
		sManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		accel = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);

		// Inflate the layout for this fragment
		return rootView;
	}

	/**
	 * Pauses the accelerometer's listener for battery saving.
	 */
	@Override
	public void onPause() {
		super.onPause();
		sManager.unregisterListener(this);
	}

	/**
	 * Unregisters the accelerometer when the application is closed.
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (accel != null) {
			sManager.unregisterListener(this);
		}
	}

	/**
	 * Resumes the accelerometer's listener for battery saving.
	 */
	@Override
	public void onResume() {
		super.onResume();
		sManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_UI);
	}

	/**
	 * Handles when sensor changes for the accelerometer.
	 * 
	 * @param event
	 *          sensor event for the accelerometer
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			gravSensorVals = lowPass(event.values.clone(), gravSensorVals);
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
			tX.setText("X= " + x);
			tY.setText("Y= " + y);
			tZ.setText("Z= " + z);

			// calculate movement
			float totalMovement = Math.abs(z - lastZ);

			if (totalMovement > MIN_FORCE) {

				// get time
				long now = System.currentTimeMillis();

				// store first movement time
				if (mFirstDirectionChangeTime == 0) {
					mFirstDirectionChangeTime = now;
					mLastDirectionChangeTime = now;
				}

				// check if the last movement was not long ago
				long lastChangeWasAgo = now - mLastDirectionChangeTime;
				if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

					// store movement data
					mLastDirectionChangeTime = now;
					mDirectionChangeCount++;

					// store last sensor data
					lastX = x;
					lastY = y;
					lastZ = z;

					// check how many movements are so far
					if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

						// check total duration
						long totalDuration = now - mFirstDirectionChangeTime;
						if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE
								&& !imageURLText.getText().toString().matches("")) {

							if (ImageFragment.image.getTag().equals("")
									&& ImageValidator.validate(imageURLText.getText().toString())) {
								ImageFragment.image.setTag("1");
								uploadImage();
							}
							else if (ImageFragment.image.getTag().equals("1")) {
								ImageFragment.image.setTag("");
								ImageFragment.image.setImageDrawable(null);
							}
							resetShakeParameters();
						}
					}
				}
				else {
					resetShakeParameters();
				}
			}
		}
	}

	/**
	 * Resets the shake parameters to their default values.
	 */
	private void resetShakeParameters() {
		mFirstDirectionChangeTime = 0;
		mDirectionChangeCount = 0;
		mLastDirectionChangeTime = 0;
		lastX = 0;
		lastY = 0;
		lastZ = 0;
	}

	/**
	 * Uploads the image to fragment.
	 */
	public void uploadImage() {
		urlString = imageURLText.getText().toString();
		OnNetwork network = new OnNetwork();
		try {
			network.execute(new URL(urlString));
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Low pass filter for the accelerometer to stabilize the values.
	 * 
	 * @param input
	 *          The input buffer for accelerometer
	 * @param output
	 *          The output buffer for accelerometer
	 * @return output buffer for accelerometer
	 */
	protected float[] lowPass(float[] input, float[] output) {
		if (output == null)
			return input;
		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + ALPHA * (input[i] - output[i]);
		}
		return output;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
