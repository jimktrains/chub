package org.metamesh.chub.crypto;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.proto.Message;
import net.i2p.crypto.eddsa.KeyPairGenerator;
import net.i2p.crypto.eddsa.spec.EdDSAPublicKeySpec;
import net.i2p.crypto.eddsa.spec.EdDSAPrivateKeySpec;

public class ECC_Crypto {

    public static KeyPair genECKey() {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();

//            ECGenParameterSpec algoParms = new ECGenParameterSpec("KeyFactory.EdDSA");
            KeyPairGenerator keyGen = new KeyPairGenerator();
            keyGen.initialize(256, random);

            KeyPair pair = keyGen.generateKeyPair();
            return pair;
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(ECC_Crypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static PublicKey generatePublic(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof EdDSAPublicKeySpec) {
            return new EdDSAPublicKey((EdDSAPublicKeySpec) keySpec);
        }
        if (keySpec instanceof X509EncodedKeySpec) {
            return new EdDSAPublicKey((X509EncodedKeySpec) keySpec);
        }
        throw new InvalidKeySpecException("key spec not recognised: " + keySpec.getClass());
    }

    public static PrivateKey generatePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        if (keySpec instanceof EdDSAPrivateKeySpec) {
            return new EdDSAPrivateKey((EdDSAPrivateKeySpec) keySpec);
        }
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            return new EdDSAPrivateKey((PKCS8EncodedKeySpec) keySpec);
        }
        throw new InvalidKeySpecException("key spec not recognised: " + keySpec.getClass());
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
            Signature dsa = getSignatureMethod(Message.SignatureType.SHA512withEd25519.name());

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

    public static Signature getSignatureMethod(String name) throws NoSuchAlgorithmException {
        if (name.equals(Message.SignatureType.SHA512withEd25519.name())) {
            return new net.i2p.crypto.eddsa.EdDSAEngine();
        }
        return Signature.getInstance(name);
    }

    private static Message.Signature sign_raw(GeneratedMessageV3 value, ChubPrivKey priv) {

        try {
            Signature dsa = getSignatureMethod(Message.SignatureType.SHA512withEd25519.name());

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
