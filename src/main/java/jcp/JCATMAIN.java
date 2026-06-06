package jcp;
import javax.swing.*;

class JCATMAIN extends JCATComponents {
     
    public JCATMAIN() {
    }

    public static void main(String[] args) {
        new javafx.embed.swing.JFXPanel();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new JCATMAIN();
            }
        });
    }

}
