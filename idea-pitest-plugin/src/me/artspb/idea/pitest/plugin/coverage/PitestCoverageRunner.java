package me.artspb.idea.pitest.plugin.coverage;

import com.intellij.coverage.CoverageSuite;
import com.intellij.coverage.JavaCoverageRunner;
import com.intellij.execution.configurations.SimpleJavaParameters;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.rt.coverage.data.ProjectData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
public class PitestCoverageRunner extends JavaCoverageRunner {

    public static final String COVERAGE_RUNNER_ID = "PitestCoverage";
    public static final String DATA_FILE_EXTENSION = "pit";

    private static final Logger LOG = Logger.getInstance("#" + PitestCoverageRunner.class.getName());

    @Override
    public void appendCoverageArgument(String sessionDataFilePath, @Nullable String[] patterns, SimpleJavaParameters parameters, boolean collectLineInfo, boolean isSampling) {
    }

    @Override
    public ProjectData loadCoverageData(@NotNull File sessionDataFile, @Nullable CoverageSuite baseCoverageSuite) {
        ProjectData data = new ProjectData();
        try {
            String reportDir = FileUtil.loadFile(sessionDataFile);
            XmlReportParser.parseReport(data, reportDir);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            LOG.warn("Failed to load coverage data from file: " + sessionDataFile.getAbsolutePath(), e);
            FileUtil.delete(sessionDataFile);
        }
        return data;
    }

    @Override
    public String getPresentableName() {
        return "Pitest";
    }

    @Override
    public String getId() {
        return COVERAGE_RUNNER_ID;
    }

    @Override
    public String getDataFileExtension() {
        return DATA_FILE_EXTENSION;
    }
}
