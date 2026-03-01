package JCATPackage;
import javax.swing.*;

class JCATMAIN extends JCATComponents {
    
    

    public JCATMAIN() {}


    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                new JCATMAIN();
            }
        });
    }

}