package src.main.lobbyserver.client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import src.main.monfight.SinglePlayerRunner;
import src.main.monfight.gui.client.ClientMainFrame;

import java.util.ArrayList;
import java.util.Arrays;


public class LobbyClientGui extends Thread{

  final JTextPane jtextFilDiscu = new JTextPane();
  final JTextField jtextInputChat = new JTextField();
  DefaultListModel<String> userListModel = new DefaultListModel<>();
  JList<String> userList = new JList<>(userListModel);
  JScrollPane scrollPane = new JScrollPane(userList); // Replace JTextPane with JScrollPane
  private String oldMsg = "";
  private Thread read;
  private String serverName;
  private int PORT;
  private String name;
  private String password;
  BufferedReader input;
  PrintWriter output;
  Socket server;
  MouseListener mouseListener = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            int index = userList.locationToIndex(e.getPoint());
            userList.setSelectedIndex(index);
            String selectedUser = (String) userList.getSelectedValue();
            JPopupMenu menu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("Match request");
            menuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
//                	System.out.println("&" + selectedUser.substring(1, selectedUser.length()));
                	jtextInputChat.setText("^" + selectedUser.substring(1, selectedUser.length()));
                	sendMessage();
                }
            });
            menu.add(menuItem);
            menu.show(userList, e.getX(), e.getY());
        }
    }
};

  public LobbyClientGui() {
    this.serverName = "localhost";
    this.PORT = 12345;
    this.name = "nickname";
    this.password = "password";

    String fontfamily = "Arial, sans-serif";
    Font font = new Font(fontfamily, Font.PLAIN, 15);

    final JFrame jfr = new JFrame("Chat");
    jfr.getContentPane().setLayout(null);
    jfr.setSize(700, 500);
    jfr.setResizable(false);
    jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Discussion
    jtextFilDiscu.setBounds(25, 25, 490, 320);
    jtextFilDiscu.setFont(font);
    jtextFilDiscu.setMargin(new Insets(6, 6, 6, 6));
    jtextFilDiscu.setEditable(false);
    JScrollPane jtextFilDiscuSP = new JScrollPane(jtextFilDiscu);
    jtextFilDiscuSP.setBounds(25, 25, 490, 320);

    jtextFilDiscu.setContentType("text/html");
    jtextFilDiscu.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // User list
    scrollPane.setBounds(520, 25, 156, 320);
    scrollPane.setFont(font);
    JScrollPane jsplistuser = new JScrollPane(scrollPane);
    jsplistuser.setBounds(520, 25, 156, 320);

    scrollPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

    // Field message user input
    jtextInputChat.setBounds(0, 350, 400, 50);
    jtextInputChat.setFont(font);
    jtextInputChat.setMargin(new Insets(6, 6, 6, 6));
    final JScrollPane jtextInputChatSP = new JScrollPane(jtextInputChat);
    jtextInputChatSP.setBounds(25, 350, 650, 50);

    // button send
    final JButton jsbtn = new JButton("Send");
    jsbtn.setFont(font);
    jsbtn.setBounds(575, 410, 100, 35);

    // button Disconnect
    final JButton jsbtndeco = new JButton("Disconnect");
    jsbtndeco.setFont(font);
    jsbtndeco.setBounds(25, 410, 130, 35);

    jtextInputChat.addKeyListener(new KeyAdapter() {
      // send message on Enter
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          sendMessage();
        }

        // Get last message typed
        if (e.getKeyCode() == KeyEvent.VK_UP) {
          String currentMessage = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMessage;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          String currentMessage = jtextInputChat.getText().trim();
          jtextInputChat.setText(oldMsg);
          oldMsg = currentMessage;
        }
      }
    });

    // Click on send button
    jsbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        sendMessage();
      }
    });

    // Connection view
    final JTextField jtfName = new JTextField(this.name);
    final JTextField jtfport = new JTextField(Integer.toString(this.PORT));
    final JTextField jtfAddr = new JTextField(this.serverName);
    final JButton jcbtn = new JButton("Connect");

    // check if those field are not empty
    jtfName.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
    jtfport.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));
    jtfAddr.getDocument().addDocumentListener(new TextListener(jtfName, jtfport, jtfAddr, jcbtn));

    // Position modules
    jcbtn.setFont(font);
    jtfAddr.setBounds(25, 380, 135, 40);
    jtfName.setBounds(375, 380, 135, 40);
    jtfport.setBounds(200, 380, 135, 40);
    jcbtn.setBounds(575, 380, 100, 40);

    // Set background
    jtextFilDiscu.setBackground(Color.LIGHT_GRAY);
    scrollPane.setBackground(Color.LIGHT_GRAY);

    // Add elements onto JFrame
    jfr.add(jcbtn);
    jfr.add(jtextFilDiscuSP);
    jfr.add(jsplistuser);
    jfr.add(jtfName);
    jfr.add(jtfport);
    jfr.add(jtfAddr);
    jfr.setVisible(true);


    // Chat information
    appendToPane(jtextFilDiscu, "<h4>Possible commands in the chat:</h4>\r\n"
    		+ "<ul>\r\n"
    		+ "  <li><b>@nickname</b> to send a private message to the user 'nickname'</li>\r\n"
    		+ "  <li><b>^nickname</b> to challenge someone to a match</li>\r\n"
    		+ "</ul><br/>\r\n"
    		+ "");

    // On connect
    jcbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        try {
          name = jtfName.getText();
          String port = jtfport.getText();
          serverName = jtfAddr.getText();
          PORT = Integer.parseInt(port);

          appendToPane(jtextFilDiscu, "<span>Connecting to " + serverName + " on port " + PORT + "...</span>");
          server = new Socket(serverName, PORT);

          appendToPane(jtextFilDiscu, "<span>Connected to " +
              server.getRemoteSocketAddress()+"</span>");

          input = new BufferedReader(new InputStreamReader(server.getInputStream()));
          output = new PrintWriter(server.getOutputStream(), true);

          // send nickname to server
          output.println(name);

          // create new Read Thread
          read = new Read();
          read.start();
          jfr.remove(jtfName);
          jfr.remove(jtfport);
          jfr.remove(jtfAddr);
          jfr.remove(jcbtn);
          jfr.add(jsbtn);
          jfr.add(jtextInputChatSP);
          jfr.add(jsbtndeco);
          jfr.revalidate();
          jfr.repaint();
          jtextFilDiscu.setBackground(Color.WHITE);
          scrollPane.setBackground(Color.WHITE);
        } catch (Exception ex) {
          appendToPane(jtextFilDiscu, "<span>Could not connect to Server</span>");
          JOptionPane.showMessageDialog(jfr, ex.getMessage());
        }
      }

    });

    // on deco
    jsbtndeco.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent ae) {
        jfr.add(jtfName);
        jfr.add(jtfport);
        jfr.add(jtfAddr);
        jfr.add(jcbtn);
        jfr.remove(jsbtn);
        jfr.remove(jtextInputChatSP);
        jfr.remove(jsbtndeco);
        jfr.revalidate();
        jfr.repaint();
        read.interrupt();
        jtextFilDiscu.setBackground(Color.LIGHT_GRAY);
        scrollPane.setBackground(Color.LIGHT_GRAY);
        appendToPane(jtextFilDiscu, "<span>Connection closed.</span>");
        output.close();
      }
    });

  }

  // check if if all field are not empty
  public class TextListener implements DocumentListener{
    JTextField jtf1;
    JTextField jtf2;
    JTextField jtf3;
    JButton jcbtn;

    public TextListener(JTextField jtf1, JTextField jtf2, JTextField jtf3, JButton jcbtn){
      this.jtf1 = jtf1;
      this.jtf2 = jtf2;
      this.jtf3 = jtf3;
      this.jcbtn = jcbtn;
    }

    public void changedUpdate(DocumentEvent e) {}

    public void removeUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") ||
          jtf2.getText().trim().equals("") ||
          jtf3.getText().trim().equals("")
          ){
        jcbtn.setEnabled(false);
      }else{
        jcbtn.setEnabled(true);
      }
    }
    public void insertUpdate(DocumentEvent e) {
      if(jtf1.getText().trim().equals("") ||
          jtf2.getText().trim().equals("") ||
          jtf3.getText().trim().equals("")
          ){
        jcbtn.setEnabled(false);
      }else{
        jcbtn.setEnabled(true);
      }
    }

  }

  // Send messages
  public void sendMessage() {
    try {
      String message = jtextInputChat.getText().trim();
      if (message.equals("")) {
        return;
      }
      this.oldMsg = message;
      output.println(message);
      jtextInputChat.requestFocus();
      jtextInputChat.setText(null);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(null, ex.getMessage());
      System.exit(0);
    }
  }

  public static void main(String[] args) throws Exception {
    LobbyClientGui client = new LobbyClientGui();
  }

  // read new incoming messages
  class Read extends Thread {
    public void run() {
      String message;
      while(!Thread.currentThread().isInterrupted()){
        try {
          message = input.readLine();
          if(message != null){
        	  if (message.charAt(0) == '[') {
        		    message = message.substring(1, message.length()-1);
        		    ArrayList<String> ListUser = new ArrayList<String>(
        		        Arrays.asList(message.split(", "))
        		    );

        		    // Clear the list model and add the new users
        		    userListModel.clear();
        		    for (String user : ListUser) {
        		        userListModel.addElement("@" + user);
                    userList.addMouseListener(mouseListener);
        		    }
    		    
        		}	else if (message.startsWith("IP: ")) {
        			SinglePlayerRunner.loadGame(message.substring(4), 1234);
        		}	
        	  else if (message.startsWith("Match request:")) {
//        			  appendToPane(jtextFilDiscu, message);
	    	    	  String msg = "You have been challenged to a match. Do you accept?";
	    	          Object[] options = {"Accept", "Deny"};
	    	          int n = JOptionPane.showOptionDialog(null,
	    	                  msg,
	    	                  "Match Invitation",
	    	                  JOptionPane.YES_NO_OPTION,
	    	                  JOptionPane.QUESTION_MESSAGE,
	    	                  null,
	    	                  options,
	    	                  options[0]);
	    	          if (n == JOptionPane.YES_OPTION) {
	    	              // User accepted the match
	    	        	  
	    	        	  SinglePlayerRunner.startGame();
	    	        	  
	    	        	  // Send to the user
	    	        	  String[] parts = message.split(" ");
	    	        	  jtextInputChat.setText("&" + parts[2] + " IP:" + InetAddress.getLocalHost().getHostAddress());
	    	        	   sendMessage();
//	    	        	   output.println(message + " " + "IP: " + InetAddress.getLocalHost().getHostAddress());
	    	        	  // System.out.println("MSG: " + parts[2]);
	    	          } else if (n == JOptionPane.NO_OPTION) {
	    	              // User denied the match
	    	        	  output.println("Match denied.");
	    	          }
        		}
        	  else if(message.startsWith("Match accept:")) {
        		  appendToPane(jtextFilDiscu, message);
        		  int ipIndex = message.indexOf(":") + 2;
        		  String ip = message.substring(ipIndex).trim();
        		  new ClientMainFrame(ip, 1234);
        	  }
        	  else {
              System.out.println(message);
              appendToPane(jtextFilDiscu, message);
            }
          }
        }
        catch (IOException ex) {
          System.err.println("Failed to parse incoming message");
        }
      }
    }
  }

  // send html to pane
  private void appendToPane(JTextPane tp, String msg){
    HTMLDocument doc = (HTMLDocument)tp.getDocument();
    HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
    try {
      editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
      tp.setCaretPosition(doc.getLength());
    } catch(Exception e){
      e.printStackTrace();
    }
  }
}