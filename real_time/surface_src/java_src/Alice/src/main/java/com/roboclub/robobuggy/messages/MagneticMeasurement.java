package com.roboclub.robobuggy.messages;

import java.util.Date;

/**
 * Message for passing IMU magnetic north message within BuggyROS
 * @author ?
 *
 * @version 0.5
 * 
 *          CHANGELOG: NONE
 * 
 *          DESCRIPTION: TODO
 */
public class MagneticMeasurement extends BaseMessage {

	public static final String VERSION_ID = "magV0.0";


	private double x;
	private double y;
	private double z;

	/**
	 * Constructs a new {@link MagMeasurement} at time now
	 * @param x x value
	 * @param y y value
	 * @param z z value
	 */
	public MagneticMeasurement(double x, double y, double z) {
		this.timestamp = new Date().getTime();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Returns the x value of the {@link MagMeasurement}
	 * @return the x value of the {@link MagMeasurement}
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Returns the y value of the {@link MagMeasurement}
	 * @return the y value of the {@link MagMeasurement}
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Returns the z value of the {@link MagMeasurement}
	 * @return the z value of the {@link MagMeasurement}
	 */
	public double getz() {
		return z;
	}

}