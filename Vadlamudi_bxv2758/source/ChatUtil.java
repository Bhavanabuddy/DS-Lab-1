/*Name          : BHAVANA VADLAMUDI
Student id  : 1001572758*/

package com.mclient.util;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import java.io.*;

public class ChatUtil{

    public void saveLog(Object textObject){
        
        JTextPane textPane = null;
        JEditorPane editorPane = null;
        FileWriter file = null;
        File f = null;
        String log = null;
        
        JFileChooser chooser = new JFileChooser();
        
        if (textObject instanceof JTextPane){
            textPane = (JTextPane)textObject;
            log = textPane.getText();
        }
        else if (textObject instanceof JEditorPane){
            editorPane = (JEditorPane)textObject;
            log = editorPane.getText();
        }
        
        int option = chooser.showSaveDialog(textPane);
        if (option == JFileChooser.APPROVE_OPTION)
            f = chooser.getSelectedFile();
        try{
            if (f != null){
                file = new FileWriter(f);
                file.write(log);
                file.close();
            }
        }
        catch(IOException ioe){
            System.out.println("Error:> error saving log...\n" + ioe.getLocalizedMessage());
        }
    }
}