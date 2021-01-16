package Task;

import java.io.*;

public class PortReader {
    private File file;
    private FileChecker checker;

    public PortReader(String path) {
        file = new File(path);
        checker = file::isFile;
    }

    public PortReader(String path, FileChecker checker) {
        file = new File(path);
        this.checker = checker;
    }

    public int getPort() throws FileNotFoundException {

        if (checker.checkFile()) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new FileReader(file))) {
                String input;
                while ((input = in.readLine()) != null) {
                    sb.append(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return processingInput(sb.toString());
        } else {
            throw new FileNotFoundException();
        }
    }

    private int processingInput(String input) {
        String s = input.replaceAll("[\\D]", "");
        return Integer.parseInt(s);
    }
}
