/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.postgen;

import com.google.protobuf.ByteString;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.Alert;
import org.metamesh.chub.util.Settings;
import org.metamesh.chub.util.StoredKeys;
import org.metamesh.chub.util.UUIDHelper;

public class PostForm extends javax.swing.JPanel {

    /**
     * Creates new form GeneratePost
     */
    public PostForm() {
        initComponents();
        Settings.initDir();

        ComboKeys.removeAllItems();
        StoredKeys.listKeys()
                .stream()
                .forEach((f) -> {
                    ComboKeys.addItem(f);
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

        ComboKeys = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        TextPassword = new javax.swing.JPasswordField();
        TextTitle = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextDescription = new javax.swing.JTextArea();
        SignButton = new javax.swing.JButton();

        ComboKeys.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("Key:");

        jLabel3.setText("Pasword:");

        jLabel2.setText("Title");

        TextDescription.setColumns(20);
        TextDescription.setRows(5);
        jScrollPane1.setViewportView(TextDescription);

        SignButton.setText("Sign");
        SignButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SignButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(jLabel2)
                                .addGap(32, 32, 32)
                                .addComponent(TextTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(TextPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(138, 138, 138)
                            .addComponent(SignButton)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(ComboKeys, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGap(19, 19, 19)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 367, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ComboKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TextPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TextTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addGap(28, 28, 28)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(SignButton)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SignButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignButtonMouseClicked

        try (FileInputStream priv_key_file = new FileInputStream(StoredKeys.openKey((String) ComboKeys.getSelectedItem()))) {
            Message.PrivateKey pk = Message.PrivateKey.parseFrom(priv_key_file);
            ChubPrivKey cpk = PBSerialize.deserialize(pk, TextPassword.getPassword());

            Message.Post post = Message.Post.newBuilder()
                    .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                    .setTitle(TextTitle.getText())
                    .setDescription(TextDescription.getText())
                    .setTimestamp(new Date().getTime())
                    .build();
            Message.SignedMessage sm = PBSerialize.sign(post, cpk);

            String postFilePath = Settings.base_dir + File.separator + UUIDHelper.bytes2UUID(sm.getId().toByteArray()) + ".post.pb";
            try (FileOutputStream out = new FileOutputStream(postFilePath)) {
                byte a[] = sm.toByteArray();
                out.write(a);
                Alert.info("Written to: " + postFilePath);
            } catch (IOException ex) {
                Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
                Alert.warning(ex.getMessage());
            }
        } catch (IOException ex) {
            Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
            Alert.warning(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_SignButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboKeys;
    private javax.swing.JButton SignButton;
    private javax.swing.JTextArea TextDescription;
    private javax.swing.JPasswordField TextPassword;
    private javax.swing.JTextField TextTitle;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
