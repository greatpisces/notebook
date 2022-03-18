public AsymmetricEchoServer() {
    System.out.println("Asymmetric Echo Server Started");
    try(ServerSocket serverSocket = new ServerSocket(6000)) {
        System.out.println("Waiting for connection...");
        Socket socket = serverSocket.accept();
        System.out.println("Connected to Client");
        try(DataInputStream in = new DataInputStream(socket.getInputStream());
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
            byte[] inputLine = new byte[171];
            PrivateKey privateKey = AsymmetricKeyUtility.getPrivateKey();
            while (true) {
                int length = in.read(inputLine);
                String buffer = AsymmetricKeyUtility.decrypt(privateKey, inputLine);
                System.out.println("Client request: " + buffer);
                if ("quit".equalsIgnoreCase(buffer)) {
                    break;
                }
                out.println(buffer);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("Asymmetric Echo Server Terminated");
}

public AsymmetricEchoClient() {
    System.out.println("Asymmetric Echo Client Started");
    try(Socket clientSocket = new Socket(InetAddress.getLocalHost(), 6000);
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {
        System.out.println("Connected to server");
        Scanner scanner = new Scanner(System.in);
        PublicKey publicKey = AsymmetricKeyUtility.getPublicKey();
        while (true) {
            System.out.print("Enter text: ");
            String inputLine = scanner.nextLine();
            byte[] encodedData = AsymmetricKeyUtility.encrypt(publicKey, inputLine);
            System.out.println(encodedData);
            out.write(encodedData);
            if ("quit".equalsIgnoreCase(inputLine)) {
                break;
            }
            String message = br.readLine();
            System.out.println("Server response: " + message);
        }
        scanner.close();
    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("Asymmetric Echo Client Terminated");
}