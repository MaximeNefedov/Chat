package ru.netology.chat.server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.netology.chat.SocketData;
import ru.netology.chat.SocketDataReader;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    public static final String SETTINGS_FILE_PATH = "settings.json";
    private final Logger logger = LoggerFactory.getLogger(Server.class);
    private final ExecutorService service;
    private final Map<Integer, Connection> connectionMap;
    private final AtomicInteger connectionsCounter;
    private int connectionId;

    public Server() {
        this.service = Executors.newCachedThreadPool();
        this.connectionMap = new ConcurrentHashMap<>();
        this.connectionsCounter = new AtomicInteger();
    }

    public static void main(String[] args) {
        new Server().run();
    }

    public void run() {
        File file = new File(SETTINGS_FILE_PATH);
        SocketData socketData = SocketDataReader.getFromFile(file);
        int portValue = socketData.getPort();
        try (ServerSocket server = new ServerSocket(portValue)) {
            logger.info("Сервер запущен, прослушиваемый порт: {}", portValue);
            while (true) {
                Socket socket = server.accept();
                logger.info("Новое соединение: {}", socket);
                int currentConnectionId = ++connectionId;
                Connection connection = new Connection(this, socket, currentConnectionId);
                connectionsCounter.incrementAndGet();
                connectionMap.put(connectionId, connection);
                service.submit(connection);
                logger.info("Обработано соединение с: {}", socket);
            }
        } catch (IOException e) {
            logger.error("Сервер будет закрыт из-за ошибки: {}", e.getMessage());
        } finally {
            connectionMap.forEach((i, c) -> System.out.println(i + " " + c));
        }
    }

    public void sendToAllConnections(String message) {
        connectionMap.forEach((id, connection) -> connection.receiveMessage(message));
    }

    public void removeConnection(int connectionId) {
        Connection removedConnection = connectionMap.remove(connectionId);
        if (removedConnection != null) {
            connectionsCounter.decrementAndGet();
        }
    }

    public int getConnectionsCounter() {
        return connectionsCounter.get();
    }
}
