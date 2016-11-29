/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.keys;

import com.google.protobuf.ByteString;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.FileNameBase64;

public class ChubPrivKey {

    public ChubPrivKey(Message.KeyId kid, PrivateKey priv) {
        this.kid = kid;
        this.key = priv;

    }

    public ChubPrivKey(Message.DistinguishedName dn, PrivateKey priv) {
        this.kid = Message.KeyId.newBuilder()
                .setDn(dn)
                .setFingerprintSha512(ByteString.copyFrom(ChubPrivKey.fingerprint_sha512(priv)))
                .build();
        this.key = priv;
    }
    public ChubPrivKey(Message.DistinguishedName dn, KeyPair kp) {
        this(dn, kp.getPrivate());
    }
    

    public Message.KeyId kid;
    public PrivateKey key;

    public String fingerprintAsString() {
        return fingerprintAsString(key);
    }

    public byte[] fingerprint() {
        return fingerprint_sha512(key);
    }

    public static String fingerprintAsString(PrivateKey key) {
        return FileNameBase64.encode(fingerprint_sha512(key));
    }
    
    public String keyAsString() {
        return FileNameBase64.encode(key.getEncoded());
    }


    public static byte[] fingerprint_sha512(PrivateKey key) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            return md.digest(key.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChubPrivKey.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

}
