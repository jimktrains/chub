/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.serialize;

import com.google.protobuf.ByteString;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.crypto.ECC_Crypto;
import org.metamesh.chub.crypto.SymmetricCrypto;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.UUIDHelper;

public class PBSerialize {

    public static Message.SignedMessage sign(Message.Post msg, ChubPrivKey pk) {
        Message.Signature sig = ECC_Crypto.sign(msg, pk);

        return Message.SignedMessage.newBuilder()
                .setPost(msg)
                .setMessageSignature(sig)
                .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                .setTimestamp((new Date()).getTime() / 1000)
                .build();
    }

    public static Message.SymmetriclyEncryptedMessage encrypt(Message.SymmetricKeyType et, char password[], Message.Post post) {
        return SymmetricCrypto.encrypt(et, password, post.toByteArray())
                .setMessageType(Message.MessageType.MessagePost)
                .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                .build();
    }

    public static Message.PrivateKey serialize(ChubPrivKey pk, Message.SymmetricKeyType et, char password[]) {

        Message.SymmetriclyEncryptedMessage priv_enc = SymmetricCrypto.encrypt(et, password, pk.key.getEncoded())
                .setMessageType(Message.MessageType.MessagePrivateKey)
                .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                .build();

        Message.PrivateKey mpk = Message.PrivateKey
                .newBuilder()
                .setEncodingType(keyEncoding(pk.key))
                .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                .setKey(priv_enc)
                .setKeyId(pk.kid)
                .build();
        return mpk;

    }

    public static Message.KeyEncodingType keyEncoding(Key k) {
        String encoding = k.getFormat();

        switch (encoding) {
            case "X.509":
                return Message.KeyEncodingType.x509;
            case "PKCS#8":
                return Message.KeyEncodingType.pkcs8;
            default:
                throw new AssertionError("Unsupported Key encoding: " + encoding);
        }
    }

    public static String stringFromKeyEncoding(Message.KeyEncodingType encoding) {
        if (encoding.equals(Message.KeyEncodingType.x509)) {
            return "X.509";
        }
        if (encoding.equals(Message.KeyEncodingType.pkcs8)) {
            return "PKCS#8";
        }

        throw new AssertionError("Unsupported Key encoding: " + encoding.name());
    }

    public static Message.PublicKey serialize(ChubPubKey pk) {
        Message.PublicKey mpk = Message.PublicKey
                .newBuilder()
                .setEncodingType(keyEncoding(pk.key))
                .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                .setKey(ByteString.copyFrom(pk.key.getEncoded()))
                .setKeyId(pk.kid)
                .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                .build();
        return mpk;
    }

    public static ChubPubKey deserialize(Message.PublicKey pk) {
        try {
            byte[] key = pk.getKey().toByteArray();
            KeySpec kspec = PBSerialize.getKeySpec(pk, key);
            PublicKey pubkey = ECC_Crypto.KEY_FACTORY.generatePublic(kspec);
            return new ChubPubKey(pk.getKeyId(), pubkey);

        } catch (GeneralSecurityException ex) {
            Logger.getLogger(PBSerialize.class
                    .getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static ChubPrivKey deserialize(Message.PrivateKey pk, char[] password) {
        try {
            byte[] key = SymmetricCrypto.decrypt(password, pk.getKey());
            KeySpec kspec = getKeySpec(pk, key);
            PrivateKey priv_key = ECC_Crypto.KEY_FACTORY.generatePrivate(kspec);
            return new ChubPrivKey(pk.getKeyId(), priv_key);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(PBSerialize.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static KeySpec getKeySpec(Message.PrivateKey pk, byte[] encodedKey) {
        if (pk.getEncodingType() == Message.KeyEncodingType.pkcs8) {
            return new PKCS8EncodedKeySpec(encodedKey);
        } else {
            throw new AssertionError(pk.getEncodingType().name() + " is not known for private keys");
        }
    }

    public static KeySpec getKeySpec(Message.PublicKey pk, byte[] encodedKey) {
        if (pk.getEncodingType() == Message.KeyEncodingType.x509) {
            return new X509EncodedKeySpec(encodedKey);
        } else {
            throw new AssertionError(pk.getEncodingType().name() + " is not known for public keys");
        }
    }

}
