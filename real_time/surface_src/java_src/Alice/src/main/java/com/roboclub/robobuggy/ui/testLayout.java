package com.roboclub.robobuggy.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.ArrayList;


class testLayout implements LayoutManager{
	ArrayList<ComponentData> components;
	
	testLayout(ArrayList<ComponentData> components){
		this.components = components;
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void layoutContainer(Container parent) {
		int frameWidth = parent.getWidth();
		int frameHeight = parent.getHeight();
		System.out.println("x"+parent.getX()+"y"+parent.getY()+"width"+frameWidth+"frameHeight"+frameHeight + " name"+parent.getName());
		for(int i = 0;i<this.components.size();i++){
			Component thisComponent = this.components.get(i).component;
			int x = (int)(this.components.get(i).percentageLeft*frameWidth);
			int y = (int)(this.components.get(i).percentageTop*frameHeight);
			int width = (int)(this.components.get(i).percentageWidth*frameWidth);
			int height = (int)(this.components.get(i).percentageHeight*frameHeight);
			thisComponent.setBounds(x, y, width, height);
		}
		// TODO Auto-generated method stub
		
	}
	
}