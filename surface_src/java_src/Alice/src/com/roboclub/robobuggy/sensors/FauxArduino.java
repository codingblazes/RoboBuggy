package com.roboclub.robobuggy.sensors;

import java.nio.ByteBuffer;

import com.roboclub.robobuggy.main.Robot;
import com.roboclub.robobuggy.messages.EncoderMeasurement;
import com.roboclub.robobuggy.messages.WheelAngleCommand;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.Subscriber;
import com.roboclub.robobuggy.serial.SerialEvent;
import com.roboclub.robobuggy.serial.SerialListener;

public class FauxArduino {
	
	private int encReset;
	private int encTickLast;
	private int encTime;

	// Set up publishers
	private Publisher encoderPub = new Publisher("/sensor/encoder");
	private Subscriber wheelAngleSub = new Subscriber("/actuator/wheelAngle", new wheelAngleCallback());
	private Subscriber brakeSub = new Subscriber("/actuator/brake", new wheelAngleCallback());
	
	private class wheelAngleCallback implements MessageListener {
		@Override
		public void actionPerformed(Message m) {
			WheelAngleCommand wac = (WheelAngleCommand) m;
			System.out.printf("Wheel commanded to position %d\n", wac.angle);
		}
	}

	private class brakeCallback implements MessageListener {
		@Override
		public void actionPerformed(Message m) {
			WheelAngleCommand wac = (WheelAngleCommand) m;
			System.out.printf("Wheel commanded to position %d\n", wac.angle);
		}
	}
	
	public FauxArduino() {
		System.out.println("Initializing Fake Arudino!!");
		
		int distance = 0;
		while(true) {
			try { Thread.sleep(500); } catch(InterruptedException ie) 
			{ throw new RuntimeException("Sleep should not be throwing"); }	

			int speed = 5;
			distance += speed;
			encoderPub.publish(new EncoderMeasurement(distance, speed));
		}
	}
}