import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class SimplifiedEditor extends JFrame implements ActionListener{ //JFrame is version of java.awt that supports java Swing lib

    JTextArea textArea = new JTextArea();
    JScrollPane scrollPane;
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem saveItem = new JMenuItem("Save");
    JMenuItem exitItem = new JMenuItem("Exit");

    // constructor
    SimplifiedEditor(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Note");
        this.setSize(500, 500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial",Font.PLAIN,20));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450,450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        openItem.addActionListener(this); // action listeners added to 3 clickable options
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        menuBar.add(fileMenu);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        this.setJMenuBar(menuBar); // even though the field was instantiated and field menu added to menuBar, still need to set this
        this.add(scrollPane); // and add this. odd?
        this.setVisible(true); // text box will not populate without this
    }


    // code to allow menu buttons to do stuff
    @Override
    public void actionPerformed(ActionEvent e) { // this is implemented from the ActionListener Interface

        // for action listener associated with openItem JMenuItem
        if(e.getSource()==openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(".")); // sets start search location to current project folder but can then search anywhere
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt"); // interactable filter for file type
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null); // pops up an "open file" dialog

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null; // text scanner class

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) { // check if file is 'real'
                        while(fileIn.hasNextLine()) { // read lines of file.
                            String line = fileIn.nextLine()+"\n"; // string reassigned to new line each loop
                            textArea.append(line); // text area appended. this is why new opened file will share same space
                        } // as previously opened file unless the text window is closed first. will also re-open
                    } // text of the same file into the text window and result in redundant text content in the window
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

            if(response == JFileChooser.APPROVE_OPTION) {  // fileChooser.approve_option will return an int 0 or 1 if location available to save
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath()); // file created with path location
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