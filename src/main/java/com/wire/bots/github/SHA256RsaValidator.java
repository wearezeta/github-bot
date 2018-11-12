package com.wire.bots.github;

import java.security.*;

public class SHA256RsaValidator {
    private final PublicKey publicKey;

    public SHA256RsaValidator(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public boolean isValid(byte[] signatureBytes, byte[] payload)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(payload);
        return signature.verify(signatureBytes);
    }
}
