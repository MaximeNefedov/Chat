package Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MessageHandler {
    private volatile boolean connectionStatus = true;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner scanner;
    private static final int DELAY = 100;

    public MessageHandler(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.scanner = new Scanner(System.in);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void send() {
        System.out.println("Введите имя:");
        out.println(scanner.nextLine());

        while (connectionStatus) {
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Введите сообщение");
            String output = scanner.nextLine();
            if (output.equals("end")) {
                out.println(output);
                connectionStatus = false;
            } else {
                out.println(output);
            }
        }
    }

    public void read() {
        String s;
        while (connectionStatus) {
            try {
                if ((s = in.readLine()) != null) {
                    System.out.println(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
