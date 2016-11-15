package org.metamesh.chub.crypto;

import org.metamesh.chub.exceptions.EncryptedPEMNoPasswordProvided;
import org.metamesh.chub.exceptions.PEMTypeNotKnow;
import org.metamesh.chub.exceptions.EncryptedPEMNoEncryptionType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

public class ECCrypto {

    public static final String CURVE = "secp384r1";

    public static KeyPair genECKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        ECGenParameterSpec algoParms = new ECGenParameterSpec("secp384r1");
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(algoParms, random);

        KeyPair pair = keyGen.generateKeyPair();
        return pair;
    }



    public static String toPem(byte v[], Optional<char[]> password, KeyType keyType, SymmetricEncryptionType encryptedType) throws IOException, EncryptedPEMNoPasswordProvided {
        List<PemHeader> headers = new ArrayList<>();
        if (encryptedType.isEncrypted) {
            if (!password.isPresent()) {
                throw new EncryptedPEMNoPasswordProvided();
            }
            headers.add(new PemHeader("Proc-Type", "4,ENCRYPTED"));
            headers.add(new PemHeader("DEK-Info", encryptedType.name()));
        }
        PemObject pem = new PemObject("CHub " + keyType.name() + " Key", headers, v);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PemWriter pemw = new PemWriter(new PrintWriter(baos))) {
            pemw.writeObject(pem);
        }

        return new String(baos.toByteArray(), StandardCharsets.US_ASCII);
    }




    // Need to figure out how to move the password out of here
    // prob need a new return type
    public static Key fromPem(String pemString, Optional<char[]> password) throws EncryptedPEMNoEncryptionType, EncryptedPEMNoPasswordProvided, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, PEMTypeNotKnow, IOException {

        PemObject pem = new PemReader(new StringReader(pemString)).readPemObject();
        byte[] decodedKey = pem.getContent();

        KeyType type;
        if (("CHub " + KeyType.PRIVATE.name() + " Key").equals(pem.getType())) {
            type = KeyType.PRIVATE;
        } else if (("CHub " + KeyType.PUBLIC.name() + " Key").equals(pem.getType())) {
            type = KeyType.PUBLIC;
        } else {
            throw new PEMTypeNotKnow(pem.getType());
        }

        boolean isEncrypted = false;
        SymmetricEncryptionType encType = null;
        for (PemHeader h : (List<PemHeader>) pem.getHeaders()) {
            if ("Proc-Type".equals(h.getName()) && "4,ENCRYPTED".equals(h.getValue())) {
                isEncrypted = true;
            }
            if ("DEK-Info".equals(h.getName())) {
                for (SymmetricEncryptionType etype : SymmetricEncryptionType.values()) {
                    if (h.getValue().equals(etype.name())) {
                        encType = etype;
                    }
                }
            }
        }
        if (isEncrypted) {
            if (encType == null) {
                throw new EncryptedPEMNoEncryptionType();
            }
            if (!password.isPresent()) {
                throw new EncryptedPEMNoPasswordProvided();
            }
            decodedKey = SymmetricCrypto.decrypt(encType, password.get(), decodedKey);
        }

        ECGenParameterSpec algoParms = new ECGenParameterSpec(CURVE);
        KeyFactory kf = KeyFactory.getInstance("EC");
        switch (type) {
            case PRIVATE:
                ECPrivateKey priv_key = (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
                return priv_key;
            case PUBLIC:
                ECPublicKey pub_key = (ECPublicKey) kf.generatePublic(new PKCS8EncodedKeySpec(decodedKey));
                return pub_key;
            default:
                throw new PEMTypeNotKnow(pem.getType());
        }
    }

    public static boolean verify(String value, String sig, PublicKey pub) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] strByte = value.getBytes("UTF-8");

        byte[] b64 = Base64.getDecoder().decode(sig);

        Signature dsa = Signature.getInstance("SHA512withECDSA");

        dsa.initVerify(pub);
        dsa.update(strByte);

        return dsa.verify(b64);
    }

    public static String sign(String value, PrivateKey priv) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException {
        byte[] strByte = value.getBytes("UTF-8");

        Signature dsa = Signature.getInstance("SHA512withECDSA");

        dsa.initSign(priv);
        dsa.update(strByte);

        byte[] realSig = dsa.sign();
        byte[] b64 = Base64.getEncoder().encode(realSig);
        return new String(b64);

    }
}
