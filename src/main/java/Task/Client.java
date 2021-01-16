package Task;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

public class Client {

    public static final String SETTINGS_FILE_PATH = "setting.txt";

    public Client() throws IOException, InterruptedException {
        PortReader portReader = new PortReader(SETTINGS_FILE_PATH);

        try (Socket socket = new Socket("127.0.0.1", portReader.getPort())) {
            MessageHandler handler = new MessageHandler(socket);
            Thread inputThread = new Thread(handler::read);
            Thread outputThread = new Thread(handler::send);

            inputThread.start();
            outputThread.start();

            inputThread.join();
            outputThread.join();
        } catch (FileNotFoundException e) {
            System.out.println("Порт не может быть прочтен из указанного файла");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Client();
    }
}