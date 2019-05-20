
package jte;

import javax.swing.SwingUtilities;
import jre.Utilities.BuildForm;
public class MyEditor3 {

    public static void main(String[] args) {
        // ENTRANCE POINT
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BuildForm().Build();
            }
        });
        
    }
    
}
