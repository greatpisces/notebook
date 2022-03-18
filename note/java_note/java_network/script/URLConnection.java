public static void getUrlConnection(String hostName) {
    try {
        URL url = new URL(hostName);
        URLConnection urlConnection = url.openConnection();
        BufferedReader br =
            new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        String line;
        while((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    } catch(IOException e) {
        e.printStackTrace();
    }
}

public static void getNioUrlConnection(String hostName) {
    try {
        URL url = new URL(hostName);
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        ReadableByteChannel channel = Channels.newChannel(inputStream);
        ByteBuffer buffer = ByteBuffer.allocate(64);
        while(channel.read(buffer) > 0) {
            System.out.println(new String(buffer.array(), Charset.forName("UTF-8")));
            buffer.clear();
        }
        channel.close();
    } catch(IOException e) {
        e.printStackTrace();
    }
}