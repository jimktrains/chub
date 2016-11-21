/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.keys;

import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChubPrivKey {

    public ChubPrivKey(String cn, PrivateKey priv) {
        this.cn = cn;
        this.key = priv;

    }
    public String cn;
    public PrivateKey key;

    public String fingerprintAsString() {
        return Base64.getEncoder().encodeToString(fingerprint())
                .replace("/", "_")
                .replace("+", ".")
                .replace("=", "-");
                
    }

    public byte[] fingerprint() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(key.getEncoded());
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChubPrivKey.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
