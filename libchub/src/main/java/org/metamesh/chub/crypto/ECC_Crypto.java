package org.metamesh.chub.crypto;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.proto.Message;

public class ECC_Crypto {

    public static final KeyFactory KEY_FACTORY;

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static KeyPair genECKey() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            ECGenParameterSpec algoParms = new ECGenParameterSpec(Message.ECCKeyType.secp384r1.name());
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(algoParms, random);

            KeyPair pair = keyGen.generateKeyPair();
            return pair;
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(ECC_Crypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static boolean verify(Message.SignedMessage sm, ChubPubKey pk) {
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

    private static boolean verify(GeneratedMessageV3 value, Message.Signature sig, ChubPubKey pub) {
        try {
            if (!sig.getCn().equals(pub.cn)) {
                return false;
            }

            Signature dsa = Signature.getInstance(sig.getSignatureType().name());

            dsa.initVerify(pub.key);
            dsa.update(value.toByteArray());

            return dsa.verify(sig.getSignature().toByteArray());
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException ex) {
            Logger.getLogger(ECC_Crypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static Message.Signature sign(Message.Post post, ChubPrivKey priv) {
        return sign_raw(post, priv);
    }

    private static Message.Signature sign_raw(GeneratedMessageV3 value, ChubPrivKey priv) {

        try {
            Signature dsa = Signature.getInstance(Message.SignatureType.SHA512withECDSA.name());

            dsa.initSign(priv.key);
            dsa.update(value.toByteArray());

            byte[] realSig = dsa.sign();
            Message.Signature sig = Message.Signature.newBuilder()
                    .setSignature(ByteString.copyFrom(realSig))
                    .setCn(priv.cn)
                    .setSignatureType(Message.SignatureType.SHA512withECDSA)
                    .build();
            return sig;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(ECC_Crypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

    }

}
