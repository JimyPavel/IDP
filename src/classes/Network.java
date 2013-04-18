package classes;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.*;

import interfaces.IMediator;
import interfaces.INetwork;
import interfaces.IState;

public class Network implements INetwork {

	private IMediator mediator;
	private int PORT = 31000;
	private String Ip = "127.0.0.1";
	private String IpServer;
	private int PortServer;
	public static ExecutorService pool = Executors.newFixedThreadPool(5);
	public static Logger logger = Logger.getLogger(Network.class);
	public static boolean running = true;
	private static final String File = "setup.txt";
	private String user;
	
	// starea curenta
	private IState state;
	
	// stari posibile
	private IState connectState;
	private IState offerRequestState;
	private IState makeOfferState;
	private IState waittingAcceptState;
	private IState waittingOfferSate;
	private IState dropOfferState;
	
	public Network (IMediator mediator)
	{
		this.mediator = mediator;
		this.mediator.registerNetwork(this);
		
		connectState = new ConnectState(this);
		offerRequestState = new OfferRequestState(this);
		makeOfferState = new MakeOfferState(this);
		waittingAcceptState = new WaittingAccept(this);
		waittingOfferSate = new WaittingOffer(this);
		dropOfferState = new DropOfferState(this);
		
		state = connectState;
		BasicConfigurator.configure();
		
	}
	@Override
	public boolean userValid(String username, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLogged(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean notifyStatusChanged(String username, Offer offer,
			String status) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addFileLogging(String fileName) {
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
	
	// connect
	@Override
	public void setIpAndPort(String userType) {
		// TODO Auto-generated method stub
		
		try{
			this.setState(getConnectState());
			this.user = userType;
			
			int lines = 0;
			FileInputStream fstream = new FileInputStream(Network.File);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			String lastIp = "";
			int lastPort = 0;
			
			// citeste adresa serverului 
			strLine = br.readLine();
			while(strLine!=null && strLine != System.getProperty("line.separator") && strLine != " "){
				
				System.out.println(strLine);
				String pieces[] = strLine.split(" ");
				
				if(pieces.length == 4)
				{
					if (lines == 0)
					{
						PortServer = Integer.parseInt(pieces[1]);
						IpServer = pieces[0];
					}
					lines++;
					
					lastPort = Integer.parseInt(pieces[1]);
					lastIp = pieces[0];
				}
				strLine = br.readLine();
			}
			br.close();
			
			PORT = lastPort + 1;
			Ip = lastIp;
			
			// anunta serverul ca s-a logat (state e connect)
			state.sendMessage();
			
			ListenToPort();
			
		} catch (IOException e) {
			logger.error("Exception when trying to read information about other users");
			e.printStackTrace();
		} catch(Exception ex)
		{
			ex.printStackTrace();
		}
	
	}

	// client -> launch offer request
	@Override
	public void LaunchOfferRequest(String product) {
		// TODO Auto-generated method stub
		this.setState(getOfferRequestState());
		state.addDetails(product);
		state.sendMessage();
	}
	
	// seller -> make offer
	@Override
	public void makeOffer(String username, Offer offer, String product){
		this.setState(getMakeOfferState());
		String details = username + ":" + offer.getProduct() + ":" + offer.getValue() + ":" + offer.getSeller();
		state.addDetails(details);
		state.sendMessage();
	}
	
	//client -> accept offer
	@Override
	public void startTransfer(String buyer, String seller, String product, String value) 
	{
		this.setState(getWaittingOfferState());
		String details = buyer+":"+seller+":"+product+":"+value;
		state.addDetails(details);
		state.sendMessage();
	}
	
	// client -> drop offer request
	@Override
	public void DropOfferRequest(String productName){
		this.setState(getDropOfferState());
		state.addDetails(productName);
		state.sendMessage();
	}
	
	// this method will create a thread that will listen to port, expecting for information
	private void ListenToPort()
	{
		pool.execute(new Runnable() {
		public void run() {
			ServerSocketChannel serverSocketChannel	= null;
			Selector selector						= null;
			logger.info("[ListenToPort] Listen to the port: " + PORT);
			
			try {
				selector = Selector.open();
				
				serverSocketChannel = ServerSocketChannel.open();
				serverSocketChannel.configureBlocking(false);
				serverSocketChannel.socket().bind(new InetSocketAddress(Ip, PORT));
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				
				while (true) {
					selector.select();
					logger.info("[ListenToPort] waitting");
					for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
						SelectionKey key = it.next();
						it.remove();
						
						if (key.isAcceptable())
							accept(key);
						else if (key.isReadable())
							read(key);
						else if (key.isWritable())
							write(key);
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
		});
	}

	public void accept(SelectionKey key) throws IOException {
		
		logger.info("[Accept] ACCEPT: ");
		
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);
		ByteBuffer buf = ByteBuffer.allocateDirect(1024);
		socketChannel.register(key.selector(), SelectionKey.OP_READ, buf);
		
		logger.info("[Accept] Connection from: " + socketChannel.socket().getRemoteSocketAddress());
	}
	
	public void read(SelectionKey key) throws IOException {
		
		logger.info("[Read] READ: ");
		
		int bytes = 0;
		ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		try {
			while ((bytes = socketChannel.read(buf)) > 0);
			
			
			buf.flip();
			byte[] bytearr = new byte[buf.remaining()];
			buf.get(bytearr);
			
			String s = new String(bytearr);
			logger.info("[Read] Message read: "+s);
			state.parseInformation(s);
			buf.clear();
			
			// check for EOF
			if (bytes == -1)
				throw new IOException("EOF");
				
			
		} catch (IOException e) {
			logger.info("[Read] Connection closed: " + e.getMessage());
			socketChannel.close();
		}
	}
	
	public void write(SelectionKey key) throws IOException {
		
		logger.info("[Write] WRITE: ");
		
		ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		try {
			while (socketChannel.write(buf) > 0);
			
			if (! buf.hasRemaining()) {
				buf.clear();
				key.interestOps(SelectionKey.OP_READ);
			}
			
		} catch (IOException e) {
			logger.info("[Write] Connection closed: " + e.getMessage());
			socketChannel.close();
			
		}
	}
	
	public void WriteToServer(final String message){
		pool.execute(new Runnable() {
			public void run() {
				logger.info("[WriteToServer] Connect to: " + IpServer + ":" + PortServer);
				logger.info("[WriteToServer] Message: " + message);
				
				Selector selector			= null;
				SocketChannel socketChannel	= null;
				
				try {
					selector = Selector.open();
					
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(false);
					socketChannel.connect(new InetSocketAddress(IpServer, PortServer));
					
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
					logger.info("[WriteToServer] Connection closed");
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
	
	public  void writeClient(SelectionKey key) throws IOException {
		
		logger.info("[WriteClient] WRITE: ");
		
		ByteBuffer buf = (ByteBuffer)key.attachment();		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		
		while (socketChannel.write(buf) > 0);
		
		if (! buf.hasRemaining()) {
			socketChannel.close();
			running = false;
		}
	}
	
	public static void connect(SelectionKey key) throws IOException {
		
		logger.info("[Connect] CONNECT: ");
		
		SocketChannel socketChannel = (SocketChannel)key.channel();
		if (! socketChannel.finishConnect()) {
			System.err.println("Eroare finishConnect");
			running = false;
		}
		
		//socketChannel.close();
		key.interestOps(SelectionKey.OP_WRITE);
	}
	
	public String getIp()
	{
		return this.Ip;
	}
	
	public int getPort()
	{
		return this.PORT;
	}
	
	public String getServerIp()
	{
		return this.IpServer;
	}
	
	public int getServerPort()
	{
		return this.PortServer;
	}

	public String getUserType()
	{
		return user;
	}
	
	public IState getConnectState()
	{
		return connectState;
	}
	
	public IState getOfferRequestState()
	{
		return offerRequestState;
	}
	
	public IState getMakeOfferState()
	{
		return makeOfferState;
	}
	
	public IState getWaittingOfferState()
	{
		return waittingOfferSate;
	}
	
	public IState getWaittingAcceptState()
	{
		return waittingAcceptState;
	}
	
	public IState getDropOfferState()
	{
		return dropOfferState;
	}
	
	public void setState(IState newState)
	{
		state = newState;
	}
	
	public IMediator getMediator()
	{
		return mediator;
	}
}
