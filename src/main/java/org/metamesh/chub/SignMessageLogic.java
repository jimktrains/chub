/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.proto.Message;

public class SignMessageLogic {

    public static void main(String args[]) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IOException {
        String filename = "/Users/jameskeener/test/fb22ef31-417d-41a4-9d45-53dac7f9bf32.post.pb";
        String pubname = "/Users/jameskeener/test/chub.pub.pb";
        
        try (FileInputStream postfis = new FileInputStream(new File(filename))) {
            Message.SignedMessage sm = Message.SignedMessage.parseFrom(postfis);
            try (FileInputStream keyfis = new FileInputStream(new File(pubname))) {
                Message.PublicKey pk = Message.PublicKey.parseFrom(keyfis);
                ChubPubKey cpk = PBSerialize.deserialize(pk);
                
                System.out.println(PBSerialize.verify(sm, cpk));
            }
        }
    }
    
}
