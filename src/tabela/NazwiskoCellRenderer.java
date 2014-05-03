package tabela;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/*
**  Center the text and highlight the focused cell
*/
public class NazwiskoCellRenderer extends DefaultTableCellRenderer
{
	public NazwiskoCellRenderer()
	{
		setVerticalAlignment( CENTER );
	}

	public Component getTableCellRendererComponent(
		JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		setFont(new Font(Font.SANS_SERIF,Font.BOLD,13));
		

		return this;
	}
}
