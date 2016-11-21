/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub;

import org.metamesh.chub.crypto.ECC_Crypto;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.keys.ChubPubKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.crypto.util.SymmetricEncryptionType;
import org.metamesh.chub.proto.Message;

/**
 *
 * @author jameskeener
 */
public class GenerateKeyForm extends javax.swing.JFrame {

    KeyPair kp;
    private final JFileChooser fc = new JFileChooser();

    /**
     * Creates new form GenerateKeyForm
     */
    public GenerateKeyForm() {
        initComponents();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.addActionListener((ActionEvent e) -> {
            ChubPrivKey priv = new ChubPrivKey(CN.getText(), kp.getPrivate());
            ChubPubKey pub = new ChubPubKey(CN.getText(), kp.getPublic());
            
            Message.PrivateKey priv_key_enc = PBSerialize.serialize(SymmetricEncryptionType.AES_256_GCM_PBKDF2WithHmacSHA256_65536, Password.getPassword(), priv);
            Message.PublicKey pub_key_enc = PBSerialize.serialize(pub);

            String privFilePath = fc.getSelectedFile().getAbsoluteFile() + File.separator + "chub.priv.pb";
            try (FileOutputStream out = new FileOutputStream(privFilePath)) {
                byte a[] = priv_key_enc.toByteArray();
                out.write(a);
            } catch (IOException ex) {
                Logger.getLogger(GenerateKeyForm.class.getName()).log(Level.SEVERE, null, ex);
                showWarningMsg(ex.getMessage());
            }
            String pubFilePath = fc.getSelectedFile().getAbsoluteFile() + File.separator + "chub.pub.pb";
            try (FileOutputStream out = new FileOutputStream(pubFilePath)) {
                byte a[] = pub_key_enc.toByteArray();
                out.write(a);
            } catch (IOException ex) {
                Logger.getLogger(GenerateKeyForm.class.getName()).log(Level.SEVERE, null, ex);
                showWarningMsg(ex.getMessage());
            }
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

        Private_Key = new javax.swing.JTextField();
        Public_Key = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        SaveButton = new javax.swing.JButton();
        GenerateButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        Password = new javax.swing.JPasswordField();
        CN = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Private_Key.setEditable(false);

        Public_Key.setEditable(false);
        Public_Key.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Public_KeyActionPerformed(evt);
            }
        });

        jLabel1.setText("Public Key");

        jLabel2.setText("Private Key");

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

        GenerateButton.setText("Generate");
        GenerateButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                GenerateButtonMousePressed(evt);
            }
        });

        jLabel3.setText("Password:");

        CN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CNActionPerformed(evt);
            }
        });

        jLabel4.setText("CN");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(GenerateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SaveButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 177, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Public_Key)
                            .addComponent(Private_Key)
                            .addComponent(CN))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(CN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Public_Key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Private_Key, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(SaveButton)
                    .addComponent(GenerateButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Public_KeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Public_KeyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Public_KeyActionPerformed

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SaveButtonActionPerformed

    public static void showWarningMsg(String text) {
        Toolkit.getDefaultToolkit().beep();
        JOptionPane optionPane = new JOptionPane(text, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog("Warning!");
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);
    }

    private void GenerateButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GenerateButtonMousePressed
        kp = ECC_Crypto.genECKey();
        Public_Key.setText(Base64.getEncoder().encodeToString(kp.getPublic().getEncoded()));
        Private_Key.setText(Base64.getEncoder().encodeToString(kp.getPrivate().getEncoded()));
    }//GEN-LAST:event_GenerateButtonMousePressed

    private void SaveButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SaveButtonMousePressed
        fc.showOpenDialog(this);
    }//GEN-LAST:event_SaveButtonMousePressed

    private void CNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CNActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GenerateKeyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GenerateKeyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GenerateKeyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GenerateKeyForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new GenerateKeyForm().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField CN;
    private javax.swing.JButton GenerateButton;
    private javax.swing.JPasswordField Password;
    private javax.swing.JTextField Private_Key;
    private javax.swing.JTextField Public_Key;
    private javax.swing.JButton SaveButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
