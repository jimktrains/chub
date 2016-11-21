/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.serialize;

import org.metamesh.chub.crypto.util.SymmetricEncryptionType;
import org.metamesh.chub.crypto.util.ECCKeyType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.bouncycastle.util.io.pem.PemHeader;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.metamesh.chub.crypto.util.DecodedPEM;
import org.metamesh.chub.exceptions.EncryptedPEMNoEncryptionType;
import org.metamesh.chub.exceptions.EncryptedPEMNoPasswordProvided;
import org.metamesh.chub.exceptions.PEMTypeNotKnow;

public class PEMSerialize {

    public static String toPem(byte value[], Optional<char[]> password, ECCKeyType keyType, SymmetricEncryptionType encryptedType) throws IOException, EncryptedPEMNoPasswordProvided {
        List<PemHeader> headers = new ArrayList<>();
        if (encryptedType.isEncrypted) {
            if (!password.isPresent()) {
                throw new EncryptedPEMNoPasswordProvided();
            }
            headers.add(new PemHeader("Proc-Type", "4,ENCRYPTED"));
            headers.add(new PemHeader("DEK-Info", encryptedType.name()));
        }
        PemObject pem = new PemObject("CHub " + keyType.name() + " Key", headers, value);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PemWriter pemw = new PemWriter(new PrintWriter(baos))) {
            pemw.writeObject(pem);
        }

        return new String(baos.toByteArray(), StandardCharsets.US_ASCII);
    }

    // Need to figure out how to move the password out of here
    // prob need a new return type
    public static DecodedPEM fromPem(String pemString, Optional<char[]> password) throws EncryptedPEMNoEncryptionType, EncryptedPEMNoPasswordProvided, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, PEMTypeNotKnow, IOException {

        PemObject pem = new PemReader(new StringReader(pemString)).readPemObject();
        byte[] decodedKey = pem.getContent();

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
//            decodedKey = SymmetricCrypto.decrypt(encType, password.get(), decodedKey);
        }

        return new DecodedPEM(decodedKey, pem.getType());
    }
    
        private static final KeyFactory KEY_FACTORY;

    static {
        try {
            KEY_FACTORY = KeyFactory.getInstance("EC");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    public static PublicKey publicKeyFromDecodedPEM(DecodedPEM pem) throws PEMTypeNotKnow, InvalidKeySpecException {
        String type = pem.type;
        byte[] decodedKey = pem.contents;

        switch (pemTypeToKeyType(type)) {
            case PUBLIC:
                PublicKey pub_key = (PublicKey) KEY_FACTORY.generatePublic(new PKCS8EncodedKeySpec(decodedKey));
                return pub_key;
            default:
                throw new PEMTypeNotKnow(type);
        }
    }

    public static PrivateKey privateKeyFromDecodedPEM(DecodedPEM pem) throws PEMTypeNotKnow, InvalidKeySpecException {
        String type = pem.type;
        byte[] decodedKey = pem.contents;

        switch (pemTypeToKeyType(type)) {
            case PRIVATE:
                PrivateKey priv_key = (PrivateKey) KEY_FACTORY.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
                return priv_key;
            default:
                throw new PEMTypeNotKnow(type);
        }
    }

    private static ECCKeyType pemTypeToKeyType(String pem_type) throws PEMTypeNotKnow {
        if (("CHub " + ECCKeyType.PRIVATE.name() + " Key").equals(pem_type)) {
            return ECCKeyType.PRIVATE;
        } else if (("CHub " + ECCKeyType.PUBLIC.name() + " Key").equals(pem_type)) {
            return ECCKeyType.PUBLIC;
        } else {
            throw new PEMTypeNotKnow(pem_type);
        }
    }
}
