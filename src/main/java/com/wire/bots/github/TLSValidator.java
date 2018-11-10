package com.wire.bots.github;

import java.net.URL;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class TLSValidator {
    private final PublicKey publicKey;

    public TLSValidator(URL url) throws InvalidKeySpecException, NoSuchAlgorithmException {
        publicKey = fetchPubKey(url);
    }

    private PublicKey fetchPubKey(URL url) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String publicKeyPEM = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvtjdLkS+FP+0fPC09j25" +
                "y/PiuYDDivIT86COVedvlElk99BBYTrqNaJybxjXbIZ1Q6xFNhOY+iTcBr4E1zJu" +
                "tizF3Xi0V9tOuP/M8Wn4Y/1lCWbQKlWrNQuqNBmhovF4K3mDCYswVbpgTmp+JQYu" +
                "Bm9QMdieZMNry5s6aiMA9aSjDlNyedvSENYo18F+NYg1J0C0JiPYTxheCb4optr1" +
                "5xNzFKhAkuGs4XTOA5C7Q06GCKtDNf44s/CVE30KODUxBi0MCKaxiXw/yy55zxX2" +
                "/YdGphIyQiA5iO1986ZmZCLLW8udz9uhW5jUr3Jlp9LbmphAC61bVSf4ou2YsJaN" +
                "0QIDAQAB";
        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public boolean isValid(byte[] signatureBytes, byte[] payload)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(payload);
        return signature.verify(signatureBytes);
    }
}
