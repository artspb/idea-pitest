package me.artspb.idea.pitest.plugin.runner;

import com.intellij.coverage.CoverageRunnerData;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.impl.DefaultJavaProgramRunner;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import me.artspb.idea.pitest.plugin.configuration.junit.JUnitConfigurationUtils;
import me.artspb.idea.pitest.plugin.execution.PitestExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class PitestProgramRunner extends DefaultJavaProgramRunner {

    public static final String RUNNER_ID = "PitestRunner";

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile runProfile) {
        return executorId.equals(PitestExecutor.EXECUTOR_ID) && runProfile instanceof RunConfigurationBase
                && JUnitConfigurationUtils.supports(runProfile);
    }

    @NotNull
    @Override
    public String getRunnerId() {
        return RUNNER_ID;
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        JavaCommandLineState javaCommandLineState = new PitestCommandLineState(environment);
        TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(environment.getProject());
        javaCommandLineState.setConsoleBuilder(builder);
        return super.doExecute(javaCommandLineState, environment);
    }

    @Nullable
    @Override
    public RunnerSettings createConfigurationData(ConfigurationInfoProvider settingsProvider) {
        return new CoverageRunnerData();
    }
}
