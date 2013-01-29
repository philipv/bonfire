package com.bonfire.order.view.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;

public class TestHandler {

	@Execute
	  public void execute(@Named("com.bonfire.order.view.commandparameter.input")String param) {
	    System.out.println("Calling the testhandler with param : " + param);
	  }
}
