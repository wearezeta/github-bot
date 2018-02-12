package com.wire.bots.github;

import com.wire.bots.sdk.factories.StorageFactory;
import com.wire.bots.sdk.storage.Storage;
import com.wire.bots.sdk.tools.Util;

public class Validator {

    private final StorageFactory storageFactory;

    public Validator(StorageFactory storageFactory) {
        this.storageFactory = storageFactory;
    }

    public boolean isValid(String botId, String signature, String payload) throws Exception {
        Storage storage = storageFactory.create(botId);
        String secret = storage.readFile("secret");
        String hmacSHA1 = Util.getHmacSHA1(payload, secret);
        String challenge = String.format("sha1=%s", hmacSHA1);
        return challenge.equals(signature);
    }
}
