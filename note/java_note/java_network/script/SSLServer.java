public SSLServer() {
    System.out.println("SSL Server Started");
    try {
        System.out.println("SSL Server Started");
//			Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.keyStore", "keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
        SSLServerSocket sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(5000);
        System.out.println("Waiting for a connection");
        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        System.out.println("Connection established");
        PrintWriter pw = new PrintWriter(sslSocket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            pw.println(inputLine);
            if ("quit".equalsIgnoreCase(inputLine)) {
                break;
            }
            System.out.println("Receiving: " + inputLine);
        }
    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("SSL Server Terminated");
}

public SSLClient() {
    System.out.println("SSL Client Started");
    try {
        System.out.println("SSL Client Started");
//			Security.addProvider(new Provider());
        System.setProperty("javax.net.ssl.trustStore", "keystore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket("localhost", 5000);
        System.out.println("Connection to SSL Server Established");
        PrintWriter pw = new PrintWriter(sslSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a message: ");
            String message = scanner.nextLine();
            pw.println(message);
            System.out.println("Sending: " + in.readLine());
            if ("quit".equalsIgnoreCase(message)) {
                break;
            }
        }
        scanner.close();
        pw.close();
        in.close();
        sslSocket.close();
    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("SSL Client Terminated");
}