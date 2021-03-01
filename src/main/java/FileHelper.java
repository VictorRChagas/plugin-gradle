import java.io.*;
import java.util.List;
import java.util.Objects;

/***
 * This class is responsible for performing all the actions related to the file manipulation
 * necessary for writing and erasing the config file.
 */
public class FileHelper {

    /***
     * This method read all the content of the file and return it all together in a single string
     *
     * @return String with all the lines concatanated
     */
    public static String readAllLines(final File file) {
        final StringBuilder stringBuilder = new StringBuilder();
        try {
            final FileReader fileReader = new FileReader(file);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while (Objects.nonNull(line = bufferedReader.readLine())) {
                stringBuilder.append(line).append("\n");
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /***
     * @param pathToFile to clear the file
     * @throws IOException if the file didn't get find the file
     */
    public static void clearContentFile(final String pathToFile) throws IOException {
        new FileOutputStream(pathToFile).close();
    }

    /***
     *
     * @param file that will be written
     * @param lines each string in this list will be written in the file
     * @throws IOException if it's not possible to write in the file
     */
    public static void writeAllLinesInFile(final File file, final List<String> lines) throws IOException {
        final FileWriter fileWriter = new FileWriter(file);
        lines.forEach(line -> {
            try {
                fileWriter.write(line + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.close();
    }
}
