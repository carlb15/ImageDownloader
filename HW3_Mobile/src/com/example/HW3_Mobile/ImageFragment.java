package com.example.HW3_Mobile;

import com.example.accel.R;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Image Fragment for the app. Displays the uploaded image or a default image.
 * 
 * @author Carl Barbee
 * @assignment Homework 3
 */
public class ImageFragment extends Fragment {

	public static ImageView image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image, container, false);
		image = (ImageView) view.findViewById(R.id.imageView1);
		image.setTag("");
		// Inflate the layout for this fragment
		return view;
	}

	/**
	 * Displays the uploaded image on the fragment.
	 * 
	 * @param bmp
	 *          uploaded image.
	 */
	public static void setImage(Bitmap bmp) {
		image.setImageBitmap(bmp);
	}
}