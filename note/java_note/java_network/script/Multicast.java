System.out.println("Multicase Time Server");
DatagramSocket serverSocket = null;

try {
    serverSocket = new DatagramSocket();

    while(true) {
        String dateText = new Date().toString();
        byte[] buffer = new byte[256];
        buffer = dateText.getBytes();

        InetAddress group = InetAddress.getByName("224.0.0.0");
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, 8888);
        serverSocket.send(packet);

        System.out.println("Date Text: " + dateText);

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {

        }
    }
} catch(SocketException e) {

} catch(IOException e) {

}

System.out.println("Multicast Time Client");
try(MulticastSocket socket = new MulticastSocket(8888);) {
    InetAddress group = InetAddress.getByName("224.0.0.0");
    socket.joinGroup(group);

    byte[] buffer = new byte[256];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

    for(int i = 0; i < 5; i++) {
        socket.receive(packet);
        System.out.println(new String(packet.getData()).trim());
    }

    socket.leaveGroup(group);
} catch(IOException e) {

}
System.out.println("Multicast Time Client Terminated");