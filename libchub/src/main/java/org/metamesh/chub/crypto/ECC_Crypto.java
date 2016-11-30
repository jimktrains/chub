package org.metamesh.chub.crypto;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.proto.Message;

public class ECC_Crypto {

    public static final KeyFactory KEY_FACTORY;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Security.addProvider(new net.i2p.crypto.eddsa.EdDSASecurityProvider());
    }
    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance("EdDSA");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static KeyPair genECKey() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            
//            ECGenParameterSpec algoParms = new ECGenParameterSpec("KeyFactory.EdDSA");
            
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EdDSA");
            keyGen.initialize(256, random);

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
            case MSG_NOT_SET:
            default:
                throw new AssertionError(sm.getMsgCase().name());
        }
        return ECC_Crypto.verify(val, sm.getMessageSignature(), pk);
    }

    private static boolean verify(GeneratedMessageV3 value, Message.Signature sig, ChubPubKey pub) {
        try {
            Signature dsa = Signature.getInstance(Message.SignatureType.SHA512withEd25519.name());

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
            Signature dsa = Signature.getInstance(Message.SignatureType.SHA512withEd25519.name());

            dsa.initSign(priv.key);
            dsa.update(value.toByteArray());

            byte[] realSig = dsa.sign();
            Message.Signature sig = Message.Signature.newBuilder()
                    .setSignature(ByteString.copyFrom(realSig))
                    .setKeyId(priv.kid)
                    .setSignatureType(Message.SignatureType.SHA512withEd25519)
                    .build();
            return sig;
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            Logger.getLogger(ECC_Crypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

    }

}
