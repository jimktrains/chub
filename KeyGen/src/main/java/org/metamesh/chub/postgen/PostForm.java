package org.metamesh.chub.postgen;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.google.protobuf.ByteString;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import org.metamesh.chub.crypto.keys.ChubPrivKey;
import org.metamesh.chub.crypto.serialize.PBSerialize;
import org.metamesh.chub.proto.Message;
import org.metamesh.chub.util.Alert;
import org.metamesh.chub.util.Settings;
import org.metamesh.chub.util.StoredKeys;
import org.metamesh.chub.util.UUIDHelper;

class TimeComboValue {

    final int h, m;

    public TimeComboValue(int h, int m) {
        this.h = h;
        this.m = m;
    }

    public String toString() {
        if (h < 0) { return "No Specific Time"; }
        String ampm = h < 12 ? "AM" : "PM";
        int h2 = h;
        if (h2 > 12) {
            h2 -= 12;
        }
        if (h2 == 0) {
            h2 = 12;
        }
        DecimalFormat df2 = new DecimalFormat("00");

        String time = df2.format(h) + ":" + df2.format(m) + " " + ampm;
        return time;
    }
}

public class PostForm extends javax.swing.JPanel {

    JFileChooser fc = new JFileChooser();
    BufferedImage image;

    /**
     * Creates new form GeneratePost
     */
    public PostForm() {
        initComponents();
        Settings.initDir();

        jComboBox1.removeAllItems();
        jComboBox2.removeAllItems();
        jComboBox1.addItem(new TimeComboValue(-1, 0));
        jComboBox2.addItem(new TimeComboValue(-1, 0));

        for (int i = 0; i < 24; i++) {
            
            for (int j = 0; j < 60; j += 15) {
                jComboBox1.addItem(new TimeComboValue(i, j));
                jComboBox2.addItem(new TimeComboValue(i, j));
            }
        }

        ComboKeys.removeAllItems();
        StoredKeys.listKeys()
                .stream()
                .forEach((f) -> {
                    ComboKeys.addItem(f);
                });
        fc.addActionListener((ActionEvent e) -> {
            try {
                image = ImageIO.read(fc.getSelectedFile());
                int w = 300;
                int h = (int) ((((double) image.getHeight(this)) / ((double) image.getWidth(this))) * 300);
                image = toBufferedImage(image.getScaledInstance(w, h, Image.SCALE_SMOOTH));
                ImagePreview.setIcon(new ImageIcon(image));
                ImagePreview.setSize(w, h);
            } catch (IOException ex) {
                Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
                Alert.warning(ex.getMessage());
            }
        });
    }

    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
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
        SignButton = new javax.swing.JButton();
        startDatePicker = new org.jdatepicker.JDatePicker();
        jLabel1 = new javax.swing.JLabel();
        endDatePicker = new org.jdatepicker.JDatePicker();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ImageSelectionBtn = new javax.swing.JButton();
        ImagePreview = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        metaphaseEditor1 = new com.metaphaseeditor.MetaphaseEditor();

        ComboKeys.setModel(new javax.swing.DefaultComboBoxModel<>());

        jLabel4.setText("User Account:");

        jLabel3.setText("Pasword:");

        jLabel2.setText("Post Title*");

        SignButton.setText("Generate Post");
        SignButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SignButtonMouseClicked(evt);
            }
        });
        SignButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SignButtonActionPerformed(evt);
            }
        });

        startDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startDatePickerActionPerformed(evt);
            }
        });

        jLabel1.setText("Start Date (optional)");

        endDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endDatePickerActionPerformed(evt);
            }
        });

        jLabel5.setText("End Date (optional)");

        jLabel6.setText("Post Body*");

        jLabel7.setText("Picture (optional)");

        ImageSelectionBtn.setText("Select");
        ImageSelectionBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ImageSelectionBtnMouseClicked(evt);
            }
        });

        ImagePreview.setText(" ");

        jLabel8.setText("Still having trouble? Contact CHUB@metamesh.org");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>());

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(metaphaseEditor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(414, 414, 414)
                        .addComponent(SignButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel1))
                                        .addGap(8, 8, 8)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(TextTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel3))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(ComboKeys, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(TextPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(ImageSelectionBtn)))
                            .addComponent(jLabel6))
                        .addGap(18, 18, 18)
                        .addComponent(ImagePreview)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(SignButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ImagePreview, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(ComboKeys, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel3))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(33, 33, 33)
                                        .addComponent(TextPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(TextTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(7, 7, 7)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(startDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(ImageSelectionBtn)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(endDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addGap(10, 10, 10)))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(metaphaseEditor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void SignButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SignButtonMouseClicked

        try (FileInputStream priv_key_file = new FileInputStream(StoredKeys.openKey(((StoredKeys)ComboKeys.getSelectedItem()).getFilename()))) {
            Message.PrivateKey pk = Message.PrivateKey.parseFrom(priv_key_file);
            ChubPrivKey cpk = PBSerialize.deserialize(pk, TextPassword.getPassword());

//            Date start = ((GregorianCalendar) (startDatePicker.getJDateInstantPanel().getModel().getValue())).getTime();
//            Date end = ((GregorianCalendar) (endDatePicker.getJDateInstantPanel().getModel().getValue())).getTime();
            System.out.println(metaphaseEditor1.getDocument());
            Message.Post.Builder pb = Message.Post.newBuilder()
                    .setId(ByteString.copyFrom(UUIDHelper.randomUUID()))
                    .setTitle(TextTitle.getText())
                    .setDescription(metaphaseEditor1.getDocument())
                    //                    .setStartTime(start.getTime())
                    //                    .setStartTime(end.getTime())
                    .setTimestamp(new Date().getTime());
            if (image != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", baos);
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
            } catch (IOException ex) {
                Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
                Alert.warning(ex.getMessage());
            }
        } catch (IOException ex) {
            Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, null, ex);
            Alert.warning(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_SignButtonMouseClicked

    private void SignButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SignButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_SignButtonActionPerformed

    private void ImageSelectionBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImageSelectionBtnMouseClicked

        fc.showOpenDialog(this);
    }//GEN-LAST:event_ImageSelectionBtnMouseClicked

    private void startDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startDatePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_startDatePickerActionPerformed

    private void endDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endDatePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endDatePickerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<StoredKeys> ComboKeys;
    private javax.swing.JLabel ImagePreview;
    private javax.swing.JButton ImageSelectionBtn;
    private javax.swing.JButton SignButton;
    private javax.swing.JPasswordField TextPassword;
    private javax.swing.JTextField TextTitle;
    private org.jdatepicker.JDatePicker endDatePicker;
    private javax.swing.JComboBox<TimeComboValue> jComboBox1;
    private javax.swing.JComboBox<TimeComboValue> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private com.metaphaseeditor.MetaphaseEditor metaphaseEditor1;
    private org.jdatepicker.JDatePicker startDatePicker;
    // End of variables declaration//GEN-END:variables

}
