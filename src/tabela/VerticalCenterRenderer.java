package tabela;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
**  Center the text and highlight the focused cell
*/
public class VerticalCenterRenderer extends DefaultTableCellRenderer
{
	public VerticalCenterRenderer()
	{
		setVerticalAlignment( CENTER );
	}
}
