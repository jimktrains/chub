/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.postgen;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.google.protobuf.ByteString;
import java.awt.event.WindowEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.Alert;
import org.metamesh.chub.util.Settings;
import org.metamesh.chub.util.UUIDHelper;

/**
 *
 * @author jim
 */
public class PreviewFrame extends javax.swing.JFrame {

    private final PostForm postForm;

    PreviewFrame(PostForm postForm) {
        initComponents();
        this.postForm = postForm;

        labelTitle.setText("Title: " + postForm.getTitle());
        labelText.setText("<html>Body:<br>" + postForm.getBody() + "</html>");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labelTitle = new javax.swing.JLabel();
        labelStartTime = new javax.swing.JLabel();
        labelEndTime = new javax.swing.JLabel();
        labelText = new javax.swing.JLabel();
        publishButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        labelTitle.setText("jLabel1");

        labelStartTime.setText("jLabel1");

        labelEndTime.setText("jLabel1");

        labelText.setText("jLabel1");

        publishButton.setText("Publish");
        publishButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                publishButtonMouseClicked(evt);
            }
        });
        publishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                publishButtonActionPerformed(evt);
            }
        });

        closeButton.setText("Continue Editing");
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelText, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelTitle)
                            .addComponent(labelStartTime)
                            .addComponent(labelEndTime))
                        .addGap(0, 187, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(closeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(publishButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelStartTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelEndTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelText, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(publishButton)
                    .addComponent(closeButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void publishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_publishButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_publishButtonActionPerformed

    private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
        setVisible(false);
    }//GEN-LAST:event_closeButtonMouseClicked

    private void publishButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_publishButtonMouseClicked
        try (FileInputStream priv_key_file = new FileInputStream(postForm.getKeyFile())) {
            Message.PrivateKey pk = Message.PrivateKey.parseFrom(priv_key_file);
            ChubPrivKey cpk = PBSerialize.deserialize(pk, postForm.getPassword());

            Message.Post.Builder pb = Message.Post.newBuilder()
                    .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                    .setTitle(postForm.getTitle())
                    .setDescription(postForm.getBody())
                    .setStartTime(postForm.getStart().getTime())
                    .setStartTime(postForm.getEnd().getTime())
                    .setTimestamp(new Date().getTime());
            if (postForm.getImage() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(postForm.getImage(), "jpg", baos);
                baos.flush();

                ByteString bs = ByteString.copyFrom(baos.toByteArray());

                pb.setImage(Message.Image.newBuilder()
                        .setImage(bs)
                        .setImageType(Message.ImageType.jpeg)
                );
            }
            Message.Post post = pb.build();
            Message.SignedMessage sm = PBSerialize.sign(post, cpk);

            String host = Settings.props.getProperty("host");
            String path_prefix = Settings.props.getProperty("path_prefix");

            String davPrefix = "http://" + host + "/" + path_prefix + "/posts/";

            Sardine sardine = SardineFactory.begin();
            sardine.setCredentials("test", "test");
            String base_name = UUIDHelper.bytes2UUID(sm.getId().toByteArray()) + ".post.pb";
            String postFilePath = Settings.base_dir + File.separator + base_name;
            try (FileOutputStream out = new FileOutputStream(postFilePath)) {
                byte a[] = sm.toByteArray();

//                sardine.createDirectory(davPrefix);
                System.out.println(davPrefix + base_name);
                sardine.put(davPrefix + base_name, a);
                out.write(a);
                //Alert.info("Written to: " + postFilePath);
                setVisible(false);
                Alert.info("Post has been sent to the server. It should appear on splash pages shortly");
            } catch (IOException ex) {
                Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
                Alert.warning(ex.getMessage());
            }
        } catch (IOException ex) {
            Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
            Alert.warning(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_publishButtonMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel labelEndTime;
    private javax.swing.JLabel labelStartTime;
    private javax.swing.JLabel labelText;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JButton publishButton;
    // End of variables declaration//GEN-END:variables
}
