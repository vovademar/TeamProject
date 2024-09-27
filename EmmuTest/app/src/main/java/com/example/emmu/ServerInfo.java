package com.example.emmu;

public class ServerInfo {
    private static final String host = "192.168.43.69";
    private static final int port = 5000;

    public static String getHttpUrl() {
        return "http://" + host + ":" + port + "/";
    }

    public static String buildUrl(String endpoint) {
        return "http://" + host + ":" + port + "/" + endpoint;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }
}
