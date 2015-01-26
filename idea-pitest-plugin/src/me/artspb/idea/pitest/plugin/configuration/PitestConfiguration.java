package me.artspb.idea.pitest.plugin.configuration;

import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.WriteExternalException;
import me.artspb.idea.pitest.plugin.configuration.junit.JUnitConfigurationUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class PitestConfiguration implements JDOMExternalizable {

    public static final Key<PitestConfiguration> PITEST_KEY = Key.create("me.artspb.idea.pitest.plugin");

    private static final String EDIT_MANUALLY = "editManually";
    private static final String TARGET_TESTS = "targetTests";
    private static final String TARGET_CLASSES = "targetClasses";
    private static final String EXCLUDED_CLASSES = "excludedClasses";
    private static final String REPORT_DIR = "reportDir";
    private static final String SOURCE_DIRS = "sourceDirs";

    private boolean editManually;
    private String targetTests;
    private String targetClasses;
    private String excludedClasses;
    private String reportDir;
    private String sourceDirs;

    public static PitestConfiguration getOrCreate(@NotNull RunConfigurationBase runConfiguration) {
        PitestConfiguration configuration = runConfiguration.getCopyableUserData(PITEST_KEY);
        if (configuration == null || !configuration.isEditManually()) {
            if (JUnitConfigurationUtils.supports(runConfiguration)) {
                configuration = JUnitConfigurationUtils.parse(runConfiguration);
            }
            runConfiguration.putCopyableUserData(PITEST_KEY, configuration);
        }
        return configuration;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        setEditManually(Boolean.valueOf(element.getAttributeValue(EDIT_MANUALLY)));
        setTargetTests(element.getAttributeValue(TARGET_TESTS));
        setTargetClasses(element.getAttributeValue(TARGET_CLASSES));
        setExcludedClasses(element.getAttributeValue(EXCLUDED_CLASSES));
        setReportDir(element.getAttributeValue(REPORT_DIR));
        setSourceDirs(element.getAttributeValue(SOURCE_DIRS));
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        if (isEditManually()) {
            element.setAttribute(EDIT_MANUALLY, String.valueOf(true));
            element.setAttribute(TARGET_TESTS, getTargetTests());
            element.setAttribute(TARGET_CLASSES, getTargetClasses());
            element.setAttribute(EXCLUDED_CLASSES, getExcludedClasses());
            element.setAttribute(REPORT_DIR, getReportDir());
            element.setAttribute(SOURCE_DIRS, getSourceDirs());
        }
    }

    public boolean isEditManually() {
        return editManually;
    }

    public void setEditManually(boolean editManually) {
        this.editManually = editManually;
    }

    public String getTargetTests() {
        return targetTests;
    }

    public void setTargetTests(String targetTests) {
        this.targetTests = targetTests;
    }

    public String getTargetClasses() {
        return targetClasses;
    }

    public void setTargetClasses(String targetClasses) {
        this.targetClasses = targetClasses;
    }

    public String getExcludedClasses() {
        return excludedClasses;
    }

    public void setExcludedClasses(String excludedClasses) {
        this.excludedClasses = excludedClasses;
    }

    public String getReportDir() {
        return reportDir;
    }

    public void setReportDir(String reportDir) {
        this.reportDir = reportDir;
    }

    public String getSourceDirs() {
        return sourceDirs;
    }

    public void setSourceDirs(String sourceDirs) {
        this.sourceDirs = sourceDirs;
    }
}