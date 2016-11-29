/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.test;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.util.Arrays;
import org.junit.Test;
import org.metamesh.chub.crypto.SymmetricCrypto;
import org.metamesh.chub.proto.Message;
import static org.junit.Assert.assertTrue;

public class SymmetricCryptoTest {

    static final byte[] testMessage = "This is a test".getBytes(StandardCharsets.UTF_8);
    static final char[] password = "\\/\\/007!!!!".toCharArray();

    @Test
    public void testSymmetricCrypto() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        Message.SymmetriclyEncryptedMessage enc_msg = SymmetricCrypto.encrypt(Message.SymmetricKeyType.AES_256_GCM_PBKDF2WithHmacSHA256_65536_128, password, testMessage).build();
        byte[] dec_msg = SymmetricCrypto.decrypt(password, enc_msg);

        assertTrue(Arrays.areEqual(testMessage, dec_msg));
    }

}
