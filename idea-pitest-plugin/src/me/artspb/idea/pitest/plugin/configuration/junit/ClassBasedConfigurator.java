package me.artspb.idea.pitest.plugin.configuration.junit;

import com.intellij.execution.junit.JUnitConfiguration;
import com.intellij.psi.util.ClassUtil;
import me.artspb.idea.pitest.plugin.configuration.PitestConfiguration;
import org.apache.commons.lang.StringUtils;

import static me.artspb.idea.pitest.plugin.Constants.ANY_IN_PACKAGE_PATTERN;

/**
 * @author Artem Khvastunov &lt;contact@artspb.me&gt;
 */
class ClassBasedConfigurator extends Configurator {

    @Override
    public void configure(JUnitConfiguration jUnitConfiguration, PitestConfiguration configuration) {
        JUnitConfiguration.Data data = jUnitConfiguration.getPersistentData();
        String targetTests = data.getMainClassName();
        String targetClasses;
        if (StringUtils.endsWith(targetTests, "Test")) {
            targetClasses = targetTests.substring(0, targetTests.lastIndexOf("Test"));
        } else {
            targetClasses = ClassUtil.extractPackageName(targetTests) + ANY_IN_PACKAGE_PATTERN;
        }
        configure(configuration, targetTests, targetClasses);
    }
}
