/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.keys;

import java.security.PublicKey;

public class ChubPubKey {

    public ChubPubKey(String cn, PublicKey pub) {
        this.cn = cn;
        this.key = pub;
    }
    public PublicKey key;
    public String cn;
}
