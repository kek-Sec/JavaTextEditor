/**
 * @author Petrakis Georgios , tp4768 
 * @repository https://github.com/kek-Sec/JavaTextEditor
 * 
 */
package jte;
import javax.swing.SwingUtilities;
import jre.Utilities.BuildForm;
public class MyEditor3 {

    public static void main(String[] args) {
        /**
         * Entry point for creating the main frame
         * 
         */
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new BuildForm().Build();
            }
        });
        
    }
    
}
