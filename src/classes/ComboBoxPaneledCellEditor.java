package classes;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;
 
public class ComboBoxPaneledCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor, Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Object> comboBox;
 
    public ComboBoxPaneledCellEditor(JComboBox<Object> comboBox) {
    	System.out.println("constructor");
        this.comboBox = comboBox;
        this.comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        // hitting enter in the combo box should stop cellediting (see below)
        this.comboBox.addActionListener(this);
    }
 
    private void setValue(Object value) {
    	System.out.println("setValue");
        comboBox.setSelectedItem(value);
    }
 
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
    	System.out.println("actionPerformed");
        // Selecting an item results in an actioncommand "comboBoxChanged".
        // We should ignore these ones.
 
        // Hitting enter results in an actioncommand "comboBoxEdited"
        if(e.getActionCommand().equals("comboBoxEdited")) {
            stopCellEditing();
        }
    }
 
    // Implementing CellEditor
    @Override
    public Object getCellEditorValue() {
    	System.out.println("getCellEditorValue");
        return comboBox.getSelectedItem();
    }
 
    @Override
    public boolean stopCellEditing() {
    	System.out.println("stopCellEditing");
        if (comboBox.isEditable()) {
            // Notify the combo box that editing has stopped (e.g. User pressed F2)
            comboBox.actionPerformed(new ActionEvent(this, 0, ""));
        }
        fireEditingStopped();
        return true;
    }
 
    @Override
    public Component getTableCellEditorComponent(javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
    	System.out.println("getTablee....Component");
    	setValue(value);
        return comboBox;
    }
 
}
