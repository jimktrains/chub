package org.metamesh.chub.crypto;

import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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

    public static final String CURVE = "secp384r1";
    public static final String SIGTYPE = "SHA512withECDSA";

    public static KeyPair genECKey()  {
        try {
            SecureRandom random = SecureRandom.getInstanceStrong();
            ECGenParameterSpec algoParms = new ECGenParameterSpec(CURVE);
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            keyGen.initialize(algoParms, random);
            
            KeyPair pair = keyGen.generateKeyPair();
            return pair;
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ECC_Crypto.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static boolean verify(GeneratedMessageV3 value, Message.Signature sig, ChubPubKey pub) {
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

    public static Message.Signature sign(GeneratedMessageV3 value, ChubPrivKey priv)  {

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
