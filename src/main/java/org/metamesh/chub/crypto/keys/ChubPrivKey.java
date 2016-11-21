/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto.keys;

import java.security.PrivateKey;

public class ChubPrivKey {
    
    public ChubPrivKey(String cn, PrivateKey priv) {
        this.cn = cn;
        this.key = priv;
        
    }
    public String cn;
    public PrivateKey key;
}
