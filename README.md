# Server
The server collects the data from the MCU board through Bluetooth network. It is written by Java using Bluecove Library

When you want to use RFCOMM protocol to send or receive bluetooth data, choose option B.
When you want to discovery the nearby BLE device, choose option B.

In option B, the software allows users to send the data to the bluetooth devices (Client) such as Arduino Board, STM32 etc. 
In the meantime, users in the client side could also send the data to the software through UART protocol. After receiving data from the client side, the software forwards the received data to the node.js server through Socket (Datagram) protocol. It is preferred that the target server IP address is 127.0.0.1 which is localhost. Furthermore, either 6969 or 51373 should be configed as the assigned port number which represents the communication between the client and server is using TCP and UDP service.

It should be noted that it is a client. Therefore, Socket Class is used instead of ServerSocket Class. 

For more information, please read the reference:
1. http://tw.gitbook.net/java/java_networking.html

2. https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part1.html

3. https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part2.html
