public UDPServer() {
    System.out.println("UDP Server Started");
    try(DatagramSocket serverSocket = new DatagramSocket(9003)) {

        while(true) {
            byte[] receiveMessage = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveMessage, receiveMessage.length);
            serverSocket.receive(receivePacket);
            String message = new String(receivePacket.getData());
            System.out.println("Received from Cilent: " + message);
            System.out.println("From: " + receivePacket.getAddress());
            serverSocket.send(new DatagramPacket(message.getBytes(), message.length(), receivePacket.getAddress(), receivePacket.getPort()));
        }
    } catch(IOException e) {
        e.printStackTrace();
    }
    System.out.println("UDP Server Terminated");
}

public UDPClient() {
    System.out.println("UDP Client Started");
    Scanner scanner = new Scanner(System.in);
    try(DatagramSocket clientSocket = new DatagramSocket()) {
        InetAddress inetAddress = InetAddress.getByName("localhost");
        while(true) {
            System.out.println("Enter a message: ");
            String message = scanner.nextLine();
            if("quit".equalsIgnoreCase(message)) {
                break;
            }
            clientSocket.send(new DatagramPacket(message.getBytes(), message.length(), inetAddress, 9003));

            DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
            clientSocket.receive(receivePacket);
            System.out.println("Received from Cilent: " + new String(receivePacket.getData()));
            System.out.println("From: " + receivePacket.getAddress());
        }
        scanner.close();
    } catch(IOException e) {
        e.printStackTrace();
    }
    System.out.println("UDP Client Terminated");
}