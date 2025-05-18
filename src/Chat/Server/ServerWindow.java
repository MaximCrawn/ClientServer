package Chat.Server;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServerWindow extends JFrame {
    private static final int POS_X = 500;
    private static final int POS_Y = 450;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private static final String LOG_FILE = "chat_history.txt";

    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea log = new JTextArea();
    private boolean isServerWorking = false;

    public ServerWindow() {
        setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        setTitle("Chat Server");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setAlwaysOnTop(true);
        setResizable(false);

        log.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(log);
        add(scrollLog, BorderLayout.CENTER);

        JPanel panelBottom = new JPanel(new GridLayout(1, 2));
        panelBottom.add(btnStart);
        panelBottom.add(btnStop);
        add(panelBottom, BorderLayout.SOUTH);

        btnStart.addActionListener(e -> startServer());
        btnStop.addActionListener(e -> stopServer());

        loadHistory();
        setVisible(true);
    }

    private void startServer() {
        if (!isServerWorking) {
            isServerWorking = true;
            log.append("Server started\n");
        }
    }

    private void stopServer() {
        if (isServerWorking) {
            isServerWorking = false;
            log.append("Server stopped\n");
        }
    }

    public synchronized void sendMessage(String from, String msg) {
        if (!isServerWorking) return;

        String fullMsg = from + ": " + msg;
        log.append(fullMsg + "\n");
        saveToFile(fullMsg);
    }

    private void saveToFile(String msg) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(msg);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHistory() {
        File file = new File(LOG_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<String> getChatHistory() {
        List<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return history;
    }
}
