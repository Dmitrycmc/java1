package ru.gb.java2.lesson8.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler {
    private DataInputStream in;
    private DataOutputStream out;
    private Server server;
    private MainWindow mainWindow;
    private String login;

    public ClientHandler(DataInputStream in, DataOutputStream out, Server server, MainWindow mainWindow) {
        this.in = in;
        this.out = out;
        this.server = server;
        this.mainWindow = mainWindow;
    }

    void send(String str) {
        try {
            out.writeUTF(str);
            System.out.println("Отправлено сообщение пользователю " + getLogin() + ": " + str);
        } catch (IOException e) {
            System.out.println("Соединение разорвано");
        }
    }

    public void handleClient() {
        try {
            authClient();
            mainWindow.refreshClients();
            System.out.println("Клиент авторизовался");
            listen();
        } catch (IOException e) {
            System.out.println("Соединение разорвано");
        }
    }

    private void authClient() throws IOException {
        String msg;
        String[] words;
        while (true) {
            msg = in.readUTF();
            System.out.println("Получено сообщение: " + msg);
            words = msg.split("\\s");
            if (Authenticator.auth(words[0], words[1])) {
                login = words[0];
                send("Success");
                return;
            }
            send("Invalid login or password");
        }
    }

    private void listen() throws IOException {
        String msg;
        while (true) {
            msg = in.readUTF();
            System.out.println("Получено сообщение от " + getLogin() + ": " + msg);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String time = dtf.format(now);

            if (msg.startsWith("/w ")) {
                String[] words = msg.split("\\s");
                String[] pureMessageWords = new String[words.length - 2];
                System.arraycopy(words, 2, pureMessageWords, 0, words.length - 2);

                String pureMessage = String.join(" ", pureMessageWords);
                server.privateMessage(getLogin(), words[1], String.format("(%s) личное from %s to %s: %s", time, getLogin(), words[1], pureMessage));
            } else {
                server.broadcast(String.format("(%s) %s: %s", time, getLogin(), msg));
            }
        }
    }

    public String getLogin() {
        return login == null ? "Unauthorized" : login;
    }
}