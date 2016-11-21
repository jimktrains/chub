/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.crypto;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;

public class KeyStore {
    public ConcurrentMap<String, ChubPubKey> public_keys = new ConcurrentHashMap<>();
    public ConcurrentMap<String, ChubPrivKey> private_keys = new ConcurrentHashMap<>();
}
