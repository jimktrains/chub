package org.metamesh.chub.crypto.util;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.metamesh.chub.proto.Message;

public enum SymmetricEncryptionType {

    None,
    AES_256_GCM_PBKDF2WithHmacSHA256_65536_128("AES", 256, "GCM", "NoPadding", "PBKDF2WithHmacSHA256", 65536, 128);

    public final boolean isEncrypted;
    public final String enc_algorithm;
    public final int key_length;
    public final String mode;
    public final String padding;
    public final String kdf_algorithm;
    public final int kdf_iterations;
    public final int tag_length;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private SymmetricEncryptionType(String enc_algorithm, int key_length, String mode, String padding, String kdf_algorithm, int kdf_iterations, int tag_length) {
        this.enc_algorithm = enc_algorithm;
        this.key_length = key_length;
        this.mode = mode;
        this.padding = padding;
        this.kdf_algorithm = kdf_algorithm;
        this.kdf_iterations = kdf_iterations;
        this.isEncrypted = true;
        this.tag_length = tag_length;
    }

    private SymmetricEncryptionType() {
        this.isEncrypted = false;
        this.enc_algorithm = null;
        this.key_length = -1;
        this.mode = null;
        this.padding = null;
        this.kdf_algorithm = null;
        this.kdf_iterations = -1;
        this.tag_length = -1;
    }

    public Cipher getCipher() throws GeneralSecurityException {
        if (!isEncrypted) {
            return new NullCipher();
        }
        return Cipher.getInstance(enc_algorithm + "/" + mode + "/" + padding, "BC");
    }

    public SecretKeyAndSalt keyFromPassword(char password[]) throws GeneralSecurityException {
        byte[] salt = getNextSalt();
        return new SecretKeyAndSalt(keyFromPassword(password, salt), salt);
    }

    public SecretKey keyFromPassword(char password[], byte salt[]) throws GeneralSecurityException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(kdf_algorithm);
        KeySpec spec = new PBEKeySpec(password, salt, kdf_iterations, key_length);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKey key = new SecretKeySpec(tmp.getEncoded(), enc_algorithm);

        return key;
    }

    private static final Random SECRAND = new SecureRandom();

    public static byte[] getNextSalt() {
        byte[] salt = new byte[16];
        SECRAND.nextBytes(salt);
        return salt;
    }

    public static SymmetricEncryptionType from(Message.EncryptionType et) {
        switch (et) {
            case AES_256_GCM_PBKDF2WithHmacSHA256_65536_128:
                return SymmetricEncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536_128;
            case UNRECOGNIZED:
            default:
                throw new AssertionError(et);
        }
    }

    public Message.EncryptionType to() {
        switch (this) {
            case AES_256_GCM_PBKDF2WithHmacSHA256_65536_128:
                return Message.EncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536_128;
            case None:
            default:
                throw new AssertionError(this.name());
        }
    }
}
