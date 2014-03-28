package com.example.HW3_Mobile;

import java.io.InputStream;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Uploads the image to the Image Fragment from the edit text on the URL
 * fragment.
 * 
 * @author Carl Barbee
 * @assignment Homework 3
 */
class OnNetwork extends AsyncTask<URL, String, Bitmap> {

	// you can report progress from the doInBackgroud() periodically
	// if you do then the progress will supplied to this method via String[] or
	// any other array.
	// in order to report progress you should call publishProgress(...) inside the
	// doInBackground()
	protected void onProgressUpdate(String... progress) {

	}

	// this method will run after the doInBackground() completes
	// the parameter supplied is the one returned by the doInBackground()
	// note that this method runs on GUI thread
	protected void onPostExecute(Bitmap result) {
		ImageFragment.setImage(result);
	}

	// This method runs on a separate thread
	@Override
	protected Bitmap doInBackground(URL... url) {

		// needs URL to load the image.

		URL urldisplay = url[0];
		Bitmap image = null;
		try {
			InputStream in = urldisplay.openStream();
			image = BitmapFactory.decodeStream(in);
		}
		catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return image;

	}

}
