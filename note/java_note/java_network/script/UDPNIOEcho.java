public UDPEchoServer() {
    System.out.println("UDPEcho Server Started");

    try(DatagramChannel channel = DatagramChannel.open()) {
        DatagramSocket socket = channel.socket();
        socket.bind(new InetSocketAddress(9000));
        ByteBuffer buffer = ByteBuffer.allocateDirect(65507);
        while(true) {
            SocketAddress client = channel.receive(buffer);
            buffer.flip();
            buffer.mark();
            StringBuilder message = new StringBuilder();
            while(buffer.hasRemaining()) {
                message.append((char)buffer.get());
            }
            System.out.println("Received: " + message);
            buffer.reset();
            channel.send(buffer, client);
            System.out.println("Sent: " + message);
            buffer.clear();
        }
    } catch(IOException e) {
        e.printStackTrace();
    }

    System.out.println("UDPEcho Server Terminated");
}

public UDPEchoClient() {
    System.out.println("UDPEchoe Client Started");
    try {
        SocketAddress remote = new InetSocketAddress("127.0.0.1", 9000);
        DatagramChannel channel = DatagramChannel.open();
        channel.connect(remote);
        String message = "The message";
        ByteBuffer buffer = ByteBuffer.allocate(message.length());
        buffer.put(message.getBytes());
        buffer.flip();
        channel.write(buffer);
        System.out.println("Sent: " + message);
        buffer.clear();
        channel.read(buffer);
        buffer.flip();
        StringBuilder sb = new StringBuilder();
        while(buffer.hasRemaining()) {
            sb.append((char)buffer.get());
        }
        System.out.println("Receive: " + sb);
    } catch(IOException e) {
        e.printStackTrace();
    }
    System.out.println("UDPEchoe Client Terminated");
}