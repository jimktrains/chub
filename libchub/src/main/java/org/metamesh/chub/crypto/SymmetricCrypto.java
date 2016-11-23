/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto;

import com.google.protobuf.ByteString;
import java.security.GeneralSecurityException;
import org.metamesh.chub.crypto.util.SecretKeyAndSalt;
import org.metamesh.chub.crypto.util.SymmetricEncryptionType;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.metamesh.chub.proto.Message;

public class SymmetricCrypto {
    
    public static Message.SymmetriclyEncryptedMessage.Builder encrypt(Message.EncryptionType et, char password[], byte src[]) {
        
        try {
            SymmetricEncryptionType eType = SymmetricEncryptionType.from(et);
            
            SecretKeyAndSalt keyAndSalt = eType.keyFromPassword(password);
            Cipher cipher = eType.getCipher();
            
            SecretKey key = keyAndSalt.key;
            byte[] salt = keyAndSalt.salt;
            cipher.init(Cipher.ENCRYPT_MODE, key);
            
            byte[] iv = cipher.getIV();
            byte[] cipherText = cipher.doFinal(src);
            
            Message.SymmetriclyEncryptedMessage.Builder sm = Message.SymmetriclyEncryptedMessage.newBuilder()
                    .setSalt(ByteString.copyFrom(salt))
                    .setIv(ByteString.copyFrom(iv))
                    .setMsg(ByteString.copyFrom(cipherText))
                    .setEncryptionType(et);

            return sm;
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(SymmetricCrypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static byte[] decrypt(char password[], Message.SymmetriclyEncryptedMessage sm) {
        try {
            SymmetricEncryptionType eType = SymmetricEncryptionType.from(sm.getEncryptionType());
            
            SecretKey key = eType.keyFromPassword(password, sm.getSalt().toByteArray());
            
            Cipher cipher = eType.getCipher();
            
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(eType.tag_length, sm.getIv().toByteArray()));
            
            byte[] plainText = cipher.doFinal(sm.getMsg().toByteArray());
            return plainText;
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(SymmetricCrypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
