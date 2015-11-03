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
    private JButton connectButton;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String serverIP = "localhost";
    private int port = 2222;
    private Socket connection;
    
    //Constructor
    public Client_Setup(String host) {
        super("Client Chat App");
        serverIP = host;
        
        connectButton = new JButton();
        connectButton.setText("Connect to Server");
        connectButton.addActionListener(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    switch (connectButton.getText()) {
                        case "Connect to Server":
                            clientStart();
                            connectButton.setText("Disconnect");
                            break;
                        case "Disconnect":
                            closeConnection();
                            connectButton.setText("Connect to Server");
                            break;    
                    }
                }
            }
        );
        add(connectButton, BorderLayout.NORTH);
        
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    //Start comunication with the server
    public void clientStart() {

        //Connect to the server
        clientArea.append(" Attempting connection... \n");
        
        try {
            connection = new Socket(InetAddress.getByName(serverIP),port);
            clientArea.append(" Connected to: " + connection.getInetAddress().getHostName() + "\n");
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            ListenThread();
        } catch (IOException ex) {
            
        }
    }
    
    public void ListenThread() {
         Thread listner = new Thread(new ReceiveMessage());
         listner.start();
    }
    
    private class ReceiveMessage implements Runnable {
        
        @Override
        public void run() {
            String message;

            userText.setEditable(true);
            try {
                while ((message = (String) input.readObject()) != null)              
                {
                    clientArea.append(message + "\n");
                    if (message.equals(" The Server is shutting all connections... "))
                    {
                        userText.setEditable(false);
                        connectButton.setText("Connect to Server");
                        closeConnection();
                    }
                }
            } catch (ClassNotFoundException | IOException ex) {

            }
        }

    }
    
    private void sendMessage(String message) {
        
        try{
            message = " USERNAME: " + message;
            output.writeObject(message);
            output.flush();
        }catch(IOException ioException){
            
        }
    }
    
    private void closeConnection() {
        
        sendMessage("is Disconnecting ");
        clientArea.append(" Closing connection... \n ");
        userText.setEditable(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            
        }
    }
}
