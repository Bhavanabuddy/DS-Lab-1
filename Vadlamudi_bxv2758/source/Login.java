/*Name          : BHAVANA VADLAMUDI
Student id  : 1001572758*/

package com.mclient;
import java.io.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.EtchedBorder;
  


public class Login extends JDialog {

  private JPanel panel = null;
  private Container c = null;
  private JLabel jLogin = null;
  private JLabel jPassword = null;
  private JTextField jtLogin = null;
  private JPasswordField jtPassword = null;
   
   

  public NewUser newuser=null;

  private JButton bOK = null;
  private JButton bCANCEL = null;
  private JButton bnewID = null;
  private String strLogin = null;
  private String userdata = null;
  private String strPassword = null;
  private boolean newUser = false;

  public Login(Dialog owner, String title, boolean modal) {
      super(owner, title, modal);
      initPanel();
      addListeners();
  }

  private void initPanel(){
      panel = new JPanel();
      panel.setBorder(new EtchedBorder(1));
      c = getContentPane();
      jLogin = new JLabel("User Name:");
      jPassword = new JLabel("   Password:   ");
      jtLogin = new JTextField(10);
      jtPassword = new JPasswordField(10);
	  bnewID=new JButton(" Register Here. ");
      bOK = new JButton("  OK  ");
      bCANCEL = new JButton("Cancel");
      c.setLayout(new GridLayout());

      c.add(panel);

	  panel.add(jLogin, 0);	 
      jLogin.setLocation(10,10);
      
	  panel.add(jtLogin, 1);
      jtLogin.setLocation(100,10);
      
	  panel.add(jPassword, 2);
      jPassword.setLocation(10,40);
      
	  panel.add(jtPassword, 3);
      panel.add(bOK, 4);
      panel.add(bCANCEL, 5);
       panel.add(bnewID,6);
  }

  private void addListeners(){

	  bnewID.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){newUserPressed();}
      });


      bCANCEL.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){System.exit(0);}
      });

      bOK.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent ae){okPressed();}
      });
		
	  
	  jtLogin.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){
            	int code = ke.getKeyCode();
              if (code == ke.VK_ENTER){
                okPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
      });

      jtPassword.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){
            	int code = ke.getKeyCode();
              if (code == ke.VK_ENTER){
                okPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
      });

      bOK.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){
            	int code = ke.getKeyCode();
              if (code == ke.VK_ENTER || code == ke.VK_SPACE){
                okPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
      });

      bCANCEL.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke){
            	int code = ke.getKeyCode();
              if (code == ke.VK_CANCEL || code == ke.VK_SPACE){
                System.exit(1);
              }
              else if (code == ke.VK_ENTER || code == ke.VK_SPACE){
                okPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
      });

		bnewID.addKeyListener(new KeyListener(){
          public void keyTyped(KeyEvent ke){}
          public void keyReleased(KeyEvent ke)
		  {
            	int code = ke.getKeyCode();
				
              if (code == ke.VK_ENTER || code == ke.VK_SPACE){
                newUserPressed();
              }
          }
          public void keyPressed(KeyEvent ke){}
       });
       
	 bnewID.addMouseListener(new MouseListener(){
          public void mouseReleased(MouseEvent me){
           newUserPressed();
          }
          public void mousePressed(MouseEvent me){}
          public void mouseExited(MouseEvent me){}
          public void mouseEntered(MouseEvent me){}
          public void mouseClicked(MouseEvent me){}
      });
	

 }
	public void newUserPressed()
	{
		String temp="newuser";
		this.setVisible(false);
		setNewUser();
	}
		     

  public void okPressed(){
    String loginValue = new String(jtLogin.getText());
    String passValue = new String(jtPassword.getPassword());
    if (loginValue == null || loginValue.equals("") || passValue == null || passValue.equals("")){}
    else{
      setLogin(loginValue);
      setPassword(passValue);
      this.setVisible(false);
        }
    }

  public void setNewUser(){
		this.newUser = true;
	}
  public void setLogin(String strLogin){
    this.strLogin = strLogin;
    }
  public void setPassword(String strPass){
    this.strPassword = strPass;
    }
	public boolean getNewUser(){ return newUser; }
  public String getLogin(){return strLogin;}
  public String getPassword(){return strPassword;}
  
}