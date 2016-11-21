/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.test;

import java.security.KeyPair;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.metamesh.chub.crypto.ECC_Crypto;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.proto.Message;
import static org.junit.Assert.assertTrue;

public class ASymmetricCryptoTest {

    static final String testMessage = "This is a test";
    static final Message.Post post;
    static {
        post = Message.Post.newBuilder()
                .setTitle(testMessage)
                .build();
    }
    static final char[] password = "\\/\\/007!!!!".toCharArray();

    static final KeyPair kp = ECC_Crypto.genECKey();
    
    static final ChubPrivKey priv = new ChubPrivKey("testcn", kp.getPrivate());
    static final ChubPubKey pub = new ChubPubKey("testcn", kp.getPublic(), priv.fingerprint());
    
    @Test
    public void testSignVerify() {
        Message.Signature sig = ECC_Crypto.sign(post, priv);
        Message.SignedMessage sm = Message.SignedMessage.newBuilder()
                .setPost(post)
                .setMessageSignature(sig)
                .build();
        assertTrue(ECC_Crypto.verify(sm, pub));
    }
}
