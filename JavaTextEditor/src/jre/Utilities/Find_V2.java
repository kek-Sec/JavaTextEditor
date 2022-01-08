/**
 * @author Petrakis Georgios , tp4768
 * 
 * Find_V2 is the second generation find and ReplaceButton window responsible for all related operations
 */
package jre.Utilities;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Find_V2 extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    int startIndex = 0;
    int select_start = -1;
    JLabel label_1, label_2;
    JTextField FindTextField, ReplaceTextField;
    JButton findBtn, findNext, ReplaceButton, ReplaceAllButton, CancelButton;
    private JTextArea TextArea;

    /**
     * Constructor , responsible for creating the window and initializing all the components
     * @param text The text area, to be passed by the parent window
     */
    public Find_V2(JTextArea text) {
        this.TextArea = text;

        label_1 = new JLabel("Find:");
        label_2 = new JLabel("Replace:");
        FindTextField = new JTextField(30);
        ReplaceTextField = new JTextField(30);
        findBtn = new JButton("Find");
        findNext = new JButton("Find Next");
        ReplaceButton = new JButton("Replace");
        ReplaceAllButton = new JButton("Replace All");
        CancelButton = new JButton("cancel");

        // Set Layout NULL
        setLayout(null);

        // Set the width and height of the label
        int labWidth = 80;
        int labHeight = 20;

        // Adding labels
        label_1.setBounds(10, 10, labWidth, labHeight);
        add(label_1);
        FindTextField.setBounds(20 + labWidth, 10, 120, 20);
        add(FindTextField);
        label_2.setBounds(10, 10 + labHeight + 10, labWidth, labHeight);
        add(label_2);
        ReplaceTextField.setBounds(20 + labWidth, 10 + labHeight + 10, 120, 20);
        add(ReplaceTextField);

        // Adding buttons
        findBtn.setBounds(225, 6, 115, 20);
        add(findBtn);
        findBtn.addActionListener(this);

        findNext.setBounds(225, 28, 115, 20);
        add(findNext);
        findNext.addActionListener(this);

        ReplaceButton.setBounds(225, 50, 115, 20);
        add(ReplaceButton);
        ReplaceButton.addActionListener(this);

        ReplaceAllButton.setBounds(225, 72, 115, 20);
        add(ReplaceAllButton);
        ReplaceAllButton.addActionListener(this);

        CancelButton.setBounds(225, 94, 115, 20);
        add(CancelButton);
        CancelButton.addActionListener(this);

        // Set the width and height of the window
        int width = 360;
        int height = 160;

        // Set size window
        setSize(width, height);

        // center the frame on the frame
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void find() {
        select_start = TextArea.getText().toLowerCase().indexOf(FindTextField.getText().toLowerCase());
        if (select_start == -1) {
            startIndex = 0;
            JOptionPane.showMessageDialog(null, "Could not find \"" + FindTextField.getText() + "\"!");
            return;
        }
        if (select_start == TextArea.getText().toLowerCase().lastIndexOf(FindTextField.getText().toLowerCase())) {
            startIndex = 0;
        }
        int select_end = select_start + FindTextField.getText().length();
        TextArea.requestFocus();
        TextArea.select(select_start, select_end);
    }

    public void findNext() {
        String selection = TextArea.getSelectedText();
        try {
            selection.equals("");
        } catch (NullPointerException e) {
            selection = FindTextField.getText();
            try {
                selection.equals("");
            } catch (NullPointerException e2) {
                selection = JOptionPane.showInputDialog("Find:");
                FindTextField.setText(selection);
            }
        }
        try {
            int select_start = TextArea.getText().toLowerCase().indexOf(selection.toLowerCase(), startIndex);
            int select_end = select_start + selection.length();
            TextArea.requestFocus();
            TextArea.select(select_start, select_end);
            startIndex = select_end + 1;

            if (select_start == TextArea.getText().toLowerCase().lastIndexOf(selection.toLowerCase())) {
                startIndex = 0;
            }
        } catch (NullPointerException e) {
        }
    }

    public void ReplaceButton() {
        try {
            find();
            if (select_start != -1)
                TextArea.replaceSelection(ReplaceTextField.getText());
        } catch (NullPointerException e) {
            System.out.print("Null Pointer Exception: " + e);
        }
    }

    public void ReplaceAllButton() {
        TextArea.setText(TextArea.getText().toLowerCase().replace(FindTextField.getText().toLowerCase(), ReplaceTextField.getText()));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findBtn) {
            find();
        } else if (e.getSource() == findNext) {
            findNext();
        } else if (e.getSource() == ReplaceButton) {
            ReplaceButton();
        } else if (e.getSource() == ReplaceAllButton) {
            ReplaceAllButton();
        } else if (e.getSource() == CancelButton) {
            this.setVisible(false);
        }
    }

}