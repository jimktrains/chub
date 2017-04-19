/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoredKeys {

    final String filename;

    public StoredKeys(String filename) {
        this.filename = filename;
    }

    public static File openKey(String f) {
        return new File(Settings.base_dir + File.separator + f);
    }

    public static List<StoredKeys> listKeys() {
        List<StoredKeys> keyList = new ArrayList<>();
        for (String f : Settings.base_dir_file.list()) {
            if (f.endsWith(".priv.pb")) {
                keyList.add(new StoredKeys(f));
            }
        }
        return keyList;
    }

    public String getFilename() {
        return filename;
    }

    public String toString() {
        return filename.split("-")[0];
    }
}
