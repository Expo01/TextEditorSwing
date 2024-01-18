import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;

public class TextEditor extends JFrame implements ActionListener{ //JFrame is version of java.awt that supports java Swing lib

    JTextArea textArea;
    JScrollPane scrollPane;
    JLabel fontLabel;
    JSpinner fontSizeSpinner;
    JButton fontColorButton;
    JComboBox fontBox;

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem exitItem;

    // --------------------------CONSTRUCTOR START----------------------
    TextEditor(){ // constructor
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // terminates program when exit button on text frame pushed
        this.setTitle("Bro text Editor"); // frame title
        this.setSize(500, 500); // frame size of window itself, not actual text space
        this.setLayout(new FlowLayout()); // FlowLayout class provides a simple layout manager for the text frame
        this.setLocationRelativeTo(null); // location on screen text frame appears

        textArea = new JTextArea(); // creates the text area
        textArea.setLineWrap(true); // text will go to next line when right border hit
        textArea.setWrapStyleWord(true); // when right border hit, it will take the whole word to the next line
        textArea.setFont(new Font("Arial",Font.PLAIN,20)); // set default font type and size

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450)); // size of text area
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // ensures scroll bar is present.
        // can choose never though and still scroll through the text field, just no bar to orient you to where you are in the doc

        // bunch of flub about creating arrows for increasing and decreasing font size
        fontLabel = new JLabel("Font: ");
        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(e -> textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizeSpinner.getValue())));

        //bunch of flub about changing color and font type
        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");

        // ----- menubar -----

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem); // it appears that just creating a menu item doesn't quite do it. need to add the menu
        // item to the menu
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        // ----- /menubar -----

        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }

    // --------------------------CONSTRUCTOR END----------------------


    @Override
    public void actionPerformed(ActionEvent e) { // this is implemented from the ActionListener Interface

        if(e.getSource()==fontColorButton) {
            JColorChooser colorChooser = new JColorChooser();

            Color color = colorChooser.showDialog(null, "Choose a color", Color.black);

            textArea.setForeground(color);
        }

        if(e.getSource()==fontBox) {
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
        }

        if(e.getSource()==openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            // Eclipse was unable to import this correctly. Clunky. Opened the same file with IntelliJ and problem fixed
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null); // pops an an "open file" dialog

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) {
                        while(fileIn.hasNextLine()) {
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                finally {
                    fileIn.close();
                }
            }
        }
        if(e.getSource()==saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                }
                catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                finally {
                    fileOut.close();
                }
            }
        }
        if(e.getSource()==exitItem) {
            System.exit(0);
        }
    }
}





/*   --- NOTES---
- Eclipse is not as good as IntelliJ. No auto-imports (although possibly can be turned on). One import wouldn't work and
    I would have needed to change the strategy for file open. I tried to change java versions and that didn't work. Just
    opening the code in IntelliJ fixed the import issue.
- the text editor appears to open the file as a String, and when opening one file and then another file, it will display
    both text files concurrently in the same text window. not exaclty what i want, where i would want to be able to open
    separate windows for each medical document



 */
