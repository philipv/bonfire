package com.bonfire.order.view.parts;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.workbench.swt.modeling.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class DetailsPart {
	
	@PostConstruct
	  public void createUi(Composite parent, EMenuService service) {
	    final Text text = new Text(parent, SWT.BORDER);
	    text.setText("Hello");
	    // Make use to use the correct ID 
	    // from the application model
	    service.registerContextMenu(text, 
	        "com.bonfire.order.view.popupmenu.table");
	    
	    Table table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    service.registerContextMenu(table, 
		        "com.bonfire.order.view.popupmenu.table");
	    String[] columns = new String[] { "ID", "Side", "Symbol", "Qty",
	    "Cum Qty" };
	    String[] data = new String[] { "Order-1", "Buy", "6758.T", "1000",
	    "200" };
	    
	    for (String column : columns) {
	    	TableColumn tableColumn = new TableColumn(table, SWT.LEFT);
	    	tableColumn.setText(column);
	    }
	    
	    for (int i=0;i<5;i++) {
	    	TableItem tableItem = new TableItem(table, SWT.LEFT);
	    	tableItem.setText(data);
	    }
	    
	    for (TableColumn tableColumn : table.getColumns()) {
	    	tableColumn.pack();
	    }
	  }
}
