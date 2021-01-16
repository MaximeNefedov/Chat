package Task;

import tests.Test69.Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Logger {
    private static Logger instance = null;
    private static File file;
    private static int counter = 1;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private static final Lock lock = new ReentrantLock();

    private Logger() {
        file = new File(Server.LOG_PATH);
    }

    public static Logger getLogger() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String msg) {
        try {
            lock.lock();
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file, true))){
                Date date = new Date();
                StringBuilder sb = new StringBuilder("[" + simpleDateFormat.format(date) + " " + counter++ + "] ");
                sb.append(msg);
                System.out.println(sb.toString());
                out.write(sb.toString() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }
}
