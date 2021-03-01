import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationPluginExtension {

    /***
     * That's going to be the address of the directory to look for the specific config file
     */
    final static String CONFIG_FILES_PATH = "C:\\Users\\Victor\\IdeaProjects\\plugin\\src\\main\\java\\config";

    /***
     * This file never changes, that's the config file of the project that will be updated
     */
    final static String PROJECT_FILE_CONFIG = "C:\\Users\\Victor\\IdeaProjects\\plugin\\src\\main\\java\\application.properties";

    /***
     * This method is responsible for overwriting the configurations in the apache delta spike configuration
     *
     * @param configFileName it will be necessary for to filter the correct config file in the directory with all
     *                       configuration files.
     */
    public static void configure(final String configFileName) {
        final File[] configFiles = new File(CONFIG_FILES_PATH).listFiles();
        if (Objects.nonNull(configFiles)) {
            Stream.of(configFiles)
                    .filter(file -> file.getName().startsWith(configFileName))
                    .findFirst()
                    .ifPresentOrElse(mergeConfigFiles, throwExceptionForNotFoundConfigFile);
        }
    }

    /***
     * This method basically perform all the necessary operations to update the configuration file
     */
    private static final Consumer<File> mergeConfigFiles = file -> {
        final LinkedHashMap<String, String> keyValueMapToOverwrite = getAllKeyValuesFromConfigFiles(file);
        final LinkedHashMap<String, String> keyValueMapToBeOverwritten = getAllKeyValuesFromConfigFiles(new File(PROJECT_FILE_CONFIG));

        final LinkedHashMap<String, String> mergedHashMap = overwriteValuesInProjectConfigFile(keyValueMapToOverwrite, keyValueMapToBeOverwritten);

        try {
            rewriteProjectFileConfig(new File(PROJECT_FILE_CONFIG), mergedHashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    };

    /***
     * This method basically read the file, read every line and extract the key and value of each line
     *
     * @param file where all the informations will be extracted from
     * @return LinkedHashMap
     */
    private static LinkedHashMap<String, String> getAllKeyValuesFromConfigFiles(final File file) {
        final String allLines = FileHelper.readAllLines(file);
        return extractKeyValueFromString(allLines);
    }

    /***
     * This method throws an exception if no any config file match to the name informed
     */
    private static final Runnable throwExceptionForNotFoundConfigFile = () -> {
        throw new IllegalArgumentException(Exceptions.NOT_FOUND_CONFIG_FILE);
    };

    /***
     * This method will get a string with all the content together and will split it to get all the key and value
     * in the file
     *
     * @param allLines in a single string
     * @return a LinkedHashMap in the order that it was found
     */
    public static LinkedHashMap<String, String> extractKeyValueFromString(final String allLines) {
        final String[] allLinesList = allLines.split("\n");
        final LinkedHashMap<String, String> keyValueHashMap = new LinkedHashMap<>();
        Stream.of(allLinesList)
                .map(line -> line.split("="))
                .filter(filterValidPairs)
                .forEach(strings -> keyValueHashMap.put(strings[0], strings[1]));
        return keyValueHashMap;
    }

    /***
     * This method is basically for prevent any error when the file is empty of if there's no mistake
     * in the values
     */
    private final static Predicate<String[]> filterValidPairs = strings -> strings.length == 2;

    /***
     * This method performs a merge into the two arrays priorizating the config files of this project
     * to overwrite it automatically
     *
     * @param keyValueMapToOverwrite this is the hashmap that have the config file based on the squad
     * @param keyValueMapToBeOverwritten this is the hashmap that have the config file of the project itself
     * @return the merged LinkedHashMap to be written in the project config file
     */
    public static LinkedHashMap<String, String> overwriteValuesInProjectConfigFile(final LinkedHashMap<String, String> keyValueMapToOverwrite,
                                                                                   final LinkedHashMap<String, String> keyValueMapToBeOverwritten) {
        keyValueMapToBeOverwritten.putAll(keyValueMapToOverwrite);
        return keyValueMapToBeOverwritten;
    }

    /***
     * This method writes all the updated content in the project config file
     */
    private static void rewriteProjectFileConfig(final File file,
                                                 final LinkedHashMap<String, String> mergedHashMap) throws IOException {
        final List<String> allLinesList = mergedHashMap.entrySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        FileHelper.clearContentFile(file.getPath());
        FileHelper.writeAllLinesInFile(file, allLinesList);
    }
}
