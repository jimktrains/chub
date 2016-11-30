/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.metamesh.chub.crypto.ECC_Crypto;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.proto.Message;

public class ASymmetricCryptoTest {

    static final String testMessage = "This is a test";
    static final Message.Post post;
    static final Message.DistinguishedName dn;

    static final KeyPair kp = ECC_Crypto.genECKey();

    static {
        post = Message.Post.newBuilder()
                .setTitle(testMessage)
                .build();
        dn = Message.DistinguishedName
                .newBuilder()
                .setCommonName(testMessage)
                .build();
    }
    static final char[] password = "\\/\\/007!!!!".toCharArray();
    static final ChubPrivKey priv = new ChubPrivKey(dn, kp);
    static final ChubPubKey pub = new ChubPubKey(dn, kp);

    @Test
    public void testSignVerify() {
        Message.Signature sig = ECC_Crypto.sign(post, priv);
        Message.SignedMessage sm = Message.SignedMessage.newBuilder()
                .setPost(post)
                .setMessageSignature(sig)
                .build();
        assertTrue(ECC_Crypto.verify(sm, pub));
    }

    @Test public void testKey() {
        try (FileInputStream priv_key_file = new FileInputStream("/Users/jameskeener/chub/jim@jimkeener.com-266hHqDrb.k0P3wLWqCJ02rZTHqBEGEct4fCxxH5xSyFV0TPq9Jb5T2gu6H8oH2.eFqYEg16wd14YH9fvYH3VQ--.pub.pb")) {
            Message.PublicKey pk = Message.PublicKey.parseFrom(priv_key_file);
            

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            System.out.println(pk.getKey().toByteArray().length);
            System.out.println(Base64.getEncoder().encodeToString(md.digest(pk.getKey().toByteArray())));
            System.out.println(Base64.getEncoder().encodeToString(pk.getKey().toByteArray()));
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(ASymmetricCryptoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
//    @Test
//    public void testTesty() {
//        for (Provider provider : Security.getProviders()) {
//            System.out.println(provider.getName());
//            for (String key : provider.stringPropertyNames()) {
//                System.out.println("\t" + key + "\t" + provider.getProperty(key));
//            }
//        }
//    }
}
