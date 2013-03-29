package classes;

import java.awt.Component;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

// class for rendering the progress bar
public class ProgressBarRender implements TableCellRenderer {

	JProgressBar progressBar;
	public ProgressBarRender(){
		this.progressBar = new JProgressBar();
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object progressBar,
			boolean isSelected, boolean hasFocus, int row, int col) {
		
		this.progressBar = (JProgressBar)progressBar;
        return this.progressBar;
	}

}
