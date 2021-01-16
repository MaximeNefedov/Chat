package Task;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.FileNotFoundException;

class PortReaderTest {

    @Test
    public void validatePort() {
        PortReader portReader = new PortReader(SettingsInstaller.PATH_NAME);
        int port = 0;
        try {
            port = portReader.getPort();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int validatePort = 8998;
        Assertions.assertEquals(validatePort, port);
    }

    @Test
    public void readingFileException() {
        FileChecker checker = Mockito.mock(FileChecker.class);
        Mockito.when(checker.checkFile())
                .thenReturn(false);
        PortReader portReader = new PortReader(SettingsInstaller.PATH_NAME, checker);
        Assertions.assertThrows(FileNotFoundException.class, portReader::getPort);
    }
}