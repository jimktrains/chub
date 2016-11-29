/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

import java.util.Base64;

public class FileNameBase64 {

    public static String encode(byte[] b) {
        return Base64.getEncoder().encodeToString(b)
                .replace("/", "_")
                .replace("+", ".")
                .replace("=", "-");
    }
    
    public static byte[] decode(String s) {
        s = s.replace("_", "/")
                .replace(".", "+")
                .replace("-", "=");
        return Base64.getDecoder().decode(s);
    }
}
