/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.serialize;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.crypto.ECC_Crypto;
import org.metamesh.chub.crypto.SymmetricCrypto;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.crypto.util.SymmetricEncryptionType;
import org.metamesh.chub.proto.Message;

public class PBSerialize {

    public static boolean verify(Message.SignedMessage sm, ChubPubKey pk)  {
        GeneratedMessageV3 val;

        switch (sm.getMsgCase()) {
            case POST:
                val = sm.getPost();
                break;
            case PRIVATEKEY:
                val = sm.getPrivateKey();
                break;
            case PUBLICKEY:
                val = sm.getPublicKey();
                break;
            case SYMMETRICLYENCRYPTEDMESSAGE:
                val = sm.getSymmetriclyEncryptedMessage();
                break;
            case ASYMMETRICLYENCRYPTEDMESSAGE:
                val = sm.getAsymmetriclyEncryptedMessage();
                break;
            case HYBRIDENCRYPTEDMESSAGE:
                val = sm.getHybridEncryptedMessage();
                break;
            case MSG_NOT_SET:
            default:
                throw new AssertionError(sm.getMsgCase().name());
        }
        return ECC_Crypto.verify(val, sm.getMessageSignature(), pk);
    }

    public static Message.SignedMessage sign(Message.Post msg, ChubPrivKey pk) {
        Message.Signature sig = ECC_Crypto.sign(msg, pk);

        return Message.SignedMessage.newBuilder()
                .setPost(msg)
                .setMessageSignature(sig)
                .build();
    }

    public static Message.SymmetriclyEncryptedMessage encrypt(SymmetricEncryptionType eType, char password[], Message.Post post) {
        return SymmetricCrypto.encrypt(eType, password, post.toByteArray())
                .setMessageType(Message.MessageType.MessagePost)
                .build();
    }

    public static Message.PrivateKey serialize(SymmetricEncryptionType eType, char password[], ChubPrivKey pk) {

        Message.SymmetriclyEncryptedMessage priv_enc = SymmetricCrypto.encrypt(eType, password, pk.key.getEncoded())
                .setMessageType(Message.MessageType.MessagePrivateKey)
                .build();

        Message.PrivateKey mpk = Message.PrivateKey.newBuilder()
                .setCn(pk.cn)
                .setEncryptionType(Message.EncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536)
                .setKey(priv_enc)
                .setEncodingType(pk.key.getFormat())
                .setType(Message.ECCKeyType.secp384r1)
                .build();
        return mpk;
    }

    public static Message.PublicKey serialize(ChubPubKey pk) {
        Message.PublicKey mpk = Message.PublicKey.newBuilder()
                .setCn(pk.cn)
                .setKey(ByteString.copyFrom(pk.key.getEncoded()))
                .setEncodingType(pk.key.getFormat())
                .setType(Message.ECCKeyType.secp384r1)
                .build();
        return mpk;
    }

    private static final KeyFactory KEY_FACTORY;

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ChubPrivKey deserialize(Message.PrivateKey pk, char[] password) {
        try {
            byte[] key = SymmetricCrypto.decrypt(password, pk.getKey());
            PrivateKey priv_key = (PrivateKey) KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(key));
            return new ChubPrivKey(pk.getCn(), priv_key);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PBSerialize.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static ChubPubKey deserialize(Message.PublicKey pk) {
        try {
            PublicKey pubkey = (PublicKey) KEY_FACTORY.generatePublic(new X509EncodedKeySpec(pk.getKey().toByteArray()));
            return new ChubPubKey(pk.getCn(), pubkey, pk.getFingerprint().toByteArray());
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(PBSerialize.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }
}
