package Task;

import java.io.*;
import java.net.Socket;

public class ClientHandler {
    private final Socket socket;
    private final Server server;
    private final Thread thread;
    private final BufferedWriter out;
    private final BufferedReader in;
    private static int clientsCount;
    private String name;

    public ClientHandler(Socket socket, Server server) throws IOException {
        clientsCount++;
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!thread.isInterrupted()) {
                    try {
                        name = in.readLine();

                        server.sendToAllConnections(String.format("%s подключился %s, %d, клиентов в чате: %d\n",
                                name, socket.getInetAddress(), socket.getPort(), clientsCount));

                        Logger.getLogger().log(String.format("%s подключился %s, %d, клиентов в чате: %d\n",
                                name, socket.getInetAddress(), socket.getPort(), clientsCount));

                        String message;

                        while ((message = in.readLine()) != null) {
                            if (message.equals("end")) {
                                ClientHandler.this.sendMessage(name + " отключился");
                                break;
                            }
                            server.sendToAllConnections(name + ": " + message);
                            Logger.getLogger().log(name + " написал: " + message);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        disconnect();
                    }
                }
            }
        });
        thread.start();
    }

    public synchronized void sendMessage(String message) {
        try {
            out.write(message + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
            disconnect();
        }
    }

    private synchronized void disconnect() {
        thread.interrupt();
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientsCount--;
        server.removeConnection(this);
        server.sendToAllConnections(name + ": отключился");
        Logger.getLogger().log(name + ": отключился");
    }

    @Override
    public String toString() {
        return name + " : " + socket.getPort() + " : " + socket.getInetAddress();
    }
}
