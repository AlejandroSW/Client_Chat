package client_chat;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
/**
 *
 * @author Alejandro
 */
public class Client_Chat extends javax.swing.JFrame {

    private Socket connection;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final int port = 2222;
    private final String serverIP = "localhost";
    /*private Socket connection2;
    private ObjectOutputStream output2;
    private InputStreamReader input2;
    private final int port2 = 2223;*/    
    
    //Start comunication with the server
    public void clientStart() {
        
        try {
            //Connect to the server
            userArea.append(" Attempting connection... \n");
            connection = new Socket(InetAddress.getByName(serverIP),port);
            userArea.append(" Connected to: " + connection.getInetAddress().getHostName() + "\n");
            
            //Connect to command server socket
            /*userArea.append(" Attempting command connection... \n");
            connection2 = new Socket(InetAddress.getByName(serverIP),port2);
            userArea.append(" Connected to: " + connection2.getInetAddress().getHostName() + "\n");*/
            
            //Create output and input streams for the messaging socket
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            
            //Create output streams for the command socket
            /*output2 = new ObjectOutputStream(connection2.getOutputStream());
            output2.flush();
            input2 = new InputStreamReader(connection2.getInputStream());*/
            
            //Create anonymous nickname if not entered manually
            if(username.getText().equals("nickname"))
            {
                String anon = "anon";
                Random generator = new Random(); 
                int a = generator.nextInt(999);
                String num = String.valueOf(a);
                anon=anon.concat(num);
                username.setText(anon);
                username.setEditable(false);
            }
            
            //Send initial message for server to resolve nickname
            sendMessage("Welcome");            
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
        public void run() 
        {            
            String message;
            userText.setEditable(true);
            
            try {
                while ((message = (String) input.readObject()) != null)              
                {
                    userArea.append(message + "\n");
                    if (message.equals(" The Server is shutting all connections... "))
                    {
                        userText.setEditable(false);
                        connectButton.setText("Connect");
                        closeConnection();
                    }
                }
            } catch (ClassNotFoundException | IOException ex) {

            }
        }

    }
    
    private void sendMessage(String message) {        
        try{
            message = " " + username.getText() + ": " + message;
            output.writeObject(message);
            output.flush();
        }catch(IOException ioException){
            
        }
    }
    
    /*private void sendCommand(String message) {    
        try{
            output2.writeObject(message);
            output2.flush();
        }catch(IOException ioException){
            
        }
    }*/
    
    private void closeConnection() {        
        sendMessage("is Disconnecting ");
        //sendCommand("Disconnect");
        userArea.append(" Closing connection... \n ");
        username.setText("nickname");
        username.setEditable(true);
        userText.setEditable(false);
        
        try{
            //Close message streams and socket
            output.close();
            input.close();
            connection.close();
            
            //Close command streams and socket
            /*output2.close();
            input2.close();
            connection2.close();*/            
        }catch(IOException ioException){
            
        }
    }
    
    public Client_Chat() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        sendButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        userArea = new javax.swing.JTextArea();
        userText = new javax.swing.JTextField();
        username = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Client Chat System");

        sendButton.setText("Send");
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendButtonActionPerformed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        userArea.setColumns(20);
        userArea.setRows(5);
        jScrollPane1.setViewportView(userArea);

        userText.setEditable(false);
        userText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userTextActionPerformed(evt);
            }
        });

        username.setText("nickname");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(userText))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(sendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(connectButton)
                        .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sendButton)
                    .addComponent(userText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        switch (connectButton.getText()) {
            case "Connect":
                clientStart();
                connectButton.setText("Disconnect");
                break;
            case "Disconnect":
                closeConnection();
                connectButton.setText("Connect");
                break;    
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
        sendMessage(userText.getText());
        //sendCommand("Chat");
        userText.setText("");
    }//GEN-LAST:event_sendButtonActionPerformed

    private void userTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userTextActionPerformed
        sendMessage(userText.getText());
        //sendCommand("Chat");
        userText.setText("");
    }//GEN-LAST:event_userTextActionPerformed

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Client_Chat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client_Chat().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton connectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton sendButton;
    private javax.swing.JTextArea userArea;
    private javax.swing.JTextField userText;
    private javax.swing.JTextField username;
    // End of variables declaration//GEN-END:variables
}
