import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class FileHelperTest {

    private final String RANDOM_TEXT_FOR_TESTING = "Write anything in here for testing!";

    @Test
    public void makeSureFileGotEmpty() throws IOException {
        final File tempFile = createTempFile();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile));
        this.writeInFile(tempFile);

        FileHelper.clearContentFile(tempFile.getPath());

        Assertions.assertNull(bufferedReader.readLine());
    }

    @Test
    public void makeSureReadAllLinesMethodReturnsCorrectly() throws IOException {
        final File tempFile = createTempFile();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile));
        this.writeInFile(tempFile);

        Assertions.assertEquals(bufferedReader.readLine(), RANDOM_TEXT_FOR_TESTING);
    }

    @Test
    public void makeSureWriteAllLinesInFileIsWorkingProperly() throws IOException {
        final List<String> linesToBeWritten = List.of(RANDOM_TEXT_FOR_TESTING, RANDOM_TEXT_FOR_TESTING, RANDOM_TEXT_FOR_TESTING);
        final File tempFile = createTempFile();
        final BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile));

        FileHelper.writeAllLinesInFile(tempFile, linesToBeWritten);

        int countLines = 0;
        while (Objects.nonNull(bufferedReader.readLine())) {
            countLines++;
        }

        Assertions.assertEquals(3, countLines);
    }

    private File createTempFile() throws IOException {
        return File.createTempFile("file", "txt");
    }

    private void writeInFile(final File tempFile) throws IOException {
        final FileWriter fileWriter = new FileWriter(tempFile);
        fileWriter.write(RANDOM_TEXT_FOR_TESTING);
        fileWriter.close();
    }
}
