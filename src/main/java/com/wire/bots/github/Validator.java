package com.wire.bots.github;

import com.wire.bots.sdk.tools.Util;

import java.io.IOException;

public class Validator {
    public boolean isValid(String botId, String signature, String payload) throws Exception {
        String secret = new Database(botId, Service.config.db).getSecret();
        if (secret == null)
            throw new IOException("Missing secret file for bot: " + botId);

        String hmacSHA1 = Util.getHmacSHA1(payload, secret);
        String challenge = String.format("sha1=%s", hmacSHA1);
        return challenge.equals(signature);
    }
}
