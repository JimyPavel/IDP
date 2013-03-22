package classes;
import javax.swing.table.DefaultTableModel;

public class TableModel extends DefaultTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TableModel(int row, int col){
		super(row,col);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	@Override
	public int getColumnCount() {
		return super.getColumnCount();
	};

	@Override
	public Object getValueAt(int row, int column) {
		return super.getValueAt(row, column);
	}
}
