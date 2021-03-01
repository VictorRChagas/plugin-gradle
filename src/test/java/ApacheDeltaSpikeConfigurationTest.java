import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class ApacheDeltaSpikeConfigurationTest {

    @Test
    public void throwsExceptionIfPassesParamisNull() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ConfigurationPluginExtension.configure(null));
    }

    @Test
    public void extractKeyValueFromStringWillHaveCorrectKeys() {
        final LinkedHashMap<String, String> keyValueFromString = ConfigurationPluginExtension
                .extractKeyValueFromString("name=João \n" + "lastName=Chagas");

        Assertions.assertTrue(keyValueFromString.containsKey("name"));
        Assertions.assertTrue(keyValueFromString.containsKey("lastName"));
    }

    @Test
    public void extractKeyValueFromStringWillHaveCorrectValues() {
        final LinkedHashMap<String, String> keyValueFromString = ConfigurationPluginExtension
                .extractKeyValueFromString("name=João\n" + "lastName=Chagas");

        Assertions.assertEquals(keyValueFromString.get("name"), "João");
        Assertions.assertEquals(keyValueFromString.get("lastName"), "Chagas");
    }

    /***
     * Note that the mapToBeOverwritten variable had the old name Victor and got overwritten by Joao
     * and the other values will continue equals.
     */
    @Test
    public void makeSureTheMapsWillBeMergedAndTheDestinationWillBeOverwritten() {
        final LinkedHashMap<String, String> mapToOverwrite = new LinkedHashMap<>(
                Map.of("name", "Joao", "lastName", "Chagas")
        );
        final LinkedHashMap<String, String> mapToBeOverwritten = new LinkedHashMap<>(
                Map.of("name", "Victor", "lastName", "Chagas")
        );

        final LinkedHashMap<String, String> mergedMap = ConfigurationPluginExtension
                .overwriteValuesInProjectConfigFile(mapToOverwrite, mapToBeOverwritten);

        Assertions.assertEquals(mergedMap.get("name"), "Joao");
        Assertions.assertEquals(mergedMap.get("lastName"), "Chagas");
    }
}
