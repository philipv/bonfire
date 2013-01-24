package com.bonfire.order.view.parts;

import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

public class OverviewPart {
	public OverviewPart(){
		System.out.println("Creating OverviewPart");
	}
	
	@Inject
	public OverviewPart(Composite parent){
		System.out.println("Got composite via DI.");
		System.out.println("Layout: " + parent.getLayout().getClass());
	}
}
