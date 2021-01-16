package Task;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 8998;
    private final CopyOnWriteArrayList<ClientHandler> connections = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        new Server();
    }

    public Server() throws IOException {
        new SettingsInstaller(PORT);

        try (ServerSocket server = new ServerSocket(PORT)) {
            Logger.getLogger().log("Сервер запущен, доступный порт: " + PORT);
            while (true) {
                Socket socket = server.accept();
                ClientHandler client = new ClientHandler(socket, this);
                connections.add(client);
                System.out.println(connections.size());
            }
        }
    }

    public void sendToAllConnections(String message) {
        for (ClientHandler connection : connections) {
            connection.sendMessage(message);
        }
    }

    public void removeConnection(ClientHandler client) {
        connections.remove(client);
        System.out.println(connections.size());
    }

//    public void closeServer() {
//
//    }
}
