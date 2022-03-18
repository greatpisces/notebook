public UDPDatagramMulticastServer() {
    System.out.println("UDPDatagram Multicast Server Started");
    try {
        System.setProperty("java.net.preferIPv6Stack", "true");
        DatagramChannel channel = DatagramChannel.open();
        NetworkInterface networkInterface = NetworkInterface.getByName("eth0");
        channel.setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        InetSocketAddress group = new InetSocketAddress("FF01:0:0:0:0:0:0:FC", 9003);

        String message = "The message";
        ByteBuffer buffer = ByteBuffer.allocate(message.length());
        buffer.put(message.getBytes());

        while(true) {
            channel.send(buffer, group);
            System.out.println("Sent: " + message);
            buffer.clear();

            Thread.sleep(1000);
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("UDPDatagram Multicast Server Terminated");
}

public UDPDatagramMulticastClient() {
    System.out.println("UDPDatagram Multicast Client Started");
    System.setProperty("java.net.preferIPv6Stack", "true");
    try {
        NetworkInterface networkInterface = NetworkInterface.getByName("eth0");
        DatagramChannel channel = DatagramChannel.open()
                .bind(new InetSocketAddress(9003))
                .setOption(StandardSocketOptions.IP_MULTICAST_IF, networkInterface);
        InetAddress group = InetAddress.getByName("FF01:0:0:0:0:0:0:FC");
        MembershipKey key = channel.join(group, networkInterface);
        System.out.println("Joined Multicast Group: " + key);
        System.out.println("Waiting for a message¡­");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.receive(buffer);
        buffer.flip();
        StringBuilder message = new StringBuilder();
        while(buffer.hasRemaining()) {
            message.append((char)buffer.get());
        }
        System.out.println("Message: " + message);
        key.drop();
    } catch(Exception e) {
        e.printStackTrace();
    }


    System.out.println("UDPDatagram Multicast Client Terminated");
}