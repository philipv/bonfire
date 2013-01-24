package com.bonfire.order.view.parts;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class DetailsPart {
	@PostConstruct
	public void createControls(Composite parent) {
		Table table = new Table(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
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
