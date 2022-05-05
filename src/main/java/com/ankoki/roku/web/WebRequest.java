package com.ankoki.roku.web;

import com.ankoki.roku.misc.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Class to execute web requests. <p>
 * These are handled on the main thread, and any async handling should
 * be dealt with by the executor.
 * See {@link java.util.concurrent.CompletableFuture#supplyAsync(Supplier)}, which could be useful.
 */
public class WebRequest {

    private int connectTimeout = -1, readTimeout = -1;
    private boolean allowRedirects = true;
    private final RequestType type;
    private final URL url;
    private final List<Pair<String, String>> headers = new ArrayList<>(), parameters = new ArrayList<>();

    /**
     * Creates a new web request.
     * @param url the url to execute on.
     * @param type the request type.
     */
    public WebRequest(URL url, RequestType type) {
        this.url = url;
        this.type = type;
    }

    /**
     * Gets the current request's URL
     *
     * @return the current URL.
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Adds a header to the current request.
     *
     * @param key   the key of the header.
     * @param value the value of the header.
     */
    public void addHeader(String key, String value) {
        this.addHeader(new Pair<>(key, value));
    }

    /**
     * Adds a header to the current request.
     *
     * @param pair the pair containing the key first, and the value of the header second.
     */
    public void addHeader(Pair<String, String> pair) {
        this.headers.add(pair);
    }

    /**
     * Adds a parameter to the current request.
     *
     * @param key   the key of the parameter.
     * @param value the value of the parameter.
     */
    public void addParameter(String key, String value) {
        this.addParameter(new Pair<>(key, value));
    }

    /**
     * Adds a parameter to the current request.
     *
     * @param pair the pair containing the key first, and the value of the parameter second.
     */
    public void addParameter(Pair<String, String> pair) {
        this.parameters.add(pair);
    }

    /**
     * Sets the connection timeout in milliseconds.
     * 0 represents an infinite time, with no timeout.
     * @param connectTimeout time to wait until timing out when connecting.
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * Sets the read timeout in milliseconds.
     * 0 represents an infinite time, with no timeout.
     * @param readTimeout time to wait until timing out when reading.
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * Allows redirects for this request.
     * @param allowRedirects if the request can redirect.
     */
    public void setAllowRedirects(boolean allowRedirects) {
        this.allowRedirects = allowRedirects;
    }

    /**
     * Executes the current request.
     * @return an optional containing the request response, if present.
     */
    public Optional<String> execute() throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(type.getMethod());
        String parameters = this.getEncodedParameters();
        if (parameters != null) {
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(parameters);
            out.flush();
            out.close();
        }
        for (Pair<String, String> header : headers) con.setRequestProperty(header.getFirst(), header.getSecond());
        if (connectTimeout != -1) con.setConnectTimeout(connectTimeout);
        if (readTimeout != -1) con.setReadTimeout(readTimeout);
        con.setInstanceFollowRedirects(allowRedirects);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        StringBuilder content = new StringBuilder();
        while ((line = in.readLine()) != null) {
            content.append(line);
        }
        in.close();
        con.disconnect();
        String response = content.length() > 0 ? content.toString() : null;
        return Optional.ofNullable(response);
    }

    /**
     * Gets the encoded parameters.
     * @return the encoded parameters. Null if there are no parameters.
     */
    public String getEncodedParameters() {
        if (parameters.size() < 1) return null;
        StringBuilder builder = new StringBuilder();
        for (Pair<String, String> entry : parameters) {
            builder.append(URLEncoder.encode(entry.getFirst(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getSecond(), StandardCharsets.UTF_8));
            builder.append("&");
        }
        return builder.toString();
    }

    /**
     * Utility to provide the possible request types.
     */
    public enum RequestType {
        GET("GET"),
        POST("POST"),
        HEAD("HEAD"),
        OPTIONS("OPTIONS"),
        PUT("PUT"),
        DELETE("DELETE"),
        TRACE("TRACE");

        final String method;
        RequestType(String method) {
            this.method = method;
        }

        /**
         * Gets the request method as a string.
         * @return the request method.
         */
        public String getMethod() {
            return method;
        }
    }
}
