/*Name          : BHAVANA VADLAMUDI
Student id  : 1001572758*/


package com.mclient;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.JEditorPane.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;
 
import com.mclient.util.ChatUtil;

    public class Server extends JFrame{

/**
*	This program enables chat between clients connected through a server using a
*	common chat area. The server here would be multi threaded. A thread would be
*	created to serve each client
*/
		InetAddress seraddress;
		private Vector clientThreads = null;
        private Vector runnables = null;
        private Hashtable hashtable = null;
        private Vector members = null;
        private JEditorPane display = null;
        private JList list = null;
        private KillerThread k = null;
        private ServerSocket server = null;
        private Socket client = null;
        private String name = null;
        private boolean listen = true;
        private JMenuBar menubar = null;
        private JMenu menu = null;
        private JScrollPane jspane = null;
        private JScrollPane jlist = null;
        private Container cnt = null;
        private JPanel icon = null;
        static private ImageIcon killerIcon = null;
        static private ImageIcon smileIcon = null;
        static private ImageIcon sadIcon = null;

        public ChatUtil chatUtil = null;

	public Server(String strInput){
            if (strInput.equals("x")){
                this.setTitle("HTTP SERVER 1.0");
                chatUtil = new ChatUtil();
                cnt = getContentPane();
                members = new Vector();

                addWindowListener(new WindowAdapter(){
                    public void windowClosing(WindowEvent we){
                        broadCast("<<SERVERSHUTDOWN>>");
                        listen = false;
                        System.exit(0);
                    }
                });

                clientThreads = new Vector();
                runnables = new Vector();
                hashtable = new Hashtable();
                list = new JList(members);
                list.setFixedCellWidth(50);
                list.setFixedCellHeight(20);
                list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                menubar = new JMenuBar();
                menubar.add(createMenu("File"));

                display = new JTextPane();
                jspane = new JScrollPane(display);
                jlist = new JScrollPane(list);

                killerIcon = new ImageIcon("killer.gif");
                sadIcon = new ImageIcon("sad.gif");
                smileIcon = new ImageIcon("smiley.gif");

                cnt.add(jspane, BorderLayout.CENTER);
                cnt.add(jlist, BorderLayout.WEST);
                cnt.add(menubar, BorderLayout.NORTH);
                setSize(300,300);
                show();

                k = new KillerThread(clientThreads, runnables, members, Thread.currentThread(), display);
    //          k = new KillerThread(clientThreads, hashtable, members, Thread.currentThread(), display);
				
                runXServer();
            }
            else{
                this.setVisible(true);
                members = new Vector();
                clientThreads = new Vector();
                runnables = new Vector();
                hashtable = new Hashtable();
                k = new KillerThread(clientThreads, runnables, members, Thread.currentThread(), null);
                runSServer();
            }
			

        }

        private JMenu createMenu(String title){
            JMenu menu = new JMenu();
            menu.setText(title);
            
            if (title.equals("File")){
                menu.setMnemonic(KeyEvent.VK_F);
                menu.add(createItem("Save log"));
                menu.add(createItem("Clear log"));
                menu.add(createItem("Exit"));
            }
            return menu;
        }


          private JMenuItem createItem(String title){
            JMenuItem menuItem = new JMenuItem(title);
            if (title.equals("Save log"))
                menuItem.setMnemonic(KeyEvent.VK_S);
            else if (title.equals("Clear log"))
                menuItem.setMnemonic(KeyEvent.VK_C);
            else if (title.equals("Exit"))
                menuItem.setMnemonic(KeyEvent.VK_X);
            menuItem.addActionListener(createActionListener(title));
            return menuItem;
          }

          private ActionListener createActionListener(String s){
            ActionListener a = null;
            
            if (s.equals("Exit")){
              a = new ActionListener(){
                   public void actionPerformed(ActionEvent ae){
                      doExit();
                    }
              };
            }
            else if (s.equals("Save log")){
              a = new ActionListener(){
                   public void actionPerformed(ActionEvent ae){
                      chatUtil.saveLog(display);
                    }
              };
            }
            else if (s.equals("Clear log")){
              a = new ActionListener(){
                   public void actionPerformed(ActionEvent ae){
                        display.setText("");
                    }
              };
            }
            return a;
          }

           

          private void doExit(){
            int intValue = JOptionPane.showConfirmDialog(this, "Do you want to shut down the server chat?", "Shut down", JOptionPane.YES_NO_OPTION);
            if (intValue == 0){
                broadCast("Server is going down...\n");
                System.exit(0);
            }
          }
 
      

	public void runXServer(){
		try{
					seraddress=InetAddress.getLocalHost();
                    server = new ServerSocket(5000,10);
					
					int approve=JOptionPane.showConfirmDialog(this,"Do u want to recover previous Chat");
					
					if(approve==0) //if yes button 
					{
							String finaltext=" Previous Chat of Users \n ******************************\n";
					FileInputStream fis=new FileInputStream("logfile.txt");
					BufferedReader br=new BufferedReader(new InputStreamReader(fis));
					
					String line=br.readLine();
					while(line!=null)
					{
						 finaltext+=line + "\r\n";
						 line=br.readLine();
					}
					finaltext+="\r\n ***********************************\n";
					br.close();
					fis.close();

					appendDisplay(display,finaltext + "\n");	
		}
                    appendDisplay(display, "Server up and running\nServer IP : "+seraddress.getHostAddress()+"\nServer  created on port number 5000.\n");
                    //insertImage("killer");

                  // insertImage("ims");
                }
		catch (IOException ioe){
			System.exit(1);
		}
        	while (listen){
                    try{
                        client = server.accept();
                        ClientThread r = new ClientThread(client, display, this);
                        Thread t = new Thread(r);
                        t.start();
                        clientThreads.add(t);
                        hashtable.put("loginID", r);
                        runnables.add(r);
                    }
                    catch (IOException ioe){
                        System.out.println("Exception caught: " + ioe.getLocalizedMessage());
                    }

		}
	}

	public void runSServer(){
		try{
                    server = new ServerSocket(5000, 10);
                    System.out.println("server>: server created at port 5000...");
                }
		catch (IOException ioe){
                    System.err.println("silent>: error creating serversocket...\n" + ioe.getLocalizedMessage());
                    System.exit(1);
		}
        	while (listen){
                    try{
                        client = server.accept();
                        ClientThread r = new ClientThread(client, display, this);
                        Thread t = new Thread(r);
                        t.start();
                        clientThreads.add(t);
                        hashtable.put("loginID", r);
                        runnables.add(r);
                    }
                    catch (IOException ioe){
                        System.out.println("silent>: exception caught during server listen loop...\n" + ioe.getLocalizedMessage());
                    }

		}
	}

	public static void main(String[] args){
            if (args.length != 0){
                if (args[0].equalsIgnoreCase("-x")){
                    Server s = new Server("x");
                }
                else if (args[0].equalsIgnoreCase("-s")){
                    Server s = new Server("s");
                }
                else{
                    System.out.println("Start the server with -x (Xwindow) or -s (silent) option:\njavac -cp Server.jar com.mclient.Server -x\nor\njavac -cp Server.jar com.mclient.Server -s");
                }
            }
            else{
                System.out.println("Start the server with -x (Xwindow) or -s (silent) option:\njavac -cp Server.jar com.mclient.Server -x\nor\njavac -cp Server.jar com.mclient.Server -s");
            }
	}

        public Vector getMembers(){
          return members;
        }

        public void setMember(String m){
          members.addElement(m);
        }

        public void removeMember(String name){
          members.removeElement(name);
        }


         public void sendButtonPosition(String posX, String posY, String caption, String initiator, String guest){
   		for (int i=0; i<runnables.size(); i++){
                    ClientThread c = (ClientThread)runnables.get(i);
                    if (c.getSocketName().equals(guest))
                        c.send("BUTTONPOSITION2#"+posX+"#"+posY+"#"+caption+"#"+initiator+"#"+guest);
                    if (c.getSocketName().equals(initiator))
                        c.send("BUTTONPOSITION2#"+posX+"#"+posY+"#"+caption+"#"+initiator+"#"+guest);
                }
        }
  
  
 
 public void sendEmoticon(String message, String strFrom)
 {
   		for (int i=0; i<runnables.size(); i++)
		{
	  	  	ClientThread c = (ClientThread)runnables.get(i);
            c.sendEmoticon(message+"#"+strFrom);
         }
      screenIt(strFrom+": "+message);
 }


        public void sendToClientFrom(String message, String toWhom, String fromWho){
            for (int i=0; i<runnables.size(); i++){
                ClientThread c = (ClientThread)runnables.get(i);
                if (c.getSocketName().equals(toWhom))
                    c.send(message+"#"+fromWho+"#"+toWhom);
            }
            screenIt(fromWho +" to "+toWhom+" "+message);
        }


        

  public void sendToClient(String message, String name){
   		for (int i=0; i<runnables.size(); i++){
	  	  	ClientThread c = (ClientThread)runnables.get(i);
          if (c.getSocketName().equals(name))
            c.send(message+"#"+name);
      }
      screenIt(message);
  }

  public void sendRejectClient(String message, String name)
  {
	  System.out.println(message + "--" + name);
  		for (int i=0; i<runnables.size(); i++)
		{
	  	  	ClientThread c = (ClientThread)runnables.get(i);
			  //  c.send(message+"#"+name);
          if (c.getSocketName().equals(name))
            c.send(message+"#"+name);
      }
  }
   

  public void sendNuke(String message, String toWhom){
  		for (int i=0; i<runnables.size(); i++){
	  	  	ClientThread c = (ClientThread)runnables.get(i);
          if (c.getSocketName().equals(toWhom))
            c.send(message+"#"+toWhom);
      }
      screenIt("nuke to "+toWhom);
      insertImage("killer");
  }


private ArrayList<String> getAllUsers()
{
	ArrayList<String> users=new ArrayList<String>();
	Connection con = null;
      java.sql.Statement stmt = null;
      ResultSet rs, rs2 = null;
	  try
	  {
	String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
stmt=con.createStatement();
rs=stmt.executeQuery("Select loginID,flag from buddies");
while(rs.next())
{
		users.add(rs.getString(1) + "," + rs.getInt(2));
}
rs.close();
stmt.close();
con.close();
return(users);
	  }
	  catch(Exception ex)
	  {
		  System.out.println("Error in Getting Users List ");
		  return(null);
	  }
}

private void saveLog(String text)
{
	
	try
	{
	FileOutputStream fos=new FileOutputStream("logfile.txt",true);
	PrintWriter pw=new PrintWriter(fos);
		pw.println(text);
		pw.close();
		fos.close();
	}
	catch(Exception ex)
	{
		 System.out.println("Error " + ex.getMessage());
	}
}
	public void broadCast(String text, ClientThread client)
	{
        Vector vct = null;
		Vector cpy=new Vector();
     	ArrayList<String> all=new ArrayList<String>();
		all=getAllUsers();

		if(text.indexOf("%")>=0)
		{
			String storage=text.split("%")[0] + text.split("%")[1];
			saveLog(storage);
		}
		members.clear();
		
		for(int i=0;i<all.size();i++)
			if(all.get(i).toString().split(",")[1].toCharArray()[0]=='1')
				members.add(all.get(i).toString().split(",")[0]);
		 
			String name="";
			if(text.indexOf("#")>0)
			{
			name=text.split("#")[1];
			}
			for (int i=0; i<runnables.size(); i++)
			{
	  	  	ClientThread c = (ClientThread)runnables.get(i);
			if(name.length()!=0 && c.getSocketName().equals(name))
               members.add(0,c.mydownloads);
			}
			cpy=this.members;

        if (text.startsWith("<<SEND_LIST>>"))
		{
            for (int i=0; i<runnables.size(); i++)
			{
                ClientThread c = (ClientThread)runnables.get(i);
                vct = new Vector(cpy);
                c.sendList(vct);
                if (list != null)
				{
                    list.repaint();
                    list.ensureIndexIsVisible(vct.size());
                }
            }
		
        }
        else{
            boolean isMember = false;
            String blocked = null;
	  	for (int i=0; i<runnables.size(); i++ ) 
		{
                    isMember = false;
                    ClientThread c = (ClientThread)runnables.get(i);
                    for(int j=0; j<client.getBlockedMembers().size(); j++)
						{
                        if (client.getBlockedMembers().get(j).equals(c.getSocketName()) )
						{
                            blocked = c.getSocketName();
                            isMember = true;
                        }
                    }
                    for(int j=0; j<c.getBlockedMembers().size(); j++)
					{
                        if (c.getBlockedMembers().get(j).equals(client.getSocketName()))
						{
                            blocked = c.getSocketName();
                            isMember = true;
                        }
                    }
                   if (blocked != null)
                        screenIt(blocked+" is blocked");
                    if (!isMember)
					{
                        c.send(text);
                    }
                }
        }
  }//end broadCast


   public void showListMessage(){
      JOptionPane.showMessageDialog(this, "You must select a person from the left list", "None selected", JOptionPane.INFORMATION_MESSAGE);
  }


  public void screenIt(String message){
      if (display == null)
          System.out.println("server>: "+message);
      else
          appendDisplay(display, message+"\n");
  }


    public void broadCast(String text){
		System.out.println("Called now for text sending ");
        for (int i=0; i<runnables.size(); i++)
		{
            ClientThread c = (ClientThread)runnables.get(i);
			
	        c.send(text);
        }
        screenIt(text);
    }


    public void insertImage(String description){
        if (display != null){
            StyledDocument doc = (StyledDocument)display.getDocument();
            Style style = doc.addStyle("StyleName", null);
            if (description.equals("killer")){
                StyleConstants.setIcon(style, killerIcon);
            try{
              doc.insertString(doc.getLength(), ">:)\n", style);
            }catch(BadLocationException ble){
              System.out.println("Bad location");
            }
            }
            else if (description.equals("sad")){
                StyleConstants.setIcon(style, sadIcon);
            try{
              doc.insertString(doc.getLength(), ":(\n", style);
            }catch(BadLocationException ble){
              System.out.println("Bad location");
            }
            }
            else if (description.equals("smiley")){
                StyleConstants.setIcon(style, smileIcon);
            try{
              doc.insertString(doc.getLength(), ":)\n", style);
            }catch(BadLocationException ble){
              System.out.println("Bad location");
            }
            }
        }
    }

    public void appendDisplay(JEditorPane display, String text){
        if (display == null){
            System.err.println("server>: " + text);
            return;
        }
        StyledDocument doc = (StyledDocument)display.getDocument();
        Style style = doc.addStyle("StyleName", null);
        try{
            doc.insertString(doc.getLength(), text, style);
            display.setCaretPosition(doc.getLength());
        }
        catch(BadLocationException ble){
            System.out.println("server: bad location error");
        }
        display.setDocument(doc);
    }

}
