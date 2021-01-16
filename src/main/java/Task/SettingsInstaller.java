package Task;

import java.io.*;

public class SettingsInstaller {
    public static final String PATH_NAME = "setting.txt";
    private static final File FILE_PATH = new File(PATH_NAME);

    public SettingsInstaller(int port) {
        try (PrintWriter out = new PrintWriter(FILE_PATH)) {
            out.print("Available port is: " + port);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
