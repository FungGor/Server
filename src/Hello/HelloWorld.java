package Hello;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloWorld {
	/**
	 * Starts the Bluetooth devices discovery. Close-by devices are printed in
	 * console.
	 * 
	 * @throws BluetoothStateException
	 * @throws InterruptedException
	 */
	
	private static void startDiscovery() throws BluetoothStateException,InterruptedException {
		DiscoveryAgent agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
		System.out.println("Starting device discovery....");
		//startInquiry(int accessCode, DiscoveryListener listener)
		agent.startInquiry(DiscoveryAgent.GIAC,new DeviceDiscoveredLoggingCallBack());
		
		synchronized (HelloWorld.class) {
			//to suspend the thread because the DeviceDiscoveredLoggingCallBack() has interrupted this class
			//forces HelloWorld.class to wait until &Discovery Thread invokes notify()
			HelloWorld.class.wait();
		}
		
	}
	
	/**
	 * Opens up a connection to the specified address. After the connection is
	 * open, it's possible to send and receive messages like in an IRC.
	 * 
	 * @param address
	 *            the address to which connect
	 * @throws IOException
	 */
	
	
	
	private static void openConnection(String address)throws IOException{
		StreamConnection connection = (StreamConnection)Connector.open(address);
		if(connection == null)
		{
			System.err.println("Could not open connection to address: "+address);
			System.exit(1);
		}
		System.out.println("Hello");
		
		//Initializes the streams
		OutputStream output = connection.openOutputStream();
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader reader = new BufferedReader(isr);
		
		System.out.println("Connection opened, type in console and press enter to send a message to: " + address);
		LocalDevice localDevice = LocalDevice.getLocalDevice();
		while(true) {
			String toSend = reader.readLine();
				
			byte[] toSendBytes = toSend.getBytes(StandardCharsets.US_ASCII);
			output.write(toSendBytes);
			System.out.println(
					"[" + localDevice.getFriendlyName() + " - " + localDevice.getBluetoothAddress() + "]: " + toSend);
			if(toSend.equals("E")) {
				System.exit(1);
			}
		}
	}
	
	/**
	 * Main method of this program. It's behavior depends on the number of
	 * arguments passed:
	 * <ul>
	 * <li>if no arguments are passed, starts Bluetooth discovery</li>
	 * <li>if one argument is passed, it is interepreted as a Bluetooth address
	 * to which connect. If the connection is succesfull, an IRC chat is
	 * started</li>
	 * </ul>
	 * 
	 * An RFCOMM Bluetooth URL follows the structure:
	 * <ul>
	 * <li>btspp://</li>
	 * <li>bluetooth address</li>
	 * <li>CN (equivalent of a TCP/IP port for the service you want to use)</li>
	 * </ul>
	 * 
	 * For reference, here's an example address from my Arduino:
	 * btspp://98D3318041DE:1.
	 * 
	 * @param args
	 *            an optional Bluetooth address to which connect
	 * @throws IOException
	 * @throws InterruptedException
	 */
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
	   String address = "btspp://001403056A45:1";
       LocalDevice local = LocalDevice.getLocalDevice();
       System.out.println("----------- LOCAL DEVICE INFORMATION -----------");
	   System.out.println("Address: " + local.getBluetoothAddress());
	   System.out.println("Name: " + local.getFriendlyName());
	   System.out.println("\n");
	   System.out.println("Please choose the option: A and B");
	   Scanner sc= new Scanner(System.in);
	   String input = sc.nextLine();
	   switch(input) {
	   case "A":
		   startDiscovery();
		   break;
	   case "B":
		   openConnection(address);
		   break;
	   }
	}

}
