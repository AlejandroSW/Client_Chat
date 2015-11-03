package client_chat;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

/**
 *
 * @author Alejandro
 */
public class Client_Setup extends JFrame{
    
    private JTextField userText;
    private JTextArea clientArea;
    private JButton sendButton;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP;
    private Socket connection;
    
    //Constructor
    public Client_Setup(String host){
        super("Client Chat App");
        serverIP = host;
        
        sendButton = new JButton();
        sendButton.setText("Send");
        sendButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                   sendMessage(userText.getText());
                   userText.setText("");
                }
            }
        );
        add(sendButton, BorderLayout.NORTH);
        
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent event){
                   sendMessage(userText.getText());
                   userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.SOUTH);
        
        clientArea = new JTextArea();
        add(new JScrollPane(clientArea), BorderLayout.CENTER);
        setSize(300,400);
        setVisible(true);
    }
    
    //Start comunication with the server
    public void clientStart(){
        try{
            //Connect to the server
            connectToServer();
            //Set textfield editable
            editableTextField(true);
            //Start Chating
            receiveMessage();
        }catch(EOFException eofException){
            clientArea.append("\n Client terminated connection");
        }catch(IOException ioException){
            
        }finally{
            closeConnection();
        }
    }
    
    private void connectToServer() throws IOException{
        clientArea.append("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP),2222);
        clientArea.append("Connected to: " + connection.getInetAddress().getHostName());
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
    }
    
    private void receiveMessage() throws IOException{
        String message = "";
        
        editableTextField(true);
        while(!message.equals("SERVER: END"))
        {
            try{
                message = (String) input.readObject();
                clientArea.append("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                
            }
        }
    }
    
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT: " + message);
            output.flush();
            clientArea.append("\nCLIENT: " + message);
        }catch(IOException ioException){
            
        }
    }
    
    private void closeConnection(){
        clientArea.append("\n Closing connection...");
        editableTextField(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            
        }
    }
    
    private void editableTextField(final boolean tof){
        SwingUtilities.invokeLater(
            new Runnable(){
                @Override
                public void run(){
                    userText.setEditable(tof);
                }
            }
        );
    }
}
