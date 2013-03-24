package classes;

import java.util.List;

import javax.swing.SwingWorker;

public class ExportTask extends SwingWorker<Integer, Integer> {
	  private static final int DELAY = 1000;

	  public ExportTask() {
	  }

	  @Override
	  protected Integer doInBackground() throws Exception {
		// TODO 3.2
		 int DELAY = 1000;
		int count = 10;
		int i     = 0;
		try {
		    while (i < count) {
		        i++;
			Thread.sleep(DELAY);
			publish(i);
			setProgress(i);
		    }
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
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