package org.metamesh.chub.apps;

import java.awt.event.WindowEvent;
import org.metamesh.chub.keygen.GenerateKeyDialog;
import org.metamesh.chub.postgen.PostTabbedFrame;
import org.metamesh.chub.util.Settings;
import org.metamesh.chub.util.StoredKeys;

public class PostGenerator {
    public static void main(String args[]) {
        Settings.init();
                
        if (StoredKeys.listKeys().isEmpty()) {
            GenerateKeyDialog diag = new GenerateKeyDialog();
            
            diag.generateKeyForm.addGeneratedListener((c) -> {
                System.out.println("GENERATED");
                diag.dispatchEvent(new WindowEvent(diag, WindowEvent.WINDOW_CLOSING));
                PostTabbedFrame.load();
            });
            
            diag.setModal(true);
            diag.setVisible(true);
            
            
        } else {
            PostTabbedFrame.load();
        }
    }
}
