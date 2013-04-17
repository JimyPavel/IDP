package classes;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;


public class Server {
	
	public static final int BUF_SIZE	= 1024;
	public static final String IP		= "127.0.0.1";
	public static final int PORT		= 30000;
	private static final String File = "setup.txt";
	static Logger logger = Logger.getLogger(Server.class);
	private static Hashtable<Integer, String> usersAddress;
	private static Hashtable<Integer, String> usersName;
	public static ExecutorService pool = Executors.newFixedThreadPool(5);
	public static boolean running = true;
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		addFileLogging("serverLog.txt");
		
		ServerSocketChannel serverSocketChannel	= null;
		Selector selector						= null;
		
		usersAddress = new Hashtable<Integer, String>();
		usersName = new Hashtable<Integer, String>();
		
		try {
			selector = Selector.open();
			
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(IP, PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			
			while (true) {
				selector.select();
				
				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
					SelectionKey key = it.next();
					it.remove();
					
					if (key.isAcceptable())
						accept(key);
					else if (key.isReadable())
						read(key);
					//else if (key.isWritable())
						//write(key);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (selector != null)
				try {
					selector.close();
				} catch (IOException e) {}
			
			if (serverSocketChannel != null)
				try {
					serverSocketChannel.close();
				} catch (IOException e) {}
		}
	}
	
	public static void accept(SelectionKey key) throws IOException {
		
		logger.info("[Server] Accept ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(BUF_SIZE);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		logger.info("[Server] Connection from: " + socketChannel.socket().getRemoteSocketAddress());
		//key.interestOps(SelectionKey.OP_WRITE);
	}
	
	public static void read(SelectionKey key) throws IOException {
		
		logger.info("[Server] READ: ");
		
		int bytes = 0;
		ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		try {
			while ((bytes = socketChannel.read(buf)) > 0);
			
			buf.flip();
			
			byte[] bytearr = new byte[buf.remaining()];
			buf.get(bytearr);
			
			String s = new String(bytearr);
			logger.info("[Server] Message: "+s);
			ParseInformation(s);
			buf.clear();
	
			// check for EOF
			if (bytes == -1)
				throw new IOException("EOF");
				
			
		} catch (IOException e) {
			logger.info("[Server] Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
	}
	
	public static void write(SelectionKey key) throws IOException {
		
		logger.info("[Server] WRITE: ");
		
		ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		try {
			while (socketChannel.write(buf) > 0);
			
			if (! buf.hasRemaining()) {
				buf.clear();
				key.interestOps(SelectionKey.OP_READ);
			}
			
		} catch (IOException e) {
			logger.info("[Server] Connection closed: " + e.getMessage());
			socketChannel.close();
		}
	}

	private static void WriteIpPort(String ip, int port, String userName, String userType){
		
		try{
			FileOutputStream fs = new FileOutputStream(Server.File, true);
			DataOutputStream out = new DataOutputStream(fs);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
			
			writer.write(ip + " " + port + " "  + userName + " " + userType);
			writer.newLine();
			writer.close();
			logger.info("[Server] User "+userName+ " of type " + userType + " connected with ip "+ip+" and port "+port);
			
		} catch(IOException ex)
		{
			logger.error("Exception when trying to write ip and port for server");
			ex.printStackTrace();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}

	}
	
	private static void addFileLogging(String fileName) {
		// TODO Auto-generated method stub
		try{
			// delete previous log file
			File f = new File(fileName);
			if(f.exists()){
				f.delete();
			}
			
			// add appender to file
			RollingFileAppender roll = new RollingFileAppender(new PatternLayout(" %-4r [%t] %-5p %c %x - %m%n"), fileName, true);
			
			roll.setImmediateFlush( true );
			roll.setBufferedIO( false );
			roll.setBufferSize( 1024 );
			roll.activateOptions();
			logger.addAppender(roll);
		
		} catch (Exception ex)
		{
			
		}
	}
	
	private static void ParseInformation(String info){
		String []pieces = info.split("]");
		if(pieces.length < 2)
		{
			logger.warn("Wrong message received: " + info);
			return;
		}
		
		String typeOfMessage = pieces[0].substring(1);
		
		if (typeOfMessage.equals("connect"))
		{
			String []address = pieces[1].split(":");
			if(address.length < 4)
			{
				logger.warn("Wrong address received: "+pieces[1]);
				return;
			}
			String ip = address[0];
			int port = Integer.parseInt(address[1]);
			String userName = address[2];
			String userType = address[3];
			
			WriteIpPort(ip, port, userName, userType);
			
			usersAddress.put(port, userType);
			usersName.put(port, userName);
		}
		else if(typeOfMessage.equals("offerRequest"))
		{
			// trimite cererea tuturor seller-ilor
			logger.info("[Server] Offer Request received");
			
			Iterator<Map.Entry<Integer, String>> it = usersAddress.entrySet().iterator();
			while (it.hasNext()) {
			  Map.Entry<Integer, String> entry = it.next();

			  if (entry.getValue().equals("Seller"))
			  {
				  SendMessage(info, entry.getKey());
			  }
			}
		}
		else if(typeOfMessage.equals("offerMade"))
		{
			logger.info("[Server] Offer received");
			// buyer, product, value, seller
			String []infos = pieces[1].split(":");
			
			if(infos.length < 4)
			{
				logger.warn("Wrong message received: " + pieces[1]);
				return;
			}
			String buyer = infos[0];
			
			Iterator<Map.Entry<Integer, String>> it = usersName.entrySet().iterator();
			while (it.hasNext()) {
			  Map.Entry<Integer, String> entry = it.next();

			  if (entry.getValue().equals(buyer))
			  {
				  SendMessage(info, entry.getKey());
			  }
			}
		}
		else if(typeOfMessage.equals("offerAccepted"))
		{
			logger.info("[Server] Offer accepted");
			String []infos = pieces[1].split(":");
			// buyer, seller, product, value
			if(infos.length < 4)
			{
				logger.warn("Wrong message received: " + pieces[1]);
				return;
			}
			
			String seller = infos[1];
			
			Iterator<Map.Entry<Integer, String>> it = usersName.entrySet().iterator();
			while (it.hasNext()) {
			  Map.Entry<Integer, String> entry = it.next();

			  if (entry.getValue().equals(seller))
			  {
				  SendMessage(info, entry.getKey());
			  }
			}
		}
	}
	
	private static void SendMessage(final String message, final int port)
	{
		pool.execute(new Runnable() {
			public void run() {
				logger.info("Connect to: " + IP + ":" + port);
				logger.info("Message: " + message);
				
				Selector selector			= null;
				SocketChannel socketChannel	= null;
				
				try {
					selector = Selector.open();
					
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.connect(new InetSocketAddress(IP, port));
					
					ByteBuffer buf = ByteBuffer.allocateDirect(1024);
					buf.put(message.getBytes());
					buf.flip();
					socketChannel.register(selector, SelectionKey.OP_CONNECT, buf);
					
					while (running){
						selector.select();
						
						for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
							SelectionKey key = it.next();
							it.remove();
							
							if (key.isConnectable())
								connect(key);
							else if (key.isWritable())
								writeClient(key);
						}
					}
					logger.info("[Client] Connection closed");
					running = true;
				} catch (IOException e) {
					e.printStackTrace();
					
				} finally {
					if (selector != null)
						try {
							selector.close();
						} catch (IOException e) {}
					
					if (socketChannel != null)
						try {
							socketChannel.close();
						} catch (IOException e) {}
				}
			}
		});
	}

	public static  void writeClient(SelectionKey key) throws IOException {
		
		logger.info("[Server] WRITE: ");
		
		ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		while (socketChannel.write(buf) > 0);
		
		if (! buf.hasRemaining()) {
			socketChannel.close();
			running = false;
		}
	}
	
	public static void connect(SelectionKey key) throws IOException {
		logger.info("[Server] CONNECT: ");
		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		if (! socketChannel.finishConnect()) {
			System.err.println("Eroare finishConnect");
			running = false;
		}
		
		//socketChannel.close();
		key.interestOps(SelectionKey.OP_WRITE);
	}
	

}
