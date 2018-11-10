package com.wire.bots.github.test;

import com.wire.bots.github.SHA256RsaValidator;
import org.junit.Test;

import java.security.*;

import static junit.framework.TestCase.assertTrue;

public class SignatureTest {

    @Test
    public void testSignatureValidation() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String data = "This is the payload";
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair keyPair = keyGen.genKeyPair();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(keyPair.getPrivate());
        signature.update(data.getBytes());
        byte[] sig = signature.sign();

        SHA256RsaValidator validator = new SHA256RsaValidator(keyPair.getPublic());

        boolean valid = validator.isValid(sig, data.getBytes());

        assertTrue("Wrong sig", valid);
    }
}
