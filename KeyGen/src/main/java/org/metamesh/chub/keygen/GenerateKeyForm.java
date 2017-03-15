/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.keygen;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.crypto.ECC_Crypto;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.Alert;
import org.metamesh.chub.util.Settings;

public class GenerateKeyForm extends javax.swing.JPanel {

    private KeyPair kp;
    private ChubPrivKey priv;
    private ChubPubKey pub;

    /**
     * Creates new form NewJPanel
     */
    public GenerateKeyForm() {
        initComponents();
    }

    private final List<Consumer<KeyGenEvent>> keyGenListeners = new ArrayList<>();

    public void addGeneratedListener(Consumer<KeyGenEvent> c) {
        System.out.println("addGeneratedListener" + c);
        keyGenListeners.add(c);
    }

    private void triggerGenerated() {
        KeyGenEvent e = new KeyGenEvent();
        System.out.println("triggerGenerated");
        System.out.println("triggerGenerated " + keyGenListeners.size());
        keyGenListeners.stream()
                .forEach((c) -> {
                    System.out.println(c);
                    c.accept(e);
                });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        email = new javax.swing.JTextField();
        GenerateButton = new javax.swing.JButton();
        Public_Key = new javax.swing.JTextField();
        Private_Key = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Password = new javax.swing.JPasswordField();
        SaveButton = new javax.swing.JButton();

        jLabel4.setText("email");

        email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailActionPerformed(evt);
            }
        });

        GenerateButton.setText("Generate");
        GenerateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                GenerateButtonMousePressed(evt);
            }
        });

        Public_Key.setEditable(false);

        Private_Key.setEditable(false);

        jLabel2.setText("Private Key");

        jLabel1.setText("Public Key");

        jLabel5.setText("Fingerprint: ");

        jLabel3.setText("Password:");

        SaveButton.setText("Save ");
        SaveButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                SaveButtonMousePressed(evt);
            }
        });
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(Public_Key, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                                    .addComponent(email)
                                    .addComponent(Private_Key, javax.swing.GroupLayout.Alignment.LEADING)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(142, 142, 142)
                                .addComponent(GenerateButton))
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(SaveButton)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(GenerateButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Public_Key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Private_Key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(SaveButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailActionPerformed

    private void GenerateButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GenerateButtonMousePressed
        kp = ECC_Crypto.genECKey();
        Message.DistinguishedName dn = Message.DistinguishedName.newBuilder()
                .setEmail(email.getText())
                .build();

        priv = new ChubPrivKey(dn, kp);
        pub = new ChubPubKey(dn, kp);

        Public_Key.setText(priv.fingerprintAsString());
        Private_Key.setText(pub.keyAsString());

        jLabel5.setText("Fingerprint: " + priv.fingerprintAsString());
    }//GEN-LAST:event_GenerateButtonMousePressed

    private void SaveButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveButtonMousePressed

        if (Password.getPassword().length < 4) {
            Alert.warning("Password must be greater than 4 characters");
            return;
        }

        for (String f : Settings.base_dir_file.list()) {
            if (f.contains(email.getText() + "-")) {
                Alert.warning("There is already a key for " + email.getText());
                return;
            }
        }

        Message.PrivateKey priv_key_enc = PBSerialize.serialize(priv, Message.SymmetricKeyType.AES_256_GCM_PBKDF2WithHmacSHA256_65536_128, Password.getPassword());
        Message.PublicKey pub_key_enc = PBSerialize.serialize(pub);

        String base_filename = email.getText()
                + "-"
                + priv.fingerprintAsString();
        String pathPrefix = Settings.base_dir
                + File.separator;

        String privFilePathbase = base_filename + ".priv.pb";
        String pubFilePathbase = base_filename + ".pub.pb";

        String privFilePath = pathPrefix + privFilePathbase;
        String pubFilePath = pathPrefix + pubFilePathbase;

        String davPrefix = "http://localhost/webdav/keys/";
        
        Sardine sardine = SardineFactory.begin();
        
        
        try (FileOutputStream out = new FileOutputStream(privFilePath)) {
            sardine.createDirectory(davPrefix);
            byte[] bytes = priv_key_enc.toByteArray();
            sardine.put(davPrefix + privFilePathbase, bytes);
            out.write(bytes);
            try (FileOutputStream pubout = new FileOutputStream(pubFilePath)) {
                bytes = pub_key_enc.toByteArray();
                pubout.write(bytes);
                sardine.put(davPrefix + pubFilePathbase, bytes);
                Alert.warning("Wrote key to " + privFilePath + " and " + pubFilePath);
                triggerGenerated();
            }
        } catch (IOException ex) {
            Logger.getLogger(GenerateKeyForm.class.getName()).log(Level.SEVERE, null, ex);
            Alert.warning(ex.getMessage());
        }
    }//GEN-LAST:event_SaveButtonMousePressed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        System.out.println("ACTION");
    }//GEN-LAST:event_SaveButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton GenerateButton;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField Private_Key;
    private javax.swing.JTextField Public_Key;
    private javax.swing.JButton SaveButton;
    private javax.swing.JTextField email;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    // End of variables declaration//GEN-END:variables
}
