package Hello;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Base64;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

public class IncomingMessagesLoggingRunnable implements Runnable{
 
	/**
	 * The Bluetooth connection between this device and the remote one.
	 */
	private StreamConnection connection;
	private String message[] = new String[2];
	int count = 0;
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
		   input = new BufferedInputStream(connection.openInputStream()); //open the input stream, ready to receive data
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
	   //https://stackoverflow.com/questions/9231598/how-to-read-all-bytes-together-via-bluetooth
	   while(true) {
		   int remining = 0;
		   int combine = 0;
		   int bytesRead = 0;
		   int total = 0;
		   byte buffer[] = new byte[1024];
		   String incomingMessage = "";
		   String reminingMessage = "";
		   String information = "";
		   String send = "";
		   try {
			  if(input.available()>2)
			  {
				  if(combine == 0 )
				  {
					  bytesRead = input.read(buffer);
					  incomingMessage = new String(buffer,0,bytesRead);
					  
					  if(bytesRead < 16)
					  {
						  combine = 1;
						  while(combine == 1 )
						  {
							 remining = input.read(buffer);
							 reminingMessage = new String(buffer,0,remining);
							 total = remining + bytesRead;
							 if(total == 16)
							 {
								 information = incomingMessage + reminingMessage;
								 message[count] = information;
								 //System.out.println("Message: "+message[count]);
								 combine = 0;
								 if(count != 1) {
									 count = count + 1;
								 }
								 else if (count == 1)
								 {
									 send = message[0]+message[1];
									 System.out.println("Sending: "+send);
									 String receive = client.echo(send);
									 System.out.println("Receiving: " + receive);
									 System.out.print("\n");
									 count = 0;
								 }
								 break;
							 }
						  }
					  }
				  }
			  }
		   }catch (IOException e) {
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
