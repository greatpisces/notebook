System.out.println("Simpler Echo Server...");

try(ServerSocket serverSocket = new ServerSocket(6000)) {
    System.out.println("Waiting for connection...");
    Socket clientSocket = serverSocket.accept();
    System.out.println("Connected to client");

    try(BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
        String inputLine;
        while((inputLine = br.readLine()) != null) {
            System.out.println("Server: " + inputLine);
            out.println(inputLine);
        }
    }
} catch(IOException e) {
    e.printStackTrace();
}


System.out.println("Simple Echo Client");
try {
    System.out.println("Waiting for connection...");
    InetAddress localAddress = InetAddress.getLocalHost();

    try(Socket clientSocket = new Socket(localAddress, 6000);
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);) {
        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        System.out.println("Connected to server");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Enter text: ");
            String inputLine = scanner.nextLine();
            if("quit".equalsIgnoreCase(inputLine)) {
                break;
            }
            out.println(inputLine);
            String response = br.readLine();
            System.out.println("Sever response: " + response);
        }

        scanner.close();
    }

} catch(IOException e) {
    e.printStackTrace();
}

System.out.println("Threaded Echo Server");
try(ServerSocket serverSocket = new ServerSocket(6000);) {
    while(true) {
        System.out.println("Waiting for connection");
        clientSocket = serverSocket.accept();

        ThreadedEchoServer tes = new ThreadedEchoServer(clientSocket);
        new Thread(tes).start();
    }
} catch(IOException e) {

}

System.out.println("Threaded Echo Server Terminated");

SSLServerSocketFactory ssf = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
ServerSocket serverSocket = ssf.createServerSocket(8000);

SSLSocketFactory ssf = (SSLSocketFactory)SSLSocketFactory.getDefault();
Socket socket = ssf.createSocket("localhost", 8000);