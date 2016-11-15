package org.metamesh.chub.exceptions;

public class PEMTypeNotKnow extends Exception {
    public PEMTypeNotKnow(String type) {
        super("PEM Type '" + type + "' Not Known");
    }
}
