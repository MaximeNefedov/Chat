package ru.netology.chat.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netology.chat.UsernameValidator;

import java.io.*;
import java.net.Socket;

public class Connection implements Runnable {
    private static final String CONNECTION_CLOSED_MESSAGE = "Соединение с сервером закрыто";
    private static final String CONNECTION_CLOSED_MARKER = "end";
    private final Logger logger = LoggerFactory.getLogger(Connection.class);
    private final Server server;
    private final Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private final int id;
    private String username;

    public Connection(Server server, Socket socket, int id) {
        this.server = server;
        this.socket = socket;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            String receivedUsername = reader.readLine();
            if (!isUsernameValid(receivedUsername)) {
                logger.info("Пользователю с невалидным именем \"{}\" отказано в соединении", receivedUsername);
                writer.println(String.format(
                        "Невалидное имя: \"%s\". В соединении отказано", receivedUsername)
                );
            } else {
                username = receivedUsername;
                writer.println("Имя валидно!");
                server.sendToAllConnections(String.format(
                        "%s подключился. Участников чата: %d",
                        username, server.getConnectionsCounter()
                        )
                );
                String message;
                while ((message = reader.readLine()) != null) {
                    if (message.length() == 0) {
                        continue;
                    }
                    if (message.equals(CONNECTION_CLOSED_MARKER)) {
                        writer.println(CONNECTION_CLOSED_MESSAGE);
                        break;
                    }
                    String userMessage = username + ": " + message;
                    server.sendToAllConnections(userMessage);
                    logger.info(userMessage);
                }
            }
        } catch (IOException e) {
            logger.error("Ошибка соединения. Описание: {}", e.getMessage());
        } finally {
            close();
        }
    }

    private boolean isUsernameValid(String username) {
        return UsernameValidator.isValid(username);
    }

    public void close() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            server.removeConnection(id);
            if (username != null) {
                String message = String.format(
                        "%s отключился,  Участников чата: %d",
                        username, server.getConnectionsCounter()
                );
                server.sendToAllConnections(message);
                logger.info(message);
            }
            logger.info("Соединение: {} закрыто", socket);
        }
    }

    public void receiveMessage(String message) {
        writer.println(message);
    }
}
