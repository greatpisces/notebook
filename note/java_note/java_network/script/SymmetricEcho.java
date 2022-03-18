public static String decrypt(String encryptedText, SecretKey sectetKey) {
    try {
        Cipher cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedBytes = decoder.decode(encryptedText);
        cipher.init(Cipher.DECRYPT_MODE, sectetKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        String decryptedText = new String(decryptedBytes);
        return decryptedText;
    } catch(Exception e) {
        e.printStackTrace();
    }
    return null;
}

public static String encrypt(String plainText, SecretKey secretKey) {
    try {
        Cipher cipher = Cipher.getInstance("AES");
        byte[] plainTextBytes = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainTextBytes);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedBytes);
        return encryptedText;
    } catch(Exception e) {
        e.printStackTrace();
    }

    return null;
}

private static SecretKey getSecretKey() {
    SecretKey keyFound = null;
    try {
        File file = new File("secretkeystore.jks");
        final KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(new FileInputStream(file), "keystorepassword".toCharArray());
        KeyStore.PasswordProtection keyPassword = new KeyStore.PasswordProtection("keypassword".toCharArray());
        KeyStore.Entry entry = keyStore.getEntry("secretKey", keyPassword);
        keyFound =((KeyStore.SecretKeyEntry)entry).getSecretKey();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return keyFound;
}

public SymmetricEchoServer() {
    System.out.println("Symmetric Echo Server Started");
    try(ServerSocket serverSocket = new ServerSocket(6000)) {
        System.out.println("Waiting for connection...");
        Socket socket = serverSocket.accept();
        System.out.println("Connected to Client");

        try(BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
            String inputLine;
            if((inputLine = br.readLine()) != null) {
                String decryptedText = decrypt(inputLine, getSecretKey());
                System.out.println("Client request: " + decryptedText);
                out.println(decryptedText);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("Symmetric Echo Server Terminated");
}

public SymmetricEchoClient() {
    System.out.println("Symmetric Echo Client Started");
    try(Socket socket = new Socket(InetAddress.getLocalHost(), 6000);
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {
        System.out.println("Connected to Server");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print("Enter text: ");
            String inputLine = scanner.nextLine();
            if ("quit".equalsIgnoreCase(inputLine)) {
                break;
            }
            String encryptedText = encrypt(inputLine, getSecretKey());
            System.out.println("Encrypted Text After Encryption: " + encryptedText);
            out.println(encryptedText);
            String response = br.readLine();
            System.out.println("Server response: " + response);
        }
        scanner.close();
    } catch(Exception e) {
        e.printStackTrace();
    }
    System.out.println("Symmetric Echo Client Terminated");
}