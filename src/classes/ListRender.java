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
			Object[] v = new Object[1];
			v[0] = (String)value;
			comp = new JComboBox<Object>(v);
			
			// adding tool tip text for this combobox
			String productName = Gui.mainProducts.get(row).getName();
			String tipText = Gui.getValue(Gui.mainOffers, (String)value,productName );
			comp.setToolTipText(tipText);
		}
		
		return comp;
	}

}
