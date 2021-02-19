# Server
The server collects the data from the MCU board through Bluetooth network. It is written by Java using Bluecove Library

The software allows users to send the data to the bluetooth devices (Client) such as Arduino Board, STM32 etc. 
In the meantime, users in the client side could also send the data to the software through UART protocol. After receiving data from the client side, the software forwards the received data to the node.js backend server with IPv4 address: 127.0.0.1 through Socket (Datagram) protocol. Furthermore, either 6969 or 51373 should be configed as the assigned port number which represents the communication between the client and server is using TCP and UDP service.


For more information, please read the reference:
1. http://tw.gitbook.net/java/java_networking.html

2. https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part1.html

3. https://www.oracle.com/technical-resources/articles/javame/bluetooth-wireless-technology-part2.html
