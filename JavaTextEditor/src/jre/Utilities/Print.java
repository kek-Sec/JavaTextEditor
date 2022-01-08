/**
 * @author Petrakis Georgios , tp4768
 * 
 * Class responsible for handling a print request from the user
 * 
 */
package jre.Utilities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JFrame;
import javax.swing.JTextArea;


public class Print extends JFrame {
    /**
     * Constructor
     * @param text The textarea passed by the parent component
     */
    public Print(JTextArea text) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName(" Print Component ");

        pj.setPrintable(new Printable() {
            public int print(Graphics pg, PageFormat pf, int pageNum) {
                if (pageNum > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2 = (Graphics2D) pg;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                text.paint(g2);
                return Printable.PAGE_EXISTS;
            }
        });

        if (pj.printDialog() == false) {
            return;
        }

        try {
            pj.print();
        } catch (PrinterException ex) {
            // handle exception
        }
    }
}
