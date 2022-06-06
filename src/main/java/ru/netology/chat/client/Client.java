package ru.netology.chat.client;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netology.chat.SocketData;
import ru.netology.chat.SocketDataReader;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String CONNECTION_CLOSED_MESSAGE_FROM_SERVER = "Соединение с сервером закрыто";
    public static final String SETTINGS_FILE_PATH = "settings.json";
    private final Logger logger = LoggerFactory.getLogger(Client.class);
    private volatile boolean connectionStatus;

    public void run() {
        File settingsFile = new File(SETTINGS_FILE_PATH);
        SocketData socketData = SocketDataReader.getFromFile(settingsFile);
        String address = socketData.getAddress();
        int port = socketData.getPort();
        logger.info("Подключение к {}:{} ...", address, port);
        try (final var socket = new Socket(address, port);
             final var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final var writer = new PrintWriter((new OutputStreamWriter(socket.getOutputStream())), true);
             final var scanner = new Scanner(System.in)) {
            logger.info("Подключено к {}:{}", address, port);
            connectionStatus = true;
            logger.info("Добро пожаловать в чат! Введите свое имя");
            String username = scanner.nextLine();
            writer.println(username);
            new Thread(() -> {
                String messageFromServer;
                try {
                    String usernameProcessingResponse = reader.readLine();
                    if (!usernameProcessingResponse.startsWith("Невалидное имя")) {
                        while ((messageFromServer = reader.readLine()) != null) {
                            if (messageFromServer.equals(CONNECTION_CLOSED_MESSAGE_FROM_SERVER)) {
                                logger.info("Нажмите enter, чтобы выйти");
                                break;
                            }
                            logger.info(messageFromServer);
                        }
                    } else {
                        logger.info("{}. Нажмите enter, чтобы выйти", usernameProcessingResponse);
                    }
                } catch (IOException e) {
                    logger.error("Соединение с сервером разорвано");
                } finally {
                    connectionStatus = false;
                }
            }).start();
            while (connectionStatus) {
                String message = scanner.nextLine();
                if (connectionStatus) {
                    writer.println(message);
                }
            }
            logger.info("Вы вышли из чата");
        } catch (IOException e) {
            logger.error(
                    "Приложение клиента будет закрыто, информация об ошибке: {}",
                    e.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        new Client().run();
    }
}