/*
    Core class of Swing Builder
 */
package jre.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class BuildForm extends JFrame{
    
    //Dec Section
    

    About AboutManager = new About();
  //  File_M FileManager = new File_M();
    
    private boolean edit = false;
    private JComboBox fontSize;
    private  JComboBox fontType;
    private JPanel p;
    public JTextArea ta;
    private JScrollPane sc;
    private JToolBar tb;
    private JButton newButton, openButton, saveButton, findButton, aboutButton,printButton;
    public JFileChooser fc;
    public File theFile;
    
    
    
    
    //end
    
    public BuildForm()
    {
        System.out.println("test");
        this.setVisible(true);
    }
    public void Build()
    {
        Base();
        FileButtons();
        FindButtons();
        AboutButton();
        printButton();
        AddStatus();
        AddFonts();
        Finalize();
        FailSafe();
    }
    private void Base()
    {
        p = new JPanel(new BorderLayout());
        ta = new JTextArea(10, 60); 
        //ta.setMargin(new Insets(3, 3, 3, 3)); 
        ta.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        ta.setTabSize(2);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);

        sc = new JScrollPane(ta); 
        p.add(sc);

        tb = new JToolBar();
        tb.setFloatable(false); 
    }
    private void printButton()
    {
        printButton = new JButton("print");
        printButton.setToolTipText("Print the file...");
        printButton.setIcon(new ImageIcon(getClass().getResource("/images/Print16.png")));
        tb.add(printButton);
        
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Print(ta);
            }
        });
        
    }
    private void FileButtons()
    {
        newButton = new JButton("new");
        openButton = new JButton("open");
        saveButton = new JButton("save");
        
        newButton.setToolTipText("New file...");
        openButton.setToolTipText("Open a file...");
        saveButton.setToolTipText("Save a file...");
       
        
        newButton.setIcon(new ImageIcon(getClass().getResource("/images/New16.png")));
        openButton.setIcon(new ImageIcon(getClass().getResource("/images/Open16.gif")));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/images/Save16.gif")));
        
        tb.add(newButton);
        tb.add(openButton);
        tb.add(saveButton);
        
        //Action listeners
        
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileManager("new");
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileManager("open");
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               FileManager("save");
            }
        });
    }
    private void FindButtons()
    {
        findButton = new JButton("find");
        
        findButton.setIcon(new ImageIcon(getClass().getResource("/images/Search16.png")));
 
        findButton.setToolTipText("Find..."); 
        
        //Action Listeners
        
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Find_V2(ta);
                UpdateStatus();
            }
        });
        
        tb.add(findButton);

    }
    private void AboutButton()
    {
        aboutButton = new JButton("about");
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutManager.doAbout();
            }
        });
        aboutButton.setToolTipText("About...");
        aboutButton.setIcon(new ImageIcon(getClass().getResource("/images/Info16.png")));
        
        tb.add(aboutButton);
    }
    private void AddStatus()
    {
      JLabel statusLabel = new JLabel("...");
      statusLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
      statusLabel.setHorizontalAlignment(JLabel.LEADING);
      p.add(statusLabel, BorderLayout.SOUTH);
      
      // leitourgikotita   ... I know that its bad. But it works. Sorry. :(
      
      //Document Listener
      ta.getDocument().addDocumentListener(new DocumentListener() {

        @Override
        public void removeUpdate(DocumentEvent e) {
         statusLabel.setText(UpdateStatus());
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
         statusLabel.setText(UpdateStatus());
        }

        @Override
        public void changedUpdate(DocumentEvent arg0) {
         statusLabel.setText(UpdateStatus());
        }
    });
      
        //Caret Listener
                ta.addCaretListener(new CaretListener() {
         public void caretUpdate(CaretEvent ce) {
            int pos = ta.getCaretPosition();
            Element map = ta.getDocument().getDefaultRootElement();
            int row = map.getElementIndex(pos);
            Element lineElem = map.getElement(row);
            int col = pos - lineElem.getStartOffset();
            //it works trust me.
            if(statusLabel.getText().contains("|")) { statusLabel.setText(" ");}
            statusLabel.setText(statusLabel.getText() + "  |  Col: " + col + " Row: " + row);
            
         }
    });

        
      
    }
    private String UpdateStatus()
    {
        //Lines & chars
        int countWord = 0; 
        int sentenceCount = 0; 
        int characterCount = 0; 
        int whitespaceCount = 0; 
        int lines = 0;
        
        for (String line : ta.getText().split("\\n"))
        {
            lines++;
            characterCount += line.length(); 
                  
             // \\s+ is the space delimiter in java 
            String[] wordList = line.split("\\s+"); 
                  
            countWord += wordList.length; 
            whitespaceCount += countWord -1; 
                  
            // [!?.:]+ is the sentence delimiter in java 
         /*   String[] sentenceList = line.split("[!?.:]+"); 
                  
            sentenceCount += sentenceList.length;  */
        }
        //End of lines & chars
        
        
        return "Lines: " + lines + " Chars: " + characterCount;
    }
    private void AddFonts()
    {
        fontType = new JComboBox();

        //GETTING ALL AVAILABLE FONT FOMILY NAMES
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (int i = 0; i < fonts.length; i++) {
            fontType.addItem(fonts[i]);
        }
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        tb.add(fontType);
        tb.addSeparator();

        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                String p = fontType.getSelectedItem().toString();
                int s = ta.getFont().getSize();
                ta.setFont(new Font(p, Font.PLAIN, s));
            }
        });

        fontSize = new JComboBox();

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        tb.add(fontSize);

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                String fontFamily = ta.getFont().getFamily();

                Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
                ta.setFont(font1);
            }
        });
    }
    
    private void Finalize()
    {
        p.add(tb, BorderLayout.NORTH);
        add(p);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //set look and feel
        
            try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
            
    }
    
    private void FileManager(String type)   //h8ela na to kanw se 3exwristo class alla ka8e prospa8ia mou apetyxe... :(
    {
       if(type == "open") { 
        JFileChooser open = new JFileChooser(); 
             int option = open.showOpenDialog(this); 

             /*
              * NOTE: because we are OPENing a file, we call showOpenDialog~ if
              * the user clicked OK, we have "APPROVE_OPTION" so we want to open
              * the file
              */
             if (option == JFileChooser.APPROVE_OPTION) {
                 Clear.clear(ta); // clear the TextArea before applying the file contents
                 try {
                     File openFile = open.getSelectedFile();
                     setTitle(openFile.getName() + " | " + "MyEditor3");
                     Scanner scan = new Scanner(new FileReader(openFile.getPath()));
                     while (scan.hasNext()) {
                         ta.append(scan.nextLine() + "\n");
                     }

                 } catch (Exception ex) { 
                     System.err.println(ex.getMessage());
                 }
             }
       }
       else if(type == "save")
       {
          saveFile();
       }
       else 
       {
        this.setTitle("MyEditor");
        ta.setText(null);
        ta.requestFocus();
       }
    }
    private void saveFile() {
        // Open a file chooser
        JFileChooser fileChoose = new JFileChooser();
        // Open the file, only this time we call
        int option = fileChoose.showSaveDialog(this);

        /*
             * ShowSaveDialog instead of showOpenDialog if the user clicked OK
             * (and not cancel)
         */
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File openFile = fileChoose.getSelectedFile();
                setTitle(openFile.getName() + " | " + "MyEditor3");

                BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
                out.write(ta.getText());
                out.close();
                edit = false;
            } catch (Exception ex) { // again, catch any exceptions and...
                // ...write to the debug console
                System.err.println(ex.getMessage());
            }
        }
    }
    
    private void FailSafe() //rwtaei gia save an to klhseis
    {
         this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int safe = JOptionPane.showConfirmDialog(null, "Save before exiting?", "Hey!", JOptionPane.YES_NO_CANCEL_OPTION);

                if (safe == JOptionPane.YES_OPTION) {
                    saveFile();
                    //this.dispose();
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);//yes

                } else if (safe == JOptionPane.CANCEL_OPTION) {

                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);//cancel
                } else {
                    //this.dispose();
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);//no
                }
                    }
        });
    }

}
