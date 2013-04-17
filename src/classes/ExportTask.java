package classes;

import java.util.List;

import javax.swing.SwingWorker;

public class ExportTask extends SwingWorker<Integer, Integer> {
	  int value;
	  public ExportTask(int value) {
		  this.value = value;
	  }

	  @Override
	  protected Integer doInBackground() throws Exception {
		int DELAY = 1000;
		int i     = 0;
		try {
		    while (i < this.value) {
		        i++;
				Thread.sleep(DELAY);
				publish(i);
				setProgress(i);
		    }
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		return 0;
	  }
	   
	  protected void process(List<Integer> chunks) {
	  }

	  @Override
	  protected void done() {
	    if (isCancelled())
	      System.out.println("Cancelled !");
	    else
	      System.out.println("Done !");
	  }
	}