/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.keys;

import com.google.protobuf.ByteString;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.FileNameBase64;

public class ChubPubKey {

    public ChubPubKey(Message.KeyId kid, PublicKey pub) {
        this.kid = kid;
        this.key = pub;
    }

    public ChubPubKey(Message.DistinguishedName dn, PublicKey pub, PrivateKey priv) {
        this.kid = Message.KeyId.newBuilder()
                .setDn(dn)
                .setFingerprintSha512(ByteString.copyFrom(ChubPrivKey.fingerprint_sha512(priv)))
                .build();
        this.key = pub;
    }

    public ChubPubKey(Message.DistinguishedName dn, KeyPair kp) {
        this(dn, kp.getPublic(), kp.getPrivate());
    }
    public final PublicKey key;
    public final Message.KeyId kid;

    public String keyAsString() {
        return FileNameBase64.encode(key.getEncoded());
    }


}
