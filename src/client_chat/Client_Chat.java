package client_chat;

/**
 *
 * @author Alejandro
 */

import javax.swing.*;

public class Client_Chat {

    public static void main(String[] args) {
        Client_Setup chat = new Client_Setup("localhost");
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat.clientStart();
    }
}
