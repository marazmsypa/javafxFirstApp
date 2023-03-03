package com.example.test.data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public final class Request {
    public static Request create(String url, String method) {
        return new Request(url, method);
    }

    private final String url;

    private final String HTTPMethod;
    private final Map<String, String> params = new HashMap<>();

    private final Map<String, String> headers = new HashMap<>();

    private String JSONstring;

    private Request(String url, String method) {
        this.url = url;
        this.HTTPMethod = method;
    }

    public Request addParam(String name, String value) {
        params.put(name, value);
        return this;
    }

    public Request addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }
    public Request addJSONstring(String json){
        this.JSONstring = json;
        return this;
    }


    public String execute() throws IOException {
        URL url = new URL(buildURL());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        if (this.HTTPMethod == "GET"){
            StringBuilder content = new StringBuilder();
            con.setRequestMethod("GET");
            for (String key : headers.keySet()){
                con.setRequestProperty(key, headers.get(key));
            }

            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

            } catch (final Exception ex) {
                ex.printStackTrace();
                content = null;
            }
            return content.toString();
        }
        if (this.HTTPMethod == "PUT"){
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            String jsonInputString = this.JSONstring;
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            StringBuilder content = new StringBuilder();

            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

            } catch (final Exception ex) {
                ex.printStackTrace();

            }
            return content.toString();
        }
        return null;
    }

    private String buildURL() {
        StringBuilder url = new StringBuilder();
        url.append(this.url);
        if (params.size() != 0) {
            url.append("?");
            url.append(
                    String.join("&",
                            params
                                    .entrySet()
                                    .stream()
                                    .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                                    .toList()));
        }

        return url.toString();
    }
}
