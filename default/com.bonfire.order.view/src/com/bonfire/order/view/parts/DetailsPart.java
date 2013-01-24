package com.bonfire.order.view.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class DetailsPart {
	@PostConstruct
	  public void createControls(Composite parent) {
		Table table = new Table(parent, SWT.MULTI/* | SWT.BORDER | SWT.FULL_SELECTION*/);
		table.setHeaderVisible (true);
		String[] columns = new String[]{"ID", "Side", "Symbol", "Qty", "Cum Qty"};
		
		for(String column:columns){
			TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
			tableColumn.setText(column);
		}
		
		for(TableColumn tableColumn:table.getColumns()){
			tableColumn.pack();
		}
	}
}
