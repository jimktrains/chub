/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jameskeener
 */
public class Settings {

    public static final String base_dir;
    public static final File base_dir_file;
    public static final String prop_file_path;
    public static final File props_file;
    static {
        base_dir = System.getProperty("user.home") + File.separator + "chub";
        base_dir_file = new File(base_dir);
        prop_file_path = base_dir + File.separator + "chub.ini";
        props_file = new File(prop_file_path);
    }

    public static void initDir() {
        if (base_dir_file.exists()) {
            if (!base_dir_file.isDirectory()) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, base_dir + " is not a directory.");
                throw new RuntimeException(base_dir + " is not a directory.");
            }
        } else {
            boolean mkdir = base_dir_file.mkdir();
            if (!mkdir) {
                Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, base_dir + " could not be created.");
                throw new RuntimeException(base_dir + " could not be created.");
            }
        }
    }

    private static Properties props = null;

    public static Properties loadProperties() {
        if (props == null) {
            File props_file = new File(prop_file_path);
            props = new Properties();
            if (props_file.exists()) {
                try {
                    props.load(new FileInputStream(props_file));
                } catch (IOException ex) {
                    Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Could not open " + prop_file_path, ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        return props;
    }
    
    public static void storeProps() {
        try {
            props.store(new FileOutputStream(props_file), "");
        } catch (IOException ex) {
            Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Could not save to "  + prop_file_path, ex);
            throw new RuntimeException(ex);
        }
    }
}
