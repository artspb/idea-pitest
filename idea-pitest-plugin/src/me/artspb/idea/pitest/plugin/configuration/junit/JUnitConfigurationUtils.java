package me.artspb.idea.pitest.plugin.configuration.junit;

import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import me.artspb.idea.pitest.plugin.Utils;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.java.JavaSourceRootType;

import java.util.*;

import static com.intellij.execution.junit.JUnitConfiguration.*;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class JUnitConfigurationUtils {

    private static final Map<String, Configurator> CONFIGURATORS = new HashMap<>();

    static {
        ClassBasedConfigurator classBasedConfigurator = new ClassBasedConfigurator();
        CONFIGURATORS.put(TEST_CLASS, classBasedConfigurator);
        CONFIGURATORS.put(TEST_METHOD, classBasedConfigurator);
        CONFIGURATORS.put(TEST_PACKAGE, new PackageBasedConfigurator());
        CONFIGURATORS.put(TEST_PATTERN, new PatternBasedConfigurator());
        CONFIGURATORS.put(TEST_DIRECTORY, new DirectoryBasedConfigurator());
    }

    public static boolean supports(RunProfile runProfile) {
        return runProfile instanceof JUnitConfiguration;
    }

    public static PitestConfiguration parse(@NotNull RunConfigurationBase runConfiguration) {
        PitestConfiguration pitestConfiguration = new PitestConfiguration();
        JUnitConfiguration jUnitConfiguration = (JUnitConfiguration) runConfiguration;
        configureTargetTestAndClasses(pitestConfiguration, jUnitConfiguration);
        configureReportAndSourcesDirs(pitestConfiguration, jUnitConfiguration);
        pitestConfiguration.setExcludedClasses("");
        pitestConfiguration.setManualParameters("");
        return pitestConfiguration;
    }

    private static void configureTargetTestAndClasses(PitestConfiguration pitestConfiguration, JUnitConfiguration jUnitConfiguration) {
        JUnitConfiguration.Data data = jUnitConfiguration.getPersistentData();
        Configurator configurator = CONFIGURATORS.get(data.TEST_OBJECT);
        if (configurator != null) {
            configurator.configure(jUnitConfiguration, pitestConfiguration);
        }
    }

    private static void configureReportAndSourcesDirs(PitestConfiguration pitestConfiguration, JUnitConfiguration jUnitConfiguration) {
        String reportDir = "";
        String sourceDirs = "";
        Module module = jUnitConfiguration.getConfigurationModule().getModule();
        if (module != null) {
            ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
            reportDir = findReportDir(moduleRootManager);
            sourceDirs = findSourceRoots(moduleRootManager);
        }
        pitestConfiguration.setReportDir(reportDir);
        pitestConfiguration.setSourceDirs(sourceDirs);
    }

    @NotNull
    private static String findReportDir(ModuleRootManager moduleRootManager) {
        String reportDir = "";
        VirtualFile compilerOutputPath = moduleRootManager.getModuleExtension(CompilerModuleExtension.class).getCompilerOutputPath();
        if (compilerOutputPath != null) {
            String target = compilerOutputPath.getParent().getPresentableUrl();
            reportDir = Utils.concatenatePath(target, "/pitest");
        }
        return reportDir;
    }

    @NotNull
    private static String findSourceRoots(ModuleRootManager moduleRootManager) {
        String sourceDirs;
        List<VirtualFile> virtualSourceRoots = moduleRootManager.getSourceRoots(JavaSourceRootType.SOURCE);
        Set<String> sourceRoots = new HashSet<>();
        for (VirtualFile sourceRoot : virtualSourceRoots) {
            sourceRoots.add(sourceRoot.getPresentableUrl());
        }
        sourceDirs = StringUtils.join(sourceRoots, ",");
        return sourceDirs == null ? "" : sourceDirs;
    }
}
