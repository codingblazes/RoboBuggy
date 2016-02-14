package com.roboclub.robobuggy.ui;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.roboclub.robobuggy.messages.EncoderMeasurement;
import com.roboclub.robobuggy.messages.ImuMeasurement;
import com.roboclub.robobuggy.ros.Message;
import com.roboclub.robobuggy.ros.MessageListener;
import com.roboclub.robobuggy.ros.NodeChannel;
import com.roboclub.robobuggy.ros.Subscriber;
import com.sun.javafx.geom.Vec2d;

public class ImuPitchGraph extends RobobuggyGUIContainer{
	ArrayList<Vec2d> list = new ArrayList();
	ChartPanel chartPanel;
	JFreeChart chart;

	
	public ImuPitchGraph(){
		XYSeries series1 = new XYSeries("Planned");
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		

		new Subscriber(NodeChannel.IMU.getMsgPath(), new MessageListener() {
		
			@Override
			public void actionPerformed(String topicName, Message m) {
				ImuMeasurement imuM = (ImuMeasurement)m;
				series1.add(imuM.getTimestamp().getTime(), imuM.getYaw());
				

				

				

		//		repaint();
			}
		});
		
		chart = ChartFactory.createXYLineChart("Pitch", "xAxisLabel", "yAxisLabel", dataset, PlotOrientation.VERTICAL, true, true, true);					
		chartPanel = new ChartPanel(chart);			
		add(chartPanel);

		  Thread thread = new Thread(){
			    public void run(){
			    	while(true){
			    		//TODO get sizing to be better 
			    		JFreeChart chart = ChartFactory.createXYLineChart("Pitch", "xAxisLabel", "yAxisLabel", dataset, PlotOrientation.VERTICAL, true, true, true);					
				/*        XYPlot xyPlot = (XYPlot) chart.getPlot();
				        NumberAxis domainAxis = (NumberAxis) xyPlot.getRangeAxis();
				        NumberAxis rangeAxis = (NumberAxis) xyPlot.getDomainAxis();
						domainAxis.setAutoRange(true);
						rangeAxis.setAutoRange(true);
						*/
			    		chartPanel = new ChartPanel(chart);	
			    		try {
			    			this.sleep(1000);
			    		} catch (InterruptedException e) {
			    			// TODO Auto-generated catch block
			    			e.printStackTrace();
			    		}
			    	}
					}
			  };
			  
			  thread.start();

		
		
		

	}
	
	
  

}
