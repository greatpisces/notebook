private void sentGet() throws Exception {
    String query = "https://www.baidu.com/";
    URL url = new URL(query);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");
    connection.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = connection.getResponseCode();
    System.out.println("Response Code: " + responseCode);
    if(responseCode == 200) {
        String response = getResponse(connection);
        System.out.println("Response: " + response.toString());
    } else {
        System.out.println("Bad Response Code: " + responseCode);
    }
}

private String getResponse(HttpURLConnection connection) {
    Map<String, List<String>> requestHeaders = connection.getHeaderFields();
    Set<String> keySet = requestHeaders.keySet();
    for (String key : keySet) {
        if ("Set-cookie".equals(key)) {
            List values = requestHeaders.get(key);
            String cookie = key + " = " +
            values.toString() + "\n";
            String cookieName =
            cookie.substring(0, cookie.indexOf("="));
            String cookieValue = cookie.substring(
            cookie.indexOf("=")+ 1, cookie.length());
            System.out.println(cookieName + ":" + cookieValue);
        }
    }
    try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String inputLine;
        StringBuilder response = new StringBuilder();
        while((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    } catch(Exception e) {
        e.printStackTrace();
    }
    return "";
}