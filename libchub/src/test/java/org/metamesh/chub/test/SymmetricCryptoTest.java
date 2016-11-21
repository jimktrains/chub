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
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.metamesh.chub.crypto.SymmetricCrypto;
import org.metamesh.chub.crypto.util.SymmetricEncryptionType;
import org.metamesh.chub.proto.Message;

public class SymmetricCryptoTest {
    static final byte[] testMessage = "This is a test".getBytes(StandardCharsets.UTF_8);
    static final char[] password = "\\/\\/007!!!!".toCharArray();

    @Test
    public void testSymmetricCrypto() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        
        Message.SymmetriclyEncryptedMessage enc_msg = SymmetricCrypto.encrypt(SymmetricEncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536, password, testMessage).build();
        byte[] dec_msg = SymmetricCrypto.decrypt(password, enc_msg);
        
        int bytes_same = 0;
        assertEquals(testMessage.length, dec_msg.length);
        
        for(int i = 0; i < testMessage.length; i++) {
            if (testMessage[i] == dec_msg[i]) bytes_same++;
        }
        assertEquals(dec_msg.length, bytes_same);
    }
}
