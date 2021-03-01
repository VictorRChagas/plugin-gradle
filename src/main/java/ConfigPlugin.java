import org.gradle.api.Plugin;
import org.gradle.api.Project;

/***
 * That is a standard for creating a custom plugin using gradle,
 * this class is responsible for calling any other method that will perform the actions
 */
public class ConfigPlugin implements Plugin<Project> {

    private static final String ENVORIMENT_PROPERTY = "envoriment";

    @Override
    public void apply(Project project) {
        final ConfigurationPluginExtension configurationPluginExtension = project.getExtensions()
                .create("configuration", ConfigurationPluginExtension.class);

        if (!project.hasProperty(ENVORIMENT_PROPERTY)) {
            throw new IllegalArgumentException(Exceptions.CONFIG_NAME_NOT_INFORMED);
        }

        project.task("config").doLast(task ->
                ConfigurationPluginExtension.configure((String) project.property(ENVORIMENT_PROPERTY)));
    }
}
