/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class SymmetricCrypto {
    public static byte[] encrypt(SymmetricEncryptionType eType, char password[], byte src[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {

        SecretKeyAndSalt keyAndSalt = eType.keyFromPassword(password);
        Cipher cipher = eType.getCipher();
        
        SecretKey key = keyAndSalt.key;
        byte[] salt = keyAndSalt.salt;
        
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] iv = cipher.getIV();
        byte[] cipherText = cipher.doFinal(src);

        // 2 = 3 ints -> iv.length, salt.length, cipherText.length , 
        byte[] message = new byte[iv.length + salt.length + cipherText.length + 12];

        System.arraycopy(intToByteArray(iv.length), 0, message, 0, 4);
        System.arraycopy(intToByteArray(salt.length), 0, message, 4, 4);
        System.arraycopy(intToByteArray(cipherText.length), 0, message, 8, 4);
        System.arraycopy(iv, 0, message, 12, iv.length);
        System.arraycopy(salt, 0, message, 12 + iv.length, salt.length);
        System.arraycopy(cipherText, 0, message, 12 + iv.length + salt.length, cipherText.length);
        return message;
    }

    private static byte[] intToByteArray(int value) {
        return new byte[]{
            (byte) (value >>> 24),
            (byte) (value >>> 16),
            (byte) (value >>> 8),
            (byte) value};
    }

    private static int byteArrayToInt(byte[] a) {
        return (a[0] << 24)
                + (a[1] << 16)
                + (a[2] >> 8)
                + (a[3]);
    }

    public static byte[] decrypt(SymmetricEncryptionType eType, char password[], byte message[]) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        byte ivlenb[] = new byte[4];
        byte saltlenb[] = new byte[4];
        byte ctlenb[] = new byte[4];

        System.arraycopy(message, 0, ivlenb, 0, 4);
        System.arraycopy(message, 4, saltlenb, 0, 4);
        System.arraycopy(message, 8, ctlenb, 0, 4);

        int ivlen = byteArrayToInt(ivlenb);
        int saltlen = byteArrayToInt(saltlenb);
        int ctlen = byteArrayToInt(ctlenb);

        byte iv[] = new byte[ivlen];
        byte salt[] = new byte[saltlen];
        byte cipherText[] = new byte[ctlen];

        System.arraycopy(message, 12, iv, 0, iv.length);
        System.arraycopy(message, 12 + iv.length, salt, 0, salt.length);
        System.arraycopy(cipherText, 12 + iv.length + salt.length, message, 0, cipherText.length);

        SecretKey key = eType.keyFromPassword(password, salt);

        Cipher cipher = eType.getCipher();
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] plainText = cipher.doFinal(cipherText);
        return plainText;
    }


}
