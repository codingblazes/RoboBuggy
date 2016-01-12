package com.roboclub.robobuggy.ui;

public class AnalyticsPanel extends RoboBuggyGUIContainer {
	private static final long serialVersionUID = 7017667286491619492L;

	private DataPanel dataPanel;
	private GraphPanel graphPanel;
	
	public AnalyticsPanel() {
		name = "analytics";
		dataPanel = new DataPanel();
		graphPanel = new GraphPanel();
		this.addComponent(dataPanel, 0, 0, 1, .6);
		this.addComponent(graphPanel, 0, .6, 1, .4);

	}
	
	public String valuesFromData()
	{
	  return dataPanel.getValues();	
	}
	
}
