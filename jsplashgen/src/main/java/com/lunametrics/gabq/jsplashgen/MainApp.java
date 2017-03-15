package com.lunametrics.gabq.jsplashgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.proto.Message.SignedMessage;

public class MainApp {

    public static void main(String[] args) {
        // Load admin pub keys
        List<Message.PublicKey> keys = loadPublicKeys(args[1]);
        // Read previous manifest
        List<Message.SignedMessage> old_manifest = readManifest(args[2]);
        // Generate new manifest
        List<Message.SignedMessage> manifest = generateManifest(args[1]);
        // sort and diff manifests
//        manifest.sort(new ManifestEntry.SortByTime());
//        old_manifest.sort(new ManifestEntry.SortByTime());
        // Load new entries
        // Sort new entries in timestamp order
        // Process new entries in order
        //   - validate
        //     - invalid:
        //      - log
        //      - note in manifest
        //   - execute
        //     - posts -> nothing
        //     - admin pub key removal
        //     - admin pub key add
        // Load all posts from manifest
        // Generate splash page
        // Write new manifest
        // Write new admin pub keys
    }

    
    private static List<Message.PublicKey> loadPublicKeys(String path) {
        File dir = new File(path + "/admin");
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(path + " must be a dir");
        }
        List<Message.PublicKey> keys = new ArrayList<>();
        for (File f : dir.listFiles()) {
            if (!f.isDirectory()) {
                try {
                    keys.add(Message.PublicKey.parseFrom(new FileInputStream(f)));
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return keys;
    }

    public static List<Message.SignedMessage> generateManifest(String path) {
        File dir = new File(path);
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(path + " must be a dir");
        }
        List<Message.SignedMessage> manifest = new ArrayList<>();
        for (File f : dir.listFiles()) {
            if (!f.isDirectory()) {
                try {
                    manifest.add(SignedMessage.parseFrom(new FileInputStream(f)));
                } catch (IOException ex) {
                    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return manifest;
    }
}
