public class MyHTTPServer {

	public static void main(String...args) {
		try {
			System.out.println("MyHTTPServer Started");
			HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
			server.createContext("/index", new IndexHandler());
			server.start();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	static class IndexHandler implements HttpHandler {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			System.out.println(exchange.getRemoteAddress());
			String response = getResponse();
			exchange.sendResponseHeaders(200, response.getBytes().length);
			OutputStream out = exchange.getResponseBody();

			Headers requestHeaders = exchange.getRequestHeaders();
			Set<String> keySet = requestHeaders.keySet();
			for (String key : keySet) {
				List values = requestHeaders.get(key);
				String header = key + " = " + values.toString() + "\n";
				System.out.print(header);
			}

			InputStream in = exchange.getRequestBody();
			if (in != null) {
				try (BufferedReader br = new BufferedReader(new InputStreamReader(in));) {
					String inputLine;
					StringBuilder request = new StringBuilder();
					while ((inputLine = br.readLine()) != null) {
						request.append(inputLine);
					}
				br.close();
				System.out.println(inputLine);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			} else {
				System.out.println("Request body is empty");
			}

			out.write(response.getBytes());
			out.close();
		}

		public String getResponse() {
			StringBuilder responseBuffer = new StringBuilder();
			responseBuffer
			.append("<html><h1>HTTPServer Home Page¡­. </h1><br>")
			.append("<b>Welcome to the new and improved web " + "server!</b><BR>")
			.append("</html>");
			return responseBuffer.toString();
		}

	}
}