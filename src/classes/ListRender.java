package classes;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

// TODO: fix this
public class ListRender implements TableCellRenderer{

	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
	
		JComponent comp = new JLabel("UNK");
		try{
			comp = (JComboBox<Object>)value;
		}
		catch(java.lang.ClassCastException ex){
			comp = new JLabel((String)value);
		}
		
		return comp;
	}

}
