package com.wire.bots.github;

import com.wire.bots.sdk.tools.Util;

import java.io.File;

public class Validator {
    private final String homeDir;

    public Validator(String secretFile) {
        this.homeDir = secretFile;
    }

    public boolean isValid(String botId, String signature, String payload) throws Exception {
        String path = String.format("%s/%s/secret", homeDir, botId);
        String secret = Util.readLine(new File(path));
        String hmacSHA1 = Util.getHmacSHA1(payload, secret);
        String challenge = String.format("sha1=%s", hmacSHA1);
        return challenge.equals(signature);
    }
}
