package com.bonfire.order.view.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class OverviewPart {
	private Label label;
	public OverviewPart(){
		System.out.println("Creating OverviewPart");
	}
	
	@Inject
	public OverviewPart(Composite parent){
		System.out.println("Got composite via DI.");
		System.out.println("Layout: " + parent.getLayout().getClass());
	}
	
	@PostConstruct
	public void createControls(Composite parent) {
		label = new Label(parent,SWT.NONE);
		label.setText("A text....");
	} 
	
	@Focus
	private void setFocus() {
		label.setFocus();
	} 
	
	@PreDestroy
	public void dispose(){
		System.out.println("Closing OverviewPart!!");
	}
}
