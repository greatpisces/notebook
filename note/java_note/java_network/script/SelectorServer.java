public class SelectorServer {

	private static Selector selector;

	static class SelectorHandler implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					System.out.println("About to select¡­");
					int readyChannels = selector.select(500);
					if(readyChannels == 0) {
						System.out.println("No tasks available");
					} else {
						Set<SelectionKey> keys = selector.selectedKeys();
						Iterator<SelectionKey> keyIterator = keys.iterator();
						while(keyIterator.hasNext()) {
							SelectionKey key = keyIterator.next();
							if(key.isConnectable()) {

							} else if(key.isAcceptable()) {

							} else if(key.isReadable()) {

							} else if(key.isWritable()) {
								String message = "Date: " + new Date(System.currentTimeMillis());
								ByteBuffer buffer = ByteBuffer.allocate(64);
								buffer.put(message.getBytes());
								buffer.flip();
								SocketChannel socketChannel = null;
								if(buffer.hasRemaining()) {
									socketChannel = (SocketChannel) key.channel();
									socketChannel.write(buffer);
								}
								System.out.println("Sent: " + message + " to: " + socketChannel);
							}
							Thread.sleep(1000);
							keyIterator.remove();
						}
					}
				} catch (IOException | InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}

	}

	public SelectorServer() {
		System.out.println("ServerSocketChannel Time Server Started");
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(5000));
			selector = Selector.open();
			new Thread(new SelectorHandler()).start();

			while(true) {
				SocketChannel socketChannel = serverSocketChannel.accept();
				System.out.println("Socket channel accepted - " + socketChannel);
				if(socketChannel != null) {
					socketChannel.configureBlocking(false);
					selector.wakeup();
					socketChannel.register(selector, SelectionKey.OP_WRITE, null);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println("ServerSocketChannel Time Server Terminated");
	}

	public static void main(String...args) {
		new SelectorServer();
	}
}