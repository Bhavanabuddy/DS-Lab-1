/*Name        : BHAVANA VADLAMUDI
Student id  : 1001572758*/


package com.mclient;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.awt.Toolkit.*;

  
    public class ClientThread implements Runnable{
        private Socket client = null;
        private JEditorPane display = null;
        private Server server = null;
        private ObjectInputStream in = null;
        private ObjectOutputStream out = null;
        private String clientName = null;
        private int dbOK = 0;
		public static String myname;
        private Vector blockedMembers = null;
		private ArrayList<String> times=null;

		public Object mydownloads=0;
	public ClientThread(Socket client, JEditorPane display, Server server)
	{
		this.client= client;
		this.display = display;
		this.server = server;
		try
		{
			times=new ArrayList<String>();
			in = new ObjectInputStream(client.getInputStream());
			out = new ObjectOutputStream(client.getOutputStream());
		}
		catch (IOException ioe)
		{
		}
	}

	public Vector getBlockedMembers(){
		return blockedMembers;
	}

	public void send(String text)
	{
		try
		{
			if(text.indexOf("%")>=0)
			{
				String []parts=text.split("%");
			times.add(parts[2].trim());
			
			if(times.size()==1)
			{
				text=parts[0] + "(0:00)" + "%" + parts[1];
				out.writeObject(text);
			}
			else
			{
				Long prev=Long.parseLong(times.get(0).substring(1));
				Long next=Long.parseLong(times.get(1).substring(1));
				
				Long diff=next-prev;
				int rel=(int)(diff/1000);
				System.out.println("Time gap is " + rel);
				text=parts[0] + "(0:" + rel + ")%" + parts[1];
				out.writeObject(text);
				
				times.remove(0);
				System.out.println("Now timestamp is " + times.size() + "---" + times.get(0));
			}
			}
				else
			out.writeObject(text + "-- hello \r\n");
		}
		catch (IOException ioe)
		{
		}
	}

  public void sendFileName(String fileName){
    try{
		System.out.println("Object to be sended is " + fileName);
      out.writeObject(fileName);
    }catch(IOException ioe){
    }
  }

  public void sendDownload(String download)
  {
	  try{
		  
      out.writeObject(download);
    }catch(IOException ioe){}
  }
  public void sendEmoticon(String emoticon){
    try{
      out.writeObject(emoticon);
    }catch(IOException ioe){}
  }

  public void sendBytes(byte b[]){
    try{
      out.writeObject(b);
    }catch(IOException ioe){
    }
  }

  public void sendList(Vector list){
    try{
      out.writeObject(list);
    }catch(IOException ioe){
    }
  }
  public void updateLoginFlag(String client)
  {
	  Connection con = null;
      java.sql.Statement stmt = null;
		try
		{
String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
stmt=con.createStatement();
String sql="update buddies set flag=0 where loginid='" + client + "'";
System.out.println("Sql " + sql);
stmt.executeUpdate(sql);
stmt.close();
con.close();
		}
		catch(Exception ex)
		{
			System.out.println("Error in updating the flag " + ex);
		}

  }
  public String getSocketName(){
      return clientName;
  }

  public void setSocketName(String strSocketName){
      clientName = strSocketName;
  }

  public Server getServer(){
      return server;
  }

  public int getPendingDownloads(String user)
  {
	  Connection con = null;
      java.sql.Statement stmt = null;
	  ResultSet rs=null;
	  int pending=0;
		try
		{
String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
stmt=con.createStatement();
String sql="select count(*) from history where flag=0 and loginid='" + user + "'";
System.out.println("Sql " + sql);
rs=stmt.executeQuery(sql);
rs.next();
pending=rs.getInt(1);
	rs.close();
stmt.close();
con.close();


		}
		catch(Exception ex)
		{
			System.out.println("Error in updating the flag " + ex);
		}
		return(pending);
  }
  public String checkLive(String user)
  {
	  
	  Connection con = null;
      java.sql.Statement stmt = null;
	  ResultSet rs=null;
	  String live="";
		try
		{
			String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
stmt=con.createStatement();
int pos=user.indexOf("[");
String newuser=user.substring(0,pos);

String sql="select flag from buddies where loginID='" + newuser + "'";
System.out.println("Sql " + sql);
rs=stmt.executeQuery(sql);
rs.next();
if(rs.getInt(1)==1)
		live="Yes";
	else
		live="No";
	rs.close();
stmt.close();
con.close();
		}
		catch(Exception ex)
		{
			System.out.println("Error in updating the flag ");
			live="NULL";
		}
		return(live);
  } 
	public void run()
	{
		try
		{
			String line = new String(" ");
			String tokens = "";
		    blockedMembers = new Vector();
			StringTokenizer tLine = null;
			String fileFrom = null;
			String fileTo = null;
			byte b[] = null;

        do{

		//  line = (String)in.readObject();
	//  server.broadCast(line+"\n");

        Object objReceived = in.readObject();
        if (objReceived instanceof String)
		{
            line = (String)objReceived;
			System.out.println("Token received " + line);
                        tLine = new StringTokenizer(line,"#");
						System.out.println("Token received " + tLine.toString());
            if (tLine.countTokens() > 1)
			{
                String nt = tLine.nextToken();
				System.out.println("Token received " + nt);
                if(nt.equals("NEWUSER"))
                {
                	nt=tLine.nextToken();
					System.out.println("Next is " + nt);
					clientName=nt.split("@")[0];
					System.out.println(clientName + " sairam");
                	dbOK=insertUser(nt);
					
                	switch(dbOK)
                	{
                		case 1 : 
						server.appendDisplay(display, "Registration Success For  "+clientName+".\n");
						server.sendRejectClient("User Registered#","Success");
						server.appendDisplay(display, "Registration Success For  "+clientName+".\n");
                        break;
                        case 4 : server.sendRejectClient("User Not Registered#","Not Success");
						server.appendDisplay(display, "wrong password for "+clientName+".\n");
                        break;
                	}
                }   
                else if (nt.equals("NAMEOK"))
				{
                    nt = tLine.nextToken();
                    clientName = nt;
                    String clientPassword = tLine.nextToken();
                    dbOK = validateUser(clientName, clientPassword);
					myname=clientName;
                    switch(dbOK){
                      case 0: 
					    server.sendRejectClient("NAMEREJECTED", clientName);
                        server.appendDisplay(display, clientName + " is not registered with US in DB.\n");
                        break;
                      case 1:
                        server.setMember(clientName);
						int downloads=getPendingDownloads(clientName);
						mydownloads=downloads;
                
						server.broadCast(clientName + " joined the Chat\n", this);
                        server.appendDisplay(display, clientName + " connected.\n");
						updateFlag(clientName);
                        server.broadCast("<<SEND_LIST>>#" + clientName, this);
                        server.insertImage("smiley");                        
                        break;
                      case 2:
                        server.sendRejectClient("DBERROR#", clientName);
                        break;
                      case 3:
                        server.sendRejectClient("DBERROR#", clientName);
                        break;
                      case 4:
                        server.sendRejectClient("WRONGPASSWORD#", clientName);
                        server.appendDisplay(display, "wrong password for "+clientName+".\n");
                        break;
                      default:
                        break;
                    }
                }
				else if(nt.equals("GETFILE"))
				{
					nt=tLine.nextToken();
					String client=nt;
					nt=tLine.nextToken();
					String fname=nt;
				}
                else if (nt.equals("DISCONNECTED")){
                    nt = tLine.nextToken();
                    server.removeMember(nt);
                    server.insertImage("sad");              
					updateLoginFlag(nt);
                    server.broadCast("<<SEND_LIST>>", null);
                }
               
                else if (nt.equals("BUTTONPOSITION")){
                  String posX = tLine.nextToken();
                  String posY = tLine.nextToken();
                  String caption = tLine.nextToken();
                  String init = tLine.nextToken();
                  String guest = tLine.nextToken();
                  server.sendButtonPosition(posX, posY, caption, init, guest);
                }
                else if(nt.equals("SMILE")){
                  String fromWho = tLine.nextToken();
                  server.sendEmoticon("SMILE2", fromWho);
                }
			 
                else if (nt.equals("KILLER")){
                  String fromWho = tLine.nextToken();
                  server.sendEmoticon("KILLER2", fromWho);
                }
                else if (nt.equals("SAD")){
                  String fromWho = tLine.nextToken();
                  server.sendEmoticon("SAD2", fromWho);                
                }
            }
        }

      server.broadCast(line + "\n", this);
      server.screenIt(line);
			}
			while (!line.equals("<<END>>"));
		}
		catch (IOException ioe){
        server.appendDisplay(display, clientName + " disconnected.\n");
     server.removeMember(clientName);

      server.broadCast("<<SEND_LIST>>", this);
		}
		catch (ClassNotFoundException cnfe){
    }
	}

	void updateFlag(String uname)
	{
		Connection con = null;
      java.sql.Statement stmt = null;
		try
		{
			String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
stmt=con.createStatement();
stmt.executeUpdate("update buddies set flag=1 where loginID='" + uname + "'");
stmt.close();
con.close();
	
		}
		catch(Exception ex)
		{
			System.out.println("Error in updating the flag ");
		}
	}
void getAllUsers()
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
rs=stmt.executeQuery("Select loginID from buddies");
while(rs.next())
{
		users.add(rs.getString(1));
}
rs.close();
stmt.close();
con.close();
	  }
	  catch(Exception ex)
	  {
		  System.out.println("Error in Getting Users List ");
	  }
}
	int insertUser(String data) //for new user
	{
	  int retValue = 0;
	  String []fields=data.split("@");
      Connection con = null;
      java.sql.Statement stmt = null;
      ResultSet rs, rs2 = null;
      String url = null;
        try
        {
      //    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
	 //con = DriverManager.getConnection ("jdbc:ucanaccess://d:\\mclient.mdb");			
          stmt = con.createStatement();
		  System.out.println("Data is " + data);
		  String sql="insert into buddies values('" + fields[0] + "','" + fields[1] + "','" + fields[2] + "',0)";
		  System.out.println(sql);
          //int rows=stmt.executeUpdate("insert into buddies(loginid,password,screename)values('" + fields[0] + "','" + fields[1] + "','"+ fields[2]+"'");
		  int rows=stmt.executeUpdate(sql);
//		  System.out.println("Return value " + rows);
          	if(rows>0)
          		{
                       retValue = 1;
                       stmt.close();
               ///        return retValue;
                   }
                   else
                   {
                    retValue = 4;
             //       return retValue;
                   }
                }
                catch(Exception sqle)
                {
                    //wrong password entered
                    retValue = 4;
           //         return retValue;
                }
     return retValue;
	}
  int validateUser(String username, String password){
      int retValue = 0;
      Connection con = null;
      java.sql.Statement stmt, stmt2 = null;
      ResultSet rs, rs2 = null;
      String url = null;
        try{
         
Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");			
System.out.println("Driver loaded ");
	 String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
        }
		catch(SQLException sqle){
          server.appendDisplay(display, "Unable to connect to Database...\n" + sqle);
          sqle.printStackTrace();
          retValue = 2;
          return retValue;
        }
        catch(Exception cnfe){
          server.appendDisplay(display, "Failed to load JDBC/ODBC driver....\n" + cnfe);
          retValue = 3;
          return retValue;
        }
        

      try{
          stmt = con.createStatement();
          rs = stmt.executeQuery("SELECT * FROM buddies WHERE loginID='"+username+"'");
          try{
            if(rs.next()){			//login existent, check password
                stmt2 = con.createStatement();
                rs2 = stmt.executeQuery("SELECT * FROM buddies WHERE loginID='"+username+"' AND password='"+password+"'");
                try{
                   if(rs2.next()){		//user valid
                       retValue = 1;
                       stmt2.close();
                       return retValue;
                   }
                   else{
                    retValue = 4;
                    stmt2.close();
                    return retValue;
                   }
                }
                catch(SQLException sqle){
                    //wrong password entered
                    retValue = 4;
                    stmt2.close();
                    return retValue;
                }
            }
            else{
             //System.out.println("server: wrong login...\n");
             stmt.close();
             retValue = 0;
             return 0;
            }
          }
          catch(SQLException sqle){
             //System.out.println("server: wrong login...\n");
             stmt.close();
             retValue = 0;
             return 0;
          }


      }
      catch(SQLException sqle){
          server.appendDisplay(display, "server: error creating statement...\n");
          sqle.printStackTrace();
      }
      try{
          con.close();
      }
      catch(SQLException sqle){
          server.appendDisplay(display, "server: database connection cannot be closed...\n");
          sqle.printStackTrace();
      }


     return retValue;

  }


    public String getDBname(){
        String sn = null;
        Properties prop = new Properties();
        try{
            File f = new File("server.properties");
            FileInputStream fin = new FileInputStream(f);
            prop.load(fin);
            sn = prop.getProperty("thinclient");
        }
        catch(IOException ioe){
            System.err.println("clientthread>: server.properties file not found...\n" + ioe.getLocalizedMessage());
            return null;
        }
        if (sn == null){
            System.err.println("clientthread>: server.properties file not found or file is empty...\n");
        }
        return sn;
    }

}
