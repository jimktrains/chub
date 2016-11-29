/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

import java.util.ArrayList;
import java.util.List;

public class StoredKeys {
    
    
    public static List<String> listKeys() {
        List<String> keyList = new ArrayList<>();
        for(String f : Settings.base_dir_file.list()) {
            if (f.endsWith(".priv.pb")) {
                keyList.add(f);
            }
        }
        return keyList;
    }
}
