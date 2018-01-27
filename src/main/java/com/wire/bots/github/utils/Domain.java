package com.wire.bots.github.utils;

public class Domain {
    public static String getDomain() {
        String env = System.getProperty("env", "prod");
        return env.equals("prod") ? "wire.com" : "zinfra.io";
    }
}
