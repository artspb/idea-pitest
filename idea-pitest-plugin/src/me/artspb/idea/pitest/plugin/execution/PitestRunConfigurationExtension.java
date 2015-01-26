package me.artspb.idea.pitest.plugin.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunConfigurationExtension;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import me.artspb.idea.pitest.plugin.configuration.junit.JUnitConfigurationUtils;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import me.artspb.idea.pitest.plugin.ui.PitestSettingsEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class PitestRunConfigurationExtension extends RunConfigurationExtension {

    @Override
    public void updateJavaParameters(RunConfigurationBase runConfiguration, JavaParameters javaParameters, RunnerSettings runnerSettings) throws ExecutionException {
    }

    @Override
    protected void readExternal(@NotNull RunConfigurationBase runConfiguration, @NotNull Element element) throws InvalidDataException {
        if (isApplicableFor(runConfiguration)) {
            PitestConfiguration.getOrCreate(runConfiguration).readExternal(element);
        }
    }

    @Override
    protected void writeExternal(@NotNull RunConfigurationBase runConfiguration, @NotNull Element element) throws WriteExternalException {
        if (isApplicableFor(runConfiguration)) {
            PitestConfiguration.getOrCreate(runConfiguration).writeExternal(element);
        }
    }

    @Nullable
    @Override
    protected String getEditorTitle() {
        return "Pitest Configuration";
    }

    @Override
    protected boolean isApplicableFor(@NotNull RunConfigurationBase runConfiguration) {
        return JUnitConfigurationUtils.supports(runConfiguration);
    }

    @Nullable
    @Override
    public SettingsEditor createEditor(@NotNull RunConfigurationBase runConfiguration) {
        return new PitestSettingsEditor(runConfiguration);
    }
}
