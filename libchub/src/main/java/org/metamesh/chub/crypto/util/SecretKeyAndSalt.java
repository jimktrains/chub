package org.metamesh.chub.crypto.util;

import javax.crypto.SecretKey;

public class SecretKeyAndSalt {

    public SecretKeyAndSalt(SecretKey key, byte[] salt) {
        this.key = key;
        this.salt = salt;
    }
    
    public SecretKey key;
    public byte[] salt;
}
