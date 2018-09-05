/*Name          : BHAVANA VADLAMUDI
Student id  : 1001572758*/

package com.mclient;
import java.util.*;
import javax.swing.*;
 
public class KillerThread extends Thread {

		//this class will remove all dead threads
    public Vector clientThreads;
    public Vector runnables;
    public Vector members;
    public Thread main;
    public JEditorPane display;

    public KillerThread(Vector clientThreads, Vector runnable, Vector members, Thread main, JEditorPane display){
        this.clientThreads = clientThreads;
        this.runnables = runnables;
        this.members = members;
        this.main = main;
        this.display = display;
        start();
    }

	public void run(){
            if (display != null)
		//display.setText("Killer Thread Started for kill all dead Threads\n");
            while (main.isAlive()){
                for (int i=0;i<clientThreads.size();i++ ){
                    if (!((Thread)clientThreads.get(i)).isAlive()){
                        clientThreads.removeElementAt(i);
                        runnables.removeElementAt(i);
                        members.insertElementAt(" ",i);
                        if (members.elementAt(i) != null){
                            try{
                                members.removeElementAt(i);
                            }
                            catch(ArrayIndexOutOfBoundsException a){
                                System.err.println("server>: killerthread error...\n" + a.getLocalizedMessage());
                            }
			}
                    }// end if
		}// end for
		try{
                    Thread.sleep(2000);
                }
		catch (InterruptedException ie){
                    System.err.println("server>: killerthread error...\n" + ie.getLocalizedMessage());
		}
            }// end while
        }// end run
}