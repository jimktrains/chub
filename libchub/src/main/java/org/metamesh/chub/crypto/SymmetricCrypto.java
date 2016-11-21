/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto;

import com.google.protobuf.ByteString;
import java.security.InvalidAlgorithmParameterException;
import org.metamesh.chub.crypto.util.SecretKeyAndSalt;
import org.metamesh.chub.crypto.util.SymmetricEncryptionType;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.metamesh.chub.proto.Message;

public class SymmetricCrypto {
    
    public static Message.SymmetriclyEncryptedMessage.Builder encrypt(SymmetricEncryptionType eType, char password[], byte src[]) {
        
        try {
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
                    .setMsg(ByteString.copyFrom(cipherText));
            
            switch(eType) {
                case AES_256_GCM_PBKDF2WithHmacSHA256_65536:
                    sm.setEncryptionType(Message.EncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536);
                    break;
                case None:
                default:
                    throw new AssertionError(eType.name());
            }

            return sm;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException ex) {
            Logger.getLogger(SymmetricCrypto.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static byte[] decrypt(char password[], Message.SymmetriclyEncryptedMessage sm) {
        try {
            SymmetricEncryptionType eType;
            switch(sm.getEncryptionType()) {
                case AES_256_GCM_PBKDF2WithHmacSHA256_65536:
                    eType = SymmetricEncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536;
                    break;
                case UNRECOGNIZED:
                default:
                    throw new AssertionError(sm.getEncryptionType().name());
                    
            }
            
            SecretKey key = eType.keyFromPassword(password, sm.getSalt().toByteArray());
            
            Cipher cipher = eType.getCipher();
            
            // @TODO: I hate this magic 128 GCM tag length :-\
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, sm.getIv().toByteArray()));
            
            byte[] plainText = cipher.doFinal(sm.getMsg().toByteArray());
            return plainText;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(SymmetricCrypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
