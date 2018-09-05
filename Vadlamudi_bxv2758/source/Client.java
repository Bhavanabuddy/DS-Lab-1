/*Name          : BHAVANA VADLAMUDI
Student id  : 1001572758*/


package com.mclient;
import com.mclient.util.ChatUtil;
import com.mclient.*;


import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import javax.swing.plaf.metal.*;
import java.beans.*;
import javax.swing.border.EtchedBorder;
import java.awt.Toolkit.*;
import javax.swing.text.*;
import java.sql.*;


public class Client extends JFrame{
        public String x="";

		private int downloadcount=0;
		
		private JTextPane output = null;
        private JTextField input = null;
        private JMenuBar menuBar = null;
        private JList list = null;

        private JPanel emoticons = null;
        private JButton smiley = null;
        private JButton sad = null;
        private JButton send = null;
		

        private JScrollPane listScrollPane = null;
        

        private ObjectOutputStream out = null;
        private ObjectInputStream in = null;
        private Socket client = null;

        private ChatUtil chatUtil = null;

        private File file = null;
        private FileInputStream fin = null;
        private FileOutputStream fout = null;
        private String name = null;
        private String serverName = null;
        private String fileName = null;
        private String privateBuddy = null;
        private String gamePall = null;
        private Vector vectorList = null;
        private Vector v = null;
        private Container container = null;
        private JPanel ePanel = null;

        private ImageIcon killerIcon = null;
        private ImageIcon smileIcon = null;
        private ImageIcon sadIcon = null;
        private ImageIcon sendIcon = null;
        private boolean isEmoticonDisabled = false;
		private ImageIcon downIcon = null;
		

        private String nameBlock = null;
        private String nameBuzz = null;
        private String strLogin, strPassword = null;
		public String myname=null;
		
        //private Private myChat[] = null;
        //private Private myChat2[] = null;
        private Login login = null;
		private NewUser n = null;
	 
        

        private int countChat = 0;
        private int countChat2 = 0;
       

		String udata="";
		int newcreate=0;


        public Client(String x1){
            super("HTTP Messenger Service ");
            x=x1;

            chatUtil = new ChatUtil();
            container = getContentPane();
            output = new JTextPane();
            output.setEditable(false);
            JScrollPane pane = new JScrollPane(output);

            addWindowListener( new WindowAdapter(){
                public void windowClosing(WindowEvent we){
                    try{
                        if (out!=null){
                            out.writeObject(name + ": has exited the Chat. Bye-bye!");
                            out.writeObject("DISCONNECTED#"+name);
                        }
                    }
                    catch (IOException ioe){
                        System.out.println("Client:> exception at windowListener\n" + ioe.getLocalizedMessage());
                    }
                    System.exit(0);
                }
            });


            input = new JTextField();
         //   input.setBorder(null);
            JScrollPane inputScroll = new JScrollPane(input);
            inputScroll.setPreferredSize(new Dimension(0, 50));
            input.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent ke){
                    if (ke.getKeyCode() == KeyEvent.VK_ENTER){
                        sendText();
                    }
                }
            });

            input.setCaretPosition(0);
            killerIcon = new ImageIcon("killer.gif");
            sadIcon = new ImageIcon("sad.gif");
            smileIcon = new ImageIcon("smiley.gif");
            sendIcon = new ImageIcon("send.gif");
			downIcon= new ImageIcon("user.gif");
			
            ePanel = new JPanel();
            ePanel.setLayout(new FlowLayout());
			
            //ePanel.setPreferredSize(new Dimension(64, 60));//   2*ICONHEIGHT
			ePanel.setPreferredSize(new Dimension(32, 90));//   2*ICONHEIGHT
            emoticons = new JPanel();
            emoticons.setPreferredSize(new Dimension(32, this.getHeight() - 80));
            emoticons.setLayout(new BorderLayout());

			
			
		 
			
            if (smileIcon.getIconHeight() > 0){
                smiley = new JButton(smileIcon);
                smiley.setPreferredSize(new Dimension(45,22));//22
            }
            else{
                smiley = new JButton(":-)");
                smiley.setPreferredSize(new Dimension(45,22));
            }
                smiley.setBorderPainted(false);

            if (sendIcon.getIconHeight() > 0){
                send = new JButton(sendIcon);
                send.setPreferredSize(new Dimension(32,32));
            }
            else{
                send = new JButton(">>");
                send.setPreferredSize(new Dimension(50, 32));
                //send.setBorderPainted(false);
            }

            if (sadIcon.getIconHeight() > 0){
                sad = new JButton(sadIcon);
                sad.setPreferredSize(new Dimension(45,22));//22
            }
            else{
                sad = new JButton(":-(");
                sad.setPreferredSize(new Dimension(45,22));
            }
            sad.setBorderPainted(false);


			vectorList = new Vector();
            vectorList.setSize(1);
            list = new JList(vectorList);
            list.setFixedCellWidth(70);
            list.setFixedCellHeight(20);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            listScrollPane = new JScrollPane(list);
            listScrollPane.setAutoscrolls(true);

			
            sad.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    try{
                        out.writeObject("SAD#"+name);
                  }
                  catch(IOException ioe){
                      System.out.println("Client:> exception at sad button mouse listener\n" + ioe.getLocalizedMessage());
                  }
                }
            });
				 
			
            send.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    sendText();
                }
            });

            smiley.addMouseListener(new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    try{
                        out.writeObject("SMILE#"+name);
                    }
                    catch(IOException ioe){
                        System.out.println("Client:> exception at smiley button mouse listener\n" + ioe.getLocalizedMessage());
                    }
                }
            });

			 
						
            ePanel.add(smiley);
            ePanel.add(sad);
		 
            emoticons.add(ePanel, BorderLayout.NORTH);
            emoticons.add(send, BorderLayout.SOUTH);

            

            try{
                serverName = getServerName();
                if (serverName == null)
                    serverName =  InetAddress.getLocalHost().toString();
            }
            catch(IOException ioe){
                serverName = "localhost";
            }

//            myChat = new Private[10];
  //          myChat2 = new Private[10];
           

    /*        for (int i=0; i<10; i++){
                myChat[i] = null;
                myChat2[i] = null;
              
            }
*/

            list.addMouseListener(createMouseListener());
            container.add(listScrollPane, BorderLayout.WEST);
            container.add(pane, BorderLayout.CENTER);
            container.add(inputScroll, BorderLayout.SOUTH);
            container.add(createMenuBar(), BorderLayout.NORTH);
            container.add(emoticons, BorderLayout.EAST);

            setSize(300,400);
            setVisible(true);

        }// end Client

	  
        public void sendText(){
            String text = input.getText();
            try{
                if (out!=null)
				{
                    if (text.equals(":)") || text.equals(":-)"))
                        out.writeObject("SMILE#"+name);
                    else if (text.equals(":(") || text.equals(":-("))
                        out.writeObject("SAD#"+name);
                    else if (text.equals(">:)"))
                        out.writeObject("KILLER#"+name);
                    else
					{
						//write logic of time here
                        out.writeObject(name + ":" + "%" + text + "%" + System.currentTimeMillis() + "%Method:Post Content-Type:Text/Plain Content-Length" + text.length() + " " + new java.util.Date().toString());
					}
                }
            }
            catch (IOException ioe){
                System.out.println("Client:> exception at KeyListener\n" + ioe.getLocalizedMessage());
            }
            input.setText("");
        }

        public MouseListener createMouseListener(){
            MouseListener ml = new MouseAdapter(){
                public void mouseReleased(MouseEvent me){
                    int button = me.getModifiers();
                    if (button == MouseEvent.BUTTON3_MASK){
                    int index = list.locationToIndex(me.getPoint());
                        if (index != -1){
                            list.setSelectedIndex(index);
                            showPopup(me);
                        }
                    }
                }
                public void mouseClicked(MouseEvent me){
                    int button = me.getModifiers();
                    if (me.getClickCount() == 2) {
                        int index = list.locationToIndex(me.getPoint());
                    }
                    if (me.MOUSE_RELEASED == me.BUTTON2_MASK){
                        int index = list.locationToIndex(me.getPoint());
                    }
                }
            };
           return ml;
        }


        public void showPopup(MouseEvent me){

        }

        private int getPosition(String window){
            int result = 0;
            Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = (int)screen.getWidth();
            int screenHeight = (int)screen.getHeight();
            int width = this.getWidth();
            int height = this.getHeight();
            Point p = new Point();
            p = this.getLocation();
            if (window.equals("chat")){
                if (p.x < width){
                    result = 1;
                    return result; // SPACE ON RIGHT
                }
            }
            /*else if(window.equals("game")){
                if (p.y > height){
                    result = 1;
                    return result; // SPACE ON TOP
                }
            }*/
            return result;
        }


        private JMenuBar createMenuBar(){
            JMenuBar menuBar = new JMenuBar();
            menuBar.add(createMenu("File"));
        return menuBar;
        }

        private JMenu createMenu(String title){
            JMenu menu = new JMenu();
            menu.setText(title);
            if(title.equals("File")){
                menu.setMnemonic(KeyEvent.VK_F);
                menu.add(createItem("Save"));
                menu.add(createItem("Clear"));
				menu.add(createItem("Get"));
                menu.add(createItem("Post"));
				//menu.add(createItem("Offline Message"));
                menu.add(createItem("Exit"));
            }
        return menu;
        }


        private JMenuItem createItem(String title){
            JMenuItem menuItem = new JMenuItem(title);
            if (title.equals("Save"))
                menuItem.setMnemonic(KeyEvent.VK_S);
            if (title.equals("Clear"))
                menuItem.setMnemonic(KeyEvent.VK_C);
			if (title.equals("Exit"))
                menuItem.setMnemonic(KeyEvent.VK_X);
			else if (title.equals("Get"))
                menuItem.setMnemonic(KeyEvent.VK_G);
			else if (title.equals("Post"))
                menuItem.setMnemonic(KeyEvent.VK_P);
            menuItem.addActionListener(createActionListener(title));
            return menuItem;
        }

        private JCheckBoxMenuItem createCheckItem(String title){
            JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(title);
            if (title.equals("Disable emoticons"))
                jCheckBoxMenuItem.setMnemonic(KeyEvent.VK_D);
            jCheckBoxMenuItem.addActionListener(createActionListener(title));
            return jCheckBoxMenuItem;
        }

        private ActionListener createActionListener(String s){
            ActionListener a = null;
            if(s.equals("Clear")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        output.setText("");
                    }
                };
            }
            else if(s.equals("Save")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        chatUtil.saveLog(output);
                    }
                };
            }
			else if(s.equals("Get")) {
				a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        try{
							String fname=JOptionPane.showInputDialog("Enter FileName to Get From HTTP Server ");
                            if (out != null)
							{
                            out.writeObject(name + " wants file " + fname + " From Server ");
                            out.writeObject("GETFILE#"+name+"#"+fname);
                            }
                        }
                        catch (IOException ioe){
                            System.out.println("Client:> exception at exitActionListener\n" + ioe.getLocalizedMessage());
                        }
                    }
                };
			}
            else if (s.equals("Exit")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        try{
                            if (out != null){
                            out.writeObject(name + " has exited the Chat. Bye-bye!");
                            out.writeObject("DISCONNECTED#"+name);
                            }
                        }
                        catch (IOException ioe){
                            System.out.println("Client:> exception at exitActionListener\n" + ioe.getLocalizedMessage());
                        }
                        System.exit(0);
                    }
                };
            }
            
            else if (s.equals("Disable emoticons")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        disableFaces();
                    }
                };
            }
            else if (s.equals("About")){
                a = new ActionListener(){
                    public void actionPerformed(ActionEvent ae){
                        about();
                    }
                };
            }
           
            return a;
        }
     

		

        public void about(){
            JOptionPane.showMessageDialog(this, "Local Messenger System \n           by\nBhavana", "About", JOptionPane.INFORMATION_MESSAGE);
        }

        public Socket getClient(){
            return client;
        }

        public ObjectOutputStream getOut(){
            return out;
        }

        public ObjectInputStream getIn(){
            return in;
        }

      
        public void showListMessage(){
            JOptionPane.showMessageDialog(this, "You must select a person from the left list", "None selected", JOptionPane.INFORMATION_MESSAGE);
        }

        public void disableFaces(){
            if (isEmoticonDisabled){
                isEmoticonDisabled = false;
                smiley.setText(null);
                smiley.setIcon(smileIcon);
                sad.setText(null);
                sad.setIcon(sadIcon);
            }
            else{
                isEmoticonDisabled = true;
                smiley.setIcon(null);
                smiley.setText(":-)");
                sad.setIcon(null);
                sad.setText(":-(");
            }
            smiley.invalidate();
            sad.invalidate();
        }

        public void setLogin(String login){
            strLogin = login;
        }

        public void setPassword(String pass){
            strPassword = pass;
        }


        private void doLogin(){
			//JOptionPane.showMessageDialog(this,"Activated this phase " + login.getNewUser());
            do{
				//JOptionPane.showMessageDialog(this,"USER :" + login);
				 
                if (login != null)
                login.setVisible(true);
                else
				{
					
                    login = new Login(new JDialog(), "HTTP CLIENT LOGIN", true);
					//JOptionPane.showMessageDialog(this,"user " + login.getNewUser());
                    login.setSize(250,150);
                    login.setLocationRelativeTo(this);
                    login.setVisible(true);
                    login.setResizable(false);
                    login.toFront();
					strLogin = login.getLogin();
                strPassword = login.getPassword();
                name = strLogin;
				myname=strLogin;
                this.setTitle(name);
				//JOptionPane.showMessageDialog(this,strLogin + "--" + strPassword);
				//break;
				   }
				   
				 //JOptionPane.showMessageDialog(this,"user " + login.getNewUser());
				if(login.getNewUser())
				{
					//JOptionPane.showMessageDialog(this,"New User in activation");
					n = new NewUser(new JDialog(), "NEW HTTP CLIENT",true);
					n.setSize(200,400);
					n.setLocationRelativeTo(this);
                   // n.setVisible(true);
                    n.setResizable(true);
                    n.toFront();
					udata=n.retData();
					//JOptionPane.showMessageDialog(this,"Prasad New Data---> " + udata);
					name = n.getName();
					myname=n.getName();
					strLogin = n.getName();
                strPassword = n.getPwd();
					newcreate=1;
					//JOptionPane.showMessageDialog(this,"For New User only " + strLogin + "--" + strPassword);
					this.setTitle(name);
					//runClient();
					break;
				}
   /*
   							udata=n.retData();
                strLogin = login.getLogin();
                strPassword = login.getPassword();
                name = strLogin;
				myname=strLogin;
	
   */
				
                this.setTitle(name);
				//runClient();
            }while(strLogin == null || strPassword == null || strLogin.equals("") || strPassword.equals(""));
			//JOptionPane.showMessageDialog(this,"Next Phase ");
            runClient();
        }

		/*
 *
 *  RunClient()
 *
 */

 
 
        public void runClient(){
            String line = new String(" ");
            Object objReceived = null;
            try{
                  client = new Socket(x,5000);

				 // JOptionPane.showMessageDialog(this,newcreate+"---" + udata);
				  //System.out.println("New user " + newcreate + "---" + udata);
                out = new ObjectOutputStream(client.getOutputStream());
                in = new ObjectInputStream(client.getInputStream());
                StringTokenizer strT = null;
                String nt = null;
				if(newcreate==1)
				{
					try{
					//	JOptionPane.showMessageDialog(this,out);
						if(out!=null)
						{
						//	JOptionPane.showMessageDialog(this,"it is not null so i am writing back to server " + udata);
							out.writeObject("NEWUSER#"+udata);
						}
					}catch (IOException ioe){
			                System.out.println(ioe.getLocalizedMessage());
					}
				}
				try{
                    if (out!=null)
					{
                        input.setText("");
                        out.writeObject("NAMEOK#" + strLogin + "#" + strPassword);
                    }
                }
                catch (IOException ioe){
                    System.out.println("Exception caught " + ioe);
                }
				
				
//  Main loop DO
                do{
                    objReceived = in.readObject();
                    int option = 0;
                    //  flash the window to notify user something was received
                    if (this.getState() == JFrame.ICONIFIED || !this.isFocused())
                        this.setVisible(true);
//  STRING
                    if(objReceived instanceof String)
					{
                        line = (String)objReceived;
                        strT = new StringTokenizer(line, "#");
                        if (strT.countTokens() > 1){
                            nt = strT.nextToken();
                            if (nt.equals("NAMEREJECTED")){
                                String n = strT.nextToken();
                                JOptionPane.showMessageDialog(this, n + " is not a valid user.", "Login error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            else if (nt.equals("DBERROR")){
                                String n = strT.nextToken();
                                JOptionPane.showMessageDialog(this, "Database error.", n+": login error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            else if (nt.equals("WRONGPASSWORD")){
                                String n = strT.nextToken();
                                JOptionPane.showMessageDialog(this, "The password does not match.", n+": login error", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
							else
								if(nt.equals("User Registered"))
								{
								JOptionPane.showMessageDialog(this,"Successfully Registered ");
								System.exit(1);
								}
							else
								if(nt.equals("User Not Registered"))
								{
								JOptionPane.showMessageDialog(this,"Not Able To Register With Application ");
								System.exit(1);
								}
                            else if (nt.equals("NUKE")){
                                JOptionPane.showMessageDialog(this, "The server chose to shut you down.", "Dave, Dave...", JOptionPane.ERROR_MESSAGE);
                                System.exit(1);
                            }
                            else if (nt.equals("SERVER")){
                                appendDisplay(output, "Server: "+strT.nextToken()+"\n");
                            }
							 
              

             

            
                else if (nt.equals("SMILE2")){
                    String fromWho = strT.nextToken();
                    if (smileIcon.getIconHeight() > 0 && !isEmoticonDisabled){
                        appendDisplay(output, fromWho + ":" + "% %");
                        insertImage("smiley");
                    }
                    else
                        appendDisplay(output, fromWho+": " + "%:-)%");
                }

                else if (nt.equals("SAD2")){
                    String fromWho = strT.nextToken();
                    if (sadIcon.getIconHeight() > 0 && !isEmoticonDisabled){
                        appendDisplay(output, fromWho + ":" + "% %");
                        insertImage("sad");
                    }
                    else
                        appendDisplay(output, fromWho + ": " + "%:-(%");
                }

                else if (nt.equals("KILLER2")){
                    String fromWho = strT.nextToken();
                    if (killerIcon.getIconHeight() > 0 && !isEmoticonDisabled){
                        appendDisplay(output, fromWho + ":" + "% %");
                        insertImage("killer");
                    }
                    else
                        appendDisplay(output, fromWho + ": " + "%>:-)%");
                }

            }// end if(countTokens)

            else{
               if (!line.equals("<<SERVERSHUTDOWN>>")){
                    appendDisplay(output, line);
               }
            }

        }


//  ********************************************************************
//							 V E C T O R
//  ********************************************************************
        else if (objReceived instanceof Vector)
		{
			//if downloads are there activate download button else not required
          v =(Vector)objReceived;
          vectorList.setSize(v.size());
		  downloadcount=Integer.parseInt(v.get(0).toString());
		  //v.remove(0);
		  int idx=0;
		  int value=-1;
		 
          for(int i=1; i<v.size(); i++,idx++)
            vectorList.set(i-1,v.get(i));
          list.ensureIndexIsVisible(v.size());
          list.repaint();
        }

//  ********************************************************************
//								B Y T E
//  ********************************************************************
				else if (objReceived instanceof byte[]){
  				final JFileChooser chooser = new JFileChooser();
	  			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          chooser.setSelectedFile(new File(fileName));
		  		chooser.addPropertyChangeListener(new PropertyChangeListener(){
  						public void propertyChange(PropertyChangeEvent pce){
	  						if(pce.getPropertyName().equals(JFileChooser.DIRECTORY_CHANGED_PROPERTY)){
                  chooser.setSelectedFile(new File(pce.getNewValue() + File.separator + fileName));
			  				}
              }
          }
				  );
  				option = chooser.showSaveDialog(Client.this.output);
          File f = chooser.getSelectedFile();
	  			if (option==JFileChooser.APPROVE_OPTION){
  	  			try{
				    		FileOutputStream fout = new FileOutputStream(f);
					    	byte[] b = (byte[])objReceived;
						    fout.write(b);
  						  fout.close();
  	  			}catch (IOException ioe){
                System.out.println("Client:> error while receiving bytes");
                ioe.printStackTrace();
            }
				  }
       }
     }
     while (!line.equals("<<SERVERSHUTDOWN>>"));
        appendDisplay(output, "Server Down\n");
        JOptionPane.showMessageDialog(this, "Error finding chat server.", "Connection error", JOptionPane.ERROR_MESSAGE);

    }
    catch (IOException ioe){
        System.out.println("Client:> error finding the chat server...");
        JOptionPane.showMessageDialog(this, "Error finding chat server.", "Connection error", JOptionPane.ERROR_MESSAGE);
    }
    catch (ClassNotFoundException cnfe){
        System.out.println("Client:> class not found exception");
        cnfe.printStackTrace();
        System.exit(0);
    }

 }

	


  
  

    public String getServerName(){
        String serverName = null;
        System.out.println();
        Properties prop = new Properties(System.getProperties());
        try{
            FileInputStream fin = new FileInputStream("client.properties");
            prop.load(fin);
            serverName = prop.getProperty("servername");
        }
        catch(IOException ioe){
            System.out.println("Client:> exception at getServerName()\n" + ioe.getLocalizedMessage());
            return null;
        }
        return serverName;
    }


    public void appendDisplay(JTextPane display, String text){

            StyledDocument doc = (StyledDocument)display.getDocument();
            Style style = doc.addStyle("newStyle", null);

            StringTokenizer strTok = new StringTokenizer(text, "%");

            if (strTok.countTokens() > 1){
                MutableAttributeSet attr = new SimpleAttributeSet();
                String strFirstElement = strTok.nextToken();
                String strSecondElement = strTok.nextToken();
                String strName = strFirstElement + "\n";
                String strMessage = "   " + strSecondElement + "\n";
                doc = (StyledDocument)display.getDocument();
                try{
                    doc.insertString(doc.getLength(), strName, style);
                    output.setCaretPosition(doc.getLength());
                }
                catch(BadLocationException ble){
                    System.out.println("server>: bad location error");
                }
                display.setDocument(doc);

                doc = (StyledDocument)display.getDocument();
                StyleConstants.setForeground(attr, new Color(1, 0, 0));
                doc.setCharacterAttributes(doc.getLength() - strName.length(), strName.length(), attr, true);
                if (!strSecondElement.trim().equals("")){
                    try{
                        doc.insertString(doc.getLength(), strMessage, style);
                        output.setCaretPosition(doc.getLength());
                    }
                    catch(BadLocationException ble){
                        System.out.println("server>: bad location error");
                    }
                }
                display.setDocument(doc);
            }
            else{
                try{
                    doc.insertString(doc.getLength(), text, style);
                    output.setCaretPosition(doc.getLength());
                }
                catch(BadLocationException ble){
                    System.out.println("server>: bad location error");
                }
                display.setDocument(doc);
            }
        }

    public void insertImage(String description){
        StyledDocument doc = (StyledDocument)output.getDocument();
        Style style = doc.addStyle("StyleName", null);

        if (description.equals("killer")){
            try{
                doc.insertString(doc.getLength(), "   ", style);
                StyleConstants.setIcon(style, killerIcon);
                doc.insertString(doc.getLength(), ">:)\n", style);
                output.setCaretPosition(doc.getLength());
            }
            catch(BadLocationException ble){
                System.out.println("Bad location");
            }
        }

        else if (description.equals("sad")){
            try{
                doc.insertString(doc.getLength(), "   ", style);
                StyleConstants.setIcon(style, sadIcon);
                doc.insertString(doc.getLength(), ":-(\n", style);
                output.setCaretPosition(doc.getLength());
            }
            catch(BadLocationException ble){
                System.out.println("Bad location");
            }
        }

        else if (description.equals("smiley")){
            try{
                doc.insertString(doc.getLength(), "   ", style);
                StyleConstants.setIcon(style, smileIcon);
                doc.insertString(doc.getLength(), ":-)\n", style);
                output.setCaretPosition(doc.getLength());
            }
            catch(BadLocationException ble){
                System.out.println("Bad location");
            }
      }
    }


        public static void main(String[] args)throws IOException{
            Client client = new Client(args[0]);
            client.doLogin();
	}
}


