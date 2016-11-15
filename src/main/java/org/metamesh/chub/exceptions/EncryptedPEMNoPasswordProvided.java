package org.metamesh.chub.exceptions;

public class EncryptedPEMNoPasswordProvided extends Exception {
    public EncryptedPEMNoPasswordProvided() {
        super("Encyrpted PEM with no Password Provided");
    }
}
