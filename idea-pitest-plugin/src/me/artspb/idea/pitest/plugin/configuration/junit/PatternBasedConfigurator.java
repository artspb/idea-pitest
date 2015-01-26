package me.artspb.idea.pitest.plugin.configuration.junit;

import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.psi.util.ClassUtil;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import org.apache.commons.lang.StringUtils;

import java.util.HashSet;
import java.util.Set;

import static me.artspb.idea.pitest.plugin.Constants.ANY_IN_PACKAGE_PATTERN;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
class PatternBasedConfigurator extends Configurator {

    private static final String PATTERNS_SEPARATOR = "||";

    @Override
    public void configure(JUnitConfiguration jUnitConfiguration, PitestConfiguration configuration) {
        JUnitConfiguration.Data data = jUnitConfiguration.getPersistentData();
        String targetTests;
        String targetClasses;
        String pattern = data.getPatternPresentation();
        String[] tests = StringUtils.split(pattern, PATTERNS_SEPARATOR);
        targetTests = StringUtils.join(tests, ",");
        Set<String> packages = new HashSet<>();
        for (String targetTest : tests) {
            packages.add(ClassUtil.extractPackageName(targetTest) + ANY_IN_PACKAGE_PATTERN);
        }
        targetClasses = StringUtils.join(packages, ",");
        configure(configuration, targetTests, targetClasses);
    }
}
