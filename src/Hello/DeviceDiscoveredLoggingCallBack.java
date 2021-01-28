package Hello;

import java.io.IOException;
import java.util.ArrayList;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import com.intel.bluetooth.RemoteDeviceHelper;

public class DeviceDiscoveredLoggingCallBack implements DiscoveryListener {

	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		//Logs the device discovered
		int found = 0;
		String target = "Test";
		String address = btDevice.getBluetoothAddress();
		try {
			String name = btDevice.getFriendlyName(false);
			boolean authenticated = btDevice.isAuthenticated();
			System.out.println("New device discovered: [" + address +"-"+ name + "-"+ authenticated+"]");
			if(name.equals(target))
			{
				System.out.println("Target Found! Connecting to the target....");
				found = 1;
				if(found == 1)
				{
					try {
						if(RemoteDeviceHelper.authenticate(btDevice, "1234") == true)
						{
							System.out.println("Successfully connected!");
						}
						else
							System.out.println("Incorrect Password!");					
					}catch(IOException CantAuthenticate) {
						System.err.println("Failed to authenticate!");
						CantAuthenticate.printStackTrace();
						found = 0;
					}
				}
			}
		}catch(IOException e) {
			System.err.println("Error while retrieving name for device [" + address + "]");
			e.printStackTrace();
		}
	}

	@Override
	public void inquiryCompleted(int discType) {
		//Logs the end of the device discovery
		System.out.println("Device discovery completed!");	    
		//very basic synchronization mechanism
		synchronized (HelloWorld.class) {
			//wake a thread up again and back to the main
			HelloWorld.class.notify();
		}
	}

	@Override
	public void serviceSearchCompleted(int transID, int respCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] services) {
		// TODO Auto-generated method stub
		
	}
   
}
