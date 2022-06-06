package ru.netology.chat;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class SocketDataReader {
    public static SocketData getFromFile(File file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(file, SocketData.class);
        } catch (IOException e) {
            String errorMessage = String.format(
                    "Ошибка чтения json файла: \"%s\"",
                    file.getName()
            );
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
