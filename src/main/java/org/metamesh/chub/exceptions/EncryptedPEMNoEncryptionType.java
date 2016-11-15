package org.metamesh.chub.exceptions;

public class EncryptedPEMNoEncryptionType extends Exception {
    public EncryptedPEMNoEncryptionType() {
        super("Encrypted PEM with no Encryption Type");
    }
}
