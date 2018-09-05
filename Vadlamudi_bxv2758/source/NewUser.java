/*Name          : BHAVANA VADLAMUDI
Student id  : 1001572758*/

 package com.mclient;

import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EtchedBorder;
import java.sql.*;


public class  NewUser extends JDialog {
	private ObjectOutputStream out = null;
        

	JTextField username,screenname;
	private JPasswordField pass,pass1;
    private Container c = null;

	JTextArea messages;
	String name,pwd;
	String userdata="";
	JButton submit;
	JMenuBar mbar;
	JComboBox jcombo;
	int fsize=10;
    public NewUser(Dialog owner, String title, boolean modal) {
      super(owner, title, modal);
      initPanel();
      addListeners();
	  setVisible(true);
	  //JOptionPane.showMessageDialog(this,"Activated");
	  
  }
	
	
  private void initPanel(){
	
	JPanel ptop=new JPanel();
    ptop.setBorder(new EtchedBorder(1));
    c = getContentPane();

	ptop.setLayout(new GridLayout(14,2));
    c.setLayout(new GridLayout());

   	c.add(ptop,BorderLayout.NORTH);

	ptop.add(new JLabel("Please Enter New User Data"),0);
	ptop.add(new JLabel(""),1);
	
	ptop.add(new JLabel("Login Id : "),2);
	ptop.add(username=new JTextField(),3);
	
	ptop.add(new JLabel("Password : "));
	ptop.add(pass=new JPasswordField(10));
   	
	ptop.add(new JLabel("Re-enter Password : "));
	ptop.add(pass1=new JPasswordField(10));
	
	ptop.add(new JLabel("Screen Name : "));
	ptop.add(screenname=new JTextField(10));
   	
    
	 submit= new JButton("submit");

	 JPanel psouth=new JPanel();
        psouth.setLayout(new BorderLayout());
		psouth.add(submit,0);
		ptop.add(psouth);

	
	setSize(300,400);
	} // initPanel
  private void addListeners(){

	username.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){
            	int code = ke.getKeyCode();
              if (code == ke.VK_ENTER){
                submitPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
      });
	  submit.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){submitPressed();}
      });
  
	 submit.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){
            	int code = ke.getKeyCode();
              if (code == ke.VK_ENTER || code == ke.VK_SPACE){
                submitPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
      });
 
	 		  
    }// add listeners

public String retData(){
	return userdata;
}
public String getData(){
	return userdata;
}
public void setData(String udata){
	this.userdata=udata;
}

public void setName(String name)
{
	this.name=name;
}
public void setPwd(String pwd)
{
		this.pwd=pwd;
}
public String getName()
{
	 return(name);
}
public String getPwd()
{
	 return(pwd);
}
public void submitPressed() 
{
try
{		
		//validate the contents here
		if(username.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,"Enter Username");
			username.requestFocus();
			return;				
		}
		if(pass.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,"Enter Password");
			pass.requestFocus();
			return;				
		}
		if(pass1.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,"Enter Password");
			pass1.requestFocus();
			return;				
		}
		if(!pass.getText().equals(pass1.getText()))
		{
			JOptionPane.showMessageDialog(this,"Password Not matched");
			return;				
		}
		if(screenname.getText().trim().equals(""))
		{
			JOptionPane.showMessageDialog(this,"Enter Screen name");
			screenname.requestFocus();
			return;				
		}
		
		System.out.println("Output object " + out);
		Connection con;
		String loginValue = new String(username.getText());
 		String passValue = new String(pass.getPassword());
 		String screen=new String(screenname.getText());
		
		if(out==null)
		{
			userdata=username.getText()+"@"+passValue+"@"+screen;
			setName(loginValue);
			setPwd(passValue);
			setData(userdata);
			JOptionPane.showMessageDialog(this,"Data Collected " + userdata);
		}
		  this.dispose();
		/*String sql="insert into buddies values('" + loginValue + "','" + passValue + "','" + screen + "')";
		JOptionPane.showMessageDialog(this,sql);
System.out.println("Sql " + sql);

         Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		 String execpath = System.getProperty("user.dir");
con = DriverManager.getConnection ("jdbc:ucanaccess://" + execpath + "\\database\\mclient.mdb");
Statement stmt=con.createStatement();
		  


		  //String sql="insert into buddies values('" + loginValue + "','" + passValue + "','" + screen + "')";
		  
		  JOptionPane.showMessageDialog(this,sql);
		  stmt.executeUpdate(sql);
		  JOptionPane.showMessageDialog(this,"New User Created");
		  this.setVisible(false);
          this.dispose();*/
	} //end of try
	catch(Exception ex)
	{
		 JOptionPane.showMessageDialog(this,"Error--> : " + ex.getMessage());
		 return;
	}
	}
}	
