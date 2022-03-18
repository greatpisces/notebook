System.out.println("Time Server Stared");
try {
    ServerSocketChannel ssc = ServerSocketChannel.open();
    ssc.bind(new InetSocketAddress(5000));
    while(true) {
        System.out.println("Waiting for request...");
        SocketChannel sc = ssc.accept();
        if(sc != null) {
            String dateAndTimeMessage = "Data" + new Date(System.currentTimeMillis());
            ByteBuffer buf = ByteBuffer.allocate(64);
            buf.put(dateAndTimeMessage.getBytes());
            buf.flip();
            while(buf.hasRemaining()) {
                sc.write(buf);
            }
            System.out.println("Sent: " + dateAndTimeMessage);
        }
    }
} catch(Exception e) {
    e.printStackTrace();
}


SocketAddress address = new InetSocketAddress("127.0.0.1", 5000);
try(SocketChannel socketChannel = SocketChannel.open(address);) {
    ByteBuffer byteBuffer = ByteBuffer.allocate(64);
    while(socketChannel.read(byteBuffer) > 0) {
        byteBuffer.flip();
        while(byteBuffer.hasRemaining()) {
            System.out.print((char)byteBuffer.get());
        }
        System.out.println();
    }
} catch(IOException e) {
    e.printStackTrace();
}