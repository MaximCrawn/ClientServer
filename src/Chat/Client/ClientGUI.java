package Chat.Client;

import Chat.Server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 388;

    private final JTextArea log = new JTextArea();

    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField("");
    private final JPasswordField tfPassword = new JPasswordField("123456");
    private final JButton btnLogin = new JButton("Login");

    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");

    private final ServerWindow server;

    public ClientGUI(ServerWindow server) {
        this.server = server;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle("Chat Client");
        setLayout(new BorderLayout());

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(new JLabel());
        panelTop.add(btnLogin);
        add(panelTop, BorderLayout.NORTH);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog, BorderLayout.CENTER);

        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        add(panelBottom, BorderLayout.SOUTH);

        btnSend.addActionListener(e -> sendMessage());
        tfMessage.addActionListener(e -> sendMessage());

        loadHistoryFromServer();
        new Timer(1000, e -> updateChatDisplay()).start();
        setVisible(true);
    }

    private void sendMessage() {
        String msg = tfMessage.getText().trim();
        if (msg.isEmpty()) return;

        server.sendMessage(tfLogin.getText(), msg);
        tfMessage.setText("");
        updateChatDisplay();
    }

    private void loadHistoryFromServer() {
        for (String line : server.getChatHistory()) {
            log.append(line + "\n");
        }
    }

    private void updateChatDisplay() {
        log.setText("");
        loadHistoryFromServer();
    }
}
