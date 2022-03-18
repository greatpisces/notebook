System.out.println("Asynchronous Server Started");
try(AsynchronousServerSocketChannel assc = AsynchronousServerSocketChannel.open()) {
    assc.bind(new InetSocketAddress(6000));
    System.out.println("Waiting for client to connect...");
    Future acceptResult = assc.accept();
    try(AsynchronousSocketChannel asc = (AsynchronousSocketChannel)acceptResult.get()) {
        System.out.println("Messages from client: ");
        while((asc != null) && (asc.isOpen())) {
            ByteBuffer buffer = ByteBuffer.allocate(64);
            Future result = asc.read(buffer);
            while(!result.isDone()) {

            }
            buffer.flip();
            String message = new String(buffer.array()).trim();
            System.out.println(message);
            if("quit".equalsIgnoreCase(message)) {
                break;
            }
        }
    }
} catch(Exception e) {
    e.printStackTrace();
}

System.out.println("Asynchronous Client Started");
try(AsynchronousSocketChannel asc = AsynchronousSocketChannel.open()) {
    Future future = asc.connect(new InetSocketAddress("localhost", 6000));
    future.get();

    System.out.println("Client is started: " + asc.isOpen());
    System.out.println("Sending messages to server: ");
    Scanner scanner = new Scanner(System.in);
    String message;
    while(true) {
        System.out.print("> ");
        message = scanner.nextLine();
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
        Future result = asc.write(buffer);
        while(!result.isDone()) {

        }
        if("quit".equalsIgnoreCase(message)) {
            break;
        }
    }
    scanner.close();
    System.out.println("Sending messages to server: ");
} catch(Exception e) {
    e.printStackTrace();
}