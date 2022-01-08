/**
 * @author Petrakis Georgios , tp4768
 * 
 * Main class for creating the TextEditor Form and building the toolbar
 */
package jre.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;

public class BuildForm extends JFrame {

    // Dec Section

    About AboutManager = new About();

    private boolean edit = false;
    private JComboBox fontSize;
    private JComboBox fontType;
    private JPanel Panel;
    public JTextArea TextArea;
    private JScrollPane sc;
    private JToolBar ToolBar;
    private JButton newButton, openButton, saveButton, findButton, aboutButton, printButton, colorButton, backgroundColorButton;
    public JFileChooser FileChooser;
    public File theFile;

    // end

    public BuildForm() {
        this.setVisible(true);
    }

    public void Build() {
        Base();
        FileButtons();
        FindButtons();
        AboutButton();
        printButton();
        AddStatus();
        AddFonts();
        AddColorPicker();
        AddBackgroundColorPicker();
        Finalize();
        FailSafe();
    }

    /**
     * Function to create the base of our Form , initializing all the components
     */
    private void Base() {
        Panel = new JPanel(new BorderLayout());
        TextArea = new JTextArea(30, 100);
        TextArea.setFont(new Font("Century Gothic", Font.PLAIN, 12));
        TextArea.setTabSize(2);
        TextArea.setLineWrap(true);
        TextArea.setWrapStyleWord(true);

        sc = new JScrollPane(TextArea);
        Panel.add(sc);

        ToolBar = new JToolBar();
        ToolBar.setFloatable(false);
    }

    /**
     * Function to create the print buttons and place them to the toolbar,
     * ActionListener handling is also done within this function
     */
    private void printButton() {
        printButton = new JButton("print");
        printButton.setToolTipText("Print the file...");
        printButton.setIcon(new ImageIcon(getClass().getResource("/images/Print16.png")));
        ToolBar.add(printButton);

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Print(TextArea);
            }
        });

    }

    /**
     * Function to create file handling buttons in the toolbar
     * ActionListener handling is also done within this function
     */
    private void FileButtons() {
        // initialize buttons
        newButton = new JButton("new");
        openButton = new JButton("open");
        saveButton = new JButton("save");
        // set tooltips
        newButton.setToolTipText("New file...");
        openButton.setToolTipText("Open a file...");
        saveButton.setToolTipText("Save a file...");

        // set icons
        newButton.setIcon(new ImageIcon(getClass().getResource("/images/New16.png")));
        openButton.setIcon(new ImageIcon(getClass().getResource("/images/Open16.gif")));
        saveButton.setIcon(new ImageIcon(getClass().getResource("/images/Save16.gif")));
        // add them to the toolbar
        ToolBar.add(newButton);
        ToolBar.add(openButton);
        ToolBar.add(saveButton);

        // Action listeners

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

    /**
     * Function for creating the buttons for displaying the super deluxe ultra
     * FIND_V2 child component
     * ActionListener handling is also done within this function
     */
    private void FindButtons() {
        findButton = new JButton("find");

        findButton.setIcon(new ImageIcon(getClass().getResource("/images/Search16.png")));

        findButton.setToolTipText("Find...");

        // Action Listeners

        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Find_V2(TextArea);
                UpdateStatus();
            }
        });

        ToolBar.add(findButton);

    }

    /**
     * Function for creating the about button and place it to the toolbar,
     * ActionListener handling is also done within this function
     */
    private void AboutButton() {
        aboutButton = new JButton("about");
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutManager.doAbout();
            }
        });
        aboutButton.setToolTipText("About...");
        aboutButton.setIcon(new ImageIcon(getClass().getResource("/images/Info16.png")));

        ToolBar.add(aboutButton);
    }

    /**
     * Function for creating the status bar and place it to the bottom of the form
     * ActionListener handling is also done within this function (for the textarea)
     */
    private void AddStatus() {
        JLabel statusLabel = new JLabel("...");
        statusLabel.setBorder(new EmptyBorder(4, 4, 4, 4));
        statusLabel.setHorizontalAlignment(JLabel.LEADING);
        Panel.add(statusLabel, BorderLayout.SOUTH);

        // leitourgikotita ... I know that its bad. But it works. Sorry. :(

        // Document Listener
        TextArea.getDocument().addDocumentListener(new DocumentListener() {

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

        // Caret Listener
        TextArea.addCaretListener(new CaretListener() {
            public void caretUpdate(CaretEvent ce) {
                int pos = TextArea.getCaretPosition();
                Element map = TextArea.getDocument().getDefaultRootElement();
                int row = map.getElementIndex(pos);
                Element lineElem = map.getElement(row);
                int col = pos - lineElem.getStartOffset();
                // it works trust me.
                if (statusLabel.getText().contains("|")) {
                    statusLabel.setText(" ");
                }
                statusLabel.setText(statusLabel.getText() + "  |  Col: " + col + " Row: " + row);

            }
        });

    }

    /**
     * Helper function for updating the status bar
     * 
     * @return the status bar text
     */
    private String UpdateStatus() {
        // Lines & chars
        int countWord = 0;
        int sentenceCount = 0;
        int characterCount = 0;
        int whitespaceCount = 0;
        int lines = 0;

        for (String line : TextArea.getText().split("\\n")) {
            lines++;
            characterCount += line.length();

            // \\s+ is the space delimiter in java
            String[] wordList = line.split("\\s+");

            countWord += wordList.length;
            whitespaceCount += countWord - 1;

            // [!?.:]+ is the sentence delimiter in java
            /*
             * String[] sentenceList = line.split("[!?.:]+");
             * 
             * sentenceCount += sentenceList.length;
             */
        }
        // End of lines & chars

        return "Lines: " + lines + " Chars: " + characterCount;
    }

    /**
     * Function for creating the menu bar and place it to the top of the form,
     * the menu bar will be used for Font handling
     * ActionListener handling is also done within this function
     */
    private void AddFonts() {
        fontType = new JComboBox();

        // GETTING ALL AVAILABLE FONT FOMILY NAMES
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        for (int i = 0; i < fonts.length; i++) {
            fontType.addItem(fonts[i]);
        }
        fontType.setMaximumSize(new Dimension(170, 30));
        fontType.setToolTipText("Font Type");
        ToolBar.add(fontType);
        ToolBar.addSeparator();

        fontType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {

                String Panel = fontType.getSelectedItem().toString();
                int s = TextArea.getFont().getSize();
                TextArea.setFont(new Font(Panel, Font.PLAIN, s));
            }
        });

        fontSize = new JComboBox();

        for (int i = 5; i <= 100; i++) {
            fontSize.addItem(i);
        }
        fontSize.setMaximumSize(new Dimension(70, 30));
        fontSize.setToolTipText("Font Size");
        ToolBar.add(fontSize);

        fontSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sizeValue = fontSize.getSelectedItem().toString();
                int sizeOfFont = Integer.parseInt(sizeValue);
                String fontFamily = TextArea.getFont().getFamily();

                Font font1 = new Font(fontFamily, Font.PLAIN, sizeOfFont);
                TextArea.setFont(font1);
            }
        });
    }

    /**
     * Function for creating the color picker and place it to the toolbar,
     * ActionListener handling is also done within this function
     */
    private void AddColorPicker() {
        colorButton = new JButton("text color");
        colorButton.setToolTipText("Text Color");
        ToolBar.add(colorButton);

        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Choose a Color", Color.BLACK);
                TextArea.setForeground(color);
            }
        });
    }

    /**
     * Function to add background color picker
     * ActionListener handling is also done within this function
     */
    private void AddBackgroundColorPicker() {
        backgroundColorButton = new JButton("background color");
        backgroundColorButton.setToolTipText("Background Color");
        ToolBar.add(backgroundColorButton);

        backgroundColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Choose a Color", Color.WHITE);
                TextArea.setBackground(color);
            }
        });
    }

    /**
     * Finalize function is called in order to pack the form and pretty much render
     * it to the user,
     * we will also set the look and feel
     */
    private void Finalize() {
        Panel.add(ToolBar, BorderLayout.NORTH);
        add(Panel);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // set look and feel

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

    /**
     * Function to handle the FileManager
     * 
     * @param type Type of operation
     */
    private void FileManager(String type) {
        if (type == "open") {
            JFileChooser open = new JFileChooser();
            int option = open.showOpenDialog(this);

            /*
             * NOTE: because we are OPENing a file, we call showOpenDialog~ if
             * the user clicked OK, we have "APPROVE_OPTION" so we want to open
             * the file
             */
            if (option == JFileChooser.APPROVE_OPTION) {
                // clear the TextArea before applying the file contents
                TextArea.setText("");
                try {
                    File openFile = open.getSelectedFile();
                    setTitle(openFile.getName() + " | " + "JavaTextEditor");
                    Scanner scan = new Scanner(new FileReader(openFile.getPath()));
                    while (scan.hasNext()) {
                        TextArea.append(scan.nextLine() + "\n");
                    }

                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        } else if (type == "save") {
            saveFile();
        } else {
            this.setTitle("JavaTextEditor");
            TextArea.setText(null);
            TextArea.requestFocus();
        }
    }

    /**
     * Function to handle the saving of the file
     */
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
                setTitle(openFile.getName() + " | " + "JavaTextEditor");

                BufferedWriter out = new BufferedWriter(new FileWriter(openFile.getPath()));
                out.write(TextArea.getText());
                out.close();
                edit = false;
            } catch (Exception ex) { // again, catch any exceptions and...
                // ...write to the debug console
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * function to ask user if he wants to save before exiting
     * if textarea is empty then it will exit without asking
     */
    private void FailSafe() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (TextArea.getText().isEmpty()) { // if textarea is empty then exit without asking
                    System.exit(0);
                } else {
                    int result = JOptionPane.showConfirmDialog(null, "Do you want to save before exiting?", "Save",
                            JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) { // if user clicks yes then save the file
                        saveFile();
                        System.exit(0);
                    } else { // if user clicks no then exit without saving
                        System.exit(0);
                    }
                }
            }
        });
    }

}
