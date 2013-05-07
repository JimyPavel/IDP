package classes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import interfaces.IState;

public class TransferState implements IState{

	private Network network;
	String details;
	
	public TransferState(Network net){
		this.network = net;
	}
	
	@Override
	public void sendMessage() {
		// TODO Auto-generated method stub
		Network.logger.info("[TransferState] "+ details);
		String []info = details.split(":");
		if(info.length < 4)
		{
			return;
		}
		
		MakeProduct(info[1], info[0], info[2], info[3]);
		
		String file = info[1]+info[0]+info[2]+".txt";
		FileInputStream fi;
		
		try {
			fi = new FileInputStream(file);
			DataInputStream di = new DataInputStream(fi);
			BufferedReader br = new BufferedReader(new InputStreamReader(di)); 
			
			String c = br.readLine();
			while(c  != null){
				for(int j=0;j<c.length();j++)
				{
					String message = "[transfer]" + info[0]+":"+info[1]+":"+info[2]+":"+c.charAt(j);
					network.WriteToServer(message);
				
					network.getMediator().transfer(info[0], info[2]);
					Thread.sleep(1000);
				}
				c = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void parseInformation(String info) {
		// TODO Auto-generated method stub
		Network.logger.info("[TransferState] Message received: "+ info);
		
		String []pieces = info.split("]");
		
		if(pieces.length < 2)
		{
			Network.logger.warn("[TransferState] Wrong message received: " + info);
			return;
		}
		
		if(pieces[0].equals("[transfer")){
			
			String []infos = pieces[1].split(":");
			
			if(infos.length < 4)
			{
				Network.logger.warn("[WaitingAcceptState] Wrong message received: " + info);
				return;
			}
			
			
			String file = infos[0]+infos[1]+infos[2]+".txt";
			
			FileOutputStream fs;
			try {
				fs = new FileOutputStream(file, true);
				DataOutputStream out = new DataOutputStream(fs);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
				
				writer.write(infos[3]);
				writer.close();
				
				network.getMediator().transfer(infos[1], infos[2]);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
	}

	@Override
	public void addDetails(String details) {
		// TODO Auto-generated method stub
		this.details = details;
	}

	private void MakeProduct(String buyer, String seller, String product, String value){
		String file = buyer+seller+product+".txt";
		try{
			int v = Integer.parseInt(value);
			
			FileOutputStream fs = new FileOutputStream(file);
			DataOutputStream out = new DataOutputStream(fs);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
			
			for(int i=0; i<v; i++){
				writer.write('a');
			}
			writer.close();
		}
		catch (Exception e) {
			
		}
	
	}
}
