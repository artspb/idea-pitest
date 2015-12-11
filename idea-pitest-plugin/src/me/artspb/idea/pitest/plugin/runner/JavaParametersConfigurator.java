package me.artspb.idea.pitest.plugin.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.util.PathUtil;
import com.intellij.util.PathsList;
import me.artspb.idea.pitest.plugin.Constants;
import me.artspb.idea.pitest.plugin.Utils;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import org.apache.commons.lang.StringUtils;
import org.pitest.boot.HotSwapAgent;
import org.pitest.mutationtest.commandline.MutationCoverageReport;

import java.util.List;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class JavaParametersConfigurator {

    public static void configureJavaParameters(JavaParameters javaParameters, PitestConfiguration configuration) {
        javaParameters.setMainClass("org.pitest.mutationtest.commandline.MutationCoverageReport");
        configureProgramParameters(javaParameters.getProgramParametersList(), configuration);
        configureClassPath(javaParameters.getClassPath());
    }

    private static void configureProgramParameters(ParametersList parameters, PitestConfiguration configuration) {
        if (configuration.isVerboseLogging()) {
            parameters.add("--verbose");
        }
        String targetClasses = configuration.getTargetClasses();
        checkTargetClasses(targetClasses);
        parameters.add("--targetClasses", targetClasses);
        parameters.add("--targetTests", configuration.getTargetTests());
        String excludedClasses = configuration.getExcludedClasses();
        if (StringUtils.isNotBlank(excludedClasses)) {
            parameters.add("--excludedClasses", excludedClasses);
        }
        String reportDir = configuration.getReportDir();
        parameters.add("--reportDir", reportDir);
        parameters.add("--sourceDirs", configuration.getSourceDirs());
        parameters.add("--outputFormats", "HTML,XML");
        String history = Utils.concatenatePath(reportDir, "/history.pit");
        parameters.add("--historyInputLocation", history);
        parameters.add("--historyOutputLocation", history);
        parameters.add("--timestampedReports=false");
        parameters.addParametersString(configuration.getManualParameters());
    }

    private static void configureClassPath(PathsList cp) {
        cp.add(PathUtil.getJarPathForClass(HotSwapAgent.class)); // TODO take it from module cp
        cp.add(PathUtil.getJarPathForClass(MutationCoverageReport.class));
    }

    private static void checkTargetClasses(String targetClasses) {
        if (Constants.ANY_CLASS_PATTERN.equals(targetClasses)) {
            Notifications.Bus.notify(new Notification("Pitest", "Pitest is configured incorrectly",
                    "Target classes parameter is configured too widely. Please check it and make it more precise.",
                    NotificationType.WARNING));
        }
    }

    public static String getReportDir(JavaParameters javaParameters) throws ExecutionException {
        List<String> parameters = javaParameters.getProgramParametersList().getParameters();
        String reportDir = null;
        for (int i = 0; i < parameters.size(); i++) {
            if ("--reportDir".equals(parameters.get(i))) {
                reportDir = parameters.get(i + 1);
                break;
            }
        }
        return reportDir;
    }
}
