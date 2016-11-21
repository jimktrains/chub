/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.util;

public class DecodedPEM {
    public DecodedPEM(byte[] contents, String type) {
        this.contents = contents;
        this.type = type;
    }
    public byte[] contents;
    public String type;
}
