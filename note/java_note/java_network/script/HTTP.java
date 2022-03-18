public class WebServer {

	public WebServer() {
		System.out.println("Webserver Started");
		try(ServerSocket serverSocket = new ServerSocket(80)) {
			while(true) {
				System.out.println("Waiting for client request");
				Socket remote = serverSocket.accept();
				System.out.println("Connection made");
				new Thread(new ClientHandler(remote)).start();
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String...args) {
		new WebServer();
	}
}

public class ClientHandler implements Runnable {

	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		System.out.println("\nClientHandler Started for " + this.socket);
		handleRequest(this.socket);
		System.out.println("ClientHandler Terminated for " + this.socket + "\n");
	}

	public void handleRequest(Socket socket) {
		try(BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String headerLine = in.readLine();
			StringTokenizer tokenizer = new StringTokenizer(headerLine);
			String httpMethod = tokenizer.nextToken();
			if("GET".equals(httpMethod)) {
				System.out.println("Get method processed");
				String httpQueryString = tokenizer.nextToken();
				StringBuilder responseBuffer = new StringBuilder();
				responseBuffer
				.append("<html><h1>WebServer Home Page¡­. </h1><br>")
				.append("<b>Welcome to my web server!</b><BR>")
				.append("</html>");
				sendResponse(socket, 200, responseBuffer.toString());
			} else {
				System.out.println("The HTTP method is not recognized");
				sendResponse(socket, 405, "Method Not Allowed");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void sendResponse(Socket socket, int statusCode, String responseString) {
		String statusLine;
		String serverHeader = "Server: WebServer\r\n";
		String contentTypeHeader = "Content-Type: text/html\r\n";

		try(DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
			if(statusCode == 200) {
				statusLine = "HTTP/1.0 200 OK" + "\r\n";
				String contentLengthHeader = "Content-Length: " + responseString.length() + "\r\n";

				out.writeBytes(statusLine);
				out.writeBytes(serverHeader);
				out.writeBytes(contentTypeHeader);
				out.writeBytes(contentLengthHeader);
				out.writeBytes("\r\n");
				out.writeBytes(responseString);
			} else if(statusCode == 405) {
				statusLine = "HTTP/1.0 405 Method Not Allowed" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			} else {
				statusLine = "HTTP/1.0 404 Not Found" + "\r\n";
				out.writeBytes(statusLine);
				out.writeBytes("\r\n");
			}
			out.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

public class HTTPClient {

	public HTTPClient() {
		System.out.println("HTTP Client Started");
		try {
			InetAddress serverInetAddress = InetAddress.getByName("127.0.0.1");
			Socket connection = new Socket(serverInetAddress, 80);
			try(OutputStream out = connection.getOutputStream();
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));) {
				sendGet(out);
				System.out.println(getResponse(in));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendGet(OutputStream out) {
		try {
			out.write("GET /default\r\n".getBytes());
			out.write("User-Agent: Mozilla/5.0\r\n".getBytes());
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getResponse(BufferedReader in) {
		try {
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine).append("\n");
			}
			return response.toString();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	public static void main(String...args) {
		new HTTPClient();
	}
}