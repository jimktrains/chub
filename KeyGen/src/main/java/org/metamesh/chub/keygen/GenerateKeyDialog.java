/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.metamesh.chub.keygen;

import javax.swing.JDialog;

/**
 *
 * @author jameskeener
 */
public class GenerateKeyDialog extends JDialog {
    public final GenerateKeyForm generateKeyForm;
    public GenerateKeyDialog() {
        generateKeyForm = new GenerateKeyForm();
        add(generateKeyForm);
        setSize(600, 200);
        setTitle("Create Community Hub User Account");
    }
}
