package me.artspb.idea.pitest.plugin.runner;

import com.intellij.coverage.CoverageDataManager;
import com.intellij.coverage.CoverageRunner;
import com.intellij.coverage.CoverageSuite;
import com.intellij.execution.DefaultExecutionResult;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.configurations.coverage.CoverageEnabledConfiguration;
import com.intellij.execution.process.ProcessAdapter;
import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.util.JavaParametersUtil;
import com.intellij.ide.browsers.OpenUrlHyperlinkInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import me.artspb.idea.pitest.plugin.coverage.PitestCoverageRunner;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
class PitestCommandLineState extends JavaCommandLineState {

    private final ExecutionEnvironment environment;
    private final RunConfigurationModule module;
    private final PitestConfiguration configuration;

    public PitestCommandLineState(ExecutionEnvironment environment) {
        super(environment);
        this.environment = environment;
        ModuleBasedConfiguration moduleBasedConfiguration = ((ModuleBasedConfiguration) environment.getRunProfile());
        module = moduleBasedConfiguration.getConfigurationModule();
        configuration = PitestConfiguration.getOrCreate(moduleBasedConfiguration);
    }

    @Override
    protected JavaParameters createJavaParameters() throws ExecutionException {
        JavaParameters javaParameters = new JavaParameters();
        JavaParametersUtil.configureModule(module, javaParameters, JavaParameters.JDK_AND_CLASSES_AND_TESTS, null);
        JavaParametersConfigurator.configureJavaParameters(javaParameters, configuration);
        return javaParameters;
    }

    @NotNull
    @Override
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        ProcessHandler processHandler = startProcess();
        ConsoleView console = createConsole(executor); // TODO replace with junit-like output view
        console.attachToProcess(processHandler);
        String reportDir = JavaParametersConfigurator.getReportDir(getJavaParameters());
        prepareCoverageRunner(environment, processHandler, reportDir);
        processHandler.addProcessListener(new ConsolePrinterListener(console, reportDir));
        return new DefaultExecutionResult(console, processHandler, createActions(console, processHandler, executor));
    }

    private void prepareCoverageRunner(ExecutionEnvironment environment, ProcessHandler processHandler, String reportDir) {
        Project project = environment.getProject();
        RunConfigurationBase runConfiguration = (RunConfigurationBase) environment.getRunProfile();
        attachToProcess(project, processHandler, runConfiguration);
        configureSuite(runConfiguration, project, reportDir);
    }

    private void attachToProcess(Project project, ProcessHandler processHandler, RunConfigurationBase runConfiguration) {
        CoverageDataManager coverageDataManager = CoverageDataManager.getInstance(project);
        coverageDataManager.attachToProcess(processHandler, runConfiguration, getRunnerSettings());
    }

    private void configureSuite(RunConfigurationBase runConfiguration, Project project, String reportDir) {
        CoverageEnabledConfiguration configuration = CoverageEnabledConfiguration.getOrCreate(runConfiguration);
        CoverageRunner currentCoverageRunner = configuration.getCoverageRunner();
        configuration.setCoverageRunner(CoverageRunner.getInstance(PitestCoverageRunner.class));
        CoverageDataManager manager = CoverageDataManager.getInstance(project);
        CoverageSuite suite = manager.addCoverageSuite(configuration);
        configuration.setCurrentCoverageSuite(suite);
        configuration.setCoverageRunner(currentCoverageRunner);
        if (reportDir != null) {
            createSessionDataFile(configuration, reportDir);
        }
    }

    private void createSessionDataFile(CoverageEnabledConfiguration configuration, String reportDir) {
        String sessionDataFilePath = configuration.getCoverageFilePath();
        File sessionDataFile = new File(sessionDataFilePath);
        try {
            FileUtil.writeToFile(sessionDataFile, reportDir);
        } catch (IOException e) {
            FileUtil.delete(sessionDataFile);
        }
    }

    private static final class ConsolePrinterListener extends ProcessAdapter {

        private final ConsoleView console;
        private final String reportDir;

        public ConsolePrinterListener(ConsoleView console, String reportDir) {
            this.console = console;
            this.reportDir = reportDir;
        }

        @Override
        public void processWillTerminate(ProcessEvent event, boolean willBeDestroyed) {
            console.print("You can find report ", ConsoleViewContentType.NORMAL_OUTPUT);
            console.printHyperlink("in the output directory", new OpenUrlHyperlinkInfo(reportDir));
            console.print(" or directly open it in your ", ConsoleViewContentType.NORMAL_OUTPUT);
            console.printHyperlink("default web browser", new OpenUrlHyperlinkInfo(reportDir + "/index.html"));
        }
    }
}
