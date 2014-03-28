package com.example.HW3_Mobile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks for valid image url.
 * 
 * @author Carl Barbee
 * @assignment Homework 3
 */
public class ImageValidator {
	/** Pattern for the regex. */
	private static Pattern pattern;
	/** Matcher for the regex */
	private static Matcher matcher;

	private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";

	/**s
	 * Constructor for the image validator.
	 */
	public ImageValidator() {
		pattern = Pattern.compile(IMAGE_PATTERN);
	}

	/**
	 * Validate image with regular expression
	 * 
	 * @param image
	 *          image for validation
	 * @return true valid image, false invalid image
	 */
	public static boolean validate(String image) {
		matcher = pattern.matcher(image);
		return matcher.matches();
	}
}
