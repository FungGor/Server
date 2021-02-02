package Hello;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

public class IncomingMessagesLoggingRunnable implements Runnable{
 
	/**
	 * The Bluetooth connection between this device and the remote one.
	 */
	private StreamConnection connection;
	
	//Initialize the socket connection
	private Socket socket = null;
	/**
	 * Instantiates a new IncomingMessagesListenerRunnable.
	 *
	 * @param connection
	 *            the {@link #connection}
	 */
	public IncomingMessagesLoggingRunnable(StreamConnection connection) {
		this.connection = connection;
	}
	
	public IncomingMessagesLoggingRunnable() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
       //Opens the connection. If this fails, the whole listening service
	   //fails.
	   InputStream input = null;
	   RemoteDevice device = null;
	   try {
		   input = new BufferedInputStream(connection.openInputStream());
		   device = RemoteDevice.getRemoteDevice(connection);
	   }catch(IOException e) {
		System.err.println("Listening service failed. Incoming message won't be displayed.");
		e.printStackTrace();
	   }
	   
	   String ip = "127.0.0.1";
	   int port = 6969;
	   //Starts Socket with node.js server
	   IncomingMessagesLoggingRunnable client = new IncomingMessagesLoggingRunnable();
	   try {
		client.socketConnect(ip,port);
	} catch (UnknownHostException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	    // Main loop of the program: reads a string incoming from the
	   // Bluetooth connection and prints it.
	   while(true) {
		   byte buffer[] = new byte[2048];
		   int bytesRead;
		   try {
			   bytesRead = input.read(buffer);
			   String incomingMessage = new String(buffer,0,bytesRead);
			   System.out.println("[" + device.getFriendlyName(false) + " - " + device.getBluetoothAddress() + "]: "
						+ incomingMessage);
			   
			   System.out.println("Sending: "+incomingMessage);
			   String receive = client.echo(incomingMessage);
			   System.out.println("Receiving: " + receive);
			  
			   
		   }catch (IOException e) {
			     // Don't rethrow this exception so if one message is lost, the
				// service continues listening.
			   System.err.println("Error while reading the incoming message.");
			   e.printStackTrace();
		   }
	   }
   }
	//mark the connection with the socket
    private void socketConnect(String ip, int port)throws UnknownHostException, IOException{
    	System.out.println("[Connecting to socket.....]");
    	this.socket = new Socket(ip,port);
    }
    
    //writes and receives the full message int the socket (String)
    public String echo (String message) {
    	try {
    		//out & in
    		PrintWriter out = new PrintWriter(getSocket().getOutputStream(),true);
    		BufferedReader in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
    		
    		//writes str in the socket and read
    		out.println(message);
    		String returnStr = in.readLine();
    		
    		return returnStr;
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    private Socket getSocket()
    {
    	return socket;
    }
}
